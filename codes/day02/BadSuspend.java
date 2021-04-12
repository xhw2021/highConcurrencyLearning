package Day02;

/**
 * @ClassName BadSuspend
 * @Description 演示resume在suspend之前，导致线程被永久挂起的情况
 * @Author 22936
 * @Date 2021/4/12 16:46
 * @Version 1.0
 */

public class BadSuspend {
    public static Object u = new Object(); // 对象锁
    static ChangeObjectThread t1 = new ChangeObjectThread("t1");
    static ChangeObjectThread t2 = new ChangeObjectThread("t2");

    public static class ChangeObjectThread extends Thread{
        public ChangeObjectThread(String name){
            super.setName(name);
        }

        @Override
        public void run() {
            synchronized(u){
                System.out.println("in " + getName());
                Thread.currentThread().suspend();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        t1.start();
        Thread.sleep(1000);
        t2.start();

        t1.resume();
        t2.resume();
        System.out.println("!");//t2先进行了resume，因此t2永久挂起了，但是状态仍为Runnable

        t1.join();
        t2.join();
    }
}
