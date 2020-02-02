package com.test


import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object transform_foreachrdd_test {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("t_f").setMaster("local[2]")
        val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
        
        val sc: SparkContext = spark.sparkContext
    
        val ssc: StreamingContext = new StreamingContext(sc, Seconds(3))
        
        //从kafka消费数据
        val params: Map[String, String] = Map[String, String](
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop101:9092,hadoop102:9092,hadoop103:9092",
            ConsumerConfig.GROUP_ID_CONFIG -> "KJ"
        )
        val topics: Set[String] = Set("slkj")
    
        val kafkaDS: InputDStream[(String, String)] =
            KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
            ssc,
            params,
            topics)
        val wordCount: DStream[(String, Int)] =
            kafkaDS.transform(rdd => {
                rdd.flatMap(_._2.split(" ")).map((_, 1)).reduceByKey(_ + _)
            })
        
        wordCount.print(100)
        import spark.implicits._
        wordCount.foreachRDD(rdd =>{
            val df: DataFrame = rdd.toDF("word","count")
            df.write.format("jdbc")
                    .option("user","root")
                    .option("password","L19940816")
                    .option("url","jdbc:mysql://hadoop101:3306/test")
                    .option("dbtable","word")
                    .mode("append")
                    .save()
        })
        
        ssc.start()
        ssc.awaitTermination()
    }
}
