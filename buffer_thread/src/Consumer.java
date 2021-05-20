import java.io.*;

public class Consumer extends Thread {
    private final Buffer buffer;

    public Consumer(Buffer b) {
        buffer = b;
    }
    public void run(){
        while (!Thread.currentThread().isInterrupted()) {
            char c = buffer.delete();
            System.out.println("Consumed " + (c > 0 ? c : "EOF"));
        }
    }
}
