package com.liukuijian.spark.core.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MapPartitions {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Map")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(10 to 70 by 10).flatten
    val rdd1: RDD[Int] = sc.parallelize(arr1,2)

    //一次处理一个分区的数据，按分区数建立连接，效率比较高，开发建议使用
//    val resultRDD: RDD[Int] = rdd1.mapPartitions(it => it.map(x => x*x))
//    val resultRDD: RDD[Int] = rdd1.mapPartitions(it => {//模拟生产环境
//      //建立与mysql的连接
//      //从mysql读数据
//      it
//    })
    //index是分区的索引， it是分区的迭代器
    val resultRDD: RDD[(Int, Int)] = rdd1.mapPartitionsWithIndex((index, it) => {
      it.map((index, _))
    })
    println(resultRDD.collect.mkString(","))
  }
}
