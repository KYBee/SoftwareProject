import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

public class Crawler {
    //for urls pending. not parsed yet
    private static List<UrlDepthPair> pendingURLs;
    //for urls already visited. needs for comparing
    private static List<String> visitedURLs;
    //for urls store.
    private static List<UrlDepthPair> storedURLs;
    //for initializing crawler with user input
    private static int maxDepth = 1;
    private static String startingUrl;

    private void addUrl(UrlDepthPair u) {
        pendingURLs.add(u);
    }

    public List getUrlList() {
        return pendingURLs;
    }

    public void printStoredUrl() {
        for (UrlDepthPair list: storedURLs) {
            System.out.println(list);
        }
        System.out.println(storedURLs.size());
    }

    public Crawler(UrlDepthPair urlPair) {
        pendingURLs = new LinkedList<> ();
        storedURLs = new LinkedList<> ();
        visitedURLs = new LinkedList<> ();
        pendingURLs.add(urlPair);
    }

    public void Parsing() {

        while (!pendingURLs.isEmpty()){

            UrlDepthPair currentPair = pendingURLs.remove(0);
            URL url = currentPair.url;

            // for debugging
            System.out.println(currentPair.getURLString());


            int depth = currentPair.getDepth();

            storedURLs.add(currentPair);
            visitedURLs.add(currentPair.getURLString());


            System.out.print(">>> ");
            System.out.println(currentPair.getURLString());

            //TODO need to fix href=" not <a href=" because of the class existence
            String urlStart = "<a";
            String midStart = "href=\"";
            String urlEnd = "\"";
            String tempUrl;
            int urlStartIndex = 0;
            int urlMidIndex = 0;
            int urlEndIndex = 0;

            try {

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.getResponseCode();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(3000);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                // Url info which visited

                String line;
                while ((line = in.readLine()) != null) {

                    urlStartIndex = line.indexOf(urlStart);

                    // keep searching in line. make while statement in case of having multiple href in one line
                    while (urlStartIndex >= 0) {

                        //find end part of the url
                        urlStartIndex = urlStartIndex + 9;
                        urlEndIndex = line.indexOf(urlEnd, urlStartIndex);

                        //in this case the end part of url is in next line
                        if (urlEndIndex < 0)
                            break;

                        tempUrl = line.substring(urlStartIndex, urlEndIndex);


                        if (depth + 1 <= maxDepth && !visitedURLs.contains(tempUrl)) {
                            try {
                                pendingURLs.add(new UrlDepthPair(tempUrl, depth + 1));
                                System.out.print("!!! ");
                                System.out.println(tempUrl);
                            } catch (MalformedURLException e) {
                                continue;
                            }
                        }


                        urlStartIndex = line.indexOf(urlStart, urlEndIndex);

                    }
                }

                in.close();
                conn.disconnect();

            } catch (Exception e) {
                System.out.println("invalid Url");
                continue;
            }

        }
    }

    public static void main(String args[]) {

        if (args.length != 2) {
            System.out.println("Format error: java Crawler <URL> <Depth>");
            //System.exit(1);
        }

        //startingUrl = args[0];
        //maxDepth = Integer.parseInt(args[1]);

        //Crawler crawler = new Crawler(new UrlDepthPair(startingUrl, 0));

        //Handling HTTPS Sites
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) { System.exit(1); }


        try {
            Crawler crawler = new Crawler(new UrlDepthPair("https://www.naver.com", 0));
            crawler.Parsing();
            crawler.printStoredUrl();
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
        }
    }
}
