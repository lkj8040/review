package com.liukuijian.spark.core.exer_sparkSQL

import java.text.DecimalFormat

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, LongType, MapType, StringType, StructField, StructType}

object MyUDAF extends UserDefinedAggregateFunction{
    override def inputSchema: StructType = StructType(StructField("city_name",StringType)::Nil)

    override def bufferSchema: StructType =
        StructType(StructField("map",MapType(StringType,LongType))::StructField("total",LongType)::Nil)

    override def dataType: DataType = StringType

    override def deterministic: Boolean = true

    override def initialize(buffer: MutableAggregationBuffer): Unit = {
        buffer(0) = Map[String, Long]()
        buffer(1) = 0L
    }

    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        if(!input.isNullAt(0)){
            val cityName: String = input.getString(0)
            val map: collection.Map[String, Long] = buffer.getMap[String, Long](0)
            buffer(0) = map + (cityName -> (map.getOrElse(cityName, 0L) + 1L))
            buffer(1) = buffer.getLong(1) + 1L
        }
    }

    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        val map1: collection.Map[String, Long] = buffer1.getMap[String, Long](0)
        val map2: collection.Map[String, Long] = buffer2.getMap[String, Long](0)
        buffer1(0) = map1.foldLeft(map2){
            case (map, (cityName, count)) =>
                map + (cityName -> (map.getOrElse(cityName,0L) + count))
        }
        buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
    }

    override def evaluate(buffer: Row): Any = {
        val cityNameAndCountTop2: List[(String, Long)] = buffer.getMap[String, Long](0).toList.sortBy(-_._2).take(2)
        val total: Long = buffer.getLong(1)
        var result: List[CityRemark] = cityNameAndCountTop2.map {
            case (city, count) => CityRemark(city, count.toDouble / total)
        }
        val other: CityRemark = CityRemark("其他" , result.foldLeft(1D)(_-_.cityRatio))
        val cityRemarks: List[CityRemark] = result :+ other
        cityRemarks.mkString(",")
    }
}
case class CityRemark(cityName: String, cityRatio: Double){
    private val f = new DecimalFormat("0.00%")
    override def toString: String = s"$cityName:${f.format(cityRatio)}"
}
