package Day02;

/**
 * @ClassName StopThreadUnsafe
 * @Description Thread.stop()会立即释放锁，导致数据不一致。
 * @Author 22936
 * @Date 2021/4/8 21:19
 * @Version 1.0
 */

public class StopThreadUnsafe {
    public static User u = new User();

    public static class User{
        private int id;
        private String name;
        public User(){
            id = 0;
            name = "0";
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +"id=" + id +", name='" + name + '\'' +'}';
        }
    }

    public static class ChangeObjectThread extends Thread{
        @Override
        public void run() {
            while (true){
                synchronized(u){
                    int v = (int)(System.currentTimeMillis()/1000);
                    u.setId(v);
                    try{
                        Thread.sleep(10);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    u.setName(String.valueOf(v));
                }
                Thread.yield();
            }
        }
    }

    public static class ReadObjectThread extends Thread{
        @Override
        public void run() {
            while (true){
                synchronized(u){
                    if(u.getId()!= Integer.parseInt(u.getName())){
                        System.out.println(u.toString());
                    }
                }
                Thread.yield();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReadObjectThread().start();

        while(true){
            Thread t = new ChangeObjectThread();
            t.start();
            Thread.sleep(5);  //此处参数为线程t等待时间，等待时间与ChangeObjectThread里的try中sleep时间差距越大，由
                                    // stop产生的异常概率越小。当小于try中的sleep时，无法进行setname操作，一直为异常。
            t.stop();
        }
    }
}
