package com.liukuijian.bigdata.sz.chapter06


object Scala09_object02 {
  def main(args: Array[String]): Unit = {
    //单例,饿汉式
    val user01 = User10.newInstance //通过伴生对象创建一个单例实例
    val user02 = User10.newInstance
    val user03 = User10.newInstance
    println(user01)
    println(user02)
    println(user03)
    println(User10.test)
    println(User10)
  }
}

class User10 private(){ //构造方法私有化
  private var name : String = "zhangsan"
}
//伴生对象可以访问伴生类中的私有内容！！！
object User10{
  //唯一的,私有的
  private val instance = new User10
  //提供公共的get方法
  def newInstance= instance

  def test = name
}