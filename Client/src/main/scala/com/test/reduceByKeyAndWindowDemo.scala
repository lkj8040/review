package com.test

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object reduceByKeyAndWindowDemo {
    def main(args: Array[String]): Unit = {
        //统计最近15秒内的单词数，每5秒统计一次
        val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("r_b_k_a_w")
        val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
        val sc: SparkContext = spark.sparkContext
        val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))
        ssc.checkpoint("./reduceByKeyAndWindow")
        val socketDS: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop101",8888)
        socketDS.flatMap(_.split(" "))
                .map((_,1))
                //reduceFunc是原来的处理逻辑
                //windowDuration是窗口的长度，表示计算的长度
                //slideDuration:是窗口的滑动步长，必须是批处理时间间隔的整数倍，如果不配置滑动步长，默认为批处理间隔
//                .reduceByKeyAndWindow(reduceFunc = _+_,windowDuration = Seconds(15),slideDuration = Seconds(10)).print
                //利用之前的计算结果计算，上次的计算结果+（新进来的计算结果-移除出窗口的计算结果）
                .reduceByKeyAndWindow(reduceFunc = _+_,
                                      invReduceFunc=_-_,
                                      windowDuration = Seconds(15),
                                      slideDuration = Seconds(10))
                .filter(kv => kv._2 > 0)
                .print(100)
        //批处理间隔：是指多久去收集一次RDD
        //窗口长度：是指reduceByKeyAndWindow的计算长度
        //窗口步长：是指每间隔多久去计算一次窗口
        
        ssc.start()
        ssc.awaitTermination()
    }
}
