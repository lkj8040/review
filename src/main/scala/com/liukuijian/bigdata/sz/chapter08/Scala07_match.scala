package com.liukuijian.bigdata.sz.chapter08

object Scala07_match {
  def main(args: Array[String]): Unit = {
    //TODO 模式匹配
    //偏函数：函数只对满足条件的数据进行操作，并不会对所有的数据进行操作

    val list = List(1,2,3,4,5,6,"test")

    //采集
    //collect 方法可以对部分数据进行采集
    val list1: List[Int] = list.collect {
      case num: Int => num + 1 //偏函数
    }
    println(list1)

    //map方法只能对全量数据进行操作，不支持偏函数
    println(list.filter(_.isInstanceOf[Int]).map(_.asInstanceOf[Int] + 1))



  }
}
