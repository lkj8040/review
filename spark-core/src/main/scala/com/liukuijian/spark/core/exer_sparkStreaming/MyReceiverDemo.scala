package com.liukuijian.spark.core.exer_sparkStreaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

object MyReceiverDemo {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("myReceiver").setMaster("local[*]")
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))

        val ds: ReceiverInputDStream[String] = ssc.receiverStream(new MyReceiver("hadoop101",8888))

        ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print

        ssc.start()

        ssc.awaitTermination()
    }
}
class MyReceiver(host:String, port:Int) extends Receiver[String](StorageLevel.MEMORY_ONLY){
    private var socket:Socket = null
    private var reader:BufferedReader = null
    override def onStart(): Unit = {
        runThread{
            try{
                socket = new Socket(host, port)
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream,"utf-8"))
                var line: String = reader.readLine()
                while (socket.isConnected && line != null){
                    store(line)
                    line = reader.readLine()
                }
            }catch{
                case e => e.getMessage
            }finally {
                restart("重新尝试连接...")
            }
        }
    }

    override def onStop(): Unit = {
        if(socket != null){
            socket.close()
        }
        if(reader != null){
            reader.close()
        }
    }

    def runThread(f: => Unit): Unit ={
        new Thread(){
            override def run() = f
        }.start()
    }
}