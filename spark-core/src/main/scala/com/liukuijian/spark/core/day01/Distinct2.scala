package com.liukuijian.spark.core.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Distinct2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Sample")
    val sc: SparkContext = new SparkContext(conf)
    val users: List[User] = List(User(10,"a"),User(20,"b"),User(20,"c"),User(30,"c"))
    val rdd1: RDD[User] = sc.parallelize(users, 2)
    val ord: Ordering[User] = new Ordering[User]() {
      override def compare(x: User, y: User): Int = x.age - y.age
    }
    val rdd2: RDD[User] = rdd1.distinct(2)
    rdd2.collect().foreach(println)
    sc.stop()
  }
}
case class User(age:Int, name: String) extends Serializable{
  
  override def hashCode(): Int = {
    age.hashCode()
  }
  
  override def equals(obj: Any) = {
    val user: User = obj.asInstanceOf[User]
    val self: User = this.asInstanceOf[User]
    self.age == user.age
  }
}
/*
1 先看hashcode
2 再看是否为同一个对象
3 然后看equals
 */