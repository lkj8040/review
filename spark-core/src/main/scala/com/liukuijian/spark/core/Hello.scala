package com.liukuijian.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Hello {
  def main(args: Array[String]): Unit = {
    //1. 创建sparkcontext， 如果在linux通过spark-submit提交，一定要去掉setMaster
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("WordCount")
    val sc: SparkContext = new SparkContext(conf)
    //2. 创建RDD
    val wordCount: RDD[String] =  sc.textFile("E:\\input")
    //3. 对RDD做各种转换操作
    val result: Array[(String, Int)] = wordCount.flatMap(_.split(" "))
                                                .map((_, 1))
                                                .reduceByKey(_ + _)
                                                .sortBy(-_._2)
    //4. 执行一个行动算子
                                                .collect()

    result.foreach(println)
    //5. 关闭sparkcontext
    sc.stop()
  }
}
