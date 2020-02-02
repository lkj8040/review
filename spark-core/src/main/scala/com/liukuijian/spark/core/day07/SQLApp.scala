package com.liukuijian.spark.core.day07

import org.apache.spark.sql.SparkSession

object SQLApp {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
                .builder()
                .master("local[*]")
                .enableHiveSupport()
                .appName("practice")
                .getOrCreate()
        import spark.implicits._
        spark.sql("use sparkpractice")
        spark.sql(
            """
              |select
              |    ci.*,
              |    pi.product_name,
              |    uv.click_product_id
              |from user_visit_action uv
              |join product_info pi on pi.product_id = uv.click_product_id
              |join city_info ci on uv.city_id = ci.city_id
            """.stripMargin).createOrReplaceTempView("t1")
        spark.udf.register("remark", RemarkUDAF)
        spark.sql(
            """
              |select
              |    area,
              |    product_name,
              |    count(*) ct,
              |    remark(city_name) remark
              |from t1
              |group by area, product_name
            """.stripMargin).createOrReplaceTempView("t2")
        spark.sql(
            """
              |select *, rank() over(partition by area order by ct desc) rk
              |from t2
            """.stripMargin).createOrReplaceTempView("t3")
        spark.sql(
            """
              |select
              |    area,
              |    product_name,
              |    ct,
              |    remark
              |from t3
              |where rk <= 3
            """.stripMargin).show(false)
//                .coalesce(1) //如果前面有聚合的话，那么df会默认分为200个分区，为了减少分区数，使用coalesce
//                .write
//                .mode("overwrite")
//                .saveAsTable("area_city_count")
        //行动算子：show  save insertInto saveAsTable
        spark.close()
    }
}
/*
1. 先把需要的字段查出来 t1
select
    ci.*,
    pi.product_name,
    uv.click_product_id
from user_visit_action uv
join product_info pi on pi.product_id = uv.click_product_id
join city_info ci on uv.city_id = ci.city_id

2.按照地区和商品进行分组聚合 t2
select
    area,
    product_name,
    count(*) ct
from t1
group by area, product_name

3.每个地区取前3的商品 开窗 rank()函数 row_number dense_rank  t3
select *, rank() over(partition by area order by ct desc) rk
from t2

4.取点击次数前3
select
    area,
    product_name,
    ct
from t3
where rk <= 3



CREATE TABLE `user_visit_action`(
  `date` string,
  `user_id` bigint,
  `session_id` string,
  `page_id` bigint,
  `action_time` string,
  `search_keyword` string,
  `click_category_id` bigint,
  `click_product_id` bigint,
  `order_category_ids` string,
  `order_product_ids` string,
  `pay_category_ids` string,
  `pay_product_ids` string,
  `city_id` bigint)
row format delimited fields terminated by '\t';
load data local inpath '/opt/module/data/user_visit_action.txt' into table sparkpractice.user_visit_action;

CREATE TABLE `product_info`(
  `product_id` bigint,
  `product_name` string,
  `extend_info` string)
row format delimited fields terminated by '\t';
load data local inpath '/opt/module/data/product_info.txt' into table sparkpractice.product_info;

CREATE TABLE `city_info`(
  `city_id` bigint,
  `city_name` string,
  `area` string)
row format delimited fields terminated by '\t';
load data local inpath '/opt/module/data/city_info.txt' into table sparkpractice.city_info;
 */
