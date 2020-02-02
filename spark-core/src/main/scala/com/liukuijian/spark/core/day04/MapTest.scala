package com.liukuijian.spark.core.day04

object MapTest {
  def main(args: Array[String]): Unit = {
    var map: Map[String, Int] = Map("a"->1,"b"->2)
    map += "a" ->(map.getOrElse("a",0) + 1)
    
    println(map.mkString(","))
  }
}
