package com.liukuijian.spark.core.day08

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WindowWordCount {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("window").setMaster("local[*]")
        //每间隔多久采集一次数据
        val ssc = new StreamingContext(conf, Seconds(5))

        val ds: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop101", 8888)

        ds
                .flatMap(_.split(" "))
                .map((_,1))
                //如果不设置slideDuration，默认为每采集一次数据计算一次
                //slideDuration是每间隔多长时间执行一次计算
                //windowTime是计算的时间的范围
                .reduceByKeyAndWindow(_+_, Seconds(15),slideDuration  = Seconds(5))
                .print

        ssc.start()
        ssc.awaitTermination()
    }
}
