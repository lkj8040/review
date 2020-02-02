package com.liukuijian.bigdata.sz.chapter02

object Scala10_operator {
  def main(args: Array[String]): Unit = {
    val s = new String("abc")
    val c = new String("abc")
    println(s == c)//等效于java的equals
    println(s.eq(c))//等效于java的==


  }
}
