package com.liukuijian.spark.core.day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Filter {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Map")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(10 to 70 by 10).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1, 2)

    val rdd2: RDD[Int] = rdd1.collect {
      case x: Int if x > 30 => x + 10
    }

    rdd2.foreach(println)
  }
}
