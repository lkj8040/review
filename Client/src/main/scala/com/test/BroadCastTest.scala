package com.test

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object BroadCastTest {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("broadcast").setMaster("local[2]")
        val sc: SparkContext = new SparkContext(conf)
        val name = "lkj"
        val broadcast: Broadcast[String] = sc.broadcast(name)
        val rdd: RDD[Int] = sc.parallelize(List(1,2,3,4,5,6),2)
        rdd.map(
            x =>{
                println(broadcast.value)
                x
            }
        ).collect
        
        
    }
}
