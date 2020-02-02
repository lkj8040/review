package com.streaming.app

import com.streaming.bean.AdsInfo
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream

object RealTimeApp extends getDStream {
    override def doSomeThing(spark:SparkSession,ssc:StreamingContext,ds:DStream[AdsInfo]) ={
        ds.print(100)
    }
}
