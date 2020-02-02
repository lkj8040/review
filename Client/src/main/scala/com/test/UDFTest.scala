package com.test

import java.util.Properties

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object UDFTest {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession.builder().appName("udf").master("local[2]").getOrCreate()
        val sc: SparkContext = spark.sparkContext
//        val props = new Properties()
//        props.setProperty("user","root")
//        props.setProperty("password","L19940816")
//        val df: DataFrame = spark.read.jdbc("jdbc:mysql://hadoop101:3306/test", "stu" ,props)
//        df.createOrReplaceTempView("stu")
//        spark.sql("select * from stu").show()
        
        val df: DataFrame = spark.read.json("./json/people.json")
        
        
        //转为DS
        import spark.implicits._
        val ds: Dataset[User1] = df.as[User1]
        
        //转为DF
        val df2: DataFrame = ds.toDF("name", "age", "salary")
        
        //转为rdd
        val rdd: RDD[User1] = ds.rdd
        
        //转为DF
        val df4: DataFrame = rdd.toDF("name","age","salary")
    
        df4.createOrReplaceTempView("json")
        def myudf(name:String) ={
            if(name != null) name else ""
        }
        spark.udf.register("myudf", myudf _)
        spark.sql("select age, myudf(name) as name, salary from json" ).show()
    }
}
case class User1(name:String, age:BigInt, salary:BigInt)
