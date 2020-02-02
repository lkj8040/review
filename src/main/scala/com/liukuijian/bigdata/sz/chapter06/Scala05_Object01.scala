package com.liukuijian.bigdata.sz.chapter06

object Scala05_Object01 {
  def main(args: Array[String]): Unit = {
    val user:User06 = new User06()
    user.name = "lisi"
    println(user.name)
    new User06()
    //Scala万物皆对象，万物皆函数
    //声明一个类等同于声明函数
    //构建对象时，类的主体内容会执行，完成类的初始化
  }
}
//声明类
//声明构造方法，在类名后增加参数列表
class User06(){
  //TODO 类的主体
  //TODO 函数的主体
  println("user06...") //这就是在创建对象，实现类的初始化

  def User06:Unit ={//这是一个普通方法，不是构造器
    println("xxxx")
  }
  var name = "zhangsan" //实际上是在初始化属性
}
