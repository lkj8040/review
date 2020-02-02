package com.liukuijian.spark.core.exer_day03

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object exer_MyPartitioner{
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("objectFile")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(Int, String)] = sc.parallelize(Array((11, "a"), (21, "b"), (31, "c"), (40, "d"), (50, "e"), (60, "f")),3)

    val rdd2: RDD[(Int, String)] = rdd.partitionBy(new exer_MyPartitioner(2)).partitionBy(new exer_MyPartitioner(2))

    val rdd3: RDD[(Int, String)] = rdd2.mapPartitionsWithIndex((index, it) => it.map{ case (key, value) => (index, key+":"+value)})

    println(rdd3.collect.mkString(","))

    Thread.sleep(1000000)
  }
}
class exer_MyPartitioner(val partitions:Int) extends Partitioner{
  override def numPartitions: Int = partitions //获取分区数

  override def getPartition(key: Any): Int =  //根据key的hash值去计算分区号
    key match{
      case null => 0
      case _ => math.abs(key.hashCode) % numPartitions
    }

  //分区器的类型和分区器的分区数相同，那么就认为是相同的分区方式
  //先用hashCode判断分区数是否相等，如果不相等直接返回false
  //再用equals方法判断类型是否相同
  override def hashCode(): Int = partitions //分区器的分区数和分区方法相同

  override def equals(obj: Any): Boolean = { //分区器的类型相同,并且
    obj match {
      case x:exer_MyPartitioner => x.numPartitions == this.numPartitions
      case _ => false
    }
  }
}

