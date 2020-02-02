import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TransformNumToEnglish {
    
    //给定非负整数N，计算所有位的和
        public static void main(String[] args){
            Scanner scan = new Scanner(System.in);
            String num = scan.nextLine();
            String[] nums = num.split("");
            int sum = 0;
            for(int i = 0; i < nums.length; i++){
                sum += Integer.parseInt(nums[i]);
            }
            char[] result = ("" + sum).toCharArray();
            Map<Character, String> map = new HashMap<>();
            map.put('1',"one");
            map.put('2',"two");
            map.put('3',"three");
            map.put('4',"four");
            map.put('5',"five");
            map.put('6',"six");
            map.put('7',"seven");
            map.put('8',"eight");
            map.put('9',"nine");
            map.put('0',"zero");
            for(int i = 0; i < result.length - 1; i++){
                System.out.print(map.get(result[i]) + " ");
            }
            System.out.print(map.get(result[result.length - 1]));
        }
}
