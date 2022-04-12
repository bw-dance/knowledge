package likou.z_suanfa_miji.b队列和栈.b括号;

import java.util.Stack;

/**
 * @Classname 使括号有效的最少添加_921
 * @Description TODO
 * @Date 2022/2/16 10:35
 * @Created by zhq
 */
public class 使括号有效的最少添加_921 {
}

class Solution03 {
    public int minAddToMakeValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (!stack.isEmpty()) {
                if (stack.peek() == '(' && stack.peek() != s.charAt(i)) stack.pop();
                else stack.push(s.charAt(i));
            } else stack.push(s.charAt(i));
        }
        return stack.size();
    }
}

class Solution04 {
    public int minAddToMakeValid(String s) {
        //左括号的需求量
        int left = 0;
        //右括号的需求量
        int right = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                right++;

            } else {
                right--;
                if (right == -1) {
                    left++;
                    right=0;
                }
            }
        }
        return right + left;
    }
}
