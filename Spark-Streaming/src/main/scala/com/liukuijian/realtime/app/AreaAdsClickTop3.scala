package com.liukuijian.realtime.app

import com.liukuijian.realtime.bean.AdsInfo
import com.liukuijian.realtime.utils.RedisUtil
import org.apache.spark.streaming.dstream.DStream
import org.json4s.jackson.JsonMethods
import redis.clients.jedis.Jedis

object AreaAdsClickTop3 extends App {
    override def doSomething(ds: DStream[AdsInfo]): Unit = {
      //        ds.print
              //要使用流式计算，先有ssc
              //需求：每天每地区热门广告top3
              //1.统计每天每地区的各个广告的点击次数按各个广告的点击次数排名取前3
        val resutlDS: DStream[((String, String), List[(String, Int)])] = ds
                .map(adsinfo => ((adsinfo.dayString, adsinfo.area, adsinfo.adsId), 1))
                .updateStateByKey((seq: Seq[Int], opt: Option[Int]) => Some(seq.sum + opt.getOrElse(0)))
                .map {
                  case ((day, area, adsId), count) => ((day, area), (adsId, count))
                }
                .groupByKey
                .map {
                  case ((day, area), it) => ((day, area), it.toList.sortBy(-_._2).take(3))
                }
        
        resutlDS.print
        
        resutlDS.foreachRDD(rdd=>{
            rdd.foreachPartition((rddIt: Iterator[((String, String), List[(String, Int)])]) =>{
            
                val client: Jedis = RedisUtil.getJedisClient
                
                rddIt.foreach{
                    case ((day, area),list) =>
                        val key: String = "day:area:ads:" + day
                        val Field: String = area
                        import org.json4s.JsonDSL._
                        val value: String = JsonMethods.compact(JsonMethods.render(list))
                        client.hset(key, Field, value)
                }
            
                client.close()
            })
        })


    }
}
