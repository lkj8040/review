package com.liukuijian.spark.core.day08

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object UpdateStateByKey {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("updateByKey")



    }
}
