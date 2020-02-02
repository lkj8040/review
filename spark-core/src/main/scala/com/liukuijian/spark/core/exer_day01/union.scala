package com.liukuijian.spark.core.exer_day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object union {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Union")
    val sc: SparkContext = new SparkContext(conf)
    val arr1= List(10 to 15).flatten
    val arr2: List[Int] = List(25 to 26).flatten
    val rdd1= sc.parallelize(arr1, 2)
    val rdd2= sc.parallelize(arr2, 2)

//    val rdd3: RDD[Int] = rdd1.union(rdd2)
//    println(rdd3.collect().mkString(","))
//
//    val rdd4: RDD[Int] = rdd1.intersection(rdd2)//交集
//    println(rdd4.collect().mkString(","))
//
//    val rdd5: RDD[Int] = rdd1.subtract(rdd2)//差集
//    println(rdd5.collect().mkString(","))
//
//    val rdd6: RDD[(Int, Int)] = rdd1.zip(rdd2) //分区数和每个分区的元素个数都一样！！！
//    println(rdd6.collect().mkString(","))

//      val rdd3: RDD[(Int, Long)] = rdd1.zipWithIndex() //和自己在分区内的索引组合成tuple
//    println(rdd3.collect().mkString(","))

    //要求分区数一样！！！
    val rdd8: RDD[(Int, Int)] = rdd1.zipPartitions(rdd2)((it1, it2) => {
//      it1.zipAll(it2, -1, -2) //总是长的为基础，不够的用-1和-2补全
      it1.zip(it2) //多余的扔掉
    })
    println(rdd8.collect().mkString(","))
  }
}
