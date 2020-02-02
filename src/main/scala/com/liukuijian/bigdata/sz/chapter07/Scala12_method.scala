package com.liukuijian.bigdata.sz.chapter07

object Scala12_method {
  def main(args: Array[String]): Unit = {
//    //集合的常用方法
//    val list = List(1,2,3,4)
//
//    //length和size是一回事
////    println(list.length)
////    println(list.size)
//
//    //组成部分
//    //list :+ 5
//    println(list.head) //head是头
//    println(list.tail) //tail是尾集合
//    println(list.last) //last是尾
//    println(list.init) //init是头集合
//
//    //循环
//    list.iterator
//    list.productIterator
//    list.foreach(println)
//    list.mkString(",")

    val list1 = List(1,2,3,4)
    println(list1.size) //长度为4
    println(list1.length) //长度为4

    println(list1.head)//头为1
    println(list1.tail)//非头即尾，取尾
    println(list1.last)//真正的尾
    println(list1.init)//非头即尾，取头

    list1.productIterator
    list1.foreach(println)
    list1.mkString(",")

  }
}
