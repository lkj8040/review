package com.liukuijian.spark.core.exer_day03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object movierecommand {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("movie")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[String] = sc.textFile("D:/ml-1m/users.dat")
    val rdd2: RDD[Char] = rdd1.flatMap(_.split("::")(1))
    val rdd3: RDD[(Char, Int)] = rdd2.map((_,1))
    val rdd4: RDD[(Char, Int)] = rdd3.reduceByKey(_+_)//男女人数
    //男女人数
    rdd4.collect.foreach(println)
    println("-------------分隔线---------------")
    //男女各年龄段人数
    val rdd5: RDD[((String, String), Int)] = rdd1.map(x => {
      val split: mutable.ArrayOps[String] = x.split("::")
      ((split(1), split(2)),1)
    })
    val rdd6: RDD[((String, String), Int)] = rdd5.reduceByKey(_+_).sortBy({
        case ((key, value),count) => (key, count)
      },false)

    val rdd7: RDD[String] = rdd6.map(x => x match {
      case ((x, "1"), y) => (x + " Under18 " + y)
      case ((x, "18"), y) => (x + " 18-24 " + y)
      case ((x, "25"), y) => (x + " 25-34 " + y)
      case ((x, "35"), y) => (x + " 35-44 " + y)
      case ((x, "45"), y) => (x + " 45-49 " + y)
      case ((x, "50"), y) => (x + " 50-55 " + y)
      case ((x, "56"), y) => (x + " 56+ " + y)
    })
    rdd7.collect.foreach(println)
    println("---------------分隔线-------------------")
    //各职业人数及男女比例
    val rdd8: RDD[((String, String), Int)] = rdd1.map(line => {
      val split: Array[String] = line.split("::")
      ((split(3), split(1)), 1)
    })
    val rdd9: RDD[((String, String), Int)] = rdd8.reduceByKey(_+_)
    val rdd10: RDD[(String, (String, Int))] = rdd9.map {
      case ((job, gender), count) => {
        (job, (gender, count))
      }
    }
    val rdd11: RDD[String] = rdd10.sortByKey().map {
      case ("0",  (gender, count)) => gender + ":other or not specified:" + count
      case ("1",  (gender, count)) => gender + ":academic/educator:" + count
      case ("2",  (gender, count)) => gender + ":artist:" + count
      case ("3",  (gender, count)) => gender + ":clerical/admin:" + count
      case ("4",  (gender, count)) => gender + ":college/grad student:" + count
      case ("5",  (gender, count)) => gender + ":customer service:" + count
      case ("6",  (gender, count)) => gender + ":doctor/health care:" + count
      case ("7",  (gender, count)) => gender + ":executive/managerial:" + count
      case ("8",  (gender, count)) => gender + ":farmer:" + count
      case ("9",  (gender, count)) => gender + ":homemaker:" + count
      case ("10", (gender, count)) => gender + ":K-12 student:" + count
      case ("11", (gender, count)) => gender + ":lawyer:" + count
      case ("12", (gender, count)) => gender + ":programmer:" + count
      case ("13", (gender, count)) => gender + ":retired:" + count
      case ("14", (gender, count)) => gender + ":sales/marketing:" + count
      case ("15", (gender, count)) => gender + ":scientist:" + count
      case ("16", (gender, count)) => gender + ":self-employed:" + count
      case ("17", (gender, count)) => gender + ":technician/engineer:" + count
      case ("18", (gender, count)) => gender + ":tradesman/craftsman:" + count
      case ("19", (gender, count)) => gender + ":unemployed:" + count
      case ("20", (gender, count)) => gender + ":writer:" + count
    }
    val rdd12: RDD[(String, Int)] = rdd11.map(line => {
      val split: Array[String] = line.split(":")
      (split(1), split(2).toInt)
    }).reduceByKey(_ + _).sortBy(_._2, false)
    rdd12.collect.foreach(println)


    println("------------分割线-------------")
    println(rdd12.map(_._2).sum())
  }
}
