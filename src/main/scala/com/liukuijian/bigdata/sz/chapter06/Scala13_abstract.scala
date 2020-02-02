package com.liukuijian.bigdata.sz.chapter06

object Scala13_abstract {
  def main(args: Array[String]): Unit = {
    new User13().test
    var user = new User13
    user.test1
  }
}
//声明抽象类：声明类时，在class前使用关键字abstract
abstract class Parent13{
  //声明普通方法
  def test():Unit={
    println("parent...")
    test()
  }
  //声明抽象方法：只有声明，没有实现
  //只要方法声明不完整，那么就是抽象的，所以不需要使用abstract关键字
  def test1
}
//子类如果继承抽象类，那么必须重写抽象方法，或声明为抽象类
//scala中重写分为两种情况，第一种是抽象方法的重写，第二种是完整方法的重写
//1)抽象方法的重写：只要将方法补充完整就可以，不用增减任何的修饰符
//2)完整方法的重写：为了明确方法是用于重写，所以需要增加修饰符override
class User13 extends Parent13{
  //完整方法的重写：需要加override
  override def test() ={
    println("user...")
  }
  //抽象方法的重写：将抽象方法补充完整
  def test1() ={
    println("xxxx")
  }
}