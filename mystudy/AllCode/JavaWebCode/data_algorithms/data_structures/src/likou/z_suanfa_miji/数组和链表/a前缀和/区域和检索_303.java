package likou.z_suanfa_miji.数组和链表.a前缀和;

/**
 * @Classname NumArray_303
 * @Description TODO
 * @Date 2022/1/10 9:20
 * @Created by zhq
 */
public class 区域和检索_303 {
    public static void main(String[] args) {
        int[] nums = {-2, 0, 3, -5, 2, -1};
        NumArray02 numArray = new NumArray02(nums);
        int i = numArray.sumRange(0, 2);
        System.out.println(i);
    }


}

//方式一：for循环遍历，时间复杂度为O（N）
class NumArray01 {

    private int[] nums;

    public NumArray01(int[] nums) {
        this.nums = nums;
    }

    public int sumRange(int left, int right) {
        int res = 0;
        for (int i = left; i <= right; i++) {
            res += nums[i];
        }
        return res;
    }
}


//方式二：前缀和，时间复杂度为O（1）
class NumArray02 {

    //方式二：
    private int[] preSum;

    public NumArray02(int[] nums) {
        //长度在原来的基础上+1，目的是维护通用的公式。
        this.preSum = new int[nums.length + 1];
        //为前置数组赋值。
        for (int i = 1; i < preSum.length; i++) {
            this.preSum[i] = this.preSum[i - 1] + nums[i - 1];
        }
    }
    //求和指定区间的值。
    public int sumRange(int left, int right) {
        return this.preSum[right + 1] - this.preSum[left];
    }
}
