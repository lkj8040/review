package com.liukuijian.bigdata.sz.chapter06

object Scala21_trait {
  def main(args: Array[String]): Unit = {
    //如果一个类同时继承了父类，并混入了特质
    //那么会优先初始化父类的相关内容
    //然后初始化混入的特质内容
    //最后再初始化类的主体内容
    var user = new User21
  }
}
trait Trait211{
  println("trait211...")
}
trait Trait21 extends Trait211 {
  //特质主体内容的初始化
  println("trait21...")
}
class Parent21 extends Trait211 {
  println("parent...")
}
class User21 extends Parent21 with Trait21 {
  println("user...")
}
//trait211...
//parent...
//trait21...
//user...
