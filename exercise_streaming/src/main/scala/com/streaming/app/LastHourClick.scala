package com.streaming.app
import com.streaming.bean.AdsInfo
import com.streaming.util.RedisUtil
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream
import org.json4s.jackson.JsonMethods
import redis.clients.jedis.Jedis

object LastHourClick extends getDStream {
    override def doSomeThing(spark: SparkSession, ssc: StreamingContext, ds: DStream[AdsInfo]): Unit = {
    
        //统计各广告最近 1 小时内的点击量趋势：各广告最近1小时(窗口)内各分钟的点击量
        //每个广告每个小时，按小时分组，窗口大小是60分钟，计算步长是6s
        //需要有状态转换
        val windowDS: DStream[AdsInfo] = ds.window(windowDuration = Minutes(60),slideDuration = Seconds(6))
        val groupedCountDS: DStream[(String, Iterable[(String, Int)])] = windowDS.map(ad => {
            ((ad.adsId, ad.hmString), 1)
        }).reduceByKey(_ + _).map {
            case ((ad, hourminute), count) => (ad, (hourminute, count))
        }.groupByKey
        
        groupedCountDS.print(1000)
        
        groupedCountDS.map{
            case (ad, it) =>{
                import org.json4s.JsonDSL._
                val key: String = ad
                val value: String = JsonMethods.compact(JsonMethods.render(it))
                (key, value)
            }
        }.foreachRDD(rdd => {
            val client: Jedis = RedisUtil.getJedisClient
            import collection.JavaConversions._
            client.hmset("last_hour_ads_click", rdd.collect.toMap)
            client.close()
        })
        
        
        
    }
}
