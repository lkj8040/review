package com.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object readwrite {
    def main(args: Array[String]): Unit = {
    /*
    时间戳，省份，城市，用户，广告，字段使用空格分割
    1516609143867 6 7 64 16
    1516609143869 9 4 75 18
    1516609143869 1 7 87 12
    统计出每一个省份广告被点击次数的 TOP3
     */
        val conf: SparkConf = new SparkConf().setAppName("readwrite").setMaster("local[2]")
        val sc: SparkContext = new SparkContext(conf)
        val rdd: RDD[String] = sc.textFile("d:/agent.log")
        rdd.map{
            line =>
                val splits = line.split(" ")
                ((splits(1),splits(4)),1)
        }.reduceByKey(_+_).map{
            case (tuple, count) => (tuple._1, (tuple._2, count))
        }.groupByKey().map{
            case (prov, it) => (prov,it.toList.sortBy(_._2)(Ordering.Int.reverse).take(3))
        }.collect.foreach(println)
    }
}
