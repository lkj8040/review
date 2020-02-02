package com.liukuijian.spark.core.day09.withstat

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object reduceByKeyAndWindow {
    def main(args: Array[String]): Unit = {
        // 统计最近15秒内的单词的次数, 每5秒统计一次
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("reduceByKeyAndWindow")
        //时间间隔五秒是指每5秒钟封装一个RDD
        val ssc = new StreamingContext(conf, Seconds(5))
        ssc.checkpoint("./ck")
        val ds: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop101",8888)
        ds
                .flatMap(_.split(" "))
                .map((_,1))
                //滑动窗口的两个概念：窗口的长度和窗口的滑动步长，必须都是采集时间间隔的整数倍
                //如果不配置滑动步长，那么默认滑动步长=采集时间间隔，即封装一个RDD计算一次
//                .reduceByKeyAndWindow(reduceFunc=_+_,windowDuration = Seconds(20),slideDuration = Seconds(10)).print
                //invReduceFunction 表示 当前计算结果 = 上次计算结果 + （最新进入窗口的数据计算结果-最新移除出窗口数据的计算结果），一定程度上提高效率
                //凡是利用之前的计算结果来计算当前结果的，都需要设置checkpoint,带来的不好的地方是，归零也会显示
                /**
                  * (are,0)
                  * (how,0)
                  * (you,0)
                  * (hello,0)
                  *
                  * */
//                .reduceByKeyAndWindow(reduceFunc=_+_,invReduceFunc = _-_,windowDuration = Seconds(20),slideDuration = Seconds(10)).print
                //filterFunc 是过滤函数，可以将零值过滤掉
                .reduceByKeyAndWindow(
                        reduceFunc=_+_,
                        invReduceFunc = _-_,
                        windowDuration = Seconds(20),
                        slideDuration = Seconds(10),
                        filterFunc = kv => kv._2 > 0).print

        ssc.start()

        ssc.awaitTermination()
    }
}
