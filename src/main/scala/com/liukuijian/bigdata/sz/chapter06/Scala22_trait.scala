package com.liukuijian.bigdata.sz.chapter06

object Scala22_trait {
  def main(args: Array[String]): Unit = {
    var user = new User22 //和函数不同，class省不省略()都可以

  }
}

trait Trait22 {
  println("trait22...")
}
trait Trait222 extends Trait22 {
  println("trait222...")
}
trait Trait2222 extends Trait222 {
  println("trait2222...")
}
class User22 extends Trait22 with Trait2222 with Trait222{
  println("user...")
}
//trait22...
//trait222...
//trait2222...
//user...