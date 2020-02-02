package com.liukuijian.spark.core.day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Sample {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Sample")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(0 to 100).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1, 2)

    //withReplacement是否放回,false表示不放回 fraction表示抽取比例，如果是true[0,无穷) flase[0,1]
    val rdd2: RDD[Int] = rdd1.sample(false, 0.1)
    println(rdd2.collect.mkString(","))
    sc.stop
  }
}
