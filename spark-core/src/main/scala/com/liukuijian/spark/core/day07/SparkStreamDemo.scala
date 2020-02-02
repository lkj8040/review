package com.liukuijian.spark.core.day07

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamDemo {
    def main(args: Array[String]): Unit = {
        //1. 创建一个StreamingContext
        val conf: SparkConf = new SparkConf().setAppName("streaming").setMaster("local[2]")
        val ssc = new StreamingContext(conf, Seconds(5))

        //2. 从数据源得到DStream
        val sourceStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop101",8888)

        //3. 对流做各种转换
        val wordCount: DStream[(String, Int)] = sourceStream.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

        //4.展示数据(行动算子)
        wordCount.print(1000)
        ssc.start()

        //5.阻止main函数退出
        ssc.awaitTermination() //阻塞方法，阻塞主线程
    }
}
