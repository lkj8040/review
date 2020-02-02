package com.liukuijian.bigdata.sz.chapter07

object Scala18_method2 {
  def main(args: Array[String]): Unit = {
    //多元数据操作
    //交集、并集、差集
    val list1 = List(1,2,"",3,4)
    val list2 = List(4,5,6,7,8)
    val list3 = List(1,2,3,4,5,6,7,8)
    //TODO 并集  List(1, 2, 3, 4, 4, 5, 6, 7)
    println(list1.union(list2))
    //TODO 交集  List(4)
    println(list1.intersect(list2))
    //TODO 差集  List(5, 6, 7)
    println(list2.diff(list1))
    //TODO 拉链  List((1,4), (2,5), (3,6), (4,7))
    println(list1.zip(list2))

    println("-----------------------------")
    //TODO 滑动窗口
    val iterator: Iterator[List[Int]] = list3.sliding(3,1)//返回的是List[Int] 的迭代器，一个一个的List集合
    while(iterator.hasNext){
      println(iterator.next.sum)
    }
    //并集 union  差集 diff  交集intersect 拉链 zip
    //滑动窗口： sliding
  }
}

