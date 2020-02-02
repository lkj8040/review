import java.util.Scanner;

public class SignInAndSignOut {

        public static void main(String [] args){
            Scanner scan = new Scanner(System.in);
            int n = Integer.parseInt(scan.nextLine());
            String[][] info = new String[n][3];
            int j = 0;
            while(j < n){
                String line = scan.nextLine();
                String[] splits = line.split(" ");
                info[j][0] = splits[0];
                info[j][1] = splits[1];
                info[j][2] = splits[2];
                
                j++;
            }
            int tmp1 = 0;
            int tmp2 = 0;
            for(int i = 1; i < n; i++){
                if(smaller(info[i][1],info[tmp1][1])){
                    tmp1 = i;
                }
                if(bigger(info[i][2],info[tmp2][2])){
                    tmp2 = i;
                }
            }
            System.out.println(info[tmp1][0] +" " + info[tmp2][0]);
        }
        private static boolean smaller(String str1, String str2){
            String[] time1 = str1.split(":");
            String[] time2 = str2.split(":");
            if(Integer.parseInt(time1[0]) < Integer.parseInt(time2[0]) ) return true;
            else if(Integer.parseInt(time1[0]) > Integer.parseInt(time2[0]) ) return false;
            else{
                if(Integer.parseInt(time1[1]) < Integer.parseInt(time2[1]) ) return true;
                else if(Integer.parseInt(time1[1]) > Integer.parseInt(time2[1])) return false;
                else{
                    if(Integer.parseInt(time1[2]) < Integer.parseInt(time2[2])) return true;
                    else return false;
                }
            }
        }
        private static boolean bigger(String str1, String str2) {
            String[] time1 = str1.split(":");
            String[] time2 = str2.split(":");
            if(Integer.parseInt(time1[0]) > Integer.parseInt(time2[0]) ) return true;
            else if(Integer.parseInt(time1[0]) < Integer.parseInt(time2[0]) ) return false;
            else{
                if(Integer.parseInt(time1[1]) > Integer.parseInt(time2[1]) ) return true;
                else if(Integer.parseInt(time1[1]) < Integer.parseInt(time2[1])) return false;
                else{
                    if(Integer.parseInt(time1[2]) > Integer.parseInt(time2[2])) return true;
                    else return false;
                }
            }
        }
}
