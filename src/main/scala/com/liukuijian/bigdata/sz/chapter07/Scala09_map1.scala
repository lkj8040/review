package com.liukuijian.bigdata.sz.chapter07

import scala.collection.mutable

object Scala09_map1 {
  def main(args: Array[String]): Unit = {
//    //Map
//    val wordToCount: mutable.Map[String, Int] = mutable.Map("a"->1, "b"->2,"c"->3)
//
//    //获取指定key的值
//    //java中取一个不存在的key，返回null
//    //scala取一个不存在的key，返回None，无法获取value，会发生错误
//    //scala取一个存在的key，返回some,可以调用get方法获取value
//    //scala中从Map中获取数据的结果类型为option（选项）只有两个值，一个是some，一个是None
//    val value: Option[Int] = wordToCount.get("a")
//    val value2: Option[Int] = wordToCount.get("d")
//    //println(value.get)
//    //println(value2.get) //nosuchelementexception
//    //println(value.getOrElse(0))
//    //println(value2.getOrElse(0))
//    wordToCount.getOrElse("d",0)
//    //Scala中Option类型专门是为了解决空指针异常的
//    //java中对象为空，调用成员方法或成员属性会发生空指针异常
//
//    val keys: Iterable[String] = wordToCount.keys
//    val values: Iterable[Int] = wordToCount.values
//    val iterator: Iterator[(String, Int)] = wordToCount.iterator
//    val keysIterator: Iterator[String] = wordToCount.keysIterator
    val map1: mutable.Map[String, Int] = mutable.Map("a"->1,"b"->2,"c"->3)
    val value1: Option[Int] = map1.get("a")
    val value2: Option[Int] = map1.get("d")
    println(value1)
    println(value2)
    println(value1.get)
    //println(value2.get)//None空指针异常
    val value3: Int = value1.getOrElse(0)
    val value4: Int = value2.getOrElse(0)
    println(value3)
    println(value4)

    println("----------------------")
    val value5: Int = map1.getOrElse("d",0)
    val value6: Int = map1.getOrElse("b",0)
    println(value5)
    println(value6)

    val keys: Iterable[String] = map1.keys
    val values: Iterable[Int] = map1.values
    val keysIterator: Iterator[String] = map1.keysIterator
    val iterator: Iterator[(String, Int)] = map1.iterator
  }
}
