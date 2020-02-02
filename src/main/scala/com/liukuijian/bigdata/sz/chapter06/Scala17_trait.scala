package com.liukuijian.bigdata.sz.chapter06

object Scala17_trait {
  def main(args: Array[String]): Unit = {
    //特征(特质)
    //如果一个类拥有某一个特征，那么需要将这个特征混入到类中
  }
}
//混入特质:extends
//混入特质时，需要使用extends关键字，如果混入多个特质，需要使用with
class User17 extends MyTrait with MyTrait1 {

}
class Emp17 extends MyTrait {

}
trait MyTrait1{

}