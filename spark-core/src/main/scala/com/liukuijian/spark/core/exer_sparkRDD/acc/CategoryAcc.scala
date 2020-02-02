package com.liukuijian.spark.core.exer_sparkRDD.acc

import com.liukuijian.spark.core.exer_sparkRDD.bean.UserVisitAction
import org.apache.spark.util.AccumulatorV2

//此acc实现每个品类的点击数、订单数和支付数的功能
//每个品类的数据被统计成Map[String,Map(String, Long)]的格式
//分别表示 cid --> [(click,clickCount),(order, orderCount),(pay, payCount)] 的数据格式
class CategoryAcc extends AccumulatorV2[UserVisitAction, Map[String, Map[String, Long]]]{
  private var map = Map[String, Map[String, Long]]()

  override def isZero: Boolean = map.isEmpty

  override def reset(): Unit = map = Map[String, Map[String,Long]]()

  override def copy(): AccumulatorV2[UserVisitAction, Map[String, Map[String, Long]]] ={
    val acc: CategoryAcc = new CategoryAcc
    acc.map = map
    acc
  }

  override def add(v: UserVisitAction): Unit = {
    if(v.click_category_id != -1){
      var other: Map[String, Long] = map.getOrElse(v.click_category_id.toString,Map())
      other += "click"-> (other.getOrElse("click",0L) + 1L)
      map   += (v.click_category_id.toString -> other)
    }else if(v.order_category_ids != "null"){
      var ords: Array[String] = v.order_category_ids.split(",")
      ords.foreach(ord =>{
        var other: Map[String, Long] = map.getOrElse(ord,Map())
        other += "order"-> (other.getOrElse("order",0L) + 1L)
        map   += (ord   -> other)
      })
    }else if(v.pay_category_ids != "null"){
      var pays: Array[String] = v.pay_category_ids.split(",")
      pays.foreach(pay =>{
        var other: Map[String, Long] = map.getOrElse(pay,Map())
        other += "pay" -> (other.getOrElse("pay",0L) + 1L)
        map   += (pay  -> other)
      })
    }
  }

  override def merge(other: AccumulatorV2[UserVisitAction, Map[String, Map[String, Long]]]): Unit = {
    other.value.foreach{
      case (cid, it) =>
        var tmp: Map[String, Long] = map.getOrElse(cid,Map())
        tmp += "click"-> (it.getOrElse("click",0L) + tmp.getOrElse("click",0L))
        tmp += "order"-> (it.getOrElse("order",0L) + tmp.getOrElse("order",0L))
        tmp += "pay"  -> (it.getOrElse("pay",0L)   + tmp.getOrElse("pay",0L))
        map += cid->tmp
    }
  }

  override def value: Map[String, Map[String, Long]] = map
}
