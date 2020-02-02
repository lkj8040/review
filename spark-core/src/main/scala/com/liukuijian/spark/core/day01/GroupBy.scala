package com.liukuijian.spark.core.day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object GroupBy {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("GroupBy")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(0 to 10).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1, 2)
    val rdd2: RDD[(Int, Iterable[Int])] = rdd1.groupBy(x => x % 3) //宽依赖  shuffle需要磁盘io，导致效率下降
    rdd2.collect().foreach(println)
    sc.stop()
  }
}
