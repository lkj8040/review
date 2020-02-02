package com.liukuijian.realtime.app
import com.liukuijian.realtime.bean.AdsInfo
import org.apache.spark.streaming.Minutes
import org.apache.spark.streaming.dstream.DStream

object LastHourAdsClickApp extends App {
    override def doSomething(ds: DStream[AdsInfo]): Unit = {
        val dsWithWindow: DStream[AdsInfo] = ds.window(Minutes(60))
    
        val adsIdWithHMAndCount: DStream[(String, Iterable[(String, Int)])] =
            dsWithWindow.map(adsinfo => ((adsinfo.adsId, adsinfo.hmString), 1)).reduceByKey(_ + _).map {
            case ((adsId, hm), count) => (adsId, (hm, count))
        }.groupByKey
        adsIdWithHMAndCount.foreachRDD{rdd =>{
        
        }}
    }
}
