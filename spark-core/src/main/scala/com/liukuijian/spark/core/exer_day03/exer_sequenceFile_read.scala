package com.liukuijian.spark.core.exer_day03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object exer_sequenceFile_read {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("objectFile")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] = sc.sequenceFile[String, Int]("./sequence")
    rdd.collect.foreach(println)
  }
}
