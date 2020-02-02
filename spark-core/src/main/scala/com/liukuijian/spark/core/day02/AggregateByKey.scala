package com.liukuijian.spark.core.day02

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object AggregateByKey {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Aggregate")
    val sc = new SparkContext(conf)
    val rdd1: RDD[(String, Int)] = sc.makeRDD(List(("a",3),("a",2),("c",4),("b",3),("c",6),("c",8)))

    //按key求每个分区的最大值和最小值，并最终求和
    val rdd2: RDD[(String, (Int, Int))] =
      rdd1.aggregateByKey((Int.MinValue,Int.MaxValue))((x,y)=>(x._1.max(y),x._2.min(y)),(x,y)=>(x._1+y._1,x._2+y._2))

    rdd2.collect.foreach(println)

    println("---------------------------我是分割线------------------------------")
    //按key求平均值
    val rdd3: RDD[(String, Double)] = rdd1.aggregateByKey((0, 0))(
      {
        case ((sum, count), value) => (sum + value, count + 1)
      },
      {
        case ((sum1, count1), (sum2, count2)) => (sum1 + sum2, count1 + count2)
      }
    ).map{
      case (key, (sum, count)) => (key, sum.toDouble / count)
    }
    rdd3.collect().foreach(println)

    sc.stop()
  }
}
