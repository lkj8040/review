package com.liukuijian.spark.core.exer_day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object MapPartitions {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Map")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(10 to 70 by 10).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1,2)
    val rdd2: RDD[Int] = rdd1.mapPartitions(it => {//按分区进行map操作，Iterator[T] => Iterator[U]
      it.map(_ * 2)
    })

    println(rdd2.collect().mkString(","))

    println(rdd1.mapPartitionsWithIndex((index, it) => {//分区索引和迭代器
      it.map((_, index))
    }).collect().mkString(","))
  }
}
