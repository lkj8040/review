import java.util.Scanner;

public class Polynomial {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        String line1 = scan.nextLine();
        String line2 = scan.nextLine();
        String[] strArr1 = line1.split(" ");
        String[] strArr2 = line2.split(" ");
        Node poly1 = new Node(0,0);
        Node poly2 = new Node(0,0);
        Node tmp1 = poly1;
        Node tmp2 = poly2;
        for(int i = 1; i < strArr1.length; i += 2){
            int poly = Integer.parseInt(strArr1[i]);
            double expr = Double.parseDouble(strArr1[i+1]);
            poly1.next = new Node(poly, expr);
            poly1 = poly1.next;
        }
        for(int i = 1; i < strArr2.length; i += 2){
            int poly = Integer.parseInt(strArr2[i]);
            double expr = Double.parseDouble(strArr2[i+1]);
            poly2.next = new Node(poly, expr);
            poly2 = poly2.next;
        }
        
        poly1 = tmp1.next;
        poly2 = tmp2.next;
        mergePolynomials(poly1, poly2);
    }
    private static void mergePolynomials(Node poly1, Node poly2){
        Node head = new Node(0,0);
        Node result = head;
        int count = 0;
        while(poly1 != null && poly2 != null){
            if(poly1.poly > poly2.poly){
                head.next = poly1;
                count++;
                poly1 = poly1.next;
                head = head.next;
            }else if(poly1.poly < poly2.poly){
                count++;
                head.next = poly2;
                poly2 = poly2.next;
                head = head.next;
            }else{
                if(poly1.expr + poly2.expr != 0){
                    poly2.expr = poly1.expr + poly2.expr;
                    head.next = poly2;
                    head = head.next;
                    poly1 = poly1.next;
                    poly2 = poly2.next;
                    count++;
                }else{
                    poly1 = poly1.next;
                    poly2 = poly2.next;
                }
            }
        }
        if(poly1 != null) {
            while(poly1 != null){
                head.next = poly1;
                count++;
                head = head.next;
                poly1 = poly1.next;
            }
        }
        if(poly2 != null) {
            while(poly2 != null){
                head.next = poly2;
                count++;
                head = head.next;
                poly2 = poly2.next;
            }
        }
        result = result.next;
        System.out.print(count);
        if(result != null) System.out.print(" ");
        while(result != null){
            System.out.print(result.poly + " ");
            System.out.print(result.expr);
            if(result.next != null){
                System.out.print(" ");
            }
            result = result.next;
        }
        System.out.println();
    }
}
class Node{
    int poly;
    double expr;
    Node next;
    public Node(int poly, double expr){
        this.poly = poly;
        this.expr = expr;
    }
}
