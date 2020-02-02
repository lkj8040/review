package com.liukuijian.bigdata.sz.chapter06

import scala.util.control.Breaks

object Scala01_object {
  def main(args: Array[String]): Unit = {
    //面向对象
    //Scala创建对象
    var user : User = new User()
    println(user.name)
    println(user.login())
  }
}
//声明类
class User{
  //声明属性/变量
  var name:String ="zhangsan"
  //声明方法
  def login():Boolean={
    true
  }
}