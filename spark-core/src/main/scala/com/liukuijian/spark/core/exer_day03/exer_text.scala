package com.liukuijian.spark.core.exer_day03

import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object exer_text {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("jdbc")
    val sc: SparkContext = new SparkContext(conf)
    //文件大小分别为 5 5 6
    // goalSize = totalSize / numSplits = totalSize / minPartitions = (16) / 4 = 4
    val rdd = sc.textFile("d:/input",4)
    // 每个文件至少一个分区，又每个文件大小不超过4字节，因此分6片
    println(rdd.getNumPartitions)

  }
}
