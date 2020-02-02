package com.liukuijian.bigdata.sz.chapter08

object Scala09_implicit {
  def main(args: Array[String]): Unit = {
    //隐式值
    implicit var name:String = "zhangsan"

    //隐式参数
     def printName(implicit name: String ="lisi"): Unit ={
      println(name)
    }
    printName//编译器编译时发现没有传递参数，那么默认会去找隐式值将隐式值赋值给函数声明中的参数
    printName()
  }
}
