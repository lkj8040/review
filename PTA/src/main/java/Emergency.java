import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Emergency {
//第1个数字，有N个城市
//第2个数字，有M条边
//第3个数字，当前所在城市c1，第4个数字，需要去拯救的城市c2
//接下来一行是N个数字(0-N-1)，表示第i个城市当前有多少拯救队
//后面是每个城市之间的路，以及他们之间的长度

//输出：c1和c2之间的最短路径的数量   最大的可携带的救援队数量
    
    /**
     * input:
     * 5 6 0 2
     * 1 2 1 5 3
     * 0 1 1
     * 0 2 2
     * 0 3 1
     * 1 2 1
     * 2 4 1
     * 3 4 1
     *
     * output:
     * 2 4
     *
     * */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int N = scan.nextInt();
        int M = scan.nextInt();
        int cur = scan.nextInt();
        int target = scan.nextInt();
        int[] weights = new int[N];
        int[][] edge = new int[N][N];
        for(int i = 0; i< N; i++){
            weights[i] = scan.nextInt();
        }
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(i == j) {
                    edge[i][j] = 0;
                }else{
                    edge[i][j] = -1;
                }
            }
        }
        for(int i = 0; i < M; i++){
            int m = scan.nextInt();
            int n = scan.nextInt();
            int k = scan.nextInt();
            edge[m][n] = k;
            edge[n][m] = k;
        }
        
        Emergency emergency = new Emergency(N, edge, weights);
        emergency.DFS(cur, target, new ArrayList<>());
        int count = 0;
        for(List<Integer> list : emergency.paths){
            int pathLen = 0;
            for(int i = 1; i < list.size(); i++){
                pathLen += edge[list.get(i-1)][list.get(i)];
            }
            if(pathLen == emergency.shortestPath){
                int sum = 0;
                count++;
                for(int i : list){
                    sum += emergency.weights[i];
                }
                if(sum > emergency.maxWeightedPath){
                    emergency.maxWeightedPath = sum;
                }
            }
        }
        System.out.print(count +" " + emergency.maxWeightedPath);
    }
    
    private int[][] edge;//不存在的边用-1表示
    private int[] vertix;//图的各个顶点
    boolean[] isVisited;
    private int[] weights;
    
    private int shortestPath = Integer.MAX_VALUE;
    private int maxWeightedPath = Integer.MIN_VALUE;
    
    public Emergency(int N, int[][] edge, int[] weights){
        this.edge = edge;
        vertix = new int[N];
        for(int i = 0; i < N; i++){
            vertix[i] = i;
        }
        this.weights = weights;
        this.isVisited = new boolean[vertix.length];
    }
    private List<List<Integer>> paths = new ArrayList<>();
    private void DFS(int cur, int target, List<Integer> stack){
        //只有没有访问过的结点才会进入DFS
        isVisited[cur] = true;
        stack.add(cur);
        if(cur == target) {//到达了目标地点,路径结束
            paths.add(new ArrayList<>(stack));
            int sum = 0;
            for(int j=1; j < stack.size(); j++){
                if(j < stack.size()){
                    sum += edge[stack.get(j-1)][stack.get(j)];
                }
            }
            if(shortestPath > sum) shortestPath = sum;
            return;
        }
        int first = getFirstNext(cur);
        List<Integer> tryV = new ArrayList<>();
        tryV.add(first);
        while(tryV.get(0) != -1){//下一个结点存在并且没有访问过
            int next = tryV.remove(0);
            if(!isVisited[next]){
                DFS(next, target, stack);
                stack.remove(stack.size()-1);
                isVisited[next] = false;
            }
            next = getNext(next, cur);
            tryV.add(next);
        }
    }
    private int getFirstNext(int cur){
        for(int i = 0; i < edge[cur].length; i++){
            if(edge[cur][i] != -1 && i != cur){
                return i;
            }
        }
        return -1;
    }
    private int getNext(int first, int cur){
        for(int i = first + 1; i < edge[cur].length; i++){
            if(edge[cur][i] != -1 && i != cur){
                return i;
            }
        }
        return -1;
    }
}