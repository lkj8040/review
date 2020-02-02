import java.util.Scanner;

public class Sum{
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        String s = scan.nextLine();
        String[] input = s.split(" ");
        int a = Integer.parseInt(input[0]);
        int b = Integer.parseInt(input[1]);
        String result = "" + (a + b);
        if(a + b < 0) {
            System.out.print("-");
            result = result.substring(1);
        }
        int len = result.length();
        char[] chars = result.toCharArray();
        int count = 0;
        String ans = "";
        for(int i = len - 1; i >= 0; i--){
            count++;
            ans = chars[i] + ans;
            if(i > 0 && count == 3){
                ans = "," + ans;
                count = 0;
            }
        }
        System.out.println(ans);
    }
}
