package com.liukuijian.spark.core.day06

import java.util.Properties

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

object JdbcRead {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
                .builder()
                .appName("jdbcread")
                .master("local[2]")
                .getOrCreate()

        val sc: SparkContext = spark.sparkContext

        //通用的读mysql
//        val df: DataFrame = spark.read.format("jdbc")
//                .option("url", "jdbc:mysql://hadoop101:3306/test")
//                .option("user", "root")
//                .option("password", "L19940816")
//                .option("dbtable", "stu")
//                .load()

        //专用的读mysql
/*
        val url = "jdbc:mysql://hadoop101:3306/test"
        val table = "stu"
        val props = new Properties()
        props.put("user","root")
        props.put("password","L19940816")
        val df: DataFrame = spark.read.jdbc(url,table,props)
        df.show()
*/

        //1.通用的写
        import spark.implicits._
/*        val df: DataFrame = sc.parallelize(Seq((20,"zw"),(18,"awk"))).toDF("age","name")
        df.write.format("jdbc")
                .option("url", "jdbc:mysql://hadoop101:3306/test")
                .option("user", "root")
                .option("password", "L19940816")
                .option("dbtable", "stu")
                .mode("append")
                .save()*/
        //2. 专用的写
        val df= sc.parallelize(List(User(20,"Zhangsan"),User(18,"Lisi"))).toDF
        val url = "jdbc:mysql://hadoop101:3306/test"
        val table = "stu"
        val props = new Properties()
        props.put("user","root")
        props.put("password","L19940816")
        df.write.mode("append").jdbc(url,table, props)

    }
}
