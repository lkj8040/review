package com.liukuijian.spark.core.day06

import org.apache.spark.SparkContext
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object MyUDF {
    def main(args: Array[String]): Unit = {

        val spark: SparkSession = SparkSession
                .builder()
                .appName("udf")
                .master("local[2]")
                .getOrCreate()

        val sc: SparkContext = spark.sparkContext

        spark.udf.register("myUDF",(s:String)=> if(s != null) s.toUpperCase() else "null")

        val df: DataFrame = spark.read.json("./json/people.json")

        df.createOrReplaceTempView("people" )

        spark.sql("select myUDF(name) as name,age,salary from people").show()

        println("------------------------------我是分割线-----------------------------------")

        spark.udf.register("avg",new Avg)
        spark.sql("select avg(salary) as sum from people").show()

    }
}
/*
* 聚合函数UDAF
* 用于弱类型 在sql语句中使用
*       select my_sum(salary) from user group by name
* 用于强类型  ds使用
* */
class MySum extends UserDefinedAggregateFunction{

    //salary的数据类型，聚合函数的输入类型
    override def inputSchema: StructType = StructType(StructField("c",DoubleType)::Nil)

    //缓冲区(缓存输入数据)类型，
    override def bufferSchema: StructType = StructType(StructField("sum",DoubleType)::Nil)

    //最终聚合后的数据类型
    override def dataType: DataType = DoubleType

    //相同的输入类型是否有相同的输出类型
    override def deterministic: Boolean = true

    //对缓冲区做初始化
    override def initialize(buffer: MutableAggregationBuffer): Unit = buffer(0) = 0D

    //分区内的聚合操作
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        //需要对传入的数据字段做非空判断
        if(!input.isNullAt(0)){
            buffer(0) = buffer.getDouble(0) + input.getDouble(0)
        }
    }

    //分区间的聚合操作
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        //分区2的数据更新到分区1
        buffer1(0) = buffer1.getDouble(0) + buffer2.getAs[Double](0)
    }
    //返回最后的聚合值
    override def evaluate(buffer: Row): Any = buffer(0)
}

//求平均值
class Avg extends UserDefinedAggregateFunction{

    override def inputSchema: StructType = StructType(StructField("f",DoubleType)::Nil)

    override def bufferSchema: StructType = StructType(StructField("i",DoubleType)::StructField("d",DoubleType)::Nil)

    override def dataType: DataType = DoubleType

    override def deterministic: Boolean = true

    override def initialize(buffer: MutableAggregationBuffer): Unit = {
        buffer(0) = 0D
        buffer(1) = 0D
    }

    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        if(!input.isNullAt(0)){
            buffer(1) = buffer.getDouble(1) + input.getDouble(0)
            buffer(0) = buffer.getDouble(0) + 1.0
        }
    }

    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        buffer1(1) = buffer1.getDouble(1) + buffer2.getDouble(1)
        buffer1(0) = buffer1.getDouble(0) + buffer2.getDouble(0)
    }

    override def evaluate(buffer: Row): Any = buffer.getDouble(1) / buffer.getDouble(0)
}