package data_struct.stack_queue_02.stacklikou;

import data_struct.stack_queue_02.stack.ArrayStack;

/**
 * @Classname EffectiveBrackets_20
 * @Description TODO
 * @Date 2021/12/27 20:59
 * @Created by zhq
 */
public class EffectiveBrackets_20 {
    public static void main(String[] args) {
        String str = "{}{";
        System.out.println(isValid(str));
    }

    public static boolean isValid(String s) {
        //奇数长度淘汰
        if (s.length() == 0 || s.length() % 2 != 0) return false;
        char[] chars = s.toCharArray();
        //右括号为首元素直接淘汰
        if (judgeIsRight(chars[0])) return false;
        ArrayStack stack = new ArrayStack();
        for (int i = 0; i < s.length(); i++) {
            char v = chars[i];
            //进入右括号时，走匹配。注意：当stack.size为0时，使用peek会出现报错。
            if (stack.isEmpty() || !judgeIsRight(v)) {
                stack.push(v);
            } else {
                char peek = (char) stack.peek();
                switch (peek) {
                    case '(':
                        if (!isMatch(stack, v, ')')) return false;
                        break;
                    case '[':
                        if (!isMatch(stack, v, ']')) return false;
                        break;
                    case '{':
                        if (!isMatch(stack, v, '}')) return false;
                        break;
                }
            }
        }
        //匹配完毕，当栈内元素清空，则说明完全匹配，否则不成立。
        return stack.isEmpty();
    }

    //判断是否为右括号
    public static boolean judgeIsRight(char v) {
        if (v == ')' || v == ']' || v == '}') return true;
        return false;
    }

    //判断)或]或}括号是否匹配。匹配返回true，否则false
    public static boolean isMatch(ArrayStack stack, char v, char k) {
        if (v != k) return false;
        stack.pop();
        return true;
    }
}
