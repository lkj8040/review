import org.apache.spark.util.AccumulatorV2

//In:一条useraction
//Out: 一个Map，记录了order：10 pay:20 click:30
class MyACC extends AccumulatorV2[UserVisitAction, Map[(String,Long),Long]]{
    //category id count
    private var map = Map[(String,Long),Long]()
    
    override def isZero: Boolean = map.isEmpty
    
    override def copy(): AccumulatorV2[UserVisitAction, Map[(String,Long),Long]] = {
        val acc: MyACC = new MyACC
        acc.map = map
        acc
    }
    
    override def reset(): Unit = map = Map[(String,Long),Long]()
    
    override def add(v: UserVisitAction): Unit = {
        if(v.click_category_id != -1){
            map += ("click"->v.click_category_id) -> (map.getOrElse("click"->v.click_category_id,0L) + 1)
        }else if(v.order_category_ids != "null"){
            val ords: Array[String] = v.order_category_ids.split(",")
            ords.foreach(ord =>{
                map += ("order"->ord.toLong) -> (map.getOrElse("order"->ord.toLong,0L) + 1)
            })
        }else if(v.pay_category_ids != "null"){
            val pays: Array[String] = v.pay_category_ids.split(",")
            pays.foreach(pay =>{
                map += ("pay"->pay.toLong) -> (map.getOrElse("pay"->pay.toLong,0L) + 1)
            })
        }
    }
    
    override def merge(other: AccumulatorV2[UserVisitAction, Map[(String,Long),Long]]): Unit = other match{
        case o:MyACC =>
            val map1: Map[(String, Long), Long] = o.value
            map1.foreach{
                case (key,count) =>
                    map += key -> (map.getOrElse(key, 0L) + count)
            }
        case _ => throw new UnsupportedOperationException("未知错误！")
    }
    
    override def value: Map[(String,Long),Long] = map
}
