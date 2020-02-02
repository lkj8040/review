package com.liukuijian.spark.core.day02

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Aggregate")
    val sc = new SparkContext(conf)
    val rdd1: RDD[(String, Int)] = sc.makeRDD(List(("a",3),("a",2),("c",4),("b",3),("c",6),("c",8)),2)

    //1. reduceByKey
    val rdd2: RDD[(String, Int)] = rdd1.reduceByKey(_+_) //分区内聚合和分区间聚合要一致
    //2. foldByKey
    val rdd3: RDD[(String, Int)] = rdd1.foldByKey(0)(_+_) //zeroValue和value类型必须一致
    //3. aggregateByKey
    val rdd4: RDD[(String, Int)] = rdd1.aggregateByKey(0)(_+_,_+_) //分区内聚合和分区间聚合可以不同，且zeroValue类型和value类型可以不同
    //4. groupByKey
    val rdd5: RDD[(String, Int)] = rdd1.groupByKey().map({
      case (key, it) => (key, it.sum)
    })
    //5. groupBy
    val rdd6: RDD[(String, Int)] = rdd1.groupBy(_._1).map({
      case (key, it) => (key, it.map(_._2).sum)
    })
    //6. combineByKey
    val rdd7: RDD[(String, Int)] = rdd1.combineByKey(x=>x:Int,(_:Int)+(_:Int),(_:Int)+(_:Int))//zeroValue可以根据value确定

    rdd2.collect.foreach(println)
    println("----------分割线-----------")
    rdd3.collect.foreach(println)
    println("----------分割线-----------")
    rdd4.collect.foreach(println)
    println("----------分割线-----------")
    rdd5.collect.foreach(println)
    println("----------分割线-----------")
    rdd6.collect.foreach(println)
    println("----------分割线-----------")
    rdd7.collect.foreach(println)

  }
}
