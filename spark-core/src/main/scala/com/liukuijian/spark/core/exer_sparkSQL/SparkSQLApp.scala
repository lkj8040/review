package com.liukuijian.spark.core.exer_sparkSQL

import org.apache.spark.sql.SparkSession

object SparkSQLApp {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
                .builder()
                .enableHiveSupport()
                .master("local[*]")
                .appName("app")
                .getOrCreate()
        spark.sql("use sparkpractice")
        spark.sql(
            """
              |select
              |    ci.*,
              |    pi.product_name
              |from user_visit_action uv
              |join product_info pi on uv.click_product_id = pi.product_id
              |join city_info ci on ci.city_id = uv.city_id
            """.stripMargin).createOrReplaceTempView("t1")
        spark.udf.register("remark",MyUDAF)
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
            """.stripMargin).show(100,false)

    }
}
/*
1. 获取所有想要的字段
select
    ci.*,
    uv.click_product_id,
    pi.product_name
from user_visit_action uv
join product_info pi on uv.click_product_id = pi.product_id
join city_info ci on ci.city_id = uv.city_id

2. 获取各区域所有商品的点击次数
select
    area,
    product_name,
    count(*) ct,
    remark(city_name) remark
from t1
group by area, product_name

3. 进行排序
select *, rank() over(partition by product_name order by ct desc) rk
from t2

4. 取前3
select
    area,
    product_name,
    ct,
    remark
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