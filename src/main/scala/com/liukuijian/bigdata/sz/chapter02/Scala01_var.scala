package com.liukuijian.bigdata.sz.chapter02
//ctrl + alt + L来进行格式化
object Scala01_var {
  def main(args: Array[String]): Unit = {
    //var 变量名称：变量名=变量值
    var i: Int = 10
    var s: String = "abc"
    var b: Boolean = true
    //Scala中声明变量也可以采用val声明，不想让变量的值发生变化,相当于final
    val d: Int = 20

  }
}
