import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object HiveTest {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    
        val spark: SparkSession = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
        
        spark.sql("show databases").show()
    }
}
