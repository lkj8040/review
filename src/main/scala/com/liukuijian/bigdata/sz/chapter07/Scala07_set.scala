package com.liukuijian.bigdata.sz.chapter07

import scala.collection.mutable

object Scala07_set {
  def main(args: Array[String]): Unit = {
//    //集合 -- Set(不可变)
//    val set: Set[Int] = Set(1,1,1,2,4)
//    //无序，不可重复
//    println(set.mkString(","))
//
//    //可变的set
//    val muSet: mutable.Set[Int] = mutable.Set(1,2,3,4)
//
//    muSet.add(5)
//
//    //可以添加也可以删除
//    muSet.update(10,false)//删除或者添加
//
//    muSet.remove(1)
//
//    println(muSet)

    val set1: Set[Int] = Set(1,2,3,4,4,4)
    println(set1.mkString(","))//自动去重处理，存放无序不可重复元素

    val muset: mutable.Set[Int] = mutable.Set(1,2,3,4)
    muset.add(10)
    muset.update(10,false)//true表示如果不存在就增加，存在就不变，false表示如果存在就删除，不存在就什么也不做
    println(muset.mkString(","))

    muset.remove(10)//删除元素，而不是根据索引删除



  }
}
