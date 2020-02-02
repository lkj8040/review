package com.liukuijian.spark.core.day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object FlatMap {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Map")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(10 to 70 by 10).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1, 2)
    //得到一个新的RDD，RDD存储的是这个数和他们的平方及立方
//    val rdd2: RDD[Int] = rdd1.flatMap(x => Array(x,x*x,x*x*x))
//    val rdd2: RDD[Int] = rdd1.flatMap(x=>if(x>=50) Array(x,x*x,x*x*x) else Array[Int]())
    val rdd2: RDD[Char] = rdd1.flatMap(x=>x+"")
    rdd2.collect().foreach(println)

    sc.stop()
  }
}
