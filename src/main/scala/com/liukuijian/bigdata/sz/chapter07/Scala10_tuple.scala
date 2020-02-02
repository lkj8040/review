package com.liukuijian.bigdata.sz.chapter07

object Scala10_tuple {
  def main(args: Array[String]): Unit = {
//    //username: zhangsan  age:20   email:xxxx
//    //tuple:元组 => 无关数据元素的组合
//    val tuple: (Int, String, Char, String) = (1, "hello",'a',"xxx")
//    //元组数据需要通过顺序号来访问访问其中的数据
//    //scala元组中的元素数量最多只能放22个
//
//    //顺序号访问
////    println(tuple._1)
////    println(tuple._2)
////    println(tuple._3)
////    println(tuple._4)
//
//    //迭代器访问
//    val iterator: Iterator[Any] = tuple.productIterator
//    while(iterator.hasNext){
//      println(iterator.next())
//    }
//    println(tuple.productElement(0))

    val tuple: (Int, String, Char, Double) = (1,"string",'A',21.0)
    println(tuple._1)
    println(tuple._2)
    println(tuple._3)
    println(tuple._4)

    val iterator: Iterator[Any] = tuple.productIterator
    while(iterator.hasNext){
      print(iterator.next + " ")
    }
    println()

  }
}

class User10{
  var username :String = _
  var age: Int = _
  var email: String = _
}
