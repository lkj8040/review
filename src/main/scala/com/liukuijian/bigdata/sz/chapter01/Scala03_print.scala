package com.liukuijian.bigdata.sz.chapter01

object Scala03_print {
    def main(args:Array[String]):Unit ={

        //Predef伴生对象在使用时不需要导入，也可以使用，因为是预先声明的对象
        println("Hello Scala!")
        Predef.println("Hello Scala!")

    }
}
