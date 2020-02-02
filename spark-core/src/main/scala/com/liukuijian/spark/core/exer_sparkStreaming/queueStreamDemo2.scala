package com.liukuijian.spark.core.exer_sparkStreaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object queueStreamDemo2 {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("demo2").setMaster("local[2]")

        val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

        //传入一个队列的方式创建DStream
        val queue: mutable.Queue[RDD[Int]] = mutable.Queue[RDD[Int]]()

        val ds: InputDStream[Int] = ssc.queueStream(queue,false)

        ds.reduce(_+_).print(100)

        ssc.start()

        while(true){
            val sc: SparkContext = ssc.sparkContext
            val rdd: RDD[Int] = sc.parallelize(1 to 100)
            queue.enqueue(rdd)
            Thread.sleep(1000)
        }
        ssc.awaitTermination()
    }
}
