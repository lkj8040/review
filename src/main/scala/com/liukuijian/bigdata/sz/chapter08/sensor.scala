package com.liukuijian.bigdata.sz.chapter08
import java.text.SimpleDateFormat
import java.util.Date
object sensor {
  def main(args: Array[String]): Unit = {
    val time1 = List(
      ( "anheqiao", 1549044122, 10.0 ),
      ( "shengbeilu", 1549044122, 32.0 ),
      ( "pinganjie", 1549044122, 25.0 )
    )
    val time2 = List(
      ( "anheqiao", 1549044123, 13.0 ),
      ( "shengbeilu", 1549044122, 34.0 ),
      ( "pinganjie", 1549044122, 27.0 )
    )
    val time3 = List(
      ( "anheqiao", 1549130522, 14.0 ),
      ( "shengbeilu", 1549130522, 33.0 ),
      ( "pinganjie", 1549130522,26.0 )
    )
    val time4 = List(
      ( "anheqiao", 1549130523, 11.0 ),
      ( "shengbeilu", 1549130522, 32.0 ),
      ( "pinganjie", 1549130522, 23.0 )
    )

//    //什么算同一天的时间
    //println(1549130522/(3600*24))

    val mylist = List(time1,time2,time3,time4)
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val flatten = mylist.flatten

    val tuples = flatten.map(t =>(t._1,sdf.format(t._2 * 1000L),t._3))
//    println(tuples)

    val stringToTuples = tuples.groupBy(_._1)

//    println(stringToTuples)
    println(stringToTuples.mapValues(_.groupBy(_._2)
                                      .toList
                                      .map(t => {
                                        var count = t._2.size
                                        val tuple = t._2.reduce((t1,t2)=>(t1._1,t2._2,t1._3+t2._3))
                                        (tuple._1,tuple._2,tuple._3/count)
                                      }))
                          .toList
                          .flatMap(kv=>kv._2))

    //方法2
//    val sdf = new SimpleDateFormat("yyyy-MM-dd")
//    val AllData: List[List[(String, Int, Double)]] = List(time1, time2, time3, time4)
//    val flatList: List[(String, Int, Double)] = AllData.flatMap(list=>list)
//
//    val formattedData: List[(String, Any, Double)] = flatList.map {
//      case (locate, time, watermark) => {
//        (locate, sdf.format(time * 1000L), watermark)
//      }
//    }
//    val locateAndTimeToWatermark: List[(String, Double)] = formattedData.map {
//      case (locate, time, watermark) => (locate + "_" + time, watermark)
//    }
//
//    val stringToTuples: Map[String, List[(String, Double)]] = locateAndTimeToWatermark.groupBy(_._1)
//
//    val stringToDoubles: Map[String, List[Double]] = stringToTuples.mapValues(_.map(kv=>kv._2))
//
//    val stringToDouble: Map[String, Double] = stringToDoubles.map(kv => {
//      (kv._1, kv._2.sum / kv._2.size)
//    })
//    val result: List[(String, (String, Double))] = stringToDouble.toList.map {
//      case (key, value) => {
//        val strings: Array[String] = key.split("_")
//        (strings(0), (strings(1), value))
//      }
//    }


    //方法2 整合
    var result = List(time1, time2, time3, time4)
                    .flatten
                    .map{case (locate, time, watermark) => (locate+ "_" + sdf.format(time * 1000L), watermark)}
                    .groupBy(_._1)
                    .mapValues(_.map(kv=>kv._2))
                    .map(kv => (kv._1, kv._2.sum / kv._2.size))
                    .toList
                    .map{case (key, value) => {val strings = key.split("_");(strings(0), (strings(1), value))}}
    println(result)
  }
}
