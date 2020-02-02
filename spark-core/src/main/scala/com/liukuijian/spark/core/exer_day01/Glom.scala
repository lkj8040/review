package com.liukuijian.spark.core.exer_day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Glom {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Glom")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(10 to 70 by 1).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1, 3)
    val rdd2: RDD[Array[Int]] = rdd1.glom()//返回每个分区的元素集合
  }
}
