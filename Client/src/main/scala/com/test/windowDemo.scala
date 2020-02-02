package com.test

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object windowDemo {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Transform")
        val ssc = new StreamingContext(conf, Seconds(3))
        
        val DS: DStream[String] =
                ssc
                    .socketTextStream("hadoop101",8888)
                    .window(windowDuration = Seconds(15),slideDuration = Seconds(9))
        
        DS.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()
        
        ssc.start()
        ssc.awaitTermination()
    }
}
