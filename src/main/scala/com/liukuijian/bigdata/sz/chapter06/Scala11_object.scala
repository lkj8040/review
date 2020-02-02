package com.liukuijian.bigdata.sz.chapter06

object Scala11_object {
  def main(args: Array[String]): Unit = {
    //TODO 面向对象 - 继承
    var user11 = new User11("zhangsan")
    print(user11)
  }
}
//父类中一般的属性和方法都使用protected关键字声明访问权限
//构造方法
class Parent11(name: String){ //这种只表示传递了个参数过来，属性是没有的

}
//Scala中继承也采用extends关键字，一样只支持单继承
//如果父类存在构造参数，那么在继承时可以直接传递构造参数
class User11(name:String) extends Parent11(name){ //等价于super(name)

}