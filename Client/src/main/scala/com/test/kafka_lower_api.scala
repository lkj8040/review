package com.test

import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaCluster, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.kafka.KafkaCluster.Err


object kafka_lower_api {
    val params: Map[String, String] = Map[String, String](
        ConsumerConfig.GROUP_ID_CONFIG -> "KJ",
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop101:9092,hadoop102:9092,hadoop103:9092"
    )

    val topics = Set("slkj")
    val groupId: String = "bigdata"

    private val cluster = new KafkaCluster(params)

    def readOffsets()={//读offset，并且设置offset
        //获取主题和分区
        val partitions: Either[Err, Set[TopicAndPartition]] = cluster.getPartitions(topics)
        //创建每个分区的写出offset的集合
        var resultMap: Map[TopicAndPartition, Long] = Map[TopicAndPartition, Long]()
        partitions match{
            //如果主题存在
            case Right(set) =>
                //获取groupId这个消费者组对各个分区的消费offset情况
                val mapAndOffset: Either[Err, Map[TopicAndPartition, Long]] = cluster.getConsumerOffsets(groupId, set)
                mapAndOffset match{
                    //如果存在消费，则将消费集合加入结果集合
                    case Right(map) =>resultMap ++= map
                    //如果不存在，就设置当前分区的offset为0，表示没有消费过该分区的数据
                    case _ =>set.foreach(partition => resultMap += partition -> 0L)
                }
        
            case _ => //表示topic不存在
        }
        resultMap
    }
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
        val ssc = new StreamingContext(conf, Seconds(3))
    
        //手动维护offset 的 低阶api
        val ds: InputDStream[String] =
            KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, String](
                ssc,
                params,
                readOffsets(),
                (handler: MessageAndMetadata[String, String]) => handler.message())
    
        ds
                .flatMap(_.split(" "))
                .map((_,1))
                .reduceByKey(_+_)
                .print
    
        saveOffsets(ds)
        ssc.start()
        ssc.awaitTermination()
    
    }

    def saveOffsets(ds: InputDStream[String])={
        ds.foreachRDD(rdd => {
            val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        
            var map: Map[TopicAndPartition, Long] = Map[TopicAndPartition, Long]()
        
            offsetRanges.foreach(offsetRange =>{
                //获取分区
                val key: TopicAndPartition = offsetRange.topicAndPartition()
                //获取offset
                val value: Long = offsetRange.untilOffset
                //存储每个分区的offset消费情况
                map += key -> value
            })
            cluster.setConsumerOffsets(groupId, map)
        })
    }
}
