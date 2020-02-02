package com.liukuijian.bigdata.sz.chapter08

object Scala04_match {
  def main(args: Array[String]): Unit = {
    //TODO - 模式匹配
    //模式匹配也可以使用至简原则
    val (id, name, age) = (1,"zhangsan",30)
    println(id)
    println(name)
    println(age)

    val map: Map[String, Int] = Map(("a",1),("b",2),("c",3))


    //map方法可以使用模式匹配，
    //一般情况下只有一个参数的场合可以使用模式匹配
    //使用模式匹配时需要将方法的小括号变成大括号
    //case不能省略
    println(map.mapValues(_ * 2))
    println(map.map(kv => kv._1 -> kv._2 * 2)) //kv 是一个tuple
    println(map.map{case (word,count)=>(word,count*2)}) //先使用模式匹配，再利用别名，只需要加个case

    println(map.filter(_._2 >= 2))
    println(map.filter{case(word, count) => count >= 2})


  }
}
