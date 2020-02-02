
import java.text.DecimalFormat

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkContext}

import scala.collection.mutable



object StatisticImpl {
    
    //需求1：所有品类点击、下单、支付数量top10
    def clickOrderPayTop10(sc: SparkContext, userVisitActionRDD: RDD[UserVisitAction]) ={
        val acc: MyACC = new MyACC
        sc.register(acc,"acc")
        userVisitActionRDD.foreach(action =>{//不能 用map，用foreach，因为map是转换算子
            acc.add(action)
        })
        
        val clickOrderPayAndCount: Map[(String, Long), Long] = acc.value
        
        clickOrderPayAndCount
                .toList
                .map{case((action, category),count)=> (category,(action,count))}
                .groupBy(_._1)
                .map{ case (key, list) =>(key, list.map(_._2)) }
                .toList
                .map{
                    case (category, list) => {
                    val tmp: Map[String, Long] = list.toMap
                    CategoryCountInfo(category.toString, tmp("click"),tmp("order"),tmp("pay"))}}
                .sortBy(info => (-info.clickCount,-info.orderCount,-info.payCount))
                .take(10)
    }
    
    //需求2：Top10热门品类中每个品类的 Top10 活跃 Session 统计
    def Top10CategorySessionCount(clickOrderPayTop10:List[CategoryCountInfo], userVisitActionRDD: RDD[UserVisitAction]) ={
        //先把top10品类取出
        val categories: List[String] = clickOrderPayTop10.map(_.categoryId)
        //对userVisitAction过滤，不是top10品类的丢弃
        val top10UserVisitAction: RDD[UserVisitAction] = userVisitActionRDD.filter(action => categories.contains(action.click_category_id.toString))
        
        //统计各个品类的session活跃次数,排序取前10
        top10UserVisitAction.map(action =>((action.click_category_id,action.session_id),1))
                .reduceByKey(_+_)
                .map{ case ((c_id, s_id), count) => (c_id, (s_id, count))}
                .groupByKey(new MyPartitioner(10))
                .mapPartitions(it => it.map{
                    case (c_id,it) =>{
                        var categoryId: Long = c_id
                        val set: mutable.TreeSet[Bean] = mutable.TreeSet[Bean]()
                        
                        it.foreach{
                            case (s_id, count) =>{
                                set.add(Bean(s_id,count))
                                if(set.size > 10) set.take(10)
                            }
                        }
                        (categoryId, set.toList)
                    }
                })
                .collect
                .foreach(println)
    }
    //页面单跳转化率统计
    def transferRate( userVisitActionRDD: RDD[UserVisitAction],pages:String) ={
        val allPages: Array[String] = pages.split(",")
        //获取每个页面的点击次数:分母
    
        val startPages: Array[String] = allPages.slice(0,allPages.length-1)
        val endPages: Array[String] = allPages.slice(1,allPages.length)
    
        val startToEndPages: Array[String] = startPages.zip(endPages).map {
            case (start, end) => start + "_" + end
        }
        val actionInTargetPages: RDD[UserVisitAction] =
            userVisitActionRDD.filter(action => startPages.contains(action.page_id.toString))

        val pageAndCount: RDD[(Long, Int)] = actionInTargetPages.map(action => (action.page_id, 1)).reduceByKey(_+_)
        //获取页面跳转的点击次数
        //注意，这里不能提前过滤出目标页面，因为过滤后时间上是不连续的！！！！
        val transPagesCount: RDD[(String, Map[String, Int])] = userVisitActionRDD.groupBy(_.session_id).map {
            case (sessionId, it) => {
                val actions: List[String] = it.toList.sortBy(_.action_time).map(action => action.page_id.toString)
                val startActions: List[String] = actions.slice(0, actions.length - 1)
                val endActions: List[String] = actions.slice(1, actions.length)
                val pageAndCount: Map[String, Int] = startActions
                        .zip(endActions)
                        .map { case (start, end) => start + "_" + end }
                        .filter(startToEndPages.contains(_))
                        .map((_, 1))
                        .groupBy(_._1)
                        .mapValues(_.map(_._2).sum)
                (sessionId,pageAndCount)
            }
        }
        val eachTransPageCount: RDD[(String, Int)] = transPagesCount
                .map(_._2.toList)
                .flatMap(x => x)
                .groupByKey()
                .map { case (pageTrans, it) => (pageTrans, it.sum) }
        
        val singlePageTotal: Map[Long, Int] = pageAndCount.collect().toMap
        val f: DecimalFormat = new DecimalFormat("0.00%")
        eachTransPageCount.map{
            case (startToEnd, count) => {
                val total: Int = singlePageTotal(startToEnd.split("_")(0).toLong)
                (startToEnd, f.format(count.toDouble/total))
            }
        }.foreach(println)
        
    }
}
class MyPartitioner(num: Int) extends Partitioner{
    override def numPartitions: Int = num
    
    override def getPartition(key: Any): Int = key match{
        case id:Long => math.abs(id.toInt) % numPartitions
        case _ => throw new IllegalArgumentException("非法category_id")
    }
    
    override def hashCode(): Int = num
    
    override def equals(obj: Any): Boolean = obj match{
        case o:MyPartitioner => o.numPartitions == this.numPartitions
        case _ => false
    }
}
case class Bean(sessionId:String, count:Int) extends Comparable[Bean]{
    override def compareTo(o: Bean): Int = o.count - this.count
}