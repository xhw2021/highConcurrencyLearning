package Day02;

/**
 * @ClassName SimpleWN
 * @Description 探究wait以及notify的用法
 * @Author 22936
 * @Date 2021/4/12 16:13
 * @Version 1.0
 */

/*
wait会释放锁，而notify也会
* */
public class SimpleWN {
    final static Object obj = new Object();

    public static class T1 extends Thread{
        public String name;
        public T1(String name){
            this.name = name;
        }


        @Override
        public void run() {
            synchronized(obj){
                System.out.println(System.currentTimeMillis()+ Thread.currentThread().getName()+": start!");
                try{
                    System.out.println(System.currentTimeMillis() + Thread.currentThread().getName()+":  wait for object ");
                    obj.wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() +Thread.currentThread().getName()+":  end!");
            }
        }
    }

    public static class T2 extends Thread{
        @Override
        public void run() {
            synchronized(obj){
                System.out.println(System.currentTimeMillis()+" : T2 start! notify one thread!");
                obj.notify();
                System.out.println(System.currentTimeMillis()+" :T2 end! ");
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new T1("t1");
        Thread t2 = new T2();
        Thread t3 = new T1("t3");
        Thread t4 = new T1("t4");
        Thread t5 = new T1("t5");
        Thread t6 = new T1("t6");
        t1.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        Thread.sleep(2000);
        t2.start();
    }
}
