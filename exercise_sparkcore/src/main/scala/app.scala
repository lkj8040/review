
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object app {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("clickorderpaytop10").setMaster("local[*]")
        val sc: SparkContext = new SparkContext(conf)
    
        val rawRDD: RDD[String] = sc.textFile("d:/user_visit_action.txt")
    
        val userVisitActionRDD: RDD[UserVisitAction] = rawRDD.map {
            line => {
                val splits: Array[String] = line.split("_")
                UserVisitAction(
                    splits(0),
                    splits(1).toLong,
                    splits(2),
                    splits(3).toLong,
                    splits(4),
                    splits(5),
                    splits(6).toLong,
                    splits(7).toLong,
                    splits(8),
                    splits(9),
                    splits(10),
                    splits(11),
                    splits(12).toLong
                )
            }
        }
    
        val clickOrderPayTop10: List[CategoryCountInfo] = StatisticImpl.clickOrderPayTop10(sc, userVisitActionRDD)
        clickOrderPayTop10.foreach(println)
        println("-------------------------------------------我是分隔线-----------------------------------------------")
        StatisticImpl.Top10CategorySessionCount(clickOrderPayTop10, userVisitActionRDD)
        println("-------------------------------------------我是分隔线-----------------------------------------------")
        StatisticImpl.transferRate(userVisitActionRDD, "1,2,3,4,5,6,7")
    }
}
