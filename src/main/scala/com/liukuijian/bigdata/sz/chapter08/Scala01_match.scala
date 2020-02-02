package com.liukuijian.bigdata.sz.chapter08

object Scala01_match {
  def main(args: Array[String]): Unit = {
    //TODO 模式匹配  --更强大
    //scala中模式匹配是为了代替java中的switch语法
    //而且scala提供了更多更强大的功能

    var a:Int = 10
    var b:Int = 20
    var operator:Char = ')'


    //①跳出判断逻辑不需要使用break
    //②scala中的模式匹配如果没有任何一个条件满足，那么会发生错误MatchError
    //③scala中的模式匹配规则是按照case的顺序依次执行，只要满足就会执行相应的逻辑
    //④匹配结果可以返回作为变量值
    var result = operator match{//result接收返回的结果
      case '+' => {a + b} //完整的写法
      case '-' => a - b
      case '*' => a * b
      case '/' => a / b
      case  _  => "illegal" //case _ 表示任意值，必须放最后
    }
    println(result)
  }
}
