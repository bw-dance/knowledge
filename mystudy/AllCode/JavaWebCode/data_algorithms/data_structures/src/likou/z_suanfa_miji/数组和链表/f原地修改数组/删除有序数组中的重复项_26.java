package likou.z_suanfa_miji.数组和链表.f原地修改数组;

/**
 * @Classname 删除有序数组中的重复项_26
 * @Description 使用快慢指针
 * @Date 2022/2/10 15:18
 * @Created by zhq
 */
public class 删除有序数组中的重复项_26 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        System.out.println(solution.removeDuplicates(nums));
    }
}

class Solution {
    public int removeDuplicates(int[] nums) {
        int slow = 0;
        int fast = 0;
        while (fast < nums.length) {
            if (nums[slow] == nums[fast]) {
                fast++;
            } else if (nums[slow] != nums[fast]) {
                slow++;
                nums[slow] = nums[fast];
            }
        }
        return slow + 1;
    }
}

class Solution01 {
    public int removeDuplicates(int[] nums) {
        int slow = 0;
        int fast = 0;
        while (fast < nums.length) {
            int s = nums[slow];
            int f = nums[fast];
            if (s == f) {
                fast++;
            } else if (nums[slow] != nums[fast]) {
                slow++;
                nums[slow] = nums[fast];
            }
        }
        return slow + 1;
    }
}