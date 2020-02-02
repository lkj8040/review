package com.liukuijian.spark.core.day03

object OrderingTest {
  def main(args: Array[String]): Unit = {
    val str1: String = max1("a","b")
    val str2: String = max2("a","b")
    val str3: String = max3("a","b")

//    val int1: Int = max1(1,2)  //error Int类型没有compare方法
    val int2: Int = max2(1,2)
    val int3: Int = max3(1,2)

    implicit val ord1: Ordering[User] = new Ordering[User]() {
      override def compare(x: User, y: User) = x.age - y.age
    }

    val user1: User = max2(User("a",10),User("b",20))
    val user2: User = max3(User("a",10),User("b",20))
    println(user1)
    println(user2)
  }

  //1
  def max1[T <: Comparable[T]](a:T,b:T)={
    if(a.compareTo(b) > 0) a else b
  }
  //2
  def max2[T](a:T,b:T)(implicit ord:Ordering[T]) ={
    if(ord.gteq(a,b)) a else b //使用函数柯里化获取比较器ord
  }
  //3
  def max3[T:Ordering](a:T, b:T) ={
    val ord: Ordering[T] = implicitly[Ordering[T]] //从冥界召唤隐式值ord
    if(ord.gteq(a,b)) a else b
  }
}
case class User(name:String, age:Int)
