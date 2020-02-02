package com.liukuijian.bigdata.sz.chapter07

object WordCountTest {
  def main(args: Array[String]): Unit = {
    val list = List(
      ("Hello Hadoop Hive Kafka", 4),
      ("Hello Hadoop Hive", 3),
      ("Hello Hadoop ", 2),
      ("Hello", 1)
    )
    //方法一
    println(list.flatMap(kv => kv._1.split(" ").map(_->kv._2)) //类似闭包，里层转换使用外层变量
                .groupBy(_._1)
                .mapValues(_.map(_._2).sum) //key不变，只取value做运算
                .toList
                .sortBy(-_._2) //desc排序
                .take(3))//取3个

    //方法二
    println(list.map(kv=>(kv._1 + " ")*kv._2) //"java " *3 表示"java java java "
                .flatMap(_.split(" "))  //切割
                .groupBy(_ + "") //直接使用 _ 会报错，需要做一个转换才让使用_，加个空串
                .map(kv=>kv._1->kv._2.length)
                .toList
                .sortBy(-_._2)
                .take(3))
  }
}
