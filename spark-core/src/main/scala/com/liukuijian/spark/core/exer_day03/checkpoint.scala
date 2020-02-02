package com.liukuijian.spark.core.exer_day03

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object checkpoint {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("checkpoint")
    val sc: SparkContext = new SparkContext(conf)
    sc.setCheckpointDir("./checkpoint")

    val rdd: RDD[String] = sc.parallelize(Array("abc"))
    val rdd2: RDD[(String, Long)] = rdd.map((_,System.currentTimeMillis()))
    rdd2.cache()
    rdd2.checkpoint() //checkpoint会自己启动一个job并将结果存储到磁盘
    //checkpoint和cache搭配使用，checkpoint就直接将内存中的结果拿过来用
    rdd2.collect().foreach(println)
    rdd2.collect().foreach(println)
    rdd2.collect().foreach(println)

    Thread.sleep(100000)

  }
}
