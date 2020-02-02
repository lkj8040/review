package com.liukuijian.bigdata.sz.chapter06

object Scala25_enum {
  def main(args: Array[String]): Unit = {
    println(Color.RED)
  }
}
object  Color extends Enumeration{
  val RED = Value(1,"red")
  val YELLOW = Value(2,"yellow")
}
object MyApp extends App{
  println("xxxx")
}