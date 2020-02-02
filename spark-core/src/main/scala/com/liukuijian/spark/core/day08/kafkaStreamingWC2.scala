package com.liukuijian.spark.core.day08

import _root_.kafka.serializer.StringDecoder
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaCluster.Err
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaCluster, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}


object kafkaStreamingWC2 {
    private val group_Id = "KJ"
    private val params: Map[String, String] = Map[String, String](
        "group.id" -> group_Id,
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG->"hadoop101:9092,hadoop102:9092,hadoop103:9092"
    )
    private val topics: Set[String] = Set("slkj")
    private val cluster = new KafkaCluster(params)

    /*
    读取offsets
    有可能是第一次启动，也可能不是
    KafaUtils
    KafkaCluster
    * */

    def readOffsets()={
        val topicAndPartitionEither: Either[Err, Set[TopicAndPartition]] = cluster.getPartitions(topics)
        var resultMap: Map[TopicAndPartition, Long] = Map[TopicAndPartition, Long]()

        topicAndPartitionEither match {
            case Right(topicAndPartitionsSet) =>
                val topicAndPartitionAndOffsetsEither: Either[Err, Map[TopicAndPartition, Long]] =
                    cluster.getConsumerOffsets(group_Id, topicAndPartitionsSet)
                topicAndPartitionAndOffsetsEither match {
                    case Right(map) =>
                        resultMap ++= map
                    case _ =>
                        //把每个分区的offset设置为 0
                        topicAndPartitionsSet.foreach(resultMap += _ -> 0L)
                }
            case _ => throw new UnsupportedOperationException("topic不存在！")
        }

        resultMap
    }

    def saveOffsets(ds:InputDStream[String])={
        //每个时间间隔内封装的rdd
        ds.foreachRDD(rdd=>{
            val hasOffsetRanges: HasOffsetRanges = rdd.asInstanceOf[HasOffsetRanges]
            val ranges: Array[OffsetRange] = hasOffsetRanges.offsetRanges
            var map: Map[TopicAndPartition, Long] = Map[TopicAndPartition, Long]()
            ranges.foreach(offsetrange=>{
                val key: TopicAndPartition = offsetrange.topicAndPartition()
                val offset: Long = offsetrange.untilOffset
                map += key -> offset
            })
            //保存消费记录
            cluster.setConsumerOffsets(group_Id,map)
        })
    }
    def createSSC()={
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("kafka")
        val ssc = new StreamingContext(conf, Seconds(5))
        ssc.checkpoint("./ck1")

        //创建一个直连kafka的DS
        val ds: InputDStream[String] = KafkaUtils createDirectStream[String, String, StringDecoder, StringDecoder, String](
                ssc,
                params,
                readOffsets(),
                (handler: MessageAndMetadata[String, String]) => handler.message()
        )
        ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print
        saveOffsets(ds)
        ssc
    }
    def main(args: Array[String]): Unit = {

        val ssc: StreamingContext = StreamingContext.getActiveOrCreate("./ck1", createSSC)

        ssc.start()

        ssc.awaitTermination()
    }
}
