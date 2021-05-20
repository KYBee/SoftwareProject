public class Buffer {

    private final int capacity;
    private char[] store;
    private int start, end, size;

    public Buffer(int size) {
        capacity = size;
        end = -1;
        start = size = 0;
        size = 0;
        store = new char[capacity];
    }

    public synchronized void insert(char ch) {
        try {
            while (size == capacity) {
                wait();
            }
            end = (end + 1) % capacity;
            store[end] = ch;
            size++;
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized char delete() {
        try {
            while (size == 0) {
                wait();
            }
            char ch = store[start];
            start = (start + 1) % capacity;
            size--;
            notifyAll();
            return ch;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        }
    }
}
