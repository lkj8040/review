package com.test

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.ReceiverInputDStream

object My_ReceriverStream_Demo {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("myReceiver").setMaster("local[*]")
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
    
        val ds: ReceiverInputDStream[String] = ssc.receiverStream(new MyReceiver("hadoop101",8888))
    
        ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print
    
        ssc.start()
    
        ssc.awaitTermination()
    }
}
