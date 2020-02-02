package com.liukuijian.bigdata.sz.chapter08

object Scala10_implicit {
  def main(args: Array[String]): Unit = {
    //隐式函数
    //当编译器发现对象没有想要的功能时，就会在作用域范围内尝试调用隐式函数将对象进行转换,这个调用过程由编译器完成
    //convert(2).myMax(6)
    implicit def convert(arg:Int):MyRichInt={
      new MyRichInt(arg)
    }
    println(2.myMax(6))
    println(2.myMin(6))
  }
}
class MyRichInt(val self: Int){
  def myMax(i:Int): Int ={
    if(self < i ) i else self
  }
  def myMin(i:Int):Int={
    if(self < i) self else i
  }
}
