package com.liukuijian.spark.core.day06

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, SparkSession, TypedColumn}
import org.apache.spark.sql.catalyst.expressions.Encode
import org.apache.spark.sql.expressions.{Aggregator, UserDefinedAggregateFunction}

object UDFDemo {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
                .builder()
                .appName("udf")
                .master("local[2]")
                .getOrCreate()

        val sc: SparkContext = spark.sparkContext

        spark.udf.register("myUDF",(s:String)=> if(s != null) s.toUpperCase() else "null")

        val df: DataFrame = spark.read.json("./json/people.json")
        import spark.implicits._
        val userDS: Dataset[User] = df.as[User]
        val col: TypedColumn[User, Double] = AVG.toColumn.name("my_avg")
        val ds: Dataset[Double] = userDS.select(col)
        ds.show()
    }
}
case class User(age:Integer, name:String)
case class AgeAVG(ageSum:Integer, ageCount:Integer){
    def ageAVG = ageSum.toDouble / ageCount
}
//强类型的聚合函数，用于DS
object AVG extends Aggregator[User, AgeAVG, Double]{
    //对缓冲区做初始化
    override def zero: AgeAVG = AgeAVG(0,0)
    //分区内的聚合
    override def reduce(b: AgeAVG, a: User): AgeAVG = a match {
        case User(age, name) if (age == null) => b
        case u:User => AgeAVG(b.ageSum+u.age,b.ageCount+1)
    }
    //分区间的聚合
    override def merge(b1: AgeAVG, b2: AgeAVG): AgeAVG = {
        AgeAVG(b1.ageSum + b2.ageSum, b1.ageCount+b2.ageCount)
    }
    //返回最终的聚合值
    override def finish(reduction: AgeAVG): Double = reduction.ageAVG
    //缓冲的编码器
    override def bufferEncoder: Encoder[AgeAVG] = Encoders.product //样例类使用该类型
    //输出的编码器
    override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}