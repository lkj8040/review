package com.test

object OrderingTest {
    def main(args: Array[String]): Unit = {
        val zhangsan = User(10,"zhangsan")
        val lisi = User(20,"lisi")
        implicit val ord:Ordering[User] = new Ordering[User]{
            override def compare(x: User, y: User): Int = x.age - y.age
        }
        println(max1[User](zhangsan,lisi))
        println(max2[User](zhangsan,lisi)(ord))
        println(max3[User](zhangsan,lisi))
    }
    
    def max1[T <: Comparable[T]](x:T,y:T) ={
        if(x.compareTo(y) > 0) x else y
    }
    
    def max2[T](x:T,y:T)(implicit ord: Ordering[T])  = {
        if(ord.gteq(x,y)) x else y
    }
    
    def max3[T:Ordering](x:T, y:T)  ={
        val ord: Ordering[T] = implicitly[Ordering[T]]
        if(ord.gteq(x,y)) x else y
    }
}
case class User(age:Int, name:String) extends Comparable[User]{
    override def compareTo(o: User): Int = this.age - o.age
}