package com.liukuijian.spark.core.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CreateRDD {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("CreateRDD")
    val sc: SparkContext = new SparkContext(conf)
    val arr1: Array[Int] = Array(10 to 80 by 10).flatten
//    val rdd1: RDD[Int] = sc.parallelize(arr1)
//    val rdd1: RDD[Int] = sc.makeRDD(arr1)
    val rdd1: RDD[Char] = sc.makeRDD("hello")
    val chars: Array[Char] = rdd1.collect()
    chars.foreach(println)
    sc.stop()
  }
  /*
    RDD编程
    1、先得到一个RDD
        1) 从文件中获取， sc.textFile(...)
        2) 从现有的scala集合来获取一个RDD    makeRDD 和 paralleize
        3) 通过其他的RDD转换得到（transformation）
    2、对各种RDD做转换
        wordcount.flatMap.map.reduceByKey
    3、有一个action
   */
}
