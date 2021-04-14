package Chapter03;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @ClassName CyclicBarrierDemo
 * @Description TODO
 * @Author 22936
 * @Date 2021/4/13 17:13
 * @Version 1.0
 */

public class CyclicBarrierDemo {
    public static class Soldier implements Runnable {
        private String soldier;
        private final CyclicBarrier cyclic;

        public Soldier( CyclicBarrier cyclic,String soldier) {
            this.soldier = soldier;
            this.cyclic = cyclic;
        }

        @Override
        public void run() {
            try{
                cyclic.await();
                doWork();
                cyclic.await();
            }catch (InterruptedException e){
                e.printStackTrace();
            }catch (BrokenBarrierException e){
                e.printStackTrace();
            }
        }

        void doWork() {
            try{
                Thread.sleep(Math.abs(new Random().nextInt()%10000));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(soldier + "： 任务完成！");
        }
    }

    public static class BarrierRun implements Runnable {
        boolean flag;
        int N;

        public BarrierRun(boolean flag, int n) {
            this.flag = flag;
            N = n;
        }

        @Override
        public void run() {
            if(flag){
                System.out.println("士兵"+N+"个任务完成！");
            }else{
                System.out.println("士兵"+N+"个集合完成！");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int N = 6;
        Thread[] allSolider = new Thread[N];
        boolean flag = false;
        CyclicBarrier cycle = new CyclicBarrier(N,new BarrierRun(flag,N));
        System.out.println("集合队伍！");
        for(int i = 0;i<N;++i){
            System.out.println("士兵" + i + "报道！");
            allSolider[i] = new Thread(new Soldier(cycle,"士兵" + i));
            allSolider[i].start();
        }
    }
}
