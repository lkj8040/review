package com.liukuijian.bigdata.sz.chapter11

object Scala02_Generic1 {
  def main(args: Array[String]): Unit = {

//    test1(new User())
//    test1(new Parent())
    test1(new Child())
  }
  //上限 所有范围比User小的类都可以传进来
  def test1[T<:User] (t: T):Unit={
    println(t)
  }
  //>: 下限在方法声明上不起作用
  def test2[T>:User] (t:T):Unit={
    println(t)
  }

}
//下限在类上声明有用
class Person[T >: User]{

}