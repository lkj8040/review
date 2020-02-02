package com.liukuijian.algorithm.string;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Convert {
    public String convert(String s, int numRows) {
        //行数为3时
        //len = 3 + 1 + 3 + 1 + ...
        //len = 3*i + (3-2)*j
        //行数为4时
        //len = 4 + 1*2 + 4 + 2
        //寻找一个周期：行数为3时，周期为3+(3-2)
        //行数为4时，周期为4+(4-2)
        //行数为n时，周期为n+(n-2)
        //假设s的长度为len，那么有len/(2n-2)个周期，一个周期的宽度为n-1,一个周期的长度为n + n-2
        //则基本宽度为len/(2n-2) *(n-1)
        //扩展宽度为： len-len/(2n-2)*(2n-2) <= n? len/(2n-2) *(n-1) + 1 : len/(2n-2)*(n-1) + (len-len/(2n-2)*(2n-2) - n)
        if(s == null || s.length() <= 1 || numRows == 1) return s;
        int len = s.length();
        int restCol = len - len/(2*numRows-2) *(2*numRows-2);//剩下多少个字符
        int Col = len/(2*numRows-2) *(numRows-1);//当前字符前面有多少列
        if(restCol > 0){
            Col = restCol <= numRows? Col+1:Col + restCol - numRows + 1; //不够1列的，Col+1，超过1列的
        }
        // i -> i/(2n-2)*(2n-2) 表示当前字符前面有多少列
        char[][] str = new char[numRows][Col];
        for(int i = 0; i < len; i++){
            int Col1 = i/(2*numRows-2)*(numRows-1) - 1;// 7/(2*3-2)*(3-1) - 1=`1
            int restCol1 = i - i/(2*numRows-2) *(2*numRows-2) + 1;// 7 - 4 + 1= 4
            int baseRow = 1;
            if(restCol1 > 0){
                Col1 = restCol1 <= numRows? Col1 + 1:Col1 + restCol1 + 1 - numRows; //3
                baseRow = restCol1 <= numRows? restCol1 - 1:numRows - 1 - (restCol1 - numRows);//1
            }
            str[baseRow][Col1]=s.charAt(i);
        }

        String res = "";
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < Col; j++){
                if(str[i][j] != '\u0000') {
                    res += str[i][j];
                    System.out.print(str[i][j]+" ");
                }else{
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

        
        return res;
    }

    public static void main(String[] args) {
        new Convert().convert("adghajgalhgalfdadagdagafagdgagaddgwtehfshsghakghlfaga",8);

        List<Integer> list = new ArrayList<>();
    }
}
