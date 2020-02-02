package com.liukuijian.bigdata.sz.chapter05

object test {
  def main(args: Array[String]): Unit = {

    def f = ()=>{
      println("f...")
      10
    }
    println(f)
    foo(f())
  }

  def foo(a: =>Int):Unit = {
    println(a)
    println(a)
  }

}
