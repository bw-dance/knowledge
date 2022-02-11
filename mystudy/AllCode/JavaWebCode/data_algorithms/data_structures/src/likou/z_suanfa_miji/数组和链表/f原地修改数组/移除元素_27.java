package likou.z_suanfa_miji.数组和链表.f原地修改数组;

/**
 * @Classname 移除元素_27
 * @Description TODO
 * @Date 2022/2/10 19:52
 * @Created by zhq
 */
public class 移除元素_27 {
    public static void main(String[] args) {
        Solution05 solution05 = new Solution05();
        System.out.println(solution05.removeElement(new int[]{0, 1, 2, 2, 3, 0, 4, 2}, 2));
    }
}

class Solution05 {
    public int removeElement(int[] nums, int val) {
        int slow = 0, fast = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                if (slow != fast)
                    nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }
}