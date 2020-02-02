package app

import acc.CategoryAcc
import bean.{CategoryCountInfo, UserVisitAction}
import org.apache.spark.{Partitioner, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable

object ImpFuncOfStatistic {

    //需求1：统计热门品类Top10的点击次数、下单次数和支付次数
    def statisticTop10ClickOrderPay(sc:SparkContext, userVisitAction: RDD[UserVisitAction])={
        //需求：每来一个action记录一次点击、下单或者支付
        //使用累加器完成这个功能
        val myAcc: CategoryAcc = new CategoryAcc
        sc.register(myAcc,"CategoryAcc")
        userVisitAction.foreach(action =>{
            myAcc.add(action)
        })
        //每种品类的每个action的数量
        val cidAndActionCount = myAcc
                        .value
                        .groupBy(_._1._1)

        val result: List[CategoryCountInfo] = cidAndActionCount
                .map{
                    case (cid, map) =>
                        CategoryCountInfo(
                            cid,
                            map.getOrElse((cid,"click"), 0L),
                            map.getOrElse((cid,"order"), 0L),
                            map.getOrElse((cid,"pay"), 0L))
                }
                .toList
                .sortBy(action => (-action.clickCount, -action.orderCount, -action.payCount))
                .take(10)

        result
    }

    //需求2:求Top10热门品类中每个品类的 Top10 活跃 Session 统计
    def statisticTop10_Click(sc:SparkContext, userVisitAction: RDD[UserVisitAction]): Unit ={
        val cids: List[String] = statisticTop10ClickOrderPay(sc, userVisitAction).map(_.categoryId)
        println(cids)
        //Top10每个品类的点击情况
        val filteredRDD: RDD[UserVisitAction] =
            userVisitAction.filter(action => cids.contains(action.click_category_id.toString))

        //每个品类每点击一次记一次
        //sid    cid   1
        val sidClickAndOne: RDD[((String, Long), Int)] =
            filteredRDD.map(action => ((action.session_id,action.click_category_id),1))

        //每个品类每个
        val sidCidAndCount: RDD[(String, (String, Int))] =
            sidClickAndOne.reduceByKey(new MyCategoryPartitioner(cids), _+_).map{
                case ((sid, cid),count) =>(cid.toString, (sid, count))
            }
        println(sidCidAndCount.getNumPartitions)
        val sidAndCount: RDD[(String, SessionIdAndCount)] = sidCidAndCount.mapPartitions (
            it => {
                var set: mutable.TreeSet[SessionIdAndCount] = mutable.TreeSet[SessionIdAndCount]()
                var CategoryId: String = ""
                it.foreach {
                    case (cid, (sid, count)) => {
                        set += SessionIdAndCount(sid, count)
                        if (set.size > 10) set = set.take(10)
                        CategoryId = cid
                    }
                }
                val sortedSet: mutable.SortedSet[(String, SessionIdAndCount)] = set.map((CategoryId,_))
                sortedSet.toIterator
            })
        sidAndCount.sortBy(_._1.toInt).collect.foreach(println)
    }

    //需求3:页面单跳转化率统计
    def statisticTransferRate(sc:SparkContext, userVisitAction: RDD[UserVisitAction], Pages:String): Unit ={
        //首先得到所有的页面以及页面的跳转情况
        val pages: Array[String] = Pages.split(",")
        val prePages: Array[String] = pages.slice(0,pages.length-1)
        val postPages: Array[String] = pages.slice(1,pages.length)
        val PageFlow: Array[String] = prePages.zip(postPages).map {
            case (prepage, postpage) => s"$prepage->$postpage"
        }
        //求每个页面的点击次数,作为分母
        // "2" -> 100
        val CountOfEachPage: collection.Map[String, Long] =
            userVisitAction
                    .filter((action: UserVisitAction) => prePages.contains(action.page_id.toString))
                    .map((action: UserVisitAction) => (action.page_id.toString,1))
                    .countByKey()

        //求页面跳转的点击次数
        // "1->2" 100
        //需要得到每一个session前一次点击、后一次点击、然后做拉链
        val sidAndAction: RDD[(String, Iterable[UserVisitAction])] = userVisitAction.groupBy(_.session_id)
        val totalPageFlow: RDD[(String, Int)] = sidAndAction.flatMap {
            case (sid, it) => {
                val actions: List[UserVisitAction] = it.toList.sortBy(_.action_time)
                val preActions: List[UserVisitAction] = actions.slice(0, actions.length - 1)
                val postActions: List[UserVisitAction] = actions.slice(1, actions.length)
                preActions.zip(postActions).map {
                    case (preAction, postAction) => s"${preAction.page_id}->${postAction.page_id}"
                }.filter(flow => PageFlow.contains(flow)).map((_, 1))
            }
        }
        val eachPageFlow: collection.Map[String, Long] = totalPageFlow.countByKey()

        //求转化率
        val result: collection.Map[String, Double] = eachPageFlow.map {
            case (k,v) => k->(v.toDouble/CountOfEachPage(k.split("->")(0)))
        }
        result.foreach(println)

    }
}

case class SessionIdAndCount(sid:String, count:Long) extends Ordered[SessionIdAndCount]{
    override def compare(that: SessionIdAndCount): Int = if(this.count < that.count) 1 else -1
}

class MyCategoryPartitioner(cids:List[String]) extends Partitioner{
    private val map: Map[String, Int] = cids.zipWithIndex.toMap

    override def numPartitions: Int = cids.length

    override def getPartition(key: Any): Int = key match {
        case (_,cid:Long) => map(cid.toString)
        case _ => throw new UnsupportedOperationException("非法key")
    }
}
