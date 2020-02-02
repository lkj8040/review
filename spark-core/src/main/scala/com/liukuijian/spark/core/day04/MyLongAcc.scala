package com.liukuijian.spark.core.day04

import org.apache.spark.util.AccumulatorV2

class MyLongAcc extends AccumulatorV2[Long, Long]{
  private var sum: Long = 0L
  override def isZero: Boolean = sum == 0

  override def copy(): AccumulatorV2[Long, Long] = {
    val acc: MyLongAcc = new MyLongAcc
    acc.sum = sum
    acc
  }

  override def reset(): Unit = sum=0

  override def add(v: Long): Unit = sum+=v

  override def merge(other: AccumulatorV2[Long, Long]): Unit =
    other match {
      case o:MyLongAcc => sum += o.sum
      case _ => throw new UnsupportedOperationException("非法操作")
    }

  override def value: Long = sum
}
