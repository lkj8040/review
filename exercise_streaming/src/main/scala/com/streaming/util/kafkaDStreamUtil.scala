package com.streaming.util


import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka010._

object kafkaDStreamUtil {
    val kafkaParams = Map[String,Object](
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG ->"hadoop101:9092,hadoop102:9092,hadoop103:9092",
        ConsumerConfig.GROUP_ID_CONFIG -> "bigdata",
        "key.deserializer" -> classOf[StringDeserializer],
        "value.deserializer" -> classOf[StringDeserializer],
        "auto.offset.reset" -> "latest"
    )
    def getKafkaDStream(ssc:StreamingContext, otherTopic: String, topics: String*) ={
        KafkaUtils.createDirectStream[String, String](
            ssc,
            org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent,
            org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe[String,String](otherTopic +: topics ,kafkaParams)
        ).map(_.value())
    }
}
