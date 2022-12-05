package likou.面试.数字马力;

import java.util.HashMap;
import java.util.Map;

public class Main2 {
    public static void main(String[] args) {
        System.out.println(climbStairs(1));
        System.out.println(climbStairs4(5));
        System.out.println(climbStairs2(45));
    }

    public static int climbStairs(int n) {
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


    public static int climbStairs2(int n) {
        //超时
        if (n <= 2) {
            return n;
        }
        return climbStairs(n - 1) + climbStairs(n - 2);
    }

    public static Map<Integer, Integer> stairs = new HashMap<>();

    public static int climbStairs3(int n) {
        if (n <= 2) {
            return n;
        }
        if (stairs.containsKey(n)) {
            return stairs.get(n);
        }
        int i = climbStairs(n - 1) + climbStairs(n - 2);
        stairs.put(n, i);
        return i;
    }

    public static int climbStairs4(int n) {
        if (n <= 2) {
            return n;
        }
        int last_bef = 1;
        int last = 2;
        int cur = 0;
        for (int i = 2; i < n; i++) {
            cur = last + last_bef;
            last_bef = last;
            last = cur;
        }
        return cur;
    }
}
