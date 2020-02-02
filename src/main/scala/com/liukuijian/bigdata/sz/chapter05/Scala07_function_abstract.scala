package com.liukuijian.bigdata.sz.chapter05

import scala.util.control.Breaks._

object Scala07_function_abstract {
  def main(args: Array[String]): Unit = {
    //将代码作为参数传递给函数
    def f= ()=>{
      println("f...")
      10
    }

//    breakable() => breakable{}
//    breakable(op: => Unit)  => Unit 无参无返回值，执行op代码片
    breakable{
      for(i <- 1 to 5) {
        if (i == 3) {
          break
        } else {
          println(i)
        }
      }
    }

    def test(op : => Unit) ={
      op
    }
    test{
      println("zhangsan")
    }
    //函数柯里化
    def curry(i:Int)(j:Int)(f:(Int,Int) => Int) :Int={
      f(i,j)
    }
    val result1 = curry(10)(20)(_+_)

    def curry1(i : Int)={
      def f1(j:Int)={
        def f2(f:(Int, Int)=>Int):Int={
          f(i,j)
        }
        f2 _
      }
      f1 _
    }
    val f1 = curry1(10)
    val f2 = f1(20)
    val result2 = f2((x,y)=>x+y)
    val result3 = f2(_+_)

    //控制抽象
    //传入一个函数，这个函数的返回值类型是boolean
    //传入任意的代码片段，只要返回值类型是boolean类型就行
    def whilex(f: => Boolean)(op : =>Unit):Unit={
      if(f) {
        op
        whilex(f)(op)
      }
    }
    whilex(1 > 0){
      println("xxxx")
    }

  }
}
