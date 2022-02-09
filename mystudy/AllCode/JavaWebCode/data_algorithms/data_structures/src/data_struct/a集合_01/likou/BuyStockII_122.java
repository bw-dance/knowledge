package data_struct.a集合_01.likou;

/**
 * @Classname BuyStockII_122
 * @Description TODO
 * @Date 2021/12/26 19:42
 * @Created by zhq
 */
public class BuyStockII_122 {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        rotateTwo(nums, 3);
        for (int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
        }
    }

    //方法一：一个一个移动，超时
    public static void rotateOne(int[] nums, int k) {
        if (k >= nums.length) k = k % nums.length;
        while (k > 0) {
            int val = nums[nums.length - 1];
            for (int i = nums.length - 1; i > 0; i--) {
                nums[i] = nums[i - 1];
            }
            nums[0] = val;
            k--;
        }
    }
    //方法二：旋转后，找对应规律。成功。
    public static void rotateTwo(int[] nums, int k) {
        if (k >= nums.length) k = k % nums.length;
        int[] brr = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            brr[i] = nums[i];
        }
        for (int i = 0; i < nums.length; i++) {
            nums[i] = brr[(i + nums.length - k) % nums.length];
        }
    }
}
