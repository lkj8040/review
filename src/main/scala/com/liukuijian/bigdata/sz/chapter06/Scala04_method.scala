package com.liukuijian.bigdata.sz.chapter06

object Scala04_method {
  def main(args: Array[String]): Unit = {
    val emp = new Emp1()
    emp.test()
  }
}
class Emp1{
  //声明方法
  //方法可以重载和重写
  def test():Unit={
    println("xxxx")
  }
  //方法重载
  def test(s:String)={
    println(s+"xxxxx")
  }
}