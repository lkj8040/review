package acc

import bean.UserVisitAction
import org.apache.spark.util.AccumulatorV2

//注意In 和 Out的定义
//这里使用Map定义Out
//例如: Map(("click",1),("pay",1),("order",1))

class CategoryAcc extends AccumulatorV2[UserVisitAction, Map[(String,String),Long]]{
    private var map: Map[(String,String),Long] = Map[(String,String),Long]()
    override def isZero: Boolean = map.isEmpty

    override def copy(): CategoryAcc = {
        val acc = new CategoryAcc
        acc.map = map
        acc
    }

    override def reset(): Unit = map = Map[(String,String),Long]()

    override def add(v: UserVisitAction): Unit = {
        if(v.click_category_id != -1){
            map += (v.click_category_id.toString,"click") -> (map.getOrElse((v.click_category_id.toString,"click"),0L) + 1L)
        }else if(v.order_category_ids != "null"){
            val orders: Array[String] = v.order_category_ids.split(",")
            orders.foreach(ord =>{
                map += (ord,"order") -> (map.getOrElse((ord,"order"),0L) + 1L)
            })
        }else if(v.pay_category_ids != "null"){
            val pays: Array[String] = v.pay_category_ids.split(",")
            pays.foreach(pay =>{
                map += (pay,"pay") -> (map.getOrElse((pay,"pay"),0L) + 1L)
            })
        }
    }

    override def merge(other: AccumulatorV2[UserVisitAction, Map[(String,String),Long]]): Unit = other match{
        case o:CategoryAcc =>
            map = o.map.foldLeft(this.map){
                case (map, (key,count)) =>
                    map + (key->(count + this.map.getOrElse(key,0L)))
            }
        case _ => throw new UnsupportedOperationException("类型错误")
    }

    override def value: Map[(String,String),Long] = map
}
