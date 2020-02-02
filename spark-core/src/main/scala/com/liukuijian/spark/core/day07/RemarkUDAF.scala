package com.liukuijian.spark.core.day07

import java.text.DecimalFormat

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, LongType, MapType, StringType, StructField, StructType}

object RemarkUDAF extends  UserDefinedAggregateFunction{
    override def inputSchema: StructType = StructType(StructField("city_name",StringType)::Nil)

    override def bufferSchema: StructType =
        StructType(StructField("map",MapType(StringType,LongType))::StructField("total", LongType)::Nil)

    override def dataType: DataType = StringType

    override def deterministic: Boolean = true

    override def initialize(buffer: MutableAggregationBuffer): Unit = {
        buffer(0) = Map[String, Long]()
        buffer(1) = 0L;
    }

    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        if(!input.isNullAt(0)){
            val city_name: String = input.getString(0)
            val map: collection.Map[String, Long] = buffer.getMap[String, Long](0)
            buffer(0) = map + (city_name -> (map.getOrElse(city_name, 0L) + 1L))
            buffer(1) = buffer.getLong(1) + 1L
        }
    }

    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        //更新总数
        buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
        buffer1(0) = buffer1.getMap[String, Long](0)
                            .foldLeft(buffer2.getMap[String, Long](0)){
                                case (map, (cityName, count)) =>
                                    map + (cityName->(map.getOrElse(cityName,0L) +count))
                            }
    }

    override def evaluate(buffer: Row): Any = {
        val cityAndCount: collection.Map[String, Long] = buffer.getMap[String, Long](0)
        val totalCount: Long = buffer.getLong(1)
        val cityAndCountTop2: List[(String, Long)] = cityAndCount.toList.sortBy(-_._2).take(2)
        val cityRemarks: List[CityRemark] = cityAndCountTop2.map {
            case (cityName, count) =>
                CityRemark(cityName, count.toDouble / totalCount)
        }
        val other: CityRemark = CityRemark("其他", cityRemarks.foldLeft(1D)(_-_.cityRatio))
        val result: List[CityRemark] = cityRemarks :+ other
        result.mkString(", ")
    }
}

case class CityRemark(cityName: String, cityRatio: Double){
    private val f = new DecimalFormat("0.00%")
    override def toString: String = s"$cityName:${f.format(cityRatio)}"
}
