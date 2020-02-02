package com.liukuijian.spark.core.day06

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object ReadAndWrite {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
                .builder()
                .appName("udf")
                .master("local[2]")
                .getOrCreate()

        val sc: SparkContext = spark.sparkContext
        //通用的读方法 ,默认格式(没有format)是 parquet
//        val df: DataFrame = spark.read.format("json").load("./json/people.json")
        val rdd: RDD[String] = sc.textFile("D:\\ml-1m\\movies.dat")
//        val df: DataFrame = spark.read.format("textFile").load("D:\\ml-1m\\movies.dat")
        val rdd2: RDD[movies] = rdd.map {
            line => {
                val splits: Array[String] = line.split("::")
                movies(splits(0).toInt, splits(1), splits(2))
            }
        }
        import spark.implicits._
        val df: DataFrame = rdd2.toDF("id","movie_name","category")

        df.show()
        //通用的写文件
        df.write.format("csv").save("d:/movies")
        df.write.mode(SaveMode.Overwrite).save("d:/movies")
    }
}
case class movies(id:Int, name:String,category:String)