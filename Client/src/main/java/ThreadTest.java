public class ThreadTest {
    
    public static void main(String[] args) {
        
        //匿名类的方式创建线程
        //start方法使当前线程进入就绪状态，但不一定立即执行，start方法会调用run方法
        MyRunnable mr = new MyRunnable();
        Thread t1 = new Thread(mr);
        Thread t2 = new Thread(mr);
        Thread t3 = new Thread(mr);
        t1.setName("t1");
        t1.start();
        t2.setName("t2");
        t2.start();
        t3.setName("t3");
        t3.start();
    }
}

class MyThread extends Thread{
    //重写的方法：子类抛的异常不大于父类，父类没有抛异常，子类不能抛
    //子类返回值如果是引用数据类型，子类不大于父类
    //权限修饰符，子类不小于父类
    int i = 0;
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName()+":" + i++);
    }
    //多个线程各自拥有自己的虚拟机栈、程序计数器
    //共享方法区、堆
    
    //使用synchronized的代码块或者方法的线程，要想执行{}中的代码，必须获得锁/同步监视器才有机会执行，{}中一般用来操作共享数据
    
    //线程同步机制：同步代码块，同步方法
    //同步方法使用的锁是：类本身或者调用该方法的对象本身
    
    //synchronized和lock的区别，lock需要new一个lock对象，在使用时需要调用lock方法和unlock方法，synchronized则是自动释放锁
    
    //死锁的理解：不同的线程分别占用对方需要的同步资源不放弃，都在等待对方放弃自己需要的同步资源，就形成了线程的死锁
    
    //出现死锁后，不会出现异常，不会出现提示，只是所有的线程都处于阻塞状态
    
    
    //线程通信：wait\notify\notifyAll
    //这三个的前提是使用同一把锁，wait()会使当前线程进入阻塞状态（已经在同步代码块中，但是又暂时放弃持有锁！！！！等待被唤醒）
    // notify会唤醒一个正在等待同步锁的被阻塞的线程
    //必须在同步代码块或同步方法中使用这三个方法，并且方法的调用者必须是同步监视器
    //这三个方法是定义在Object类中的！
    
    //sleep和wait的异同
    //相同点：一旦执行，都会使线程进入阻塞状态
    //不同点：声明的位置不同，sleep是声明在Thread类中的静态方法、wait是声明在Object类中的成员方法
    //sleep可以在任意场景中调用，阻塞当前所在线程，wait则只能在同步 机制中使用
    //sleep时间到，会自动醒来，不会释放锁，wait需要和notify配合使用，会释放锁
    //sleep不会释放锁，这意味着，当前线程即使睡着了其他线程也无法获得锁(在睡着的时候其他线程无法执行)
    //而wait方法则不同，会释放锁，意味着当前线程阻塞，其他线程可以接续执行(在睡着的时候其他线程执行)
    
    //实现callable接口：可以有返回值、可以抛异常、可以带泛型
    
    //futureTask的好处是可以调用get()方法获取线程的返回值，get()方法同时会阻塞当前线程
    
    //具体步骤：实现callable接口、重写call方法、创建对象、创建FutureTask、创建线程对象、调用start方法、调用get方法获取返回值
    
    //wait和notify需要用在同步代码块中由同步监视器调用
    
    
}
class MyRunnable implements Runnable{
    int ticket = 100;
    @Override
    public void run() {
        while(true){
            synchronized (this){
                if(ticket > 0)
                    System.out.println(Thread.currentThread().getName() + ":" + ticket--);
                else
                    break;
            }
        }
    }
}
