package com.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object funcTest {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("func").setMaster("local[2]")
        
        conf.registerKryoClasses(Array(classOf[(myclass)]))
        val sc = new SparkContext(conf)
    
        val rdd: RDD[(String, Int)] = sc.makeRDD(Array(("a",1),("b",2),("c",3)))
        
        new myclass(10).myFun(rdd)
    }
    

}
class myclass(i: Int) extends Serializable {
    def myFun(rdd: RDD[(String, Int)]) ={
        var tmp = i
        rdd.foreach{
            case (str, int) => println(str + "," + int + "," + tmp)
        }
    }
}
