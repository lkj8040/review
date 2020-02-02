package com.liukuijian.bigdata.sz.chapter02

object Scala09_datatransfer {
  def main(args: Array[String]): Unit = {
    val d = 10.0
    val i: Int = d.toInt
    val s = d.toString()
    val str = "123"
    println(str.toInt)
    println(s)
    println(i)
  }
}
