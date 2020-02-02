package com.liukuijian.spark.core.exer_day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object sample {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Sample")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(0 to 100).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1, 2)

    val rdd2: RDD[Int] = rdd1.sample(false, 0.1)//withReplacement=false表示不放回，fraction=0.1表示抽10%
    //seed一般不用设置
    println(rdd2.collect.mkString(","))
  }
}
