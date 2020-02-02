public class OrderTest {
    public static void main(String[] args) {
        Order order = new Order();
        //默认初始化->显式初始化/代码块初始化->构造器初始化->对象.属性.方法
        System.out.println(order.a);
    }
}
class Order{
    int a;
    {
        a = 20;
    }
//    public Order(){
//        a = 10;
//    }
}
