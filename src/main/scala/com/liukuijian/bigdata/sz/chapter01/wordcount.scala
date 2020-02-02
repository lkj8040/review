package com.liukuijian.bigdata.sz.chapter01

object wordcount {
  def main(args: Array[String]): Unit = {
    val tupleList = List(("Hello Scala Spark World", 4), ("Hello Scala Spark", 3), ("Hello Scala", 2), ("Hello", 1))
    println(tupleList.map(kv => (kv._1+" ")* kv._2)
                     .flatMap(_.split(" "))
                     .groupBy(_+"")
                     .map(x=>(x._1,x._2.length))
                     .toList
                     .sortBy(-_._2)
                     .take(3)
    )

    val tuples = List(("Hello Scala Spark World", 4), ("Hello Scala Spark", 3), ("Hello Scala", 2), ("Hello", 1))
    println(tuples.flatMap(t => t._1.split(" ").map((_, t._2)))//形成閉包
                  .groupBy(_._1)
      //对键值对每个value都应用一个函数，但是key不会发生变化。
                  .mapValues(_.map(_._2).sum)// map ->map-> list -> tuple  只取value
                  .toList
                  .sortBy(-_._2)
                  .take(3)
    )



  }
}
