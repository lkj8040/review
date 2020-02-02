package com.liukuijian.spark.core.exer_sparkStreaming

import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}


object Spark_KafkaStreaming {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("kafka")
        val ssc = new StreamingContext(conf, Seconds(5))
        val params = Map[String, String](
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop101:9092,hadoop102:9092,hadoop103:9092",
            ConsumerConfig.GROUP_ID_CONFIG -> "mygroup"
        )
        val ds: InputDStream[(String, String)] =
            KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
            ssc,
            params,
            Set("slkj"))
        ds.flatMap(_._2.split(" ")).map((_,1)).reduceByKey(_+_).print
        ssc.start()
        ssc.awaitTermination()
    }
}
