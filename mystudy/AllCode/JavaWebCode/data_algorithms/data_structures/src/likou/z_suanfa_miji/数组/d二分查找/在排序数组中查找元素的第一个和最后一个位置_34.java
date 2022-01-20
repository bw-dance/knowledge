package likou.z_suanfa_miji.数组.d二分查找;

/**
 * @Classname 在排序数组中查找元素的第一个和最后一个位置_34
 * @Description TODO
 * @Date 2022/1/14 16:53
 * @Created by zhq
 */
public class 在排序数组中查找元素的第一个和最后一个位置_34 {
    public static void main(String[] args) {
//        int[] nums = {1, 2, 2, 2, 4};
//        Solution03 solution03 = new Solution03();
//        System.out.println(solution03.searchRange(nums, 4));
//        Solution04 solution04 = new Solution04();
//        System.out.println(solution04.searchRange(nums, 1));
        Long a = 1L;
        for (int i = 1; i <= 1000; i++) {
            a *= i;
        }
        System.out.println(a);
    }
}

//返回目标元素第一次出现的位置
class Solution03 {
    public int searchRange(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                right = mid - 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            }
        }
        //防止出边界。如在1,2,2,2,2,3中查找5  不同考虑right的取值范围
        return left >= nums.length || nums[left] != target ? -1 : left;
    }
}

//返回目标元素最后一次出现的位置
class Solution04 {
    public int searchRange(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            }
        }
        //防止出边界。如在1,2,2,2,2,3中查找0  不用考虑left的取值范围。
        return right < 0 || nums[right] != target ? -1 : right;
    }
}

//题解
class Solution05 {
    public int[] searchRange(int[] nums, int target) {
        int arr[] = new int[2];
        int first = getIndex(nums, target, true);
        int last = getIndex(nums, target, false);
        arr[0] = first;
        arr[1] = last;
        return arr;
    }

    public int getIndex(int[] nums, int target, boolean isLeft) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                if (isLeft) {
                    right = mid - 1;
                } else left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            }
        }
        if (isLeft) {
            return left >= nums.length || nums[left] != target ? -1 : left;
        } else return right < 0 || nums[right] != target ? -1 : right;
    }
}


