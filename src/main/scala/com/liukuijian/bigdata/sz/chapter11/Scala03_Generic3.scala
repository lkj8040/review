package com.liukuijian.bigdata.sz.chapter11

import com.liukuijian.bigdata.sz.chapter01.Scala_var._

object Scala03_Generic3 {
  def main(args: Array[String]): Unit = {
    def test(implicit password: String)={
      println(password)
    }
//    implicit val xxx:String = "hello" //方法一

    val str: String = implicitly[String] //方法二,使用导入的隐式值

    test(str)
  }
}
