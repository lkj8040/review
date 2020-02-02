package com.liukuijian.bigdata.sz.chapter07

object Scala01_array {
  def main(args: Array[String]): Unit = {
    //集合 - Array - 数组(不可变)
    //创建数组 -1
    //val array: Array[String] = new Array[String](3)
    //创建数组 -2
    val array: Array[String] = Array("","","")


    //元素操作
    //访问数组元素，需要采用小括号来定位
    //修改数据
    array(0) = "scala"
    array(1) = "java"
    array(2) = "python"
    array.foreach(println)

    //增加数据
    //数组可以添加数据，但是添加后会产生新的数组，所以数组是不可变的
//    var array1:Array[String] = array :+ "d"
    //scala中的运算符如果使用冒号结尾，那么调用的顺序为从右到左
//    var array2:Array[String] = "e" +: array //  array.+:("e")
//    println(array1 == array)
    //集合打印
//    println(array)
//    for(s <- array){
//      println(s)
//    }
//    array.foreach(s=>println(s))
//    array.foreach(println(_))
//    array.foreach(println)
    //使用集合中的每个元素拼接成字符串，以","隔开
//    println(array.mkString(","))

//    val array = new Array[String](3)
//    array(0) = "zhangsan"
//    array(1) = "lisi"
//    array(2) = "wangwu"
//
//    val array2: Array[String] = Array("","","")
//    array2(0) = "java"
//    array2(1) = "scala"
//    array2(2) = "spark"
//    //array2(3) = "flink" //array index out of bounds
//    println(array2.mkString(","))
//
//    for(s <- array2){
//      println(s)
//    }
//
//    val buffer2: Array[Any] = "teddy" +: array2 //不可变性，所以返回一个新的数组
//
//    println(buffer2 == array2)
//
//    println(buffer2.mkString(" "))
//
//    val strings: Array[String] = array2 :+ "hello" //在尾部添加元素
//
//    println(strings == array2)
//
//    println(strings.mkString(","))
//
//    strings.foreach(println)
//    println("-----------------------------")
//    strings.foreach((x:String) => {println(x)})
//    strings.foreach(x => println(x))
//    println("-----------------------------")
//    strings.foreach(println(_))

  }
}
