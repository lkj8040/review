package com.liukuijian.spark.core.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Union {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Union")
    val sc: SparkContext = new SparkContext(conf)
    val arr1= List(10 to 21).flatten
    val arr2: List[Int] = List(15 to 25).flatten
    val rdd1= sc.parallelize(arr1, 2)
    val rdd2= sc.parallelize(arr2, 2)

    //并集
//    val rdd3: RDD[Int] = rdd1.union(rdd2)
    val rdd3: RDD[Int] = rdd1 ++ rdd2 //底层使用union

    //交集
    val rdd4: RDD[Int] = rdd1.intersection(rdd2)

    //差集
    val rdd5: RDD[Int] = rdd1.subtract(rdd2)

    //拉链操作 -- 常用
    val rdd6: RDD[(Int, Int)] = rdd1.zip(rdd2) //对于每个RDD：①每个分区内的元素个数必须相等 ②分区数也必须相等  =>元素的总数一样

    //元素和自己的索引做拉链
    val rdd7: RDD[(Int, Long)] = rdd1.zipWithIndex()

    //分区数一样就可以做拉链
    val rdd8: RDD[(Int, Int)] = rdd1.zipPartitions(rdd2)((it1,it2)=>{it1.zipAll(it2,-1,-2)})//多余的地方补默认值
    val rdd9: RDD[(Int, Int)] = rdd1.zipPartitions(rdd2)((it1,it2)=>{it1.zip(it2)})//多余的扔掉

    println(rdd3.collect.mkString(","))
    println(rdd4.collect.mkString(","))
    println(rdd5.collect.mkString(","))
    println(rdd6.collect.mkString(","))
    println(rdd7.collect.mkString(","))
    println(rdd8.collect.mkString(","))
    println(rdd9.collect.mkString(","))

    sc.stop()
  }
}
