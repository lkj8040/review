package com.liukuijian.spark.core.exer_day02

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object exer1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("exer1")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[(String, Int)] = sc.makeRDD(List(("spark",2),("hadoop",6),("hadoop",4),("spark",6)))
    //键值对的key表示图书名称，value表示某天图书销量，请计算每个键对应的平均值，也就是计算每种图书的每天平均销量。
    val rdd2: RDD[(String, Iterable[Int])] = rdd1.groupByKey()
    val rdd3: RDD[(String, Double)] = rdd2.map({
      case (book, it) => (book, it.sum.toDouble / it.size)
    })
    rdd3.collect.foreach(println)
  }
}
