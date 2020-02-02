package com.liukuijian.spark.core.day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object SortBy {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Sample")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: List[Int] = List(10,10,20,20,15,16)
    val rdd1: RDD[Int] = sc.parallelize(arr1, 2)

    val rdd2: RDD[Int] = rdd1.sortBy(x=>x, ascending=false) //ascending=false表示降序
    println(rdd2.collect().mkString(","))
  }
}
