import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object AreaGoodsTop3 {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
        
        val spark: SparkSession = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
        spark.udf.register("udaf_distribution", udaf_distribution)
        spark.sql("use sparkpractice")
        
        spark.sql(
            """
              |select
              |    area, product_name, click_count, dist
              |from(
              |    select
              |        area, product_name, click_count, dist, rank() over(partition by area order by click_count desc ) rk
              |    from(
              |        select
              |            ci.area, pi.product_name, count(*) as click_count, udaf_distribution(ci.city_name) as dist
              |        from user_visit_action uv
              |        join city_info ci
              |        on uv.city_id = ci.city_id
              |        join product_info pi
              |        on uv.click_product_id = pi.product_id
              |        group by ci.area, pi.product_name
              |    )t1
              |)t2
              |where rk <= 3
            """.stripMargin).show(100,false)
        
        spark.stop()
        
    }
}
/*
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
load data local inpath '/opt/module/datas/user_visit_action.txt' into table sparkpractice.user_visit_action;

CREATE TABLE `product_info`(
  `product_id` bigint,
  `product_name` string,
  `extend_info` string)
row format delimited fields terminated by '\t';
load data local inpath '/opt/module/datas/product_info.txt' into table sparkpractice.product_info;

CREATE TABLE `city_info`(
  `city_id` bigint,
  `city_name` string,
  `area` string)
row format delimited fields terminated by '\t';
load data local inpath '/opt/module/datas/city_info.txt' into table sparkpractice.city_info;


各区域热门商品 Top3

各区域每个商品的点击次数 t1
select
    area, product_name, click_count, dist
from(
    select
        area, product_name, click_count, dist, rank() over(partition by area order by click_count desc ) rk
    from(
        select
            ci.area, pi.product_name, count(*) as click_count, udaf_ditribution(uv.city_name) as dist
        from user_visit_action uv
        left join city_info ci
        on uv.city_id = ci.city_id
        left join product_info pi
        on uv.click_product_id = pi.product_id
        group by ci.area, pi.product_name
    )t1
)t2
where rk <= 3






 */