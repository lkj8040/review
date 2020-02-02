package com.liukuijian.bigdata.sz.chapter06

import scala.beans.BeanProperty

object Scala04_class {
  def main(args: Array[String]): Unit = {
    val user = new User0()
    println(user.age)
    user.name = "lisi"//等价于调用setter方法 user.name$eq("lisi")
    println(user.name)//等价于调用getter方法=> user.name()

    //标准的javaBean规范要求以set和get开头
    //scala为了解决javaBean通用的问题，提供了注解@BeanProperty
    user.setId(100)
    var id = user.getId

  }
}
//scala中声明类不需要使用public，默认就是public，而且可以多次使用
//scala中的类也可以继承父类：extends
class User0{
  //声明属性
  //编译时会将属性声明为私有属性，并同时提供默认为public修饰的setter和getter方法
  //private String name;
  var name: String = "zhangsan"//必须要显式地初始化
  //编译时如果声明为private，则get和set方法是private方法
  private var email:String =_

  var age: Int = _ //如果想要属性可以默认初始化，那么可以采用下划线赋值,基本数据类型为0，引用数据类型为null
  //如果使用val声明属性，必须显式初始化
  //当使用val声明时，属性会被声明为final，而且不会生成setter方法，只提供getter方法
  //private final String favorite;
  @BeanProperty
  val favorite : String = "food"

  //如果属性使用注解，那么编译时会自动生成对应的get和set方法
  @BeanProperty
  var id : Int = _


}
class User1 extends User0 {

}
class User2{

}
class User3{

}