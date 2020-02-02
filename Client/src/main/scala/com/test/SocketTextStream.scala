package com.test

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SocketTextStream {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("sockettext").setMaster("local[2]")
        
        val spark: SparkSession =SparkSession.builder().config(conf).getOrCreate()
        
        val sc: SparkContext = spark.sparkContext
    
        val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))
        
        val ds: ReceiverInputDStream[String] =
            ssc.socketTextStream("hadoop101",8888, StorageLevel.MEMORY_ONLY)
        
        val wordcount: DStream[(String, Int)] = ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
        
        wordcount.print(1000)
        
        ssc.start()
        
        ssc.awaitTermination()
    }
}
