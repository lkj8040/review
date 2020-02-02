package com.test

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, Row, SparkSession, TypedColumn}
import org.apache.spark.sql.expressions.Aggregator

object UDAF_DS{
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession.builder().appName("udf").master("local[2]").getOrCreate()
        val sc: SparkContext = spark.sparkContext
        val df: DataFrame = spark.read.json("./json/people.json")
        
        df.createOrReplaceTempView("json")
        val df2: DataFrame = spark.sql("select if(name is null, \"\", name) name, if(age is null, 0, age) age, if(salary is null, 0, salary) salary from json")
        val df3: DataFrame = df2.select(df2("name"),df2("age").cast("int"),df2("salary"))
        val avg: AVG = new AVG
        val col: TypedColumn[User2, Double] = avg.toColumn.name("avg_salary")
        import spark.implicits._
        val ds: Dataset[User2] = df3.as[User2]
        ds.select(col).show
        
    }
}
//IN BUFFER OUT
class AVG extends Aggregator[User2, buffer, Double]{
    override def zero: buffer = buffer(0D,0)
    
    override def reduce(b: buffer, a: User2): buffer =
        a match {
            case User2(name, age, salary) if (salary == null) => b
            case _ => buffer(b.sum + a.salary, b.count + 1)
        }
    
    override def merge(b1: buffer, b2: buffer): buffer = {
        buffer(b1.sum + b2.sum, b1.count + b2.count)
    }
    
    override def finish(reduction: buffer): Double = reduction.sum/reduction.count
    
    override def bufferEncoder: Encoder[buffer] = Encoders.product
    
    override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}
case class buffer(sum:Double, count:Integer)
case class User2(name:String, age:Integer, salary:Double)
