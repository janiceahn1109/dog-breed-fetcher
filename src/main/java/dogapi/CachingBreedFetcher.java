package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {

    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        List<String> result = delegate.getSubBreeds(breed);
        cache.put(breed, result);
        return result;
    }
    public int getCallsMade() {
        if (delegate instanceof BreedFetcherForLocalTesting) {
            return ((BreedFetcherForLocalTesting) delegate).getCallCount();
        }
        return -1;
    }
}
