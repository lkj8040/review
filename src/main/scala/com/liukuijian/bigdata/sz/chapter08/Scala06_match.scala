package com.liukuijian.bigdata.sz.chapter08

object Scala06_match {
  def main(args: Array[String]): Unit = {
    //TODO - 模式匹配 -- 匹配对象

    //匹配对象时，会首先将对象传递到类的伴生对象的unapply方法
    //unapply方法会返回option类型的数据，然后进行数据比对，如果全部比对成功，那么执行对应的逻辑
    //    //如果比对不成功，那么执行下一个case
    //这种方式非常繁琐，所以scala不会这么做，scala采用样例类来实现类似的功能
    val user = User("zhangsan",20)
    val result: String = user match {
      case User("zhangsan", 20) => "yes"
      case _ => "no"
    }
    println(result)
  }
}
case class User(name:String, age:Int)