package app

import bean.UserVisitAction
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ProjectApp {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("myProject")
        val sc: SparkContext = new SparkContext(conf)

        val userVisitAction: RDD[UserVisitAction] = sc.textFile("d:/user_visit_action.txt").map(line =>{
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
                splits(12).toLong)
        })

        ImpFuncOfStatistic.statisticTop10ClickOrderPay(sc, userVisitAction)

        ImpFuncOfStatistic.statisticTop10_Click(sc, userVisitAction)

        ImpFuncOfStatistic.statisticTransferRate(sc, userVisitAction,"1,2,3,4,5,6,7")

    }
}
