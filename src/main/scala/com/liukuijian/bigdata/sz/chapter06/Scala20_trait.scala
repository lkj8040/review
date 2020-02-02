package com.liukuijian.bigdata.sz.chapter06

object Scala20_trait {
  def main(args: Array[String]): Unit = {
    //Scala支持对象的动态混入特质
    //遵循了OCP原则
    //创建对象时可以动态添加功能,想扩展就扩展
    var user = new User20 with Trait20
    user.insert()
    user.update()
  }
}
class User20{
  def insert():Unit ={
    println("insert user data...")
  }
}

trait Trait20{
  def update():Unit={
    println("update user data...")
  }
}