import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

public class Crawler {
    /** for urls pending. not parsed yet */
    private static List<UrlDepthPair> pendingURLs;
    /** for urls already visited. needs for comparing */
    private static Set<String> visitedURLs;
    /** for urls store */
    private static List<UrlDepthPair> storedURLs;
    /** for initializing crawler with user input */
    private static int maxDepth;
    private static String startingUrl;

    /** printing result*/
    public void printStoredUrl() {
        for (UrlDepthPair list: storedURLs) {
            System.out.println(list);
        }

        System.out.print("Total URLs: ");
        System.out.println(storedURLs.size());
    }

    public void printCurrentUrl(String currentUrl, int op) {
        if (op == 0) {
            System.out.print(">>> ");
        } else {
            System.out.print("!!! ");
        }
        System.out.println(currentUrl);
    }

    /** assign LinkedList */
    public Crawler(UrlDepthPair urlPair) {
        pendingURLs = new LinkedList<> ();
        storedURLs = new LinkedList<> ();
        visitedURLs = new HashSet<> ();
        pendingURLs.add(urlPair);
    }

    /** for parsing process */
    public void Parsing() {
        //handling HTTPS sites
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
        } catch (Exception e) {
            System.out.println("HTTPS auth error");
            System.exit(1);
        }

        // start parsing
        while (!pendingURLs.isEmpty()){
            UrlDepthPair currentPair = pendingURLs.remove(0);
            //parse current url
            try {
                int depth = currentPair.getDepth();
                URL url = new URL(currentPair.getURLString());
                printCurrentUrl(currentPair.getURLString(), 0);

                //add currentPair information into storeURLs and visitedURLs
                storedURLs.add(currentPair);
                visitedURLs.add(currentPair.getURLString());

                if (depth == maxDepth)
                    continue;

                //split token for html url
                String urlStart = "<a href=\"";
                String urlEnd = "\"";
                int urlStartIndex;
                int urlEndIndex;
                //temporary variable for parsed url
                String tempUrl;

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // handling web connection timeout for long waiting
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(3000);

                // handling the url which send valid response code
                if (conn.getResponseCode() < 200 || conn.getResponseCode() > 299)
                    continue;

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                for (String line; (line = in.readLine()) != null;) {
                    urlStartIndex = line.indexOf(urlStart);

                    // keep searching in line. make while statement in case of having multiple href in one line
                    while (urlStartIndex >= 0) {
                        // find end part of the url
                        urlStartIndex = urlStartIndex + 9;
                        urlEndIndex = line.indexOf(urlEnd, urlStartIndex);

                        // in this case the end part of url is in next line
                        if (urlEndIndex < 0)
                            break;

                        tempUrl = line.substring(urlStartIndex, urlEndIndex);
                        // handling MalformedURLException when create new UrlDepthPair and cycled url
                        if (visitedURLs.add(tempUrl)) {
                            try {
                                pendingURLs.add(new UrlDepthPair(tempUrl, depth + 1));
                                printCurrentUrl(tempUrl, 1);
                            } catch (MalformedURLException e) {
                                continue;
                            } finally {
                                urlStartIndex = line.indexOf(urlStart, urlEndIndex);
                            }
                        }
                    }
                }

                // close bufferedReader and disconnect connection
                in.close();
                conn.disconnect();

            } catch (IOException | ClassCastException | IllegalArgumentException e) {
                //ignore invalid url exception. MalformedException is also a child class of IOException.
                continue;
            }
        }
    }

    public static void main(String args[]) {
        // handling wrong number of input parameter
        if (args.length != 2) {
            System.out.println("Format error: java Crawler <URL> <Depth>");
            System.exit(1);
        }

        // handling NumberFormatException
        try {
            startingUrl = args[0];
            maxDepth = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Enter Url with Depth (Integer)");
            System.exit(1);
        }

        // handling MalformedURLException
        try {
            Crawler crawler = new Crawler(new UrlDepthPair(startingUrl, 0));
            crawler.Parsing();
            crawler.printStoredUrl();
        } catch (MalformedURLException e) {
            System.out.println("Invalid Url");
            System.exit(1);
        }
    }
}