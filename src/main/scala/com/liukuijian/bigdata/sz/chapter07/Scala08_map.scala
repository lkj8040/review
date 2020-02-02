package com.liukuijian.bigdata.sz.chapter07

import scala.collection.mutable

object Scala08_map {
  def main(args: Array[String]): Unit = {
//    //集合 - Map(不可变)
//    //Map集合中保存的是键值对K-V对象
//    //Scala中键值对使用特殊符号声明: K->V
//    val map: Map[String, Int] = Map(("a",1), ("b",2),"c"-> 3)
//    println(map.mkString(":"))
//    //集合 - Map(可变)
//    val mumap: mutable.Map[Any, Int] = mutable.Map(("a",1), ("b",2),"c"-> 3)
//    mumap.put("d",4)
//    mumap.update("d",5)
//    mumap("d") = 6
//    mumap.remove("d")
//
//    println(mumap)
//    println(mumap.foreach(println))

    val map1: Map[String, Int] = Map("a"->1,"b"->2,"c"->3)//Map(a -> 1, b -> 2, c -> 3)
    println(map1)

    val mumap: mutable.Map[String, Int] = mutable.Map()
    mumap.put("a",1)
    println(mumap)
    mumap.update("a",10)
    println(mumap)
    mumap("a")=20
    println(mumap)
    val maybeInt: Option[Int] = mumap.remove("a")
    println(maybeInt)
    maybeInt.getOrElse(0)

  }
}
