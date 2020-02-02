package com.liukuijian.bigdata.sz.chapter07

import scala.collection.mutable

object Scala11_tuple2 {
  def main(args: Array[String]): Unit = {
    //如果元组中元素的数量只有两个，那么将这样的元组称之为对偶元组
    //也称之为 键值对
//    val tuple = ("a",1)
//
//    val tuple1: (String, Int) = "b" -> 2
//
//    val map = mutable.Map(("a",1), ("b",2))
//
//    for(kv <- map){
//      print(kv._1 + ", ")
//      println(kv._2)
//    }

    val tuple1: (String, Int) = ("a",1)
    val tuple2: (String, Int) = "b" -> 2
    val map: mutable.Map[String, Int] = mutable.Map(tuple1,tuple2)

    for(kv <- map){
      print(kv._1 + "->")
      println(kv._2)
    }
    println(map)

  }
}
