package com.liukuijian.spark.core.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Map {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Map")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(10 to 80 by 10).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1,2)
    //得到新的RDD
    rdd1.map(x=>x*x).collect.foreach(println)


  }
}
