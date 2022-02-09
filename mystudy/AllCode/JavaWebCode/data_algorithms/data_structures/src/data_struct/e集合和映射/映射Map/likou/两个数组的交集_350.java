package data_struct.e集合和映射.映射Map.likou;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname 两个数组的交集_349
 * @Description TODO
 * @Date 2022/2/8 22:33
 * @Created by zhq
 */
public class 两个数组的交集_350 {
    public static void main(String[] args) {
        Solution02 solution02 = new Solution02();
        int[] arr1 = {4, 9, 5};
        int[] arr2 = {9, 4, 9, 8, 4};
        solution02.intersection(arr1, arr2);


        Solution03 solution03 = new Solution03();
        solution03.intersect(arr1, arr2);
    }
}

class Solution02 {
    public int[] intersection(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map1 = new HashMap<>();
        Map<Integer, Integer> map2 = new HashMap<>();
        for (int i = 0; i < nums1.length; i++)
            map1.put(nums1[i], map1.getOrDefault(nums1[i], 0) + 1);

        for (int i = 0; i < nums2.length; i++)
            map2.put(nums2[i], map2.getOrDefault(nums2[i], 0) + 1);

        List<Integer> collect = map1.keySet().stream().filter(map2::containsKey).collect(Collectors.toList());
        List<Integer> list = new ArrayList<>();
        for (Integer item : collect
        ) {
            //相交元素的数量
            Integer num = map1.get(item) <= map2.get(item) ? map1.get(item) : map2.get(item);
            for (int i = 0; i < num; i++)
                list.add(item);
        }
        return list.stream().mapToInt(Integer::valueOf).toArray();
    }
}

class Solution03 {
    public int[] intersect(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int index1 = 0;
        int index2 = 0;
        List<Integer> res = new ArrayList<>();
        while (index1 < nums1.length && index2 < nums2.length) {
            if (nums1[index1] < nums2[index2])
                index1++;
            else if (nums1[index1] > nums2[index2])
                index2++;
            else if (nums1[index1] == nums2[index2]) {
                res.add(nums1[index1]);
                index1++;
                index2++;
            }
        }
        return res.stream().mapToInt(Integer::valueOf).toArray();
    }
}
