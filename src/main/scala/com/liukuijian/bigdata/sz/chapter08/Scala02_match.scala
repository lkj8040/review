package com.liukuijian.bigdata.sz.chapter08

object Scala02_match {
  def main(args: Array[String]): Unit = {
    //TODO 模式匹配 -- 模式守卫

    //如果想要表达匹配某个范围的数据，可以在模式匹配中增加条件限制
    def abs(x:Int) ={
      x match {
        case i:Int if i >= 0 => -i
        case j:Int if j < 0 => -j
        case _  => "type illegal"
      }
    }
    println(abs(1))
  }
}
