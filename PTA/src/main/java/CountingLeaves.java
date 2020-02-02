import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CountingLeaves {
    // 总结点数N   非叶子结点数M
// M行： 意味着一个非叶子结点一行
// 01 表示 给定的非叶子结点编号 1 是它的子结点的数目 接下来是它的1个子节点编号
// 01 固定表示根节点
    
    public static void main(String[] args){
        
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int i = 0,j,id,k,child;
        ArrayList<Integer> list;
        HashMap<Integer,ArrayList<Integer>> map = new HashMap<>();
        int[] record = new int[n];//用来存储n个结点
        
        while(i < m){//m个非叶子结点，接下来会有m行数据
            id = sc.nextInt();//两位id直接不管你，看作整数就完事
            k = sc.nextInt();//当前非叶子结点有有多少子结点
            list = new ArrayList<>();
            for(j = 0;j < k;j++){//所有的子结点
                child = sc.nextInt();
                list.add(child);
            }
            map.put(id, list);
            i++;
        }
        List<Integer> result = new ArrayList<>();
        List<Integer> target = new ArrayList<>();
        result.add(1);
        result.add(null);
        int level = 0;
        while(result.get(0) != null){
            int childNode = result.remove(0);
            List<Integer> listTmp = map.get(childNode);
            if(listTmp == null) target.add(childNode);
            else{
                for(Integer nodeChild : listTmp){
                    result.add(nodeChild);
                }
            }
            if(result.get(0) == null){
                result.remove(0);
                result.add(null);
                record[level++] = target.size();
                target.clear();
            }
        }
        for(i = 0; i < level-1;i++){
            System.out.print(record[i] + " ");
        }
        System.out.print(record[i]);
    }
}
