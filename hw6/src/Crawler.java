import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

public class Crawler {

    //for urls pending. not parsed yet
    private static List<UrlDepthPair> pendingURLs;
    //for urls store. already parsed
    private static List<UrlDepthPair> storedURLs;
    private static int maxDepth = 1;
    private static String startingUrl;

    private void addUrl(UrlDepthPair u) {
        pendingURLs.add(u);
    }
    public List getUrlList() {
        return pendingURLs;
    }

    public Crawler(UrlDepthPair urlPair) {
        pendingURLs = new LinkedList<> ();
        storedURLs = new LinkedList<> ();
        pendingURLs.add(urlPair);
    }

    public void Parsing() {

        while (!pendingURLs.isEmpty()){
            UrlDepthPair currentPair = pendingURLs.remove(0);
            URL url = currentPair.url;
            int depth = currentPair.getDepth();

            //TODO need to fix href=" not <a href=" because of the class existence
            String urlStart = "<a href=\"h";
            String urlEnd = "\"";
            String tempUrl;
            int urlStartIndex = 0;
            int urlEndIndex = 0;


            System.out.println(url);
            System.out.println(depth);

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

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                //conn.getResponseCode();
                //conn.setConnectTimeout(10000);
                //conn.setReadTimeout(3000);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                for (String line; (line = in.readLine()) != null; ) {

                    urlStartIndex = line.indexOf(urlStart);
                    urlEndIndex = 0;

                    System.out.println(line);


                    // keep searching in line. make while statement in case of having multiple href in one line
                    while (urlStartIndex >= 0) {

                        System.out.println(line);


                        //find end part of the url
                        urlStartIndex = urlStartIndex + 9;
                        System.out.println(urlStartIndex);
                        urlEndIndex = line.indexOf(urlEnd, urlStartIndex);
                        System.out.println(urlEndIndex);

                        //in this case the end part of url is in next line
                        if (urlEndIndex < 0) {
                            continue;
                        }

                        tempUrl = line.substring(urlStartIndex, urlEndIndex);
                        urlStartIndex += urlEndIndex;

                        System.out.println(tempUrl);

                        if (depth + 1 <= maxDepth) {
                            pendingURLs.add(new UrlDepthPair(tempUrl, depth + 1));
                        }

                        line = line.substring(urlEndIndex);
                        urlStartIndex = line.indexOf(urlStart);

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
        try {
            Crawler crawler = new Crawler(new UrlDepthPair("https://www.naver.com", 0));
            crawler.Parsing();

        } catch (Exception e) {
            System.out.println("Error");
        }

    }
}
