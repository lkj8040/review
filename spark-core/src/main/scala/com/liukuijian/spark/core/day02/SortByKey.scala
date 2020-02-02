package com.liukuijian.spark.core.day02

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object SortByKey {
  implicit val ord:Ordering[User] = new Ordering[User]{
    override def compare(x: User, y: User): Int = x.age - y.age
  }
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Aggregate")
    val sc = new SparkContext(conf)
//    val rdd1: RDD[(String, Int)] = sc.makeRDD(List(("a",3),("a",2),("c",4),("b",3),("c",6),("c",8)),2)
//    val rdd2: RDD[(String, Int)] = rdd1.sortByKey(ascending = false)
    val rdd1: RDD[User] = sc.parallelize(User(10,"d")::User(20,"e")::Nil,2)
    val rdd2: RDD[(User, Int)] = rdd1.map((_,1)).sortByKey(ascending = false)
    rdd2.collect.foreach(println)
  }
}
case class User(age:Int , name:String)