package com.liukuijian.spark.core.exer_day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

object SortBy {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Sample")
    val sc: SparkContext = new SparkContext(conf)
    val arr1= List("hello","abc","aaa","abcde")
    val rdd1= sc.parallelize(arr1, 2)

    //实现先按长度排序，再按字典逆序排
    val rdd2: RDD[String] = rdd1.sortBy(x=>(x.length,x))(Ordering.Tuple2(Ordering.Int,Ordering.String.reverse), ClassTag(classOf[(Int, String)]))
    println(rdd2.collect.mkString(","))
    val rdd3: RDD[String] = rdd1.sortBy(x=>(x.length,x), ascending = true)//升序排
    println(rdd3.collect.mkString(","))
  }
}
