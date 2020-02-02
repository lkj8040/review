package com.liukuijian.realtime.utils

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
//使用kafka0.10版本创建DStream


object MyKafkaUtil {
    val kafkaParams: Map[String, Object] = Map[String, Object](
        "bootstrap.servers" -> "hadoop101:9092,hadoop102:9092,hadoop103:9092",
        "key.deserializer" -> classOf[StringDeserializer],
        "value.deserializer" -> classOf[StringDeserializer],
        "group.id" -> "bigdata",
        "auto.offset.reset" -> "latest",
        "enable.auto.commit" -> (true: java.lang.Boolean)
    )

    def getKafkaStream(ssc:StreamingContext, topic:String,otherTopic:String *): DStream[String] ={
        //创建一个kafkaDStream
        KafkaUtils.createDirectStream[String,String](
            ssc,
            PreferConsistent,//固定写法
            Subscribe[String,String](otherTopic :+ topic,kafkaParams) //消费哪个主题，以及kafka的配置参数
        ).map(record => record.value())
    }
}
