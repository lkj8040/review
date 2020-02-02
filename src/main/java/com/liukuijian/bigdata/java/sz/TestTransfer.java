package com.liukuijian.bigdata.java.sz;

public class TestTransfer {
    public static void main(String[] args) {
        byte b = 10;
        test(b);
    }
//    public static void test(byte b){
//        System.out.println("bbbb");
//    }
//    public static void test(short s){//在数值不变的情况下优先转换为short，类型提升是逐级提升的
//        System.out.println("ssss");
//    }
    public static void test(int i){
        System.out.println("iiii");
    }
    public static void test(char c){//char类型的取值范围是0-65535，无法表示负数，因此无法将byte提升为char类型
        System.out.println("cccc");
    }
}
