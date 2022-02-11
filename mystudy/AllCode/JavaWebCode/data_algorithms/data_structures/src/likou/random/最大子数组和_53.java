package likou.random;

import java.util.Arrays;

/**
 * @Classname 最大子数组和_53
 * @Description TODO
 * @Date 2022/2/9 22:09
 * @Created by zhq
 */
public class 最大子数组和_53 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.maxSubArray(new int[]{5, 4, -1, 7, 8}));
    }
}

class Solution {
    public int maxSubArray(int[] nums) {
        if (nums.length == 1) return nums[0];
        int[] arr = new int[nums.length];
        arr[0] = nums[0];
        for (int i = 1; i < arr.length; i++) {
            arr[i] = arr[i - 1] + nums[i];
        }
        int max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] - arr[i] > max) max = arr[j] - arr[i];
            }
        }

        return max;
    }
}
