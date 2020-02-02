package com.liukuijian.bigdata.sz.chapter06

object Scala09_object {
  def main(args: Array[String]): Unit = {
    val user01 = new User09("zhangsan")
    println(user01.name)
  }

}
//scala中可以在声明主构造器的参数时直接生成属性,使用var或者val
//scala可以在声明构造参数时使用修饰符（var | val）
//var表示可以修改，val表示不可修改
class User09(var name: String){ //不用再对name进行初始化了！！！！ 声明一个属性的同时对这个属性进行初始化

}

