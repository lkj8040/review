package com.test

import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object MyPartitioner {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("MyPartitioner")
        val sc: SparkContext = new SparkContext(conf)
        val rdd: RDD[(Int, String)] = sc.parallelize(Array((11, "a"), (21, "b"), (31, "c"), (40, "d"), (50, "e"), (60, "f")),3)
    
        val rdd2: RDD[(Int, String)] = rdd.partitionBy(new MyPartitioner(2)).partitionBy(new MyPartitioner(2))
    
        val rdd3: RDD[(Int, String)] = rdd2.mapPartitionsWithIndex((index, it) => it.map{ case (key, value) => (index, key+":"+value)})
    
        println(rdd3.collect.mkString(","))
    
        Thread.sleep(1000000)
    }
}
class MyPartitioner(val num:Int) extends Partitioner{
    override def numPartitions: Int = num
    
    override def getPartition(key: Any): Int = {
        key match {
            case null => 0
            case _ => math.abs(key.hashCode) % numPartitions
        }
    }
    
    override def hashCode(): Int = num
    
    override def equals(obj: Any): Boolean =  {
        obj match{
            case x:MyPartitioner => x.numPartitions == this.numPartitions
            case _ => false
        }
    }
}