import java.util.*;
import java.util.concurrent.*;

public class UrlPool {
    // List of pending urls to be crawled
    BlockingQueue<UrlDepthPair> pending_urls;
    // List of all the urls we've seen -- this forms the result
    List<UrlDepthPair> seen_urls;
    // List of url string for comparing
    Set<String> compare_urls;
    // Maximum crawl depth
    int maxDepth;
    // Count of waiting threads
    int waits;

    // Constructor
    public UrlPool(int maxDepth) {
        this.maxDepth = maxDepth;
        pending_urls = new LinkedBlockingQueue<>();
        seen_urls = new LinkedList<>();
        compare_urls = new HashSet<>();
        waits = 0;
    }

    // Get the next UrlDepthPair to crawl
    public UrlDepthPair getNextPair() {

        synchronized(this) {
            this.waits++;
        }

        UrlDepthPair pair;

        try {
            pair = pending_urls.take();
        } catch (InterruptedException e) {
            pair = null;
        }

        synchronized(this) {
            this.waits--;
        }

        return pair;
    }

    // Add a new pair to the pool if the depth is
    // less than the maximum depth to be considered.
    public synchronized void addPair(UrlDepthPair pair) {
        if (compare_urls.add(pair.getURLString())) {
            seen_urls.add(pair);
            if (pair.getDepth() < maxDepth) {
                try { pending_urls.put(pair); } catch (InterruptedException e) {}
            }
        }
    }
    
    // Get the number of waiting threads
    public synchronized int getWaitCount() {
        return waits;
    }
    
    // Get all the urls seen
    public List<UrlDepthPair> getSeenUrls() {
        return seen_urls;
    }
}
