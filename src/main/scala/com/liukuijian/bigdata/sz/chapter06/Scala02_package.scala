package com.liukuijian.bigdata.sz.chapter06


class Demo{
  def main(args: Array[String]): Unit = {

  }
}
package xxxx{

  import com.liukuijian.bigdata.sz.chapter06.xxxx.yyyy.Scala02_package

  class test{
    //私有的
    private var username = "zhangsan"
    //公共的
    def test(): Unit ={
      println("test....")
    }
    //包私有权限，[*]表示可以给当前包以及子包使用
    private[chapter06] var age : Int = 20
    //受保护的访问权限，只能给当前类和子类用
    protected var email:String = ""
  }
  package yyyy{
    object Scala02_package {
      def main(args: Array[String]): Unit = {
        //面向对象
//        1.分类管理
//        2.区分相同名称的类
//        3.访问权限
//
//        scala语言对package的语法进行了扩展
//        1.包路径和源码文件所在的路径没有关系
//        2.package关键字可以多次声明,形成父包，子包的概念
//        3.package可以嵌套使用
//        4.子包可以直接使用父包中的内容,不需要导包
//        5.java.lang.*，scala.*,Predef.*无需导入
//        6.scala是完全面向对象的语言，万物皆对象，所以package也是对象
//        7.权限：scala中什么都不写就是public公共的访问权限。
//               scala中private语法和java一样,都表示只有当前类能够访问
//               scala中包访问权限可以让指定的包和子包访问，使用[*]规定包的使用范围
//               scala中protected访问权限只能给当前类和子类访问，同包无法使用
        new test().age
      }
    }
  }
}
private class Emp{

}

