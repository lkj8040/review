package com.test

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, StructField, StructType}

class MySum_UDAF extends UserDefinedAggregateFunction{
    //聚合函数的输入类型
    override def inputSchema: StructType = StructType(Array(StructField("c",DoubleType)))
    //缓冲区的输入类型
    override def bufferSchema: StructType = StructType(StructField("sum",DoubleType)::Nil)
    //最终聚合后的数据类型
    override def dataType: DataType = DoubleType
    //相同的输入类型是否有相同的输出类型
    override def deterministic: Boolean = true
    //初始化
    override def initialize(buffer: MutableAggregationBuffer): Unit = buffer(0) = 0D
    //分区内更新
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        if(!input.isNullAt(0)){
            buffer(0) = buffer.getDouble(0) + input.getDouble(0)
        }
    }
    //分区间聚合
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        buffer1(0) = buffer1.getDouble(0) + buffer2.getDouble(0)
    }
    //返回最后的聚合值
    override def evaluate(buffer: Row): Any = buffer(0)
}
