package com.liukuijian.bigdata.sz.chapter02

object Scala09_datatype {
  def main(args: Array[String]): Unit = {
    val b : Byte = 127
    val a : Long = 127L
    val c = 1.0f
    val cc: Char = 'A' + 1//‘A’和1都是常量，在编译时会自动进行计算，因此是可行的
    println(cc)
  }
}
