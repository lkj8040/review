import java.util.Scanner;

public class Bet {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        double a = 1;
        for(int i = 0; i < 3; i++){
            double x = in.nextDouble();
            double y = in.nextDouble();
            double z = in.nextDouble();
            if(x > y){
                if(x > z){
                    a *= x;
                    System.out.print("W" + " ");
                }else{
                    a *= z;
                    System.out.print("L" + " ");
                }
            }else{
                if(y > z){
                    a *= y;
                    System.out.print("T" + " ");
                }else{
                    a *= z;
                    System.out.print("L" + " ");
                }
            }
        }
        String result = String.format("%.2f",(a*0.65-1) * 2);
        System.out.println(result);
    }
}
