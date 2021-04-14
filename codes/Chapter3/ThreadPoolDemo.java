package Chapter03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName ThreadPoolDemp
 * @Description TODO
 * @Author 22936
 * @Date 2021/4/14 14:18
 * @Version 1.0
 */

public class ThreadPoolDemo {

    public static class MyTask implements Runnable{
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis()/1000 + ": Thread ID is " + Thread.currentThread().getId());
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        MyTask task = new MyTask();
        ExecutorService exec = Executors.newFixedThreadPool(10);
//        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0;i<100;i++){
            exec.submit(task);
        }
        exec.shutdown();

    }


}
