package com.liukuijian.bigdata.sz.chapter06

object Scala23_trait {
  def main(args: Array[String]): Unit = {
    val mysql = new MySQL
    //特质的功能叠加
    //多个特质的初始化顺序为从左到右，那么多个特质的方法的执行顺序为从右到左
    //特质中使用的super关键字，表示的不是上一级trait，而是上一个trait
    mysql.operData()
  }
}
trait Operate{
  println("operate...")
  def operData():Unit={
    println("操作数据...")
  }
}
trait Memory extends Operate {
  println("memory...")
  override def operData():Unit={
    print("向内存中")
    super.operData()
    println("执行的memory")
  }
}
trait File extends Operate{
  println("file...")
  override def operData():Unit={
    print("向文件中")
    super.operData()
    println("执行的file")
  }
}
//初始化顺序：operate => File => Memory
//执行顺序：Memory => File => operate
class MySQL extends File with Memory {

}