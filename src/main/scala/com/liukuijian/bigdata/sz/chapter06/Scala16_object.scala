package com.liukuijian.bigdata.sz.chapter06

object Scala16_object {
  def main(args: Array[String]): Unit = {
    //有两种构建对象的方法
    //1、使用new构建对象时，等同于调用类的构造方法
    var user1 = new User16()
    //2、scala中还可以使用伴生对象的apply方法来直接构建对象
    var user2 = User16.apply
    //scala可以自动识别伴生对象的apply方法，所以apply方法可以省略
    var user3 = User16()
    //apply方法不能省略参数列表的小括号，否则返回的是伴生对象
    var user4 = User16
    //apply可以返回不同类型的对象
    var user5 = User16("lisi")
    println(user1)
    println(user2)
    println(user3)
    println(user4)
    println(user5)
  }
}
class User16(private var name:String="zhangsan"){
}
object User16{
  def apply(): User16 = new User16(name)
  def apply(name: String) :String = name
}