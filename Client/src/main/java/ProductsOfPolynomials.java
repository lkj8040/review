import java.util.*;

public class ProductsOfPolynomials {
    //求两个多项式的乘积
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Map<Integer, Double> map1 = new HashMap<>();
        int n1 = sc.nextInt();
        for(int i = 0; i < n1; i++){
            int expr = sc.nextInt();
            double coff = sc.nextDouble();
            map1.put(expr, coff);
        }
        Map<Integer, Double> map2 = new HashMap<>();
        int n2 = sc.nextInt();
        for(int i = 0; i < n2; i++){
            int expr = sc.nextInt();
            double coff = sc.nextDouble();
            map2.put(expr, coff);
        }
        Map<Integer, Double> map3 = new HashMap<>();
        for(Map.Entry<Integer, Double> entry1: map1.entrySet()){
            for(Map.Entry<Integer, Double> entry2: map2.entrySet()){
                int expr = entry1.getKey() + entry2.getKey();
                double coff = entry1.getValue() * entry2.getValue();
                if(coff != 0.0){
                    if(!map3.containsKey(expr)){
                        map3.put(expr, coff);
                    }else{
                        double coffCheck = map3.get(expr) + coff;
                        if(coffCheck != 0.0){
                            map3.put(expr, coffCheck);
                        }else{
                            map3.remove(expr);
                        }
                    }
                }
            }
        }
        TreeMap<Integer, Double> result = new TreeMap<>(map3);
        if(result.isEmpty()){
            System.out.println(0);
            return;
        }
        
        System.out.print(result.size() + " ");
        Iterator<Map.Entry<Integer, Double>> iter = result.descendingMap().entrySet().iterator();
        
        while(iter.hasNext()){
            Map.Entry<Integer, Double> entry = iter.next();
            if(iter.hasNext()){
                String print = String.format("%d %.1f ", entry.getKey(),entry.getValue());
                System.out.print(print);
            }else{
                String print = String.format("%d %.1f", entry.getKey(),entry.getValue());
                System.out.println(print);
            }
        }
    }
}
