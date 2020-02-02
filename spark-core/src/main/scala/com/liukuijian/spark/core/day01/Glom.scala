package com.liukuijian.spark.core.day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Glom {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Glom")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(10 to 70 by 10).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1, 2)
    val rdd2: RDD[Array[Int]] = rdd1.glom()
    rdd2.collect().foreach(x => println(x.mkString(",")))
    sc.stop()
  }
}
