package data_struct.b栈和队列02.stacklikou;

import java.util.Stack;

/**
 * @Classname EffectiveBrackets_20
 * @Description TODO
 * @Date 2021/12/27 20:59
 * @Created by zhq
 */
public class EffectiveBrackets_20_2 {
    public static void main(String[] args) {
        String str = "{}{";
        System.out.println(isValid(str));
    }

    public static boolean isValid(String s) {
        if (s.length() == 0 || s.length() % 2 != 0) return false;
        Stack<Character> stack = new Stack<>();
        for (char v : s.toCharArray()) {
            if (v == '(') {
                stack.push(')');
            } else if (v == '[') {
                stack.push(']');
            } else if (v == '{') {
                stack.push('}');
            } else if (stack.isEmpty() || stack.pop() != v) return false;
        }
        return stack.isEmpty();
    }
}
