package com.liukuijian.spark.core.exer_sparkRDD.app

import com.liukuijian.spark.core.exer_sparkRDD.bean.UserVisitAction
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ProjectApp {
  def main(args: Array[String]): Unit = {
    //分别统计每个品类点击的次数, 下单的次数和支付的次数
    val start: Long = System.currentTimeMillis()
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("practice")
    val sc: SparkContext = new SparkContext(conf)
    val rawRDD: RDD[String] = sc.textFile("d:/user_visit_action.txt")

    val actionRDD: RDD[UserVisitAction] = rawRDD.map(line => {
      val splits: Array[String] = line.split("_")
      UserVisitAction(
        splits(0),
        splits(1).toLong,
        splits(2),
        splits(3).toLong,
        splits(4),
        splits(5),
        splits(6).toLong,
        splits(7).toLong,
        splits(8),
        splits(9),
        splits(10),
        splits(11),
        splits(12).toLong)
    })
//    ImpCategoryCount.statisticOfCategory(sc,actionRDD)
    ImpCategoryCount.statisticOfTop10_2(sc,actionRDD)
    sc.stop()
    val end: Long = System.currentTimeMillis()
    println("total time：" + (end - start) +"ms")
  }
}
