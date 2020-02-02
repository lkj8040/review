package com.liukuijian.bigdata.sz.chapter11

object Scala01_Generic {
  def main(args: Array[String]): Unit = {
    //scala中泛型默认也是不可变的
    //但scala支持泛型可变，
    //+T => 泛型协变
    //-T => 泛型逆变
//    val test : Test[User] = new Test[Child]() //+T
//    val test : Test[User] = new Test[Parent]() //-T
//    println(test)
  }
}
class Parent {}
class User extends Parent {}
class Child extends User {}
class Test[-T] {}

