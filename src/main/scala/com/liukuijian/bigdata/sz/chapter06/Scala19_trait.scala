package com.liukuijian.bigdata.sz.chapter06
import _root_.java.io._

object Scala19_trait {
  def main(args: Array[String]): Unit = {

    var user = new User19()
    user.insert

    //OCP原则，代码可以扩展，但是不允许修改代码
    //user.update
    //装饰者设计模式:IO
    //new BufferedInputStream(new FileInputStream(""))
    var userExt = new User199(user)
    userExt.update()
    userExt.insert()

  }
}
class User19{
  def insert():Unit={
    println("insert user data...")
  }
//  def update():Unit={
//    println("update user data...")
//  }
}
class User199(user:User19){
    def update():Unit={
      println("update user data...")
    }
    def insert():Unit={
      user.insert()
    }
}