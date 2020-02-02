package com.liukuijian.spark.core.exer_day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Coalesce {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Coalesce")
    val sc = new SparkContext(conf)
    val rdd1: RDD[Int] = sc.makeRDD(List(10,10,20,20,15,16),2)
    println(rdd1.getNumPartitions)
//    val rdd2: RDD[Int] = rdd1.coalesce(4)
//    println(rdd2.getNumPartitions)
//    val rdd3: RDD[Int] = rdd1.coalesce(4, true) //强制增加分区数，会shuffle
//    println(rdd3.getNumPartitions)
    val rdd4: RDD[Int] = rdd1.repartition(4)//可以增加分区数
    println(rdd4.getNumPartitions)
  }
}
