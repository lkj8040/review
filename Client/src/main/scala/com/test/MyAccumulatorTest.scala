package com.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MyAccumulatorTest {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("accTest").setMaster("local[2]")
        val sc: SparkContext = new SparkContext(conf)
        val acc: MyAccumulate = new MyAccumulate
        sc.register(acc,"myAcc")
    
        val rdd: RDD[Int] = sc.makeRDD(List(1,2,3,4,5,6),2)
        rdd.map(
            x =>{
                acc.add(1)
                x
            }
        ).collect
        println(acc.value)
    }
}
