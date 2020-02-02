package com.liukuijian.spark.core.day08

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.ReceiverInputDStream

object WindowCount2 {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("window").setMaster("local[*]")
        //每间隔多久采集一次数据
        val ssc = new StreamingContext(conf, Seconds(3))

        val ds: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop101", 8888)
        ssc.checkpoint("./ck2")
        ds
                .flatMap(_.split(" "))
                .map((_,1))
                //如果不设置slideDuration，默认为每采集一次数据计算一次，必须是
                //slideDuration是每间隔多长时间执行一次计算
                //windowTime是计算的时间的范围
                //invReduceFunc函数的计算逻辑是   当前计算结果=上一次计算结果 + (新进来窗口的数据计算结果 - 移除出窗口的数据的计算结果)
                //invReduceFunc一定程度上可以提高效率，由于使用了上一次的计算结果，因此需要设置checkpoint
                .reduceByKeyAndWindow(_+_,invReduceFunc = _-_, Seconds(15),  slideDuration = Seconds(6),filterFunc = _._2>0)
                .print

        ssc.start()
        ssc.awaitTermination()
    }
}
