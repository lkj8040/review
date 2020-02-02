package com.liukuijian.spark.core.day09.kafka

import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount1 {
    def main(args: Array[String]): Unit = {

        val ssc: StreamingContext = StreamingContext.getActiveOrCreate("./ck1",createSSC)
        ssc.start()
        ssc.awaitTermination()
    }

    def createSSC()={
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
        val ssc = new StreamingContext(conf, Seconds(3))

        val params: Map[String, String] = Map[String, String](
            ConsumerConfig.GROUP_ID_CONFIG -> "KJ",
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop101:9092,hadoop102:9092,hadoop103:9092"
        )
        KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](
            ssc,
            params,
            Set("slkj"))
                .flatMap {
                    case (_, v) => v.split(" ").map((_,1))
                }
                .reduceByKey(_+_)
                .print
        ssc
    }
}
