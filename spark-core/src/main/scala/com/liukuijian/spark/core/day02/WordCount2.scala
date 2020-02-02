package com.liukuijian.spark.core.day02

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object WordCount2 {
  def main(args: Array[String]): Unit = {
    /*
    1516609143867 6 7 64 16
    1516609143869 9 4 75 18
    1516609143869 1 7 87 12
    计算每个省份的广告点击数top3的广告及他们的点击次数
     */
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Aggregate")
    val sc = new SparkContext(conf)
    //分隔取数据
    val rdd1: RDD[((String, String), Int)] = sc.textFile("D:/agent.log").map(
      line => {
        val split: Array[String] = line.split(" ")
        ((split(1), split(4)), 1)
      })
    //每个省份每个广告的总点击次数
    val rdd2: RDD[((String, String), Int)] = rdd1.reduceByKey(_+_)
    //每个省份的所有广告的点击次数
    val rdd3: RDD[(String, Iterable[(String, Int)])] = rdd2.map{
        case ((pro, ad), count) => (pro, (ad, count))
    }.groupByKey()
    //对每个省份的广告点击次数进行排名，取前3名
    val rdd4: RDD[(String, List[(String, Int)])] = rdd3.map {
        case (pro, it) => (pro, it.toList.sortBy(_._2)(Ordering.Int.reverse).take(3))
    }
    val result: RDD[(String, List[(String, Int)])] = rdd4.sortBy(_._1.toInt)

    result.collect.foreach(println)
  }
}
