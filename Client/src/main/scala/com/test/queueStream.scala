package com.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable


object queueStream {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("queue")
        
        val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
        
        val sc: SparkContext = spark.sparkContext
    
        val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))
        val queue: mutable.Queue[RDD[Int]] = mutable.Queue[RDD[Int]]()
        //true表示一个微批次处理一个RDD，false表示一次处理尽可能多的RDD
        val ds: InputDStream[Int] = ssc.queueStream(queue,true)
        ds.reduce(_+_).print(100)
        ssc.start()
        while(true){
            queue.enqueue(sc.parallelize(List(1 to 10 by 2).flatten))
            println(queue.length)
            Thread.sleep(200)
        }
        ssc.awaitTermination()
    }
}
