public class Main {

    public static void main(String[] args) {
        System.out.println("Program starting");
        Buffer buffer = new Buffer(5);
        Producer prod = new Producer(buffer);
        Consumer cons = new Consumer(buffer);

        prod.start();
        cons.start();

        try {
            prod.join();
            cons.interrupt();
        } catch (InterruptedException e) {}

        System.out.println("End of Program");

    }

}
