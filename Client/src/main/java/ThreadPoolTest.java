import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    static int a = 0;
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        ThreadPoolExecutor pool = (ThreadPoolExecutor) threadPool;
        
        pool.setCorePoolSize(5);
        
        pool.setKeepAliveTime(1000L, TimeUnit.SECONDS);
        
        pool.execute(() -> {
            while(true) {
                synchronized (ThreadTest.class){
                    if(a < 10) {
                        System.out.println(Thread.currentThread().getName()  + ":" + a++);
                    }
                    else {
                        break;
                    }
                    ThreadTest.class.notify();//notify不会立即释放锁，只是告诉其他线程可以去竞争锁了
                    try {
                        ThreadTest.class.wait();//wait会真正释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        pool.execute(() -> {
            while(true) {
                synchronized (ThreadTest.class){
                    if(a < 10) {
                        System.out.println(Thread.currentThread().getName()  + ":" + a++);
                    }
                    else {
                        break;
                    }
                    ThreadTest.class.notify();
                    try {
                        ThreadTest.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        pool.shutdown();
        
    }
}
