package com.liukuijian.bigdata.sz.chapter07

import scala.collection.mutable

object Scala20_method {
  def main(args: Array[String]): Unit = {
    val map1: mutable.Map[String, Int] = mutable.Map("a"->1,"b"->2,"c"->3)
    val map2: mutable.Map[String, Int] = mutable.Map("a"->4,"d"->5,"c"->6)

    println(map1.toList.union(map2.toList).groupBy(_._1).mapValues(_.map(_._2).sum))

//    println(map2.foldLeft(map1)((map, kv) => {
//      map(kv._1) = map.getOrElse(kv._1, 0) + kv._2; map
//    }))

    println(map2.foldLeft(map1)((map, kv) => {
      map(kv._1) = map.getOrElse(kv._1, 0) + kv._2 //让map2中的每个tuple和map1集合做合并运算，每次运算返回一个map集合
      map
    }))
  }
}
