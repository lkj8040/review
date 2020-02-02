package com.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

object sparksql_readwrite {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().set("spark.sql.sources.default","parquet")
        val spark: SparkSession =
            SparkSession
                .builder()
                .config(conf)
                .master("local[2]")
                .appName("readwrite")
                .getOrCreate()
        
        val sc: SparkContext = spark.sparkContext
        
        val rdd: RDD[(Int, String)] = sc.makeRDD(Array((10,"zhangsan"),(11,"lisi"),(12,"wangwu")),2)
        
        import spark.implicits._
        val df: DataFrame = rdd.toDF("age", "name")
        df.write.format("jdbc")
                .option("url","jdbc:mysql://hadoop101:3306/test")
                .option("user","root")
                .option("dbtable","stu")
                .option("password","L19940816")
                .mode(SaveMode.Append)
                .save()
    }
}
