package likou.z_suanfa_miji.b队列和栈.e字符串去重;


import java.util.Stack;

/**
 * @Classname 去除重复字母_316
 * @Description TODO
 * @Date 2022/3/2 15:42
 * @Created by zhq
 */
public class 去除重复字母_316 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.removeDuplicateLetters("bcabc"));

    }
}

class Solution {
    public String removeDuplicateLetters(String s) {
        //字符
        char[] chars = s.toCharArray();
        //栈，用于记录无重复元素
        Stack<Character> stack = new java.util.Stack<>();
        //用于基础s中每个字符的数量
        int[] nums = new int[256];
        for (int i = 0; i < chars.length; i++)
            nums[chars[i]]++;
        //用于记录栈中是否包含某个元素    查询速度为O（1），如果使用stack提供的contains，查询速度是O（n）
        boolean[] isContains = new boolean[256];
        for (int i = 0; i < chars.length; i++) {
            //true，说明该元素已经添加过
            if (isContains[chars[i]]) {
                nums[chars[i]]--;
                continue;
            }
            //优化1：判断当前元素是否小于栈顶元素，如果小于，则栈顶元素出栈。
            while (!stack.isEmpty() && chars[i] < stack.peek() && nums[stack.peek()] > 0) {
                isContains[stack.peek()] = false;
                stack.pop();
            }
            stack.push(chars[i]);
            nums[chars[i]]--;
            isContains[chars[i]] = true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        while (!stack.isEmpty()) stringBuilder.append(stack.pop());
        //将栈内的字符串翻转。
        return stringBuilder.reverse().toString();
    }
}


class Solution02 {
    public String removeDuplicateLetters(String s) {
        //字符
        char[] chars = s.toCharArray();
        //栈，用于记录无重复元素
        Stack<Character> stack = new java.util.Stack<>();
        //用于基础s中每个字符的数量
        int[] nums = new int[256];
        for (int i = 0; i < chars.length; i++)
            nums[chars[i]]++;
        //用于记录栈中是否包含某个元素
//        boolean[] isContains = new boolean[256];
        for (int i = 0; i < chars.length; i++) {
            //true，说明该元素已经添加过
            if (stack.contains(chars[i])) {
                nums[chars[i]]--;
                continue;
            }
            //优化1：判断当前元素是否小于栈顶元素，如果小于，则栈顶元素出栈。
            while (!stack.isEmpty() && chars[i] < stack.peek() && nums[stack.peek()] > 0) {
                stack.pop();
            }
            stack.push(chars[i]);
            nums[chars[i]]--;
        }
        StringBuilder stringBuilder = new StringBuilder();
        while (!stack.isEmpty()) stringBuilder.append(stack.pop());
        //将栈内的字符串翻转。
        return stringBuilder.reverse().toString();
    }
}

