package Chapter03;


import Day02.BadSuspend;

import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName LockSupportDemo
 * @Description TODO
 * @Author 22936
 * @Date 2021/4/12 19:51
 * @Version 1.0
 */

public class LockSupportDemo {

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
//                Thread.currentThread().suspend();
                LockSupport.park();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        t1.start();
        Thread.sleep(1000);
        t2.start();

//        t1.resume();
//        t2.resume();
        LockSupport.unpark(t1);
        LockSupport.unpark(t2);
        t1.join();
        t2.join();
    }
}
