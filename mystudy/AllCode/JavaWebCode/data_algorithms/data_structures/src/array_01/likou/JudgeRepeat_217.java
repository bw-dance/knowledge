package array_01.likou;

import java.util.Arrays;

/**
 * @Classname JudgeRepeat_217
 * @Description TODO
 * @Date 2021/12/26 21:43
 * @Created by zhq
 */
public class JudgeRepeat_217 {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4};
        System.out.println(containsDuplicate(nums));
    }

    public static boolean containsDuplicate(int[] nums) {
        if (nums.length==0){
            return false;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return true;
            }
        }
        return false;
    }
}
