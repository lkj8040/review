package com.liukuijian.bigdata.sz.chapter05

object TestWhile {
  def main(args: Array[String]): Unit = {
    //f: => Boolean 返回值类型为Boolean的任意代码片段
    def myWhile(f: => Boolean)( op : => Unit):Unit ={//函数柯里化
      if(f){
        op
        myWhile(f)(op)
      }
    }
    var i = 1;
    myWhile(i <= 10){
      println(i)
      i += 1
    }
  }
}
