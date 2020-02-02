package com.liukuijian.spark.core.exer_sparkRDD.app

import com.liukuijian.spark.core.exer_sparkRDD.acc.CategoryAcc
import com.liukuijian.spark.core.exer_sparkRDD.bean.{CategoryCountInfo, UserVisitAction}
import org.apache.spark.{Partitioner, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.{immutable, mutable}


object ImpCategoryCount {

    def statisticOfCategory(sc: SparkContext, actionRDD: RDD[UserVisitAction]) = {
        //将actionRDD中的action进行分类统计，得到每个品类的点击次数、订单数、支付数
        //首先进行注册
        val acc: CategoryAcc = new CategoryAcc
        sc.register(acc, "myCategoryAcc")

        actionRDD.foreach(action => {
            acc.add(action)
        })
        val cidAndClickOrderPayCount: Map[String, Map[String, Long]] = acc.value
        val info: immutable.Iterable[CategoryCountInfo] = cidAndClickOrderPayCount.map {
            case (cid, it) =>
                CategoryCountInfo(cid, it("click"), it("order"), it("pay"))
        }
        info
            .toArray
            .sortBy(info => (-info.clickCount, -info.orderCount, -info.payCount))
            .take(10)
    }

    def statisticOfTop10(sc: SparkContext, actionRDD: RDD[UserVisitAction]) = {
        val cidAndClickOrderPayCountTop10: Array[CategoryCountInfo] = statisticOfCategory(sc, actionRDD)

        val cids: Array[String] = cidAndClickOrderPayCountTop10.map(_.categoryId).sortBy(_.toInt)
        for (cid <- cids) {

            val actionInTop10: RDD[UserVisitAction] =
                actionRDD.filter(action => cid == action.click_category_id.toString)

            val sessioncidAndOne: RDD[((String, String), Int)] =
                actionInTop10.map(action => ((action.session_id, action.click_category_id.toString), 1))

            val sessioncidAndCount: RDD[((String, String), Int)] = sessioncidAndOne.reduceByKey(_ + _)

            val tuples: Array[(String, List[(String, Int)])] = sessioncidAndCount.sortBy(-_._2).take(10).map {
                case ((sid, cid), count) => (cid, (sid, count))
            }.groupBy(_._1).map {
                case (key, list) => (key, list.map(_._2).toList)
            }.toArray
            println(tuples.mkString(","))
        }

        //    result.collect.foreach(println)

    }

    def statisticOfTop10_1(sc: SparkContext, actionRDD: RDD[UserVisitAction]) = {
        //获取点击次数top10的品类id
        val cidAndClickOrderPayCountTop10: Array[CategoryCountInfo] = statisticOfCategory(sc, actionRDD)
        //map为数组
        val cids: Array[String] = cidAndClickOrderPayCountTop10.map(_.categoryId)

        //如果action的cid在cids中，说明这个action点击了top10中的cid
        val actionInTop10: RDD[UserVisitAction] =
            actionRDD.filter(action => cids.contains(action.click_category_id.toString))

        //统计每个session点击top10中每种cid的次数
        val sessioncidAndOne: RDD[((String, String), Int)] =
            actionInTop10.map(action => ((action.session_id, action.click_category_id.toString), 1))

        val sessionAndcidCount: RDD[((String, String), Int)] = sessioncidAndOne.reduceByKey(_ + _)

        //转换成以cid为key，方便后面统计每种cid各个session的点击次数
        val cidAndsidCount: RDD[(String, (String, Int))] = sessionAndcidCount.map {
            case ((sid, cid), count) => (cid, (sid, count))
        }
        //取每种cid点击次数top10的session及其点击次数
        val result: RDD[(String, List[(String, Int)])] =
            cidAndsidCount
                    .groupByKey()
                    .mapValues(_.toList.sortBy(-_._2).take(10)).sortBy(_._1.toInt)

        result.collect.foreach(println)
    }

    def statisticOfTop10_2(sc: SparkContext, actionRDD: RDD[UserVisitAction]) = {
        //获取点击次数top10的品类id
        val cidAndClickOrderPayCountTop10: Array[CategoryCountInfo] = statisticOfCategory(sc, actionRDD)
        //map为数组
        val cids: Array[String] = cidAndClickOrderPayCountTop10.map(_.categoryId)

        //如果action的cid在cids中，说明这个action点击了top10中的cid
        val actionInTop10: RDD[UserVisitAction] =
            actionRDD.filter(action => cids.contains(action.click_category_id.toString))

        //统计每个session点击top10中每种cid的次数
        val sessioncidAndOne: RDD[((String, String), Int)] =
            actionInTop10.map(action => ((action.session_id, action.click_category_id.toString), 1))

        // 依次是  sid cid count
        val sessionAndcidCount: RDD[(String, (String, Int))] =
            sessioncidAndOne.reduceByKey(new CategoryPartitioner(cids), _ + _).map{
                case ((sid, cid), count) => (cid, (sid, count))
            }
        val cidAndsidCount: RDD[(String, sidAndCount)] = sessionAndcidCount.mapPartitions(it => {
            //对每个分区的统计数据，取前10, TreeSet中存放sid 和 count
            var set: mutable.TreeSet[sidAndCount] = new mutable.TreeSet[sidAndCount]()
            var categoryId = ""
            it.foreach {
                case (cid ,(sid, count)) =>
                    set += sidAndCount(sid, count)
                    if (set.size > 10) set = set.take(10)
                    categoryId = cid
            }
            set.map((categoryId, _)).toIterator
        })
        cidAndsidCount.sortBy{
            case (key,it) => (key.toInt,-it.Count)
        }.collect.foreach(println)
    }

    //需求3：漏斗分析
    def statisticOfTop10_3(sc: SparkContext, actionRDD: RDD[UserVisitAction],pages:String) = {
        //1. 得到目标页面
        val splits: Array[String] = pages.split(" ")

        //2.得到我们需要计算点击量的页面
        val prePages: Array[String] = splits.slice(0, splits.length - 1)
        val postPages: Array[String] = splits.slice(1, splits.length)
        val pageFlow: Array[String] = prePages.zip(postPages).map {
            case (prePage, postPage) => s"$prePage->$postPage"
        }
        //3.计算目标点击量(分母)
        val pageAndCount: collection.Map[Long, Long] =
            actionRDD
                    .filter(action => prePages.contains(action.page_id.toString))
                    .map(action => (action.page_id, 1))
                    .countByKey()

        //4. 目标跳转流量的数量(分子)
        val sessionIdGrouped: RDD[(String, Iterable[UserVisitAction])] = actionRDD.groupBy(_.session_id)
        val totalPageFlow = sessionIdGrouped.flatMap {
            case (sessionId, actionIt) =>
                val actions: List[UserVisitAction] = actionIt.toList.sortBy(_.action_time)
                val preActions: List[UserVisitAction] = actions.slice(0, actions.length - 1)
                val postActions: List[UserVisitAction] = actions.slice(1, actions.length)
                preActions.zip(postActions).map {
                    case (preAction, postAction) => s"${preAction.page_id}->${postAction.page_id}"
                }.filter(flow => pageFlow.contains(flow))
        }.map((_,1)).countByKey()

        val result: Iterable[Double] = totalPageFlow.map {
            case (pageFlow, count) => count.toDouble / pageAndCount(pageFlow.split("->")(0).toLong)
        }
        result.foreach(println)


    }


}

/*累加器在行动算子中使用才是合理的，如果在转换算子中使用可能会造成结果的错误*/

//自定义一个分区器，使得所有具有相同cid的键值对进入同一个分区
class CategoryPartitioner(cids:Array[String]) extends Partitioner{

    private val map: Map[String, Int] = cids.zipWithIndex.toMap

    override def numPartitions: Int = cids.length

    override def getPartition(key: Any): Int = key match{
            //每个cid对应一个分区，不同的key进入不同的分区
        case (_:String,cid:String) => map(cid)
        case _ => throw new UnsupportedOperationException("未知cid异常")
    }
}
case class sidAndCount(sid:String, Count:Int) extends Ordered[sidAndCount]{
    override def compare(that: sidAndCount): Int = if(this.Count < that.Count) 1 else -1
}