package Day02;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName notifyOrder
 * @Description 探索notify的唤醒顺序是否随机的
 * @Author 22936
 * @Date 2021/4/12 16:38
 * @Version 1.0
 */

public class notifyOrder {
        //对象锁
        private final Object object = new Object();
        private List<Integer> sleep = new LinkedList<>();
        private List<Integer> notify = new LinkedList<>();

        //该线程作为一个唤醒线程
        public void startThread(int i) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (object) {
                        try {
                            System.out.println(Thread.currentThread().getName()+"进入休眠");
                            sleep.add(i);
                            object.wait();
                            System.out.println(Thread.currentThread().getName()+"线程已经唤醒");
                            notify.add(i);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.setName("Thread"+i);
            t.start();
        }

        public static void main(String[] args) {
            notifyOrder a = new notifyOrder();
            for(int i =1;i<5;i++){
                a.startThread(i);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
            for(int i =1;i<5;i++){
                synchronized (a.object) {
                    a.object.notify();
                }
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("休眠顺序"+a.sleep);
            System.out.println("唤醒顺序"+a.notify);


        }
}

