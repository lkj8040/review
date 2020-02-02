package com.liukuijian.bigdata.sz.chapter08

object Scala05_match {
  def main(args: Array[String]): Unit = {
    //TODO 模式匹配 -- 样式类
    //样例类的作用其实就是为了模式匹配
    //所以和普通类的声明方式不一样
    Student("zhangsan").name
  }
}

//样例类使用case关键字声明
//样例类的构造参数列表不能省略
//当一个类被声明为样例类时，编译器会自动生成伴生类和伴生对象，而且同时生成了大量的方法
//样例类和普通类基本一致
//按照给定的参数自动生成apply方法
//样例类如果声明构造参数，等同于声明类的属性为val类型
//工作中声明的类一般都是样例类
case class Student(name:String){

}