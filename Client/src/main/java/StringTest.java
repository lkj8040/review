import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class StringTest {
    
    public static void main(String[] args) {
        //抽象类和接口的区别
        //构造器：抽象类中必须有构造器，接口中没有构造器
        //抽象类有单继承限制，接口没有单继承限制，接口是多实现的
        //方法：抽象类中可以定义抽象方法，也可以定义普通方法，接口中只能定义public abstract修饰的抽象方法、jdk1.8增加了静态方法、默认方法
        //属性：抽象类中可以定义任意属性、接口中则只能定义public static final修饰的常量
        
        //String类实现了Comparable接口、Serializable接口
        //String类是final，不能被继承
        //String中定义的char[]数组是final的，必须且只能被赋值一次
        //字符串创建方法：new String 和 直接字面量方式存储的位置是不同的，一个在堆中，一个在方法区中
        //字符串常量池中是不会存储相同的内容的，不重复元素
        //常量池中只会存储一份数据，目的就是共享数据：线程共享堆、方法区
        //方法区：字符串常量池、静态域、类加载信息这些东西在JVM内存中都是独一份的！！！！
        
        //方法区也叫元空间
        
        //StringBuilder\StringBuffer叫可变的字符序列 new String(sb) new StringBuffer(str)
        //String 是不可变的字符序列
        
        //StringBuffer和StringBuilder几乎完全一样，唯一的区别在于，StringBuffer是线程安全的，而StringBuilder是线程不安全的
        
        //底层是可变的char[]数组，对于频繁的这种append操作，应该使用stringbuffer或者stringbuilder
        //扩容机制：初始长度为13，默认情况下扩容为原来的2倍+2,同时将原数组中的数据复制到新的数组,这个过程性能消耗最大
        
        //自然排序：Comparable接口，需要重写compareTo方法:像String、包装类、时间类、日期类等都实现了Comparable接口
        //定制排序：Comparator接口，需要重写compare方法：用于两个对象的比较Compare(obj1, obj2)
        
        //BigInteger和BigDecimal这两个类稍作了解，大整型和大浮点型
        
        
        //ArrayList和LinkedList的区别：LinkedList适合频繁的插入和删除操作，底层实现是双向链表
        //ArrayList是List接口的主要实现类，线程不安全，底层是数组，不适合插入和删除操作
        //底层实现是数组+扩容机制，默认扩容为原来的1.5倍 : newSize = size >> 1 + size,同时需要将数据拷贝到新数组
        //默认数组的初始长度为10，jdk1.8修改为懒汉式加载，延迟了数组的创建时机，只有当真正向集合中插入第一个元素时，才会创建数组。
        
        //LinkedList：内部声明了Node类的first和last属性，默认值为null
        //每来一个数据，封装一个Node，然后链接到双向链表中
        
        //HashSet中存储的对象必须重写hashCode方法和equals方法，原则：用于equals方法的fields也必须用于hashCode的计算
        //TreeSet使用的不是equals方法比较两个对象是否相等，而是使用定制排序或自然排序来确定两个对象是否相等
        
        //HashMap和HashTable的区别：
        //HashMap是Map接口的主要实现类，线程不安全，效率高，可以存储null的key和value
        //LinkedHashMap:在HashMap的基础上维护了一张双向链表，适合频繁的遍历操作
        //TreeMap：底层是红黑树，按照key进行排序
        //Hashtable：是Map的古老实现类，线程安全，key-value不能为null
        
        
        //HashMap的底层实现原理：底层初始化时创建长度为16的数组，用于存放一个一个的Entry
        //Entry就是用来封装key-value的类，jdk1.8使用的是Node，同样也是使用懒汉式加载，第一次put操作才创建数组
        //插入key-value时，先用hashCode方法计算key的hash值，经过和数组长度-1的&运算后得到Entry应该存放的位置
        //如果该位置没有元素，那么可以直接放入
        //如果已经存在元素，那么需要进一步比较hash值是否相等，如果和该位置上的所有元素的hash值都不相等那么可以直接插入
        //如果hash值也相等，那么进一步调用equals方法比较，如果依然返回true那么就用当前的value值覆盖旧的value值
        //如果equals返回false那么可以插入
        //HashMap的底层实现是数组+链表+红黑树 jdk1.8 扩容机制：当元素个数达到0.75总数组长度时，数组扩容为原来的2倍长度
        //当数组中某个索引上的链表的元素个数超过8，且当前数组的长度大于64时，该索引位置上的链表将改为红黑树存储
        Map<Integer, Double> map3 = new HashMap<>();
        
        map3.remove(1);
    
        TreeMap<Integer, Double> map = new TreeMap<>(map3);

    }
}
