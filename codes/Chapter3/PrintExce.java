package Chapter03;

import java.util.concurrent.*;

/**
 * @ClassName DivTask
 * @Description TODO
 * @Author 22936
 * @Date 2021/4/14 15:25
 * @Version 1.0
 */

public class PrintExce{
    public static class DivTask implements Runnable {
        int a,b;

        public DivTask(int a, int b) {
            this.a = a;
            this.b = b;
        }
        @Override
        public void run() {
            double re = a/b;
            System.out.println(re);
        }
    }

    public static class TraceThreadPoolExecutor extends ThreadPoolExecutor{
        public TraceThreadPoolExecutor(int corePoolSize,int maximumPoolSize,
                                       long keepAliveTime,TimeUnit unit,BlockingQueue<Runnable> workQueue){
            super(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
        }

        @Override
        public void execute(Runnable task) {
            super.execute(wrap(task,clientTrace(),Thread.currentThread().getName()));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(wrap(task,clientTrace(),Thread.currentThread().getName()));
        }

        public Exception clientTrace(){
            return new Exception("client stack trace");
        }

        private Runnable wrap(final Runnable task,final Exception clientStack,String clientThreadName){
            return new Runnable(){
              @Override
              public void run(){
                  try{
                      task.run();
                  }catch(Exception e){
                      clientStack.printStackTrace();
                      throw e;
                  }
                }
            };
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor pools = new TraceThreadPoolExecutor(0,Integer.MAX_VALUE,
                0L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
        for(int i = 0;i<5;i++){
            //获取异常的方法1
//            Future re = pools.submit(new DivTask(100,i));
//            re.get();
            //获取异常的方法2
            pools.execute(new DivTask(100,i));
        }
    }

}



