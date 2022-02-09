package data_struct.e集合和映射.映射Map.likou;

import java.util.Arrays;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Classname 两个数组的交集_349
 * @Description TODO
 * @Date 2022/2/8 22:33
 * @Created by zhq
 */
public class 两个数组的交集_349 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.intersection(new int[]{1,2,2,1}, new int[]{2,2})));
    }
}

class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {
        //boxed，将int类型的数据转换成Integer类型
        Set<Integer> collect = Arrays.stream(nums1).boxed().collect(Collectors.toSet());
        //distinct 去重
        return Arrays.stream(nums2).distinct().filter(item -> collect.contains(item)).toArray();
    }
}

class Solution01 {
    public int[] intersection(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0 || nums2 == null || nums2.length == 0) {
            return new int[0];
        }
        Set<Integer> tar = Arrays.stream(nums1).boxed().collect(Collectors.toSet());
        Set<Integer> res = new HashSet<>();
        for (int i = 0; i < nums2.length; i++) {
            if (tar.contains(nums2[i])) res.add(nums2[i]);
        }
        return res.stream().mapToInt(Integer::valueOf).toArray();
    }
}
