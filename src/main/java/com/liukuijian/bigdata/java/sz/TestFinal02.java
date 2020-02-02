package com.liukuijian.bigdata.java.sz;

import java.lang.reflect.Field;

public class TestFinal02 {
    public static void main(String[] args) throws Exception{
        //可以通过反射修改String 对象的value[]字符数组达到修改final的字符串的目的
        String s = "abc";
        Field field = String.class.getDeclaredField("value");
        field.setAccessible(true);
        char[] o = (char[]) field.get(s);
        o[1] = 'Q';
        System.out.println(s);
    }
}
