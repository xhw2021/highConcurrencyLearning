package Chapter03;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName IntLock
 * @Description TODO
 * @Author 22936
 * @Date 2021/4/13 15:32
 * @Version 1.0
 */

public class IntLock implements Runnable{
    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    int lock;

    public IntLock(int lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try{
            if(lock == 1){
                lock1.lockInterruptibly();
                try{
                    Thread.sleep(500);
                }catch(InterruptedException e){}
                lock2.lockInterruptibly();
            }else{
                lock2.lockInterruptibly();
                try{
                    Thread.sleep(500);
                }catch(InterruptedException e){}
                lock1.lockInterruptibly();
            }
        }catch(InterruptedException e){
            e.printStackTrace();
            System.out.println(lock + "线程被中断。");
        }finally{
            if(lock1.isHeldByCurrentThread()) lock1.unlock();
            if(lock2.isHeldByCurrentThread()) lock2.unlock();
            System.out.println(Thread.currentThread().getId() + ": 线程退出!");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        IntLock r1 = new IntLock(1);
        IntLock r2 = new IntLock(2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();

        Thread.sleep(500);

        t2.interrupt();
    }
}
