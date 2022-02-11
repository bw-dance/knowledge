package likou.z_suanfa_miji.数组和链表.e优势洗牌;

import java.util.*;

/**
 * @Classname 优势洗牌_870
 * @Description TODO
 * @Date 2022/2/10 9:48
 * @Created by zhq
 */
public class 优势洗牌_870 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums1 = {2, 0, 4, 1, 2};
        int[] nums2 = {1, 3, 0, 0, 2};
        System.out.println(Arrays.toString(solution.advantageCount(nums1, nums2)));
    }
}

class Solution {
    public int[] advantageCount(int[] nums1, int[] nums2) {
        //使用map存储nums2中数据的顺序（废除原因：nums2中可能存在重复元素）
        Map<Integer, Deque<Integer>> nums2Map = new HashMap<>();
        for (int i = 0; i < nums2.length; i++) {
            Deque<Integer> indexList = nums2Map.getOrDefault(nums2[i], new LinkedList<>());
            indexList.add(i);
            nums2Map.put(nums2[i], indexList);
        }
        //排序
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        //用于nums1的返回
        int[] arr = new int[nums1.length];
        //记录num2的最大数
        int length = nums2.length - 1;
        //记录偏移量（用最小的数干最大的数的数量）
        int tag = 0;
        for (int i = 0; i < nums1.length; i++) {
            if (nums1[i] <= nums2[i - tag]) {
                //获取最快的那匹马的位置   输或者平局都用最菜的马
                Integer integer = nums2Map.get(nums2[length]).pop();
                //换成我方最菜的马。
                arr[integer] = nums1[i];
                length--;
                tag++;
            } else if (nums1[i] > nums2[i - tag]) {
                //获取最快的那匹马的位置
                Integer integer = nums2Map.get(nums2[i - tag]).pop();
                //换成我方最菜的马。
                arr[integer] = nums1[i];
            }
        }
        return arr;
    }
}
class  Solution01{
    int[] advantageCount(int[] nums1, int[] nums2) {
        int n = nums1.length;
        // 给 nums2 降序排序   优先队列记录当前元素的索引和值。
        PriorityQueue<int[]> maxpq = new PriorityQueue<>(
                (int[] pair1, int[] pair2) -> {
                    return pair2[1] - pair1[1];
                }
        );
        for (int i = 0; i < n; i++) {
            maxpq.offer(new int[]{i, nums2[i]});
        }
        // 给 nums1 升序排序
        Arrays.sort(nums1);
        // nums1[left] 是最⼩值，nums1[right] 是最⼤值
        int left = 0, right = n - 1;
        int[] res = new int[n];
        while (!maxpq.isEmpty()) {
            int[] pair = maxpq.poll();
            // maxval 是 nums2 中的最⼤值，i 是对应索引
            int i = pair[0], maxval = pair[1];
            if (maxval < nums1[right]) {
                // 如果 nums1[right] 能胜过 maxval，那就⾃⼰上
                res[i] = nums1[right];
                right--;
            } else {
                // 否则⽤最⼩值混⼀下，养精蓄锐
                res[i] = nums1[left];
                left++;
            }
        }
        return res;
    }

}