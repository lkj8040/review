package com.liukuijian.spark.core.day07

import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaStreamingDemo {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("kafkaDataSource").setMaster("local[*]")

        val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
        //从kafka读数据 --> 使用直连的方式访问kafka分区数据！！效率更高
        val params: Map[String, String] = Map[String, String](
            "group.id" -> "KJ",
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG->"hadoop101:9092,hadoop102:9092,hadoop103:9092"
        )
        val ds: InputDStream[(String, String)] =
            //创建一个直连kafka的DS
            KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
            ssc,
            params,
            Set("slkj"))
        ds.flatMap(_._2.split(" ")).map((_,1)).reduceByKey(_+_).print(1000)
        ssc.start()
        ssc.awaitTermination()
    }
}
