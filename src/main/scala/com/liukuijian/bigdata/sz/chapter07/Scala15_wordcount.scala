package com.liukuijian.bigdata.sz.chapter07

object Scala15_wordcount {
  def main(args: Array[String]): Unit = {
    //TODO WordCount
    val list = List("Hello World","Hello World","Hello Spark","Hello Scala Hadoop")
    //获取单词出现次数排名前3
    println(list.flatMap(_.split(" ")) //拆分
                .groupBy(_+"") //分组
                .map(kv => (kv._1, kv._2.length)) //将kv=>(k,v)
                .toList //将Map转换为List
                .sortWith(_._2 > _._2)//排序
                .take(3)) //取3个

    //当函数给定什么值就返回什么值的场合参数不能使用下划线代替，会出现错误

  }
}
