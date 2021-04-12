package Day02;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @ClassName ArrayListMultiThread
 * @Description TODO
 * @Author 22936
 * @Date 2021/4/12 19:33
 * @Version 1.0
 */

public class ArrayListMultiThread {
    static Vector<Integer> al = new Vector<Integer>(10);
    public static class AddThread implements Runnable{
        @Override
        public void run() {
            for (int i=0; i<100000;i++){
                al.add(i);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddThread());
        Thread t2 = new Thread(new AddThread());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(al.size());
    }
}
