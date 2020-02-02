package com.streaming.app


import com.streaming.bean.AdsInfo
import com.streaming.util.RedisUtil
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.json4s.jackson.JsonMethods
import redis.clients.jedis.Jedis


object AreaClickTop3 extends getDStream {
    override def doSomeThing(spark:SparkSession,ssc:StreamingContext,ds:DStream[AdsInfo])={
        //每天每地区热门广告top3
        val result: DStream[((String, String), List[(String, Int)])] =
                ds
                .map(ads => ((ads.dayString, ads.area, ads.adsId), 1))
                .updateStateByKey((seq: Seq[Int], opt: Option[Int]) => Some(seq.sum + opt.getOrElse(0)))
                .map { case ((day, area, ad), count) => ((day, area), (ad, count)) }
                .groupByKey()
                .map { case (key, it) => (key, it.toList.sortBy(-_._2).take(3)) }
        result.print(1000)
        
        result.foreachRDD{
            rdd =>
                rdd.foreachPartition((rddIt: Iterator[((String, String), List[(String, Int)])]) =>{
                    val client: Jedis = RedisUtil.getJedisClient
                    rddIt.foreach{
                        case ((day,area), it)=>{
                            val key: String = "day:area:ads:"+day
                            val Field: String = area
                            import org.json4s.JsonDSL._
                            val value: String = JsonMethods.compact(JsonMethods.render(it))
                            client.hset(key,Field,value)
                        }
                    }
                    client.close()
                })
        }
        
    }
}
