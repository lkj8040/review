package com.liukuijian.bigdata.sz.chapter07

object Scala19_method {
  def main(args: Array[String]): Unit = {
    //TODO 集合 - 常用方法 聚合 折叠 扫描
//    val list1 = List(1,2,3,4)
//
//    //按照指定的规则进行计算
//    //TODO 简化规约 聚合
//    //采用自定义的计算规则对集合的数据进行聚合操作
//    println(list1.reduce(_+_)) //注意返回的类型的一致性
//    // List(1,2,3,4)
//    // reduce 逻辑：f(f(f(1, 2), 3), 4)  =>  a a a
//
//    //reduceLeft:   f(f(f(1,2),3),4) => a b a
//    list1.reduceLeft(_-_)
//    //reduceRight:  f(1,f(2,f(3,4)))=> a b b
//    list1.reduceRight(_-_)
//
//    // TODO 折叠： 将集合中的数据和集合外的数据进行聚合，结果类型为集合外的数据类型
//    println(list1.fold("")(_ + _.toString)) //利用函数柯里化
//
//    //foldLeft: f(f(f(f(f(z,1),2),3),4)
//    println(list1.foldLeft("")(_+_))
//
//    println(list1.map(_.toString).reduce(_ + _)) //利用map - reduce
//
//    //foldRight: f(1,f(2,f(3,f(4,z))))
//    println(list1.foldRight("")(_+_))//交换，传入op(x,y) 计算op(y,x)
//
//    //TODO 扫描
//    //集合外的数据称之为初始值，作为fold方法的第一个参数列表的参数出现
//    println(list1.scan("")(_ + _.toString))
//    println(list1.scanLeft("")(_ + _))
//    println(list1.scanRight("")(_+_))

    val list1 = List(1,2,3,4)

    println(list1.reduce(_ + _))//求和
    println(list1.reduceLeft(_ + _))// f:(B,Int)=>B  并且B类型和Int类型存在子父类关系
    list1.reduceRight(_ + _) //op:(Int,B)=>B  并且B>Int

    println("------------------------------")
    println(list1.fold("")(_ + _.toString)) //AAA集合外面的数据和集合里面的数据进行聚合，结果类型为集合外面的数据类型
    println(list1.foldLeft("")(_ + _))//BAB
    println(list1.foldRight("")(_ + _))//ABB

    //scan的作用和fold完全一样，只不过将每个步骤扫描
    println(list1.scan("")(_ + _.toString))
    println(list1.scanLeft("")(_ + _))
    println(list1.scanRight("")(_ + _))
  }
}
