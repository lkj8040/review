package com.streaming.app

import com.streaming.bean.AdsInfo
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.streaming.util.kafkaDStreamUtil
import org.apache.spark.streaming.dstream.DStream

trait getDStream {
    private val topics = "ads_log"
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("conf").setMaster("local[*]")
        val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
        val sc: SparkContext = spark.sparkContext
        val ssc = new StreamingContext(sc, Seconds(3))
        val ds: DStream[String] = kafkaDStreamUtil.getKafkaDStream(ssc, topics)
        val ds2: DStream[AdsInfo] = ds.transform(rdd => {
            rdd.map(
                line => {
                    val splits: Array[String] = line.split(",")
                    AdsInfo(splits(0).toLong, splits(1), splits(2), splits(3), splits(4))
                }
            )
        })
        ssc.checkpoint("./project_realtime")
        
        doSomeThing(spark,ssc,ds2)
        
        ssc.start()
        ssc.awaitTermination()
    }
    
    def doSomeThing(spark:SparkSession,ssc:StreamingContext,ds:DStream[AdsInfo])
    
}
