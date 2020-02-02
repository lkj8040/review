package com.liukuijian.bigdata.sz.chapter05

object Scala05_function_exam {
  def main(args: Array[String]): Unit = {
    //3.函数作为返回值

    //将函数作为返回值和将函数的结果作为返回值是有区别的
    def f(string: String)={
      println("f...")
    }
    //将结果返回
//    def f1()={
//      f("ss")
//    }
//
//    def f11()={
//      f _
//    }

    //将函数作为返回值使用
//    f11()("zhangsan")

    def test()={
      def f1()={
        println("...")
      }
      f1 _
    }
    test()()

    def test1(i: Int, j:Int, f:(Int, Int)=> Int):Int={
      f(i,j)
    }
    println(test1(10,20,_+_))

//    将复杂的逻辑简单化，将多个参数分解成一个一个的参数
//    这种代码书写方式称之为函数柯里化
    def test2(i:Int) ={
      def f1(j: Int) ={
        def f2(f:(Int, Int)=> Int)={
          f(i,j)
        }
        f2 _
      }
      f1 _
    }
    println(test2(10)(20)(_+_))

    //函数编程：闭包
    //函数将使用的外部的变量包含到函数的内容，形成闭合的效果，称之为闭包   内部函数访问外部函数的变量，改变了外部变量的生命周期
    //可以改变外部变量的生命周期
    //函数柯里化：把一个参数列表的多个参数按照调用的先后次序进行分解，使复杂的逻辑简单化
//    def f(i: Int) ={
//      def f1():Int ={
//        i + 10
//      }
//      f1 _
//    }
//    println(f(10)())

  }
}
