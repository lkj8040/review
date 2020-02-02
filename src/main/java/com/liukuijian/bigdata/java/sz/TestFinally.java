package com.liukuijian.bigdata.java.sz;

public class TestFinally {
    public static void main(String[] args) {
        System.out.println(test());
        getName();
    }

    public static int test(){
        // rtnVal
        int i = 0;
        try{
            return i++; //1) _tmp=0; 2) i=1; 3) rtnVal = _tmp;
        }finally {
//            return ++i; //4)_tmp1=2; 5) i=2; 6) rtnVal = _tmp1;
            ++i;
        }

    }
    public static void getName(){
        //获取当前类的全类名
        try{
            xxx.test();
        }catch (RuntimeException e){
            StackTraceElement[] stackTrace = e.getStackTrace();
            for(StackTraceElement s : stackTrace){
                if("main".equals(s.getMethodName())){
                    System.out.println(s.getClassName());
                }
            }

        }
    }
}

class xxx{
    public static void test(){
        throw new RuntimeException();
    }
}
