package com.liukuijian.bigdata.sz.chapter02

object Scala06_String {
  def main(args: Array[String]):Unit={
    val name: String = "zhangsan"
    val age: Int = 20
    println("\"username\"")
    //插值字符串
    print(s"username=$name")
  }
}
