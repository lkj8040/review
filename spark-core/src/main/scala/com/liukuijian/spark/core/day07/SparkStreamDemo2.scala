package com.liukuijian.spark.core.day07

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object SparkStreamDemo2 {
    def main(args: Array[String]): Unit = {
        //1. 创建一个StreamingContext
        val conf: SparkConf = new SparkConf().setAppName("streaming").setMaster("local[2]")
        val ssc = new StreamingContext(conf, Seconds(3))
        val queue: mutable.Queue[RDD[Int]] = mutable.Queue[RDD[Int]]()
        val rddStream: InputDStream[Int] = ssc.queueStream(queue,false)
        rddStream.reduce(_+_).print(1000)
        ssc.start()
        while(true){
            val sc: SparkContext = ssc.sparkContext
            val rdd: RDD[Int] = sc.parallelize(1 to 100)
            queue.enqueue(rdd)
            println(queue.length)
            Thread.sleep(10)
        }
        ssc.awaitTermination()
    }
}
