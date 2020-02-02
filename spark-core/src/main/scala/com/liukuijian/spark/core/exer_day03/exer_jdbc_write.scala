package com.liukuijian.spark.core.exer_day03

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object exer_jdbc_write {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("jdbc")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(Int, String)] = sc.makeRDD(List((15,"ha"),(16,"ge"),(18,"li")),2)
    val driver: String = "com.mysql.jdbc.Driver"
    val url: String = "jdbc:mysql://hadoop101:3306/test"
    val username: String = "root"
    val password: String = "L19940816"
    rdd.foreachPartition{
      it => {
        Class.forName(driver)
        val conn: Connection = DriverManager.getConnection(url,username,password)
        val sql: String = "insert into stu values(?,?)"
        val ps: PreparedStatement = conn.prepareStatement(sql)
        it.foreach{
          case (age, name) =>{
            ps.setInt(1,age)
            ps.setString(2,name)
            ps.addBatch()
          }
        }
        ps.executeBatch()
        ps.close()
        conn.close()
      }
    }
    sc.stop()
  }
}
