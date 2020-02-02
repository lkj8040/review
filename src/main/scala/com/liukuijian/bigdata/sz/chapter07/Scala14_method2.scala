package com.liukuijian.bigdata.sz.chapter07

object Scala14_method2 {
  def main(args: Array[String]): Unit = {
    val list1 = List(1,4,3,2)
    val list2 = List(4,5,6,7)
    val list3 = List(list1,list2)

    //TODO 映射/转换，将指定的数据转换为其他的数据
    //集合的map方法会将集合中的每一个元素都执行map方法中传递的逻辑
    //map方法会将每一个数据转换后的结果放置到新的集合中返回
    println(list1.map(_*2))


    //TODO 扁平化 :::
    //将一个整体数据拆分成一个一个的个体来使用，称为扁平化
    println(list3.flatten.map(_*2))



    //TODO 扁平映射
    //flatMap等同于将map和flatten融合在一起实现功能
    //输入参数为集合中的每一个数据
    //输出的结果为扁平化后的数据集合
    println(list3.flatMap(_.map(_*2)))
    println(list3.flatMap(x => x.map(_*2)))



    //TODO 过滤
    //将集合中所有的数据进行逻辑判断，返回结果为true保留，如果为false就舍弃
    println(list1.filter(_ % 2==0))
    var list4 = List("Spark","Scala","Hadoop")
    println(list4.filter(_.startsWith("S")))

    //TODO 分组
    //通过指定的规则进行分组
    //指定的规则的返回值作为分组的key，数据就是分组后的Value
    println(list1.groupBy(_ % 2))
    println(list4.groupBy(_.charAt(0)))

    //TODO 排序
    //通过指定的规则进行排序，默认升序
    println(list1.sortBy(_*1))
    println(list1.sortBy(_*1)(Ordering.Int.reverse))
    println(list1.sortBy(_ * 1).reverse)
    val list5 = List("1","2","11","3","22")
    println(list5.sortBy(_.toInt))

    //TODO 复杂排序
    var student1 = new Student
    student1.name = "zhangsan"
    student1.id = 1
    var student2 = new Student
    student2.name = "lisi"
    student2.id = 2
    val list6 = List(student1,student2)
    //元组可以进行比较，先比较第一个，再比较第2个，依次类推
    println(list6.sortBy(stu => (stu.id, stu.name)))

    println(list6.sortWith((o1, o2) => {
      if (o1.id > o2.id) true
      else if (o1.id < o2.id) false
      else o1.name > o2.name
    }))
  }
}

class Student{
  var id: Int = _
  var name:String = _
  override def toString: String = {
    "user[" + id +"," + name + "]"
  }
}