package com.liukuijian.spark.core.exer_day02

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object exer2 {
  def main(args: Array[String]): Unit = {
    /*
1516609143867 6 7 64 16
1516609143869 9 4 75 18
1516609143869 1 7 87 12
计算每个省份的广告点击数top3的广告及他们的点击次数
 */
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Aggregate")
    val sc = new SparkContext(conf)
    var rdd1: RDD[((String, String), Int)] = sc.textFile("D:/agent.log").map {
      case line => {
        val split: Array[String] = line.split(" ")
        ((split(1), split(4)), 1)
      }
    }
    val rdd2: RDD[((String, String), Int)] = rdd1.reduceByKey(_+_)
    val rdd3: RDD[(String, Iterable[(String, Int)])] = rdd2.map {
      case ((pro, ad), count) => (pro, (ad, count))
    }.groupByKey()
    val rdd4: RDD[(String, List[(String, Int)])] = rdd3.map {
      case (pro, it) => (pro, it.toList.sortBy(_._2)(Ordering.Int.reverse).take(3))
    }
    val rdd5: RDD[(String, List[(String, Int)])] = rdd4.sortBy(_._1.toInt)
    rdd5.collect.foreach(println)
  }
}
