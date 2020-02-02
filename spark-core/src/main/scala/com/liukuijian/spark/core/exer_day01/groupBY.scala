package com.liukuijian.spark.core.exer_day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object groupBY {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("GroupBy")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(0 to 10).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1, 2)
    val rdd2: RDD[(Int, Iterable[Int])] = rdd1.groupBy(x => x % 3)//x表示rdd中的数据   x%3表示生成的key，返回每个key的iterator
    println(rdd2.collect.mkString(","))
  }
}
