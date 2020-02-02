package com.liukuijian.bigdata.sz.chapter05

object Scala09_test {
  def main(args: Array[String]): Unit = {
    //f(10)(20)(xxx) =>200

    //柯里化方式
    def f(x:Int)(y:Int)(f1:(Int,Int)=>Int):Int={
      f1(x,y)
    }
    println(f(10)(20)(_*_))
    println(f(10)(20)(
      (x,y) => {
        (y + (x+"").substring(1)).toInt
      }
    ))

  }
}
