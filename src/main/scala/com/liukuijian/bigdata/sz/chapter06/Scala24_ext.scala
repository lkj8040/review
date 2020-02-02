package com.liukuijian.bigdata.sz.chapter06

object Scala24_ext {
  def main(args: Array[String]): Unit = {
    val user1 = new User24
    user1.id = 1
    val user2 = new User24
    user2.id = 1
    println(user1.equals(user2))//true
    val clazz: Class[User24] = classOf[User24]
    println(clazz)
  }
}
class User24{
  var id:Int = _

  override def equals(o:Any):Boolean={
    //Scala语言中[]表示泛型
    //isInstanceOf判断是否为指定类型的实例
    if(o.isInstanceOf[User24]){
      //asInstanceOf转换为指定类型的实例
      val other: User24 = o.asInstanceOf[User24]
      this.id == other.id
    }else{
      false
    }
  }
}
