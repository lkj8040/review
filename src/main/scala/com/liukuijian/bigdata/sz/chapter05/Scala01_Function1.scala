package com.liukuijian.bigdata.sz.chapter05

object Scala01_Function1 {
  def main(args: Array[String]): Unit = {
    //Scala多范式，既是完全的面向对象的语言，也是函数式编程语言
    //Scala是一个完全面向函数式编程的语言

    //声明函数：def 函数名(参数名1:参数类型1,参数名2:参数类型2) :函数返回值类型 ={函数体}
    //函数 & 方法
    //类中的函数就是方法
    //Scala可以在任意的语法结构嵌套语法结构，函数(方法)中可以声明函数
    def test():Unit ={
      println("xxxx")
    }
    test()

    def f4( sex : String = "男", name : String ): Unit = {
      println(name + " " + sex)
    }
    // Scala函数中参数传递是，从左到右
    f4(name="wusong")


  }
}
