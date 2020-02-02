package com.liukuijian.bigdata.sz.chapter05

object Scala03_function_nightmare {
  def main(args: Array[String]): Unit = {
    //至简原则
    def f1(): String={
      //return "Lisi"
      "Lisi"
    }
    //一般函数中不需要使用return语法，函数可以根据满足条件的最后一行代码作为返回值,会对返回值类型进行自动类型推断
    def f2()={
      var flg= true
      if(flg) 10 else "zhangsan"//10是数值类型，"zhangsan"是引用类型，公共的祖先类型是Any
    }
    //如果函数体只有一行代码，那么{}可以省略
    def f3() = "zhangsan"
    println(f3())

    //如果没有参数，那么也可以省略()
    //如果函数声明时省略(),那么调用也需要省略()，为了统一访问原则，类似于变量访问，就好像在使用一个变量
    def f4 ="weige"
    val name="zhangsan"
    println(f4)
    println(name)

    //
    //如果函数返回值使用Unit，那么函数不会使用最后一行作为返回值，使用Unit，恒返回()
    //如果想要省略Unit,但是又不想使用最后一行代码作为返回值，那么等号=可以省略， 既不想写Unit，又不想有返回值，就省略=
    def f5() {
      "zhangsan"
    }
    print(f5())

    //省略函数名：如果不关心函数的名称，只关心函数的实现，那么可以省略函数名，使用匿名函数
    //匿名函数可以赋值给变量
    val f6 = () => println("xxxx")
    f6()

    val f7 = (x:Int)=>x+"hello world"
    //只有一行代码，省略{}、如果返回值类型可以推断出来，返回值类型不需要写,不关心函数名
    println(f7(10))
  }
}
