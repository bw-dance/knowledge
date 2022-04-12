package likou.z_suanfa_miji.b队列和栈.c单调栈;

import java.util.Arrays;
import java.util.Stack;

/**
 * @Classname 下一个更大元素II_503
 * @Description TODO
 * @Date 2022/2/17 10:12
 * @Created by zhq
 */
public class 下一个更大元素II_503 {
}

class Solution04 {
    public int[] nextGreaterElements(int[] nums) {
        Stack<Integer> stack = new Stack<>();
        int size = nums.length;
        int[] res = new int[size];
        Arrays.fill(res,-1);//默认全部初始化为-1
        for (int i = 0; i < size *2; i++) {
           while (!stack.isEmpty()&&nums[i%size]>nums[stack.peek()]){
               res[stack.pop()] = nums[i%size];
           }
           stack.push(i%size);
        }
        return res;
    }
}