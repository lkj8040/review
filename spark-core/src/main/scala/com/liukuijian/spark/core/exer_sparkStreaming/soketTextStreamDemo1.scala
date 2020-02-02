package com.liukuijian.spark.core.exer_sparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object soketTextStreamDemo1 {
    def main(args: Array[String]): Unit = {
        //1.创建ssc
        val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("demo1")
        //StreamingContext有两个主要参数，conf和Seconds， Seconds表示每间隔多久去拉取一次数据。
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))

        //2. 从数据源得到DStream
        // 通过socket的方式得到DStream
        val socketDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop101",8888)

        //3. 执行具体的逻辑
        val ds: DStream[(String, Int)] = socketDStream.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

        //4. 展示计算结果
        ds.print(1000)

        //5. 行动算子
        ssc.start()
        ssc.awaitTermination()
    }
}
