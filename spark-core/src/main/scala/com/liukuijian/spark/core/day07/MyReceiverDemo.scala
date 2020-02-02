package com.liukuijian.spark.core.day07

import java.io.{BufferedReader, InputStream, InputStreamReader}
import java.net.Socket

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver

object MyReceiverDemo {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("receiverDemo").setMaster("local[*]")

        val ssc = new StreamingContext(conf, Seconds(5))
        //获取一个自定义的receiver
        val ds: ReceiverInputDStream[String] = ssc.receiverStream(new MyReceiver("hadoop101", 8888))

        ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print(1000)

        ssc.start()

        ssc.awaitTermination()
    }
}
/*
1. 接收socket数据
 */
class MyReceiver(host:String, port:Int) extends Receiver[String](StorageLevel.MEMORY_ONLY){
    var socket: Socket = null
    var bufferedReader:BufferedReader = null
    //接收器启动的时候回调这个函数
    override def onStart(): Unit = {
        //onStart 要求在子线程中执行读数据的操作！！！
        runInThread{
            try{
                //创建一个socket
                socket = new Socket(host,port)
                //从socket读取数据
                bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream, "utf-8"))
                //把数据发送出去
                var line: String = bufferedReader.readLine()
                //使用store把数据存储起来
                while(socket.isConnected && line != null){
                    store(line)
                    line = bufferedReader.readLine()
                }
            }catch{
                case e => println(e.getMessage)
            }finally {
                restart("重启receiver...")
            }
        }

    }
    //接收器停止的时候回调这个函数：释放资源
    override def onStop(): Unit = {
        if(socket != null){
            socket.close()
        }
        if(bufferedReader != null){
            bufferedReader.close()
        }
    }

    def runInThread(f: => Unit): Unit ={
        new Thread(){
            override def run() = f
        }.start()
    }
}
