package likou.z_suanfa_miji.b队列和栈.c单调栈;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @Classname 下一个更大元素I_496
 * @Description TODO
 * @Date 2022/2/16 17:17
 * @Created by zhq
 */
public class 下一个更大元素I_496 {
}

class Solution10 {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int[] res = new int[nums1.length];
        Map<Integer, Integer> map = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < nums2.length; i++) {
            while (!stack.isEmpty() && stack.peek() < nums2[i]) {
                map.put(stack.pop(), nums2[i]);
            }
            stack.push(nums2[i]);
        }
        for (int i = 0; i < nums1.length; i++) {
            if (map.containsKey(nums1[i])) res[i] = map.get(nums1[i]);
            else res[i]=-1;
        }
        return res;
    }
}
