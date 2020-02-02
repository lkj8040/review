package com.liukuijian.bigdata.sz.chapter07

import scala.collection.mutable.ListBuffer

object Scala06_listbuffer {
  def main(args: Array[String]): Unit = {
    //集合 - Seq - List（可变）
    val list: ListBuffer[Int] = ListBuffer(1,2,3,4)

    list.insert(1,1)

    list.update(0,7)

    list.append(5)

    list.remove(2)

    list.foreach(println)

    list.mkString(",")
  }
}
