package com.liukuijian.udtf;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class EventJsonUDTF extends GenericUDTF {
    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        // 一列一行转为两列N行
        fieldNames.add("event_name"); //字段名 字段类型
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("event_json"); //字段名 字段类型
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }
    
    @Override
    public void process(Object[] objects) throws HiveException {
        //输入一条数据，这条数据是使用CommonFiedUDF(line, "et")得到的一个[{...},{...},{...}]字符串，里面包含多个事件
        String input = objects[0].toString();
        //如果传进来的字符串为空，直接过滤掉该条数据
        if(StringUtils.isBlank(input)){
            return;
        }else{
            try {
                JSONArray jsonObject = new JSONArray(input);
                if(jsonObject == null){
                    return;
                }
                //循环遍历每个事件
                for(int i = 0; i < jsonObject.length(); i++){
                    String[] result = new String[2];
                    try{
                        //取出每个事件的名称
                        result[0] = jsonObject.getJSONObject(i).getString("en");
                        //取出每个事件整体
                        result[1] = jsonObject.getString(i);
                    }catch(Exception e){
                        continue;
                    }
                    forward(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void close() throws HiveException {
    
    }
}
