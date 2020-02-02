package com.liukuijian.bigdata.sz.chapter05

object TestControl {
  def main(args: Array[String]): Unit = {

    // （1）传递代码块
//    foo({
//      println("aaa")
//    })

    // （2）函数参数只有一个，小括号可以省略
    foo{
      println("aaa")
    }
    //输出
//    aaa
//    ()
//    aaa
//    ()
  }

  def foo(a: =>Unit):Unit = {
    println(a)
    println(a)
  }



}
