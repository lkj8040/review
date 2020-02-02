public class SingleInstanceTest {
    public static void main(String[] args) throws InterruptedException {
        
        SingleInstance s1 = SingleInstance.getInstance();
        SingleInstance s2 = SingleInstance.getInstance();
        SingleInstance s3 = SingleInstance.getInstance();
        SingleInstance s4 = SingleInstance.getInstance();
        
        Thread t1 = new Thread(){
            @Override
            public void run(){
                for (int i = 0; i < 100; i++)
                    System.out.println(i);
            }
        };
        //执行顺序：由父及子，静态先行
        //默认初始化、显式初始化、代码块->构造器初始化、对象名.方法初始化
        t1.start();
        for (int i = 0; i < 100; i++)
            System.out.println(i);
        t1.join();
        System.out.println("----------------------我是分割线--------------------------------");
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++){
                System.out.println(SingleInstance.getInstance());
            }
        });
        t2.start();
        t2.join();
        
    }
    
}
class SingleInstance{
    //1.私有化类的构造器
    private SingleInstance(){
    
    }
    //2.创建一个私有的对象实例
    //懒汉式加载
    private static SingleInstance instance = null;//延迟加载，爽一些
    //3.提供公共的静态的get方法
    public static SingleInstance getInstance(){
        if(instance == null){
            synchronized (SingleInstance.class){
                if(instance == null){
                    instance = new SingleInstance();
                }
            }
        }
        return instance;
    }
}
