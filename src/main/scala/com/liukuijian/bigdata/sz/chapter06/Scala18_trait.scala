package com.liukuijian.bigdata.sz.chapter06


object Scala18_trait {
  def main(args: Array[String]): Unit = {

    //特质从实现原理上其实就是interface和抽象类的集合体
    //所以当使用extends来混入特质时，编译时采用的是接口的实现:implements
    var emp = new Emp18
    emp.test1()
  }
}
class Parent18{
  var pname:String = "zhangsan"
}
//如果一个类只有特质没有父类，那么可以使用extends来混入特质
//如果一个类既有父类又有特质，那么使用extends来继承父类，使用with来混入特质
//可以让特质继承父类
// 1.class Emp18 extends MyTrait18
// 2.class Emp18 extends Parent18 with MyTrait18 {
// 3.trait MyTrait18 extends Parent18
class Emp18 extends MyTrait18{
  override val num:Int = 10
  def test():Unit={
  }
  override def test1():Unit={
  }
}
trait MyTrait18 extends Parent18 {
  //声明属性
  //在特质中声明属性，等同于声明属性的抽象set和get方法
  val num:Int = 20
  //声明抽象方法
  def test
  //声明完整的方法
  //在编译后，会调用一个特殊的类完成方法的调用
  def test1():Unit={
    print("xxxx")
  }
}