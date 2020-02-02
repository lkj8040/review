package com.liukuijian.bigdata.sz.chapter06
//import java.util._



object Scala03_import {
  def main(args: Array[String]): Unit = {
//    import
//    1.导包
//    util.ArrayList
//    2.scala导入包中所有的类使用下划线来代替*
//    import java.util._
//    3.import 可以在任意的地方使用
//    4.import 可以模仿java中的静态导入,但不需要使用static
//    5.import 可以在一行中导入多个类
//    import java.util.{ArrayList,Map,List,_}
//    6.屏蔽类
//    import java.sql.{Array=>_,Date=>_}
//    val date = new Date()
//    7.scala中访问包中的类使用的包路径为相对路径，默认以当前包为准
//     如果不想使用相对路径，可以使用绝对路径，使用根路径: _root_
//    var map  = new _root_.java.util.HashMap()
//    8.可以给类取别名
//    import java.util.{HashMap => JavaHashMap}
//    new JavaHashMap()
//    type S = String
//    new S()
  }
}
package java{
  package util{
    class HashMap{

  }
  }
}
