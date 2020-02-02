package com.liukuijian.bigdata.java.sz;
//import java.lang.reflect.Field;
public class TestString {
    public static void main(String[] args) {
        //可变字符串
        //StringBuffer , StringBuilder
        //所谓的线程安全问题，是多线程并发执行时，对共享内存中的共享对象的属性进行操作
        //多线程： 单线程
        //线程：cpu执行权分配的基本单位(内核级线程才是cpu执行权的分配单位，用户级线程不是)
        //进程：操作系统资源分配和调度的基本单位
        //共享内存： 多线程共享的内存：堆内存，方法区内存    =>  各自拥有独立的栈
        //           :将对象放置在独享内存中（栈上分配，逃逸分析）
        //共享对象： 多线程访问的是同一个对象
        //           :多例，每一个线程使用一个对象
        //web => springmvc => spring => mybatis => db
        //属性： 对象属性只有一个方法
        //修改：多线程同时修改

        final Dept2 dept = new Dept2();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                dept.name = "开发部";
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(dept.name);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                dept.name = "测试部";
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(dept.name);
            }
        });
        t1.start();
        t2.start();
        System.out.println("main...");
    }
}
class Dept2{
    public String name = "";
}
