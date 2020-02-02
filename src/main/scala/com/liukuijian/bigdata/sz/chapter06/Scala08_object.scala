package com.liukuijian.bigdata.sz.chapter06

object Scala08_object {
  def main(args: Array[String]): Unit = {
    //TODO 面向对象 -- 构造方法
    val user01 = new User08()
    val user02 = new User08("lisi",20)
  }
}
//scala中构造方法分为两大类，主构造方法&& 辅助构造方法
//在类名后增加参数列表的方式称之为主构造方法，主要用于类的初始化
//在类中声明的构造方法，称之为辅助构造构造方法，用于辅助主构造方法完成一些初始化操作，类似于java中构造方法的初始化
//辅助构造方法的声明采用关键字this来声明
//辅助构造方法不能直接构造对象，没有办法直接完成类的初始化，必须直接或间接调用主构造方法
//调用构造方法时，被调用的辅助构造方法必须提前声明，辅助构造方法的先后顺序很重要！！！
class User08(){
  var name:String = _
  var age:Int = _

  def this(name:String){
    this() //需要先使用主构造方法，再使用辅助构造方法
    this.name = name
  }
  def this(name:String, age:Int){
    this(name)//辅助构造方法需要直接或间接调用主构造方法,并且this(name)需要先定义
    this.age = age
  }
}
