package com.test

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object updateStateByKeyDemo {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
        val ssc = new StreamingContext(conf, Seconds(5))
        val ds: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop101",8888)
        val resultDS: DStream[(String, Int)] = ds.flatMap(_.split(" ")).map((_,1))
        //seq是本批次该key计算的结果
        //opt是上批次该key计算的结果，类型用Option包装，因为上批次可能没有该key，如果没有就用0代替
        //每次计算使用本批次的rdd计算结果加上上批次的计算结果得到的就是最新结果。
        //由于需要使用上一次的计算结果，因此需要将上次的计算结果缓存起来
        //updateStateByKey((seq:Seq[Int],opt:Option[Int]) => Some(seq.sum + opt.getOrElse(0)))
        ssc.checkpoint("./updateStateByKey_ck")
        val updatedDS: DStream[(String, Int)] =
            resultDS.updateStateByKey((seq:Seq[Int], opt:Option[Int]) => Some(seq.sum + opt.getOrElse(0)))
        updatedDS.print(100)
        ssc.start()
        ssc.awaitTermination()
    }
}
