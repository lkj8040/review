package com.liukuijian.spark.core.day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Coalease {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Sample")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: List[Int] = List(10,10,20,20,15,16)
    val rdd1: RDD[Int] = sc.parallelize(arr1, 2)
    println(rdd1.getNumPartitions)
//    val rdd2: RDD[Int] = rdd1.coalesce(4) //没有增加分区数
//    val rdd2: RDD[Int] = rdd1.coalesce(4,true) //如果打开shuffle就可以增加分区
    val rdd2: RDD[Int] = rdd1.repartition(4) //一定会shuffle，底层是调用coalesce
    println(rdd2.getNumPartitions)
    sc.stop()
  }
}
/*
coalesce一般用来减少分区，默认shuffle=false
如果希望减少分区，尽量避免shuffle

repartition只能用来增加分区，限定shuffle=true

如果一个分区中的元素进入多个分区，那么就会shuffle
如果只进入一个分区，那么就不会shuffle，这样的好处是不需要磁盘io
 */
