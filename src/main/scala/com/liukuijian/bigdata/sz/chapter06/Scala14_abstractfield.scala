package com.liukuijian.bigdata.sz.chapter06

object Scala14_abstractfield {
  def main(args: Array[String]): Unit = {

    //匿名子类
    new Parent14 {
      override var age: Int = 10
    }
    //多态:scala中的多态不能省略类型！！！
    //如果省略类型，就默认为子类类型，不构成多态了
    val person:Parent14 = new User14

    var user14:Parent14 = new User14
    println(user14.name)
  }
}
abstract class Parent14(){
  //完整属性
  val name: String = "lisi"

  //抽象属性：只有声明，而没有初始化
  //编译时，不会生成类的私有属性，而是会生成属性的对应的set和get方法，但是都是抽象的
  //scala中的属性(底层是属性对应的set和get方法)和方法都是动态绑定的，而java只有方法是动态绑定的
  var age:Int
}
class User14 extends Parent14{
  //抽象属性的重写：将抽象属性补充完整
  var age:Int = 10

  //完整属性的重写：使用override修饰
  //可变的变量不能被重写，只有val才能被重写!!!!
  override val name:String = "zhangsan"
}