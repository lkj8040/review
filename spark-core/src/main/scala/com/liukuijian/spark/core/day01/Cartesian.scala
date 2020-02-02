package com.liukuijian.spark.core.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Cartesian {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Union")
    val sc: SparkContext = new SparkContext(conf)
    val arr1= List(10 to 11).flatten
    val arr2: List[Int] = List(15 to 16).flatten
    val rdd1= sc.parallelize(arr1, 2)
    val rdd2= sc.parallelize(arr2, 2)

    //笛卡尔积
    val rdd3: RDD[(Int, Int)] = rdd1.cartesian(rdd2)
    println(rdd3.collect.mkString(","))
  }
}
