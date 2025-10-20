package dogapi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        BreedFetcher fetcher = new CachingBreedFetcher(new DogApiBreedFetcher());
        try {
            System.out.println("Sub-breeds of bulldog: " + fetcher.getSubBreeds("bulldog"));
        } catch (BreedFetcher.BreedNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    /**
     * Return the number of sub breeds that the given dog breed has according to the
     * provided fetcher.
     *
     * @param breed        the name of the dog breed
     * @param breedFetcher the breedFetcher to use
     * @return the number of sub breeds. Zero should be returned if there are no sub breeds
     * returned by the fetcher
     */
    public static int getNumberOfSubBreeds(String breed, BreedFetcher fetcher) {
        try {
            List<String> subs = fetcher.getSubBreeds(breed);
            return subs.size();
        } catch (BreedFetcher.BreedNotFoundException e) {
            return 0; // return 0 when the breed is invalid, as the tests expect
        }
    }
}