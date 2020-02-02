package com.liukuijian.bigdata.java.sz;

import java.util.ArrayList;
import java.util.List;

public class TestGerneric {
    public static void main(String[] args) {
        List userTests = new ArrayList();
        userTests.add(new EmpTest());
        //java中集合泛型不可变，没有继承
        //泛型是对数据类型的约束
        //List<UserTest> user = new ArrayList<ChildTest>(); //error

        //泛型无法对声明之前的数据做约束
        //只能对声明之后的操作进行类型约束
        List<UserTest> userList = userTests;

        System.out.println(userList);
        //泛型一般在进行类型操作的时候会出错，如果不进行类型操作，那么就不会有问题
        for (UserTest user: userList){
            System.out.println(user);//报错
        }
    }
}
class ParentTest{

}
class UserTest extends ParentTest{

}
class ChildTest extends UserTest{

}
class EmpTest{

}

