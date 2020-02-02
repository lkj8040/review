package com.liukuijian.bigdata.sz.chapter05

object Scala08_function_lazy {

  def main(args: Array[String]): Unit = {
    //惰性函数 ---> 延迟加载
    //当函数的返回值被声明为lazy时，函数的执行将被推迟，直到使用时才会加载

    lazy val res = sum(10,30)
    println("------------")
    println("res=" + res)//直到使用res时才会调用sum函数，因此会先打印出-------------,再调用sum函数

  }

  def sum(i: Int, i1: Int):Int ={
    println("sum...")
    i + i1
  }
}
