package Day02;

/**
 * @ClassName ThreadGroupName
 * @Description TODO
 * @Author 22936
 * @Date 2021/4/12 17:32
 * @Version 1.0
 */

public class ThreadGroupName implements Runnable {
    public static void main(String[] args) {
        ThreadGroup tg = new ThreadGroup("PrintGroup");
        Thread t1 = new Thread(tg,new ThreadGroupName(),"T1" );
        Thread t2 = new Thread(tg,new ThreadGroupName(),"T2" );
        t1.start();
        t2.start();
        System.out.println(tg.activeCount());
//        tg.list();
    }


    @Override
    public void run() {
        String groupAndName = Thread.currentThread().getThreadGroup().getName() + "-" + Thread.currentThread().getName();
        while (true) {
            System.out.println("I am " + groupAndName);
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
