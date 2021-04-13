package Chapter03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @ClassName SemapDemo
 * @Description TODO
 * @Author 22936
 * @Date 2021/4/13 16:35
 * @Version 1.0
 */

public class SemapDemo implements Runnable {
    final Semaphore semp = new Semaphore(6);
    static int i = 0;
    @Override
    public void run() {
        try{
            semp.acquire();
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getId() + ": done!");
            semp.release();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(20);
        final SemapDemo demo = new SemapDemo();
        for(int i = 0;i<20;i++){
            exec.submit(demo);
        }
    }
}
