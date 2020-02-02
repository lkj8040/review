package com.liukuijian.spark.core.exer_day03

object OrderingTest {
  def main(args: Array[String]): Unit = {
    implicit val ord:Ordering[User] = new Ordering[User]{
      override def compare(x: User, y: User) = x.age - y.age
    }
    val zs: User = User("zs",20)
    val ls: User = User("ls",18)
    val bigger: User = max1(zs,ls)
    val larger: User = max2(zs,ls)
    println(bigger)
    println(larger)
  }

  def max[T <: Comparable[T]](x:T,y:T) ={
    if(x.compareTo(y) > 0) x else y
  }

  def max1[T](x:T, y:T)(implicit ord:Ordering[T])={
    if(ord.gteq(x,y)) x else y
  }

  def max2[T: Ordering](x:T,y:T) ={
    val ord: Ordering[T] = implicitly[Ordering[T]]
    if(ord.gteq(x,y)) x else y
  }
}
case class User(name:String, age:Int)
