package com.liukuijian.spark.core.exer_day03

import java.sql.{DriverManager}

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

object exer_jdbc_read {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("jdbc")
    val sc: SparkContext = new SparkContext(conf)

    val driver: String = "com.mysql.jdbc.Driver"
    val url: String = "jdbc:mysql://hadoop101:3306/test"
    val username: String = "root"
    val password: String = "L19940816"

    val JDBCrdd: JdbcRDD[(Int, String)] = new JdbcRDD[(Int, String)](
      sc,
      () => {
        Class.forName(driver)
        DriverManager.getConnection(url, username, password)
      },
      "select * from stu where age >= ? and age <= ?",
      0,
      100,
      2,
      ResultSet => (ResultSet.getInt(1), ResultSet.getString(2))
    )
    JDBCrdd.collect.foreach(println)

  }
}
