package com.liukuijian.spark.core.exer_day03

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object exer_serializable {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      .setAppName("SerDemo")
      .setMaster("local[*]")
      // 替换默认的序列化机制 可以省(如果调用registerKryoClasses)
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      // 注册需要使用 kryo 序列化的自定义类
      .registerKryoClasses(Array(classOf[Searcher]))

    val sc = new SparkContext(conf)
    val rdd: RDD[String] = sc.parallelize(Array("hello world", "hello java", "spark", "hahah"), 2)
    val searcher = new Searcher("hello")
    val result: RDD[String] = searcher.getMatchedRDD2(rdd)
    result.collect.foreach(println)
  }
}
class Searcher(val query: String) extends Serializable {
  // 判断 s 中是否包括子字符串 query
  def isMatch(s: String) = {
    s.contains(query)
  }
  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD1(rdd: RDD[String]) = {
    rdd.filter(isMatch) //
  }

  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD2(rdd: RDD[String]) = {
    rdd.filter(_.contains(query))
  }
}
