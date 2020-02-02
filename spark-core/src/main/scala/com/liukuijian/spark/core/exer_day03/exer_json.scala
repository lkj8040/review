package com.liukuijian.spark.core.exer_day03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object exer_json {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("objectFile")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[String] = sc.textFile("./json")
    import scala.util.parsing.json.JSON
    val rdd2: RDD[Option[Any]] = rdd.map(JSON.parseFull) //解析json格式的文件
    rdd2.collect.foreach(println)
  }
}
