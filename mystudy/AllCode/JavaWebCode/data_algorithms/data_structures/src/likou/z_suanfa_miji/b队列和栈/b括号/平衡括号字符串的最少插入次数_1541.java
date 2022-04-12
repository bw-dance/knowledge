package likou.z_suanfa_miji.b队列和栈.b括号;

import java.util.Stack;

/**
 * @Classname 平衡括号字符串的最少插入次数_1541
 * @Description TODO
 * @Date 2022/2/16 15:18
 * @Created by zhq
 */
public class 平衡括号字符串的最少插入次数_1541 {
    public static void main(String[] args) {
        Solution06 solution06 = new Solution06();
        solution06.minInsertions("()())))()");
    }
}

class Solution06 {
    public int minInsertions(String s) {
        //实际插入的元素的数量
        int res = 0;
        //需求的右侧括号的数量
        int need = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                need += 2;
                //当遇到左括号时，若对右括号的需求量为奇数，需要插⼊ 1 个右括号。因为⼀个左括号需要两个右括号嘛，右括号的需求必须是偶数
                if (need % 2 == 1) {
                    // 插⼊⼀个右括号
                    res++;
                    // 对右括号的需求减⼀
                    need--;
                }
            } else {
                need--;
                //如果right=-1,说明多了一个右括号，如果多了一个右括号，则需要再多一个左括号和右括号。
                if (need == -1) {
                    //需要插入一个左括号
                    res++;
                    //右括号的需求量变为1
                    need = 1;
                }
            }
        }
        return res + need;
    }
}

class Solution07 {
    public int minInsertions(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {

        }
        return 0;
    }
}

