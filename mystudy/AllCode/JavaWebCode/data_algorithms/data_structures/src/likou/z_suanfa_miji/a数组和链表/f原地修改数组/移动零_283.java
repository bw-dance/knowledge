package likou.z_suanfa_miji.a数组和链表.f原地修改数组;

import java.util.Arrays;

/**
 * @Classname 移动零_283
 * @Description TODO
 * @Date 2022/2/11 9:27
 * @Created by zhq
 */
public class 移动零_283 {
    public static void main(String[] args) {
        Solution08 solution07 = new Solution08();
        solution07.moveZeroes(new int[]{1, 0, 1});
    }
}

class Solution07 {
    public void moveZeroes(int[] nums) {
        int slow = 0;
        int fast = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                fast++;
            } else {
                int slowV = nums[slow];
                nums[slow] = nums[fast];
                nums[fast] = slowV;
                slow++;
                fast++;
            }
        }
        System.out.println(Arrays.toString(nums));
    }
}


//优化：fast每一轮都是递增，可以用i代替fast
class Solution08 {
    public void moveZeroes(int[] nums) {
        int slow = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                int slowV = nums[slow];
                nums[slow] = nums[i];
                nums[i] = slowV;
                slow++;
            }
        }
    }
}
