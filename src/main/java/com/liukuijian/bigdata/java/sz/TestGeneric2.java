package com.liukuijian.bigdata.java.sz;

public class TestGeneric2 {
    public static void main(String[] args) {
        test(new UserTest());
    }
    public static <T> void test(T c){
        System.out.println(c);
    }
    //上限
    public static <T extends  UserTest> void test1(T t){
        System.out.println(t);
    }
    //下限
    public static void test2(Class<? super UserTest> t){
        System.out.println(t);
    }
    //上限
    public static void test3(Class<? extends UserTest> t){
        System.out.println(t);
    }
}
