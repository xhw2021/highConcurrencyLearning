package Day02;

/**
 * @ClassName GoodSuspend
 * @Description 提供一个比较可靠的suspend（）函数。
 * 现象：刚开始时输出即有in changeObjectThread也有in read；过了1s后，in changeObjectThread被挂起，只有in read；再过2s以后，in changeObjectThread
 * 被resume，又变为既有in changeObjectThread也有in read。
 * @Author 22936
 * @Date 2021/4/12 16:57
 * @Version 1.0
 */

public class GoodSuspend {
    public static Object u = new Object();

    public static class ChangeObjectThread extends Thread{
        volatile boolean suspendme = false;

        public void suspendMe(){
            suspendme = true;
        }

        public void resumeMe(){
            suspendme =false;
            synchronized (this){
                notify();
            }
        }

        @Override
        public void run() {
            while(true){

                synchronized (this){
                    while(suspendme)
                        try{
                            wait();
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                }

                synchronized(u){
                    System.out.println("in ChangeObjectThread!");
                }
                Thread.yield();
            }
        }

    }

    public static class ReadObjectThread extends Thread{
        @Override
        public void run() {
            while(true){
                synchronized (u){
                    System.out.println("in read!");
                }
                Thread.yield();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ChangeObjectThread t1 = new ChangeObjectThread();
        ReadObjectThread t2 = new ReadObjectThread();
        t1.start();
        t2.start();
        Thread.sleep(1000);
        t1.suspendMe();
        System.out.println("suspend t1 2 sec");
        Thread.sleep(2000);
        System.out.println("resume t1");
        t1.resumeMe();

    }
}
