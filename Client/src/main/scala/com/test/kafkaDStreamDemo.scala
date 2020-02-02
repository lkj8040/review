package com.test

import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object kafkaDStreamDemo {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("kafkaDStream").setMaster("local[*]")
        val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
        val sc: SparkContext = spark.sparkContext
        val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))
        var parms: Map[String, String] = Map[String,String](
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop101:9092,hadoop102:9092,hadoop103:9092",
            ConsumerConfig.GROUP_ID_CONFIG->"KJ"
        )
        val ds: InputDStream[(String, String)] =
            KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
            ssc,
            parms, //parameters
            Set("slkj") ) //topics
        ds.flatMap(_._2.split(" ")).map((_,1)).reduceByKey(_+_).print(100)
        ssc.start()
        ssc.awaitTermination()
    }
}
