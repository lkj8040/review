package com.liukuijian.realtime.app

import com.liukuijian.realtime.bean.AdsInfo
import com.liukuijian.realtime.utils.MyKafkaUtil
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

trait App {
    def main(args: Array[String]): Unit = {
        //1. 创建StreamingContext
        val conf: SparkConf = new SparkConf().setAppName("window").setMaster("local[*]")
        //每间隔多久采集一次数据
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
        ssc.checkpoint("./ck1")
        //2.从kafka读数据
        val ds: DStream[AdsInfo] = MyKafkaUtil.getKafkaStream(ssc,"ads_log")
                .map(line =>{
                    val splits: Array[String] = line.split(",")
                    AdsInfo(splits(0).toLong,splits(1),splits(2),splits(3),splits(4))
                })
        
        //2. 操作DStream
        doSomething(ds)
        //3. 启动SSC和阻止main方法退出
        ssc.start()
        ssc.awaitTermination()
    }
    def doSomething(ds: DStream[AdsInfo]): Unit
//    def onStart(ssc: StreamingContext): Unit
}
