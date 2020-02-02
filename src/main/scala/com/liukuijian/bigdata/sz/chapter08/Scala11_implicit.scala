package com.liukuijian.bigdata.sz.chapter08

import com.liukuijian.bigdata.sz.chapter08.Scala11_implicit.Teacher

object Scala11_implicit extends PersonTrait{
  //隐式值
  implicit val hello:String = "hello"
  //隐式类不能放在top-level objects
  implicit class MyRichInt(val self: Int){
    def myMax(i:Int): Int ={
      if(self < i ) i else self
    }
    def myMin(i:Int):Int={
      if(self < i) self else i
    }
  }
  //隐式函数
  implicit def myf(arg: Int) ={
    println(arg)
    class say(var num: Int){
      num = arg
      def hello(): Unit ={
        println(num)
      }
    }
    new say(arg)
  }
  //隐式参数
  def sayhello(implicit name:String ): Unit ={
    println(name)
  }
  def main(args: Array[String]): Unit = {
    1.hello()
    1.myMax(10)
    sayhello

    val teacher = new Teacher()
    teacher.eat()
    teacher.say()
  }
  class Teacher {
    def eat(): Unit = {
      println("eat...")
    }
  }
}
trait PersonTrait {
}
object PersonTrait {
  // 隐式类 : 类型1 => 类型2
  implicit class Person5(user:Teacher) {
    def say(): Unit = {
      println("say...")
    }
  }
}

