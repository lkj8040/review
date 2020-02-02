package com.liukuijian.bigdata.sz.chapter06

object Scala12_object {
  def main(args: Array[String]): Unit = {
    new User12()
    //111
    //222
    //333
    //444
  }
}
class Parent12(){
  println("111")
  def this(name:String){
    this()
    println("222")
  }
}
class User12(name:String) extends Parent12(name){
  println("333")
  def this(){
    this(name)
    println("444")
  }
}
