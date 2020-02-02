import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Redix {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String N1 = sc.next();
        String N2 = sc.next();
        int tag = sc.nextInt();
        int redix = sc.nextInt();
        if(tag == 1){
            //redix进制转为十进制
            int decimalNum = redixToBinaryString(N1, redix);
            int left = 0;
            int right = redix;
            for(int i = 0; i < redix; i++){
                int tmp = redixToBinaryString(N2, i);
                if(tmp == decimalNum){
                    if(i < 10) System.out.println(i);
                    else{
                        System.out.println((char)(i - 10 + 'a'));
                    }
                    return;
                }
            }
        }else if(tag == 2){
            int decimalNum = redixToBinaryString(N2, redix);
            for(int i = 0; i < 35; i++){
                int tmp = redixToBinaryString(N1, i);
                if(tmp == decimalNum){
                    if(i < 10) System.out.println(i);
                    else{
                        System.out.println((char)(i - 10 + 'a'));
                    }
                    return;
                }
            }
        }
        System.out.println("Impossible");
    }
    //任意进制返回其10进制的真值
    private static Map<Character, Integer> map = new HashMap<>();
    static {
        for(char i = '0'; i <= '9'; i++){
            map.put(i, i - '0');
        }
        for(char i = 'a'; i <= 'z'; i++){
            map.put(i, i - 'a' + 10);
        }
    }
    private static int redixToBinaryString(String num, int redix){
        String str = num;
        int len = str.length();
        int result = 0;
        for(int i = 0; i < len; i++){
            result += map.get(str.charAt(i)) * power(len - i - 1, redix);
        }
        return result;
    }
    private static int power(int expr, int redix){
        int multi = 1;
        for(int i = 0; i < expr; i++){
            multi *= redix;
        }
        return multi;
    }
}
