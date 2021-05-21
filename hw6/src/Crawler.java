import java.io.*;
import java.net.*;
import java.util.*;

public class Crawler {

    private List<UrlDepthPair> pendingURLs;

    private void addUrl(UrlDepthPair u) {
        pendingURLs.add(u);
    }

    private List getUrlList() {
        return pendingURLs;
    }

    public Crawler(UrlDepthPair urlPair) {
        pendingURLs = new LinkedList<> ();
        pendingURLs.add(urlPair);
    }

    public static void main(String args[]) throws MalformedURLException {

        String startingUrl;
        int maxDepth;

        if (args.length != 2) {
            System.out.println("Format error: java Crawler <URL> <Depth>");
            System.exit(1);
        }

        startingUrl = args[0];
        maxDepth = Integer.parseInt(args[1]);

        Crawler crawler = new Crawler(new UrlDepthPair(startingUrl, 0));






        try {
            URL url = new URL(startingUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //conn.getResponseCode();
            //conn.setConnectTimeout(10000);
            //conn.setReadTimeout(3000);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            for (String line; (line = in.readLine()) != null; ) {
                System.out.println(line);
            }

            in.close();
            conn.disconnect();

            //} catch (MalformedURLException e) {
        } catch (Exception e) {
            System.out.println("Invalid URL");
        }




    }
}
