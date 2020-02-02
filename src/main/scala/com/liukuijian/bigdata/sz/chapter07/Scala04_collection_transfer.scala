package com.liukuijian.bigdata.sz.chapter07

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Scala04_collection_transfer {
  def main(args: Array[String]): Unit = {
//    val array = Array(1,2,3)
//    val buffer = ArrayBuffer(1,2,3)
//
//    //不可变 ->可变
//    val buffer1: mutable.Buffer[Int] = array.toBuffer
//
//    //可变 -> 不可变
//    val array1: Array[Int] = buffer.toArray
//
//    val matrix: Array[Array[Array[String]]] = Array.ofDim[Array[String]](3,4)
    val array1 = Array(1,2,3)
    val buffer: ArrayBuffer[Int] = ArrayBuffer(1,2,3)
    
    val toBuffer: mutable.Buffer[Int] = array1.toBuffer
    val toArray: Array[Int] = buffer.toArray
    
    //创建二维数组
    val array2: Array[Array[String]] = Array.ofDim[String](3,4)

  }
}
