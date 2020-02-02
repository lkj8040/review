package com.liukuijian.bigdata.sz.chapter06

object Scala15_singleton {
  def main(args: Array[String]): Unit = {
    var user1:User15.type = User15
    println(user1)
    println(User15)
  }
}
//伴生对象
//单例对象
object User15{

}
class User15{

}