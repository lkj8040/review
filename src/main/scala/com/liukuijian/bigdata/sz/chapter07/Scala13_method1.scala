package com.liukuijian.bigdata.sz.chapter07

object Scala13_method1 {
  def main(args: Array[String]): Unit = {
    //集合常用方法
//    val list = List(1,2,3,4,4)

    //功能
//    println(list.sum)
//    println(list.min)
//    println(list.max)
//    println(list.product)//乘积

    //功能 -简单
    //TODO 是否包含指定元素
//    println(list.contains(2))

    //TODO 反转
//    println(list.reverse)

    //TODO 取数据
//    println(list.take(2))

    //TODO 从右边开始取数据
//    println(list.takeRight(2))

    //TODO 去重
//    println(list.distinct)

    //TODO 根据条件采集数据
    //list.collect()

    val list = List(1,2,3,4,4)

    println(list.sum)
    println(list.max)
    println(list.min)
    println(list.product)//乘积

    println(list.contains(3))

    println(list.reverse)

    println(list.take(2))

    println(list.takeRight(1))

    println(list.takeRight(3))

    println(list.distinct)

    println("------------------------")


  }
}
