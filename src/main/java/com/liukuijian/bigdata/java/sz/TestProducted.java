package com.liukuijian.bigdata.java.sz;

public class TestProducted {
    public static void main(String[] args) throws Exception{
        //访问权限
        //方法的提供者：java.lang.Object
        //方法的调用者：com.liukuijian.bigdata.java.sz.TestProtected
        EmpX emp = new EmpX();
        emp.clone();

        String s = "123";

    }
}

class EmpX{
    //必须重写clone方法，这时EmpX和TestProduced在同一个包下
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
