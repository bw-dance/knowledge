package data_struct.a集合_01.likou;

import java.util.Arrays;

/**
 * @Classname OnlyAppearOneNum_136
 * @Description TODO
 * @Date 2021/12/26 21:56
 * @Created by zhq
 */
public class OnlyAppearOneNum_136 {
    public static void main(String[] args) {
        int[] nums = {4, 1, 2, 1, 2};
        singleNumberTwo(nums);
    }

    public static int singleNumber(int[] nums) {
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 1; i += 2) {
            if (nums[i] != nums[i + 1]) {
                return nums[i];
            }
        }
        if (nums.length % 2 != 0) {
            return nums[nums.length - 1];
        }
        return -1;
    }

    public static int singleNumberTwo(int[] nums) {
        int single = 0;
        for (int num : nums) {
            single ^= num;
        }
        return single;
    }
}
