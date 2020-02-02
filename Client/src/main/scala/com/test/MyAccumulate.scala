package com.test

import org.apache.spark.util.AccumulatorV2

class MyAccumulate extends AccumulatorV2 [Long, Long]{
    //第一个Long表示IN, 第二个Long表示OUT,其中OUT必须是线程安全的或者必须是原子性的
    private var sum:Long = 0L
    
    //判断累加值是否等于0
    override def isZero: Boolean = sum == 0
    
    override def copy(): AccumulatorV2[Long, Long] = {
        val acc = new MyAccumulate
        acc.sum = sum
        acc
    }
    
    //将累加值重置为0
    override def reset(): Unit = sum = 0L
    
    override def add(v: Long): Unit = sum += v
    
    override def merge(other: AccumulatorV2[Long, Long]): Unit = {
        other match{
            case o:MyAccumulate => sum += o.sum
            case _ => throw new UnsupportedOperationException("不支持的累加器类型")
        }
    }
    
    override def value: Long = sum
}
