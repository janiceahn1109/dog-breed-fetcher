package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * All failures are reported as BreedNotFoundException to align
 * with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {

    private static final String BASE_URL = "https://dog.ceo/api/breed/";
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub-breeds for the given breed from the dog.ceo API.
     *
     * @param breed the breed to fetch sub-breeds for
     * @return list of sub-breeds for the given breed (may be empty)
     * @throws BreedNotFoundException if the breed does not exist or if any error occurs
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

        if (breed == null || breed.isEmpty()) {
            throw new BreedNotFoundException("Breed name cannot be null or empty.");
        }

        String url = BASE_URL + breed.toLowerCase() + "/list";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {


            if (response.body() == null) {
                throw new BreedNotFoundException("Empty response from API.");
            }

            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);


            if (jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("error")) {
                throw new BreedNotFoundException("Breed not found: " + breed);
            }


            JSONArray subBreedsArray = jsonObject.getJSONArray("message");
            List<String> subBreeds = new ArrayList<>();

            for (int i = 0; i < subBreedsArray.length(); i++) {
                subBreeds.add(subBreedsArray.getString(i));
            }


            return subBreeds;

        } catch (IOException e) {

            throw new BreedNotFoundException("Failed to fetch breed data: " + breed);
        }
    }
}
//error fixed
