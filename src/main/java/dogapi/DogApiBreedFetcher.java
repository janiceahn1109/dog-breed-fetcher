package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private static final String BASE_URL = "https://dog.ceo/api/breed/";
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String url = BASE_URL + breed + "/list";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new BreedNotFoundException("API request failed with code: " + response.code());
            }

            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            String status = jsonObject.getString("status");

            // If breed not found
            if (status.equals("error")) {
                throw new BreedNotFoundException(jsonObject.getString("message"));
            }

            // If breed exists
            JSONArray subBreedsArray = jsonObject.getJSONArray("message");
            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < subBreedsArray.length(); i++) {
                subBreeds.add(subBreedsArray.getString(i));
            }

            // Return empty list if breed exists but has no sub-breeds
            return subBreeds;
        } catch (IOException e) {
            // Only throw if something went *really* wrong (e.g., network)
            throw new BreedNotFoundException("Unable to fetch breed data: " + breed);
        }
    }
}