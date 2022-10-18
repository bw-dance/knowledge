package data_struct.e集合和映射.去重集合.likou;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class 集合求交集_349 {
    public static void main(String[] args) {

    }

}
class SolutionDistinct {
    public int[] intersection(int[] nums1, int[] nums2) {
        if(nums1.length==0||nums2.length ==0) return new int[0];
        //boxed：将int转换为Integer
        Set<Integer> collect = Arrays.stream(nums1).boxed().collect(Collectors.toSet());
        return Arrays.stream(nums2).distinct().filter(item-> collect.contains(item)).toArray();
    }
}