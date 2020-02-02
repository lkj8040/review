package com.liukuijian.spark.core.day09.withstat

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
/**
  *
  * 有状态转换操作
  *
  * */
object UpdateStateByKey {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
        val ssc = new StreamingContext(conf, Seconds(5))
        //如果不设置checkpoint就无法记录上一次的计算结果！！！
        ssc.checkpoint("./ck")
        val ds: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop101",8888)
        val resultDS: DStream[(String, Int)] = ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

        //seq是本次该key计算的结果
        //opt是上次该key计算的结果，类型使用option包装，这样在key不存在时返回0
        //每次使用本批次的rdd计算结果和上一次的计算结果进行合并得到有状态的计算结果
        val updateDS: DStream[(String, Int)] = resultDS.updateStateByKey(updateFunc = (seq:Seq[Int], opt:Option[Int])=>Some(opt.getOrElse(0)+seq.sum))

        updateDS.print

        ssc.start()

        ssc.awaitTermination()
    }
}
