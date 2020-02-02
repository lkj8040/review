package com.liukuijian.bigdata.sz.chapter02

object Scala02_print {
  def main(args:Array[String]):Unit={
//    var name:String = "lkj"
//    var age:Int = 18
//    println(name + " " + age)
//    printf("name=%s age=%d\n",name, age)
//    val s =
//      """
//        |select
//        |   name,
//        |   age,
//        |from user
//        |where name="lkj"
//      """.stripMargin
//    println(s)
//    val s1=
//      //加个s是表示要使用$name的方式引用变量吗
//      s"""
//         |select
//         |   name,
//         |   age,
//         |from user
//         |where name="$name" and age=${age+2}
//        """.stripMargin
//    println(s1)
//
//    val s2 =s"name=$name"
//    println(s2)
//    var some = ""
//    def func = {
//      some + age
//    }

    val res = for(i <- 1 to 10) yield i
    println(res)

  }
}
