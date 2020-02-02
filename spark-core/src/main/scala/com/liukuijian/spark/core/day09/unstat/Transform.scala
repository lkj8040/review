package com.liukuijian.spark.core.day09.unstat

import java.util.Properties

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Transform {
    def main(args: Array[String]): Unit = {

        val conf: SparkConf = new SparkConf().setAppName("transform").setMaster("local[*]")
        val ssc = new StreamingContext(conf, Seconds(5))
        val ds: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop101",8888)

        val resultDS: DStream[(String, Int)] = ds.transform(
            rdd =>rdd
                    .flatMap(_.split(" "))
                    .map((_,1))
                    .reduceByKey(_+_))

        resultDS.print

        val spark: SparkSession =
            SparkSession
                .builder
                .config(conf)
                .getOrCreate

        import spark.implicits._
        val props = new Properties()
        props.put("user","root")
        props.put("password","L19940816")
        //将数据写出到数据库
        resultDS.foreachRDD(rdd =>{
                rdd
                    .toDF("word","count")
                    .write
                    .mode("append")
                    .jdbc("jdbc:mysql://hadoop101:3306/test","word",props)
        })

        ssc.start()
        ssc.awaitTermination()
    }
}
