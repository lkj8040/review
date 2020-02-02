package com.liukuijian.spark.core.day08

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Window3 {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("window").setMaster("local[*]")
        //每间隔多久采集一次数据
        val ssc = new StreamingContext(conf, Seconds(3))
        //通常的做法，使用window函数创建DS, 这种方式不能使用invReduceFunc函数
        val ds: DStream[String] = ssc.socketTextStream("hadoop101",8888).window(Seconds(15),Seconds(6))

        ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print

        ssc.start()

        ssc.awaitTermination()
    }
}
