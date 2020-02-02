
import java.text.DecimalFormat

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{ArrayType, DataType, DoubleType, IntegerType, MapType, StringType, StructField, StructType}

object udaf_distribution extends UserDefinedAggregateFunction{
    
    //存储格式为: Map["北京":10]
    override def inputSchema: StructType = StructType(StructField("ele",StringType)::Nil)
    
    override def bufferSchema: StructType = StructType(StructField("map",MapType(StringType, IntegerType))::Nil)
    
    override def dataType: DataType = StringType
    
    override def deterministic: Boolean = true
    
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
        buffer(0) = Map[String, Int]()
    }
    
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        if(!input.isNullAt(0)){
            val city: String = input.getString(0)
            var map: collection.Map[String, Int] = buffer.getMap[String, Int](0)
            map += city -> (map.getOrElse(city, 0) + 1)
            buffer(0) = map
        }
    }
    
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        var map1: collection.Map[String, Int] = buffer1.getMap[String, Int](0)
        val map2: collection.Map[String, Int] = buffer2.getMap[String, Int](0)
        map2.foreach{
            case (city, count) => {
                map1 += city -> (map1.getOrElse(city,0) + count)
            }
        }
        buffer1(0) = map1
    }
    
    override def evaluate(buffer: Row): Any = {
        val map: collection.Map[String, Int] = buffer.getMap[String,Int](0)
        val list: List[(String, Int)] = map.toList.sortBy(-_._2).take(2)
        val total: Int = map.toList.map(_._2).sum
        var result: Map[String, Double] = list.map {
            case (city, count) => (city, count.toDouble / total)
        }.toMap
        result += "其他" -> result.toList.foldLeft(1.0)(_-_._2)
        val f = new DecimalFormat("0.00%")
        result.toList.map{
            case (city, percent) =>
                s"$city:${f.format(percent)}"
        }.mkString(",")
    }
}
