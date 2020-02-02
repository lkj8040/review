package com.test

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

object UDAF_Test {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession.builder().appName("udf").master("local[2]").getOrCreate()
        val sc: SparkContext = spark.sparkContext
        val mysum: MySum_UDAF = new MySum_UDAF
        spark.udf.register("my_sum", mysum)
    
        val df: DataFrame = spark.read.json("./json/people.json")
        
        df.createOrReplaceTempView("json")
        
        spark.sql("select my_sum(salary) total_sum from json").show(false)
        spark.stop()
    }
}
