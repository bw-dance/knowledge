package likou.z_suanfa_miji.b队列和栈.c单调栈;

import java.util.Stack;

/**
 * @Classname 每日温度_739
 * @Description TODO
 * @Date 2022/2/17 8:45
 * @Created by zhq
 */
public class 每日温度_739 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] temperatures = {73, 74, 75, 71, 69, 72, 76, 73};
        System.out.println(solution.dailyTemperatures(temperatures));
    }
}

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int slow = 0;
        int fast = 1;
        int[] res = new int[temperatures.length];
        while (slow < temperatures.length) {
            if (fast >= temperatures.length) {
                res[slow] = 0;
                slow++;
                fast = slow + 1;
            } else {
                if (temperatures[slow] <= temperatures[fast]) {
                    fast++;
                } else {
                    res[slow] = fast - slow;
                    slow++;
                    fast = slow + 1;
                }
            }
        }
        return res;
    }
}

class Solution01 {
    public int[] dailyTemperatures(int[] temperatures) {
        Stack<Integer> stack = new Stack();
        int[] res = new int[temperatures.length];
        for (int i = temperatures.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                Integer top = stack.pop();
                res[top] = i - top;
            }
            stack.push(i);
        }
        return res;
    }
}
