package likou.z_suanfa_miji.数组和链表.b区间差;

import java.util.Arrays;

/**
 * @Classname 航班预订统计_1109
 * @Description TODO
 * @Date 2022/1/11 17:14
 * @Created by zhq
 */
public class 航班预订统计_1109 {
    public static void main(String[] args) {
        int[][] bookings = {{3,3,5}, {1, 3, 20}, {1, 2, 15}};
        Solution01 solution01 = new Solution01();
        int[] results = solution01.corpFlightBookings(bookings, 3);
        System.out.println(Arrays.toString(results));
    }
}

class Solution01 {
    //差分数组
    private int[] diff;

    //初始化一个数组，区间操作在此数组上进行
    public void proDifference(int[] nums) {
        diff = new int[nums.length];
        diff[0] = nums[0];
        //构建差分数组
        for (int i = 1; i < diff.length; i++)
            diff[i] = nums[i] - nums[i - 1];
    }

    public void increment(int i, int j, int val) {
        diff[i] += val;
        //如果j+1==diff.length,说明j是数组的最后一个元素，则不需要再减去val了
        if (j + 1 < diff.length) diff[j+1] -= val;
    }

    //结果返回
    public int[] result() {
        int[] res = new int[diff.length];
        res[0] = diff[0];
        for (int i = 1; i < res.length; i++)
            res[i] = res[i - 1] + diff[i];
        return res;
    }

    //业务处理
    public int[] corpFlightBookings(int[][] bookings, int n) {
        int[] nums = new int[n];
        proDifference(nums);
        for (int l = 0; l < bookings.length; l++) {
            int i = bookings[l][0];
            int j = bookings[l][1];
            int val = bookings[l][2];
            increment(i - 1, j-1, val);
        }
        return result();
    }

}