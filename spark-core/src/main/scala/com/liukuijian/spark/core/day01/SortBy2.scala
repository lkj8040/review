package com.liukuijian.spark.core.day01

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

object SortBy2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Sample")
    val sc: SparkContext = new SparkContext(conf)
    val arr1= List("hello","abc","aaa","abcde")
    val rdd1= sc.parallelize(arr1, 2)
    //先按长度升序，长度相同再按字典降序
    val rdd2: RDD[String] = rdd1.sortBy(x =>(x.length,x))(Ordering.Tuple2(Ordering.Int,Ordering.String.reverse),ClassTag(classOf[(Int,String)]))
    println(rdd2.collect().mkString(","))
    sc.stop()
  }
}
/*
Ordering -> Comparator 定制排序 compare
Ordered -> Comparable 自然排序 compareTo
*/
