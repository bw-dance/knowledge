package likou.面试;

public class Main2 {
    public static void main(String[] args) {
        System.out.println(climbStairs(1));
        System.out.println(climbStairs(10));
        System.out.println(climbStairs(45));
    }
    public static int climbStairs(int n){
        if (n <= 2) {
            return n;
        }
        int pre = 1, last = 2;
        for (int i = 2; i < n; i++) {
            int cur = pre + last;
            pre = last;
            last = cur;
        }
        return last;
    }
}
