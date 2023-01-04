package likou.面试.华为od;

import java.util.Scanner;

public class 士兵过河 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        int mil =  sc.nextInt();
        String[] strAry = str.split(" ");
        int[] colors = new int[strAry.length];
        for(int i = 0;i<strAry.length;i++){
            colors[i] = Integer.valueOf(strAry[i]);
        }
        int  maxCount = 0;
        for(int i=colors.length-mil;i<colors.length-1;i++){
            int count = 1;
            for(int j=colors.length-mil+1 ; j< colors.length;j++){

                if(colors[i]==colors[j]){
                    count++;
                }
            }
            if(count>maxCount){
                maxCount=count;
            }
        }
        System.out.println(maxCount);

    }
}
