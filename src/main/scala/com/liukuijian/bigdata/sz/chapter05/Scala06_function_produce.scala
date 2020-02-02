package com.liukuijian.bigdata.sz.chapter05

object Scala06_function_produce {
  def main(args: Array[String]): Unit = {
    //递归在函数式编程语言中是最常见的算法
    //1 函数出口
    //2 函数调用自身
    //3 递归调用时的参数应该有规律
    //4 递归函数不能省略返回值类型 原因:递归函数无法推断出返回值类型

    //例 阶乘5!
    def test(num: Int): Int = {
      if(num == 1) 1 else num * test( num - 1)
    }
    println(test(5))
  }
}
