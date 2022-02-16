package likou.z_suanfa_miji.a数组和链表.a前缀和;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname SumKey
 * @Description TODO
 * @Date 2022/1/11 9:31
 * @Created by zhq
 */
public class 和为key的数组_560 {
    public static void main(String[] args) {
        int[] nums = {1, 1, 1};
        System.out.println(Solution02.subarraySum(nums, 2));
    }
}

//方法一：使用前缀和+双层for循环
class Solution01 {
    public static int subarraySum(int[] nums, int k) {

        int[] preSum = new int[nums.length + 1];
        for (int i = 1; i < preSum.length; i++)
            preSum[i] = preSum[i - 1] + nums[i - 1];
        int count = 0;
        for (int i = 1; i < preSum.length; i++) {
            for (int j = 0; j < i; j++) {
                if (preSum[i] - preSum[j] == k) {
                    count++;
                }
            }
        }
        return count;
    }
}

class Solution02 {
    public static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        int preFixSum = 0;
        int count = 0;
        for (int num : nums) {
            preFixSum += num;
            if (map.containsKey(preFixSum - k))
                count += map.get(preFixSum - k);
            //前缀和数量累加
            map.put(preFixSum, map.getOrDefault(preFixSum, 0) + 1);
        }
        return count;
    }
}