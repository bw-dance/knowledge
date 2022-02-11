package likou.z_suanfa_miji.数组和链表.d二分查找;

/**
 * @Classname 二分查找_704
 * @Description TODO
 * @Date 2022/1/14 9:09
 * @Created by zhq
 */
public class 二分查找_704 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums01 = {-1, 0, 3, 5, 9, 12, 18, 21, 35, 48, 52, 60};
        System.out.println(solution.search(nums01, 12));
        Solution02 solution02 = new Solution02();
        int[] nums02 = {-1, 0, 3, 5, 9, 12, 18, 21, 35, 48, 52, 60};
        System.out.println(solution02.search(nums02, 12));
//        System.out.println(solution.search(nums01, -1));
//        System.out.println(solution.search(nums01, 48));
//        int[] nums02 = {-1, 0, 3, 5, 9, 12};
//        System.out.println(solution.search(nums02, 9));
//        int[] nums03 = {-1, 0, 3, 5, 9, 12};
//        System.out.println(solution.search(nums03, 2));
    }
}

class Solution {
    public int search(int[] nums, int target) {
        int first = 0, last = nums.length - 1, mod = nums.length / 2;
            while (first < last) {
                //缩短区间
                if (nums[mod] == target) {
                    return mod;
                } else if (nums[mod] > target) {
                    last = mod - 1;
                    mod = (mod - first) / 2;
                } else if (nums[mod] < target) {
                    first = mod + 1;
                    mod = mod + (last - mod) / 2;
                }
            }
        //用于最后last=fist这个元素的值与target的比较。
        return nums[last] == target?last:-1;
    }
}


class Solution02 {
    public int search(int[] nums, int target) {
        int first = 0, last = nums.length - 1, mid = 0;
        while (first<= last) {
            mid = first + (last - first) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                last = mid - 1;
            } else if (nums[mid] < target) {
                first = mid + 1;
            }
        }
        return -1;
    }
}