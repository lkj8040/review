package com.liukuijian.udf;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONException;
import org.json.JSONObject;

//udf函数是如何使用的： f(字段名,'key') => 新的字段值
public class CommonFieldUDF extends UDF {
    
    //evalute函数是非常灵活的，可以重载！！！！！！！！
    //一进一出函数
    //line就是一行数据，15xxx|{"ap":"","cm":{...},"et":[{...},{...}]}
    
    //自定义udf函数只需要实现evaluate方法！！！！
    
    public String evaluate(String line, String key) throws JSONException{
        //1. 处理line 服务器时间 | json
        //输入的key可以是st、cm、et
        String[] log = line.split("\\|");
        //log的长度不为2，或者log[1]为空
        if(log.length != 2 || StringUtils.isBlank(log[1])){
            return "";
        }
        //拿到{"ap":"","cm":{...},"et":[{...},{...}]}作为json对象
        JSONObject jsonObject = new JSONObject(log[1].trim());
        String result = "";
        if("et".equals(key)){
            if(jsonObject.has("et")){
                result = jsonObject.getString("et");//这里的处理方式是将所有的et事件都返回[{...},{...},{...}]
            }
        }else if("st".equals(key)){
            result = log[0].trim();//返回15xxx
        }else{
            JSONObject cm = jsonObject.getJSONObject("cm");
            if(cm.has(key)){
                result = cm.getString(key);//返回cm下的某个公共字段
            }
        }
        return result;
        
    }
}
