package com.liukuijian.bigdata.sz.chapter07

import scala.collection.mutable.ArrayBuffer

object Scala02_arraybuffer {
  def main(args: Array[String]): Unit = {
//    //集合 --Array -- 数组(可变)
//    //可变集合的普通方法(update\insert\remove\append)一般不会产生新的集合，但是使用运算符方法一般会产生新的集合
//    val buffer = new ArrayBuffer[String]()
//    //增加
//    buffer.append("java","python","scala")
//    buffer.insert(0,"c++","php")
//    //方法链,方法调用后返回的是对象本身
//    val buffer3: ArrayBuffer[String] = buffer += "matlab" += "golang"
//
//    //修改
//    buffer(0) = "matlab" //和update是一样的，在编译时会自动转换
//    buffer.update(0,"matlab")
//
//    buffer.remove(1,2)//删除索引为1的元素,删除两个
//    //特别的：drop方法会产生新的集合
//    val newBuffer: ArrayBuffer[String] = buffer.drop(1) //产生新的数组
//    val newBuffer2: ArrayBuffer[String] = buffer.dropRight(1)
//    println(newBuffer)
//    println(newBuffer == buffer)
//    println(newBuffer2)
//
//    //在可变集合中，调用运算符方法会创建新的集合
//    val buffer2: ArrayBuffer[String] = buffer.-("java")
//
//    println(buffer2)
//
//    println(buffer == buffer3)
//    println(buffer)
//    println(buffer3)

    val arrayBuffer:ArrayBuffer[String] = ArrayBuffer()
    arrayBuffer.append("java","scala","python")

    arrayBuffer.insert(1,"c++")//java c++ scala python

    arrayBuffer.remove(0,2)// scala python

    arrayBuffer.update(1,"spark") //scala spark

    arrayBuffer +="java" += "c++" //scala spark java c++ 方法链调用

    println(arrayBuffer.mkString(","))

    val buffer: ArrayBuffer[String] = arrayBuffer += "hello"
    println(buffer == arrayBuffer) //true， 返回的是同一个对象
    println(arrayBuffer.mkString(","))
    println(buffer.mkString(","))

//    val str: String = arrayBuffer.remove(0) //返回的是删除的对象，String类型
//    println(str.mkString(","))
    println("----------------------------")
    val strings: ArrayBuffer[String] = arrayBuffer.drop(0)//返回的是从第1个元素以后的对象，新对象
    println(strings.eq(arrayBuffer)) //== 判断内容是否相等，返回true， .eq判断地址值是否相等，返回false
    println(strings == arrayBuffer)
    println(arrayBuffer) //arrayBuffer长度不变
    println(strings)
    println("----------------------------")
    // arrayBuffer = scala,spark,java,c++,hello
    val str: ArrayBuffer[String] = arrayBuffer.-("java")//创建新的对象
    println(str == arrayBuffer)
    println(str)
    println(arrayBuffer)
  }
}
