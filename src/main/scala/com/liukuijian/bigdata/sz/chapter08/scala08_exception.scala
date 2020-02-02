package com.liukuijian.bigdata.sz.chapter08

import java.io.FileInputStream

object scala08_exception {
  def main(args: Array[String]): Unit = {
    //TODO 异常
    //Scala中的异常采用的也是模式匹配
    //scala中异常没有分类，所以不需要在编译时提示
    //而且也不需要显式地使用try，catch进行捕捉
    //一般将范围小的异常放前面，范围大的异常放后面
//    val in:FileInputStream = new FileInputStream("xxx")
    val i = 0
    try{
      val s = 10 / i
    }catch {
      case e:ArithmeticException => println("除数为0")
      case e:Exception => println("其他异常")
    }finally{

    }
    def test():Nothing = {
      throw new Exception("不对")
    }
    println(test)
  }
}
