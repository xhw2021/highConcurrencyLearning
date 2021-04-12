package Day02;

import java.sql.Timestamp;

/**
 * @ClassName interrupt
 * @Description 中断的三个函数：
 * 1. Thread.interrupt（）  中断线程并设置中断标记位
 * 2. Thread.isInterrupted()  通过检查中断标记位检查线程是否被中断，要注意sleep方法
 * 3. Thread.interrupted()  静态方法，判断是否中断，并清除中断标志位
 * @Author 22936
 * @Date 2021/4/12 15:52
 * @Version 1.0
 */

public class interrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                int i = 0;
                while(true){
                    if(Thread.currentThread().isInterrupted()){
                        System.out.println("Interrupted!");
                        break;
                    }
                    System.out.println(i++);
                    try {
                        Thread.sleep(1000);  //此处的sleep清楚了中断标记位，因此如果不重置中断标记位（line 29），无法跳出这个循环。
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted1!");
                        Thread.currentThread().interrupt();
                    }
                    Thread.yield();
                }
            }
        };

        t1.start();
        Thread.sleep(3000);
        t1.interrupt();
    }
}
