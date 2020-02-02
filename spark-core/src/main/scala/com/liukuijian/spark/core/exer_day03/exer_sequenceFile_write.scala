package com.liukuijian.spark.core.exer_day03

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object exer_sequenceFile_write {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("objectFile")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] = sc.parallelize(Array(("lisi",10),("zhangsan",20),("wangwu",18)),2)
    rdd.saveAsSequenceFile("./sequence")
  }
}
