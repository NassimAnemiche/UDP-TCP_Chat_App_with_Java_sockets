public class ThreadTest extends Thread {

    private int counter = 0;

    @Override
    public void run() {
        while (true) {
            System.out.println(getName() + " : " + counter++);
            try {
                Thread.sleep(100); // 100 ms
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void main(String[] args) {

        ThreadTest t1 = new ThreadTest();
        ThreadTest t2 = new ThreadTest();
        ThreadTest t3 = new ThreadTest();

        t1.start();
        t2.start();
        t3.start();
    }
}
