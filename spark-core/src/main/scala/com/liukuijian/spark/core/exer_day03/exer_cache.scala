package com.liukuijian.spark.core.exer_day03

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object exer_cache {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("cache")
    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(Array("ab","bc"),2)
    val rdd2: RDD[String] = rdd.flatMap(x => {
      println("flatmap...")
      x.split("")
    })
    val rdd3: RDD[(String, Int)] = rdd2.map((_,1))
//    rdd3.cache()
    val rdd4: RDD[(String, Int)] = rdd3.repartition(4) //reduceByKey会自动对shuffle操作的中间数据做持久化操作
    val rdd5: RDD[(String, Int)] = rdd4.reduceByKey(_+_).map(x=>x)
    println(rdd5.collect.mkString(","))
    println("--------------------------------我是分割线-----------------------------------------")
    println(rdd5.collect.mkString(","))
    Thread.sleep(1000000)
  }
}
