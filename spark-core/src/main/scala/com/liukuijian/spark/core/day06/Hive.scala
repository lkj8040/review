package com.liukuijian.spark.core.day06

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

object Hive {
    def main(args: Array[String]): Unit = {
        System.setProperty("HADOOP_USER_HOME","liukuijian")
        val spark: SparkSession = SparkSession
                .builder()
                .appName("hive")
                .enableHiveSupport()
                .master("local[*]")
                .config("spark.sql.warehouse.dir","hdfs://hadoop101:9000/user/hive/warehouse")
                .config("spark.sql.hive.convertMetastoreParquet", false)
                .getOrCreate()

        val sc: SparkContext = spark.sparkContext
        //从hive读数据
        spark.sql("use gmall")
        spark.sql("select * from stud").show()

        //向hive写数据
        import spark.implicits._
        val df: DataFrame = Seq(("zq","cq","china","19")).toDF("name","area","course","score")
        //表可以不存在, 如果表不存在，会创建一个新表
        df.write.mode("append").saveAsTable("stud")//看列名不看位置

        //将数据插入到已经存在的表，表如果不存在就会抛异常
        df.write.insertInto("stud") //看位置不看列名

        //可以通过写sql语句的方式将数据写入hive
        spark.sql("create table mytable (name string, area string, course string, score string)")
        spark.sql("insert into table mytable select * from stud")

        //创建数据库尽量在hive中创建，不要在spark中创建数据库，因为spark中创建数据库的默认地址是在本地。
        //或者 ： sparksession.config("spark.sql.warehouse.dir","hdfs://hadoop101:9000/user/hive/warehouse")

/*
1.hive-site.xml文件copy
2.mysql驱动
3.依赖：spark-hive依赖
4.enableHiveSupport()
 */
        spark.close()
    }
}
