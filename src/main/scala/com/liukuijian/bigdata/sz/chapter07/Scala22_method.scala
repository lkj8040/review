package com.liukuijian.bigdata.sz.chapter07

object Scala22_method {
  def main(args: Array[String]): Unit = {
    val result1 = (0 to 100).map{
      case _ => Thread.currentThread.getName
    }
    //par:并行
    val result2 = (0 to 100).par.map{case _ => Thread.currentThread.getName}
    println(result1)
    println(result2)
  }
}
