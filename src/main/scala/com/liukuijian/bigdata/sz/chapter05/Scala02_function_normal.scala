package com.liukuijian.bigdata.sz.chapter05

object Scala02_function_normal {
  def main(args: Array[String]): Unit = {
    //1.函数无参数无返回值
    //    def f1(): Unit ={
    //      println("f1...")
    //    }
    //    f1()
    //    //2.函数有参数无返回值
    //    def f2(name: String): Unit ={
    //      println(s"name=$name")
    //    }
    //    f2("zhangsan")
    //    //3.函数有参数有返回值
    //    def f3(i : Int): Int = {
    //      return i+ 20
    //    }
    //    println(f3(20))
    //    //4.函数无参数有返回值
    //    def f4(): String={
    //      return "zhangsan"
    //    }
    //    println(f4())
    //5.函数有多个参数，有返回值
    //    def f5(i : Int , j:Int) :Int={
    //      return i+j
    //    }
    //    println(f5(10,20))

    //6.函数返回值类型不确定
    //Scala的致简原则，简化函数声明，越简单越好
    //    def f6(b : Boolean):String={
    //      if(b){
    //        return null
    //      }else{
    //        return "zhangsan"
    //      }
    //    }
    //7.可变参数,可变参数放参数列表最后
    //    def f7(i:Int *): Unit ={
    //      println(i)
    //    }
    //    f7(1,2,3,4,5)

    //8.参数的默认值
    //函数的所有参数都为val类型
    //    def f8(name:String): Unit ={
    //      var username = name
    //      if(username == null){
    //        username = "zhangsan"
    //      }
    //      println("name=" + username)
    //    }
    //    f8(null)

    //    def f88(name: String="zhangsan"): Unit ={
    //      print("name=" + name)
    //    }
    //    f88()

    //9.函数的参数的匹配规则是从左向右
    //传参时可以声明参数的名字,此时的匹配就会按指定的名字来匹配
    def f9(name: String = "zhangsan", age: Int): Unit = {
      println("name=" + name + "\tage=" + age)
    }
    f9(age = 30)

    //打印九層塔
    for (i <- new Range(1, 18, 2); j = (18 - i) / 2) {
      println(" " * j + "*" * i)
    }
  }
}
