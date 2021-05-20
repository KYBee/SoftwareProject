import java.io.*;

public class Producer extends Thread {
    private final Buffer buffer;
    private InputStreamReader in = new InputStreamReader(System.in);

    public Producer(Buffer b) {
        buffer = b;
    }
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int c = in.read();

                if (c == -1) break;
                if (c == '\n' || c == '\r') continue;
                System.out.println("Produced " + (char) c);
                buffer.insert((char) c);
            }
        } catch (IOException e) {
        }
    }
}
