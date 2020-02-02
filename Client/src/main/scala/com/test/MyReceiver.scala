package com.test

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

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