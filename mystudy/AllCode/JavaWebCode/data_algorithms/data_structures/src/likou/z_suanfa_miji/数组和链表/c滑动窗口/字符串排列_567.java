package likou.z_suanfa_miji.数组和链表.c滑动窗口;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname 字符串排列_567
 * @Description TODO 数组解法
 * @Date 2022/1/12 16:41
 * @Created by zhq
 */
public class 字符串排列_567 {
    public static void main(String[] args) {
        Solution02 solution02 = new Solution02();
        System.out.println(solution02.checkInclusion("ab", "bpaboapb"));
        Solution03 solution03 = new Solution03();
        System.out.println(solution03.checkInclusion("ab", "bpaboapb"));
    }
}

class Solution02 {
    public boolean checkInclusion(String s1, String s2) {
        int l1Len = s1.length();
        int l2Len = s2.length();
        //目标子串长度大于主串长度
        if (l1Len > l2Len) return false;
        //记录目标子串所需各个元素的数量
        Map<Character, Integer> s1Map = new HashMap<>();
        for (int i = 0; i < l1Len; i++)
            s1Map.put(s1.charAt(i), s1Map.getOrDefault(s1.charAt(i), 0) + 1);
        Map<Character, Integer> s2Map = new HashMap<>();
        int left = 0, right = 0;
        while (right < l2Len) {
            //往窗口中添加元素
            s2Map.put(s2.charAt(right), s2Map.getOrDefault(s2.charAt(right), 0) + 1);
            //窗口大小满足字串的长度
            while ((right - left + 1) == l1Len) {
                //当两个map相同时，达到目的
                if (s1Map.equals(s2Map)) return true;
                //当right+1==length时，到了最后s2的最后一个字符，之后的right++操作会造成越界
                if (right + 1 < l2Len) {
                    right++;
                    //窗口向右活动一个空间
                    //添加新元素的数量到窗口
                    s2Map.put(s2.charAt(right), s2Map.getOrDefault(s2.charAt(right), 0) + 1);
                    //减去旧窗口中元素的数量
                    s2Map.put(s2.charAt(left), s2Map.getOrDefault(s2.charAt(left), 0) - 1);
                    //移除窗口中不再使用的元素
                    if (s2Map.get(s2.charAt(left)) == 0) s2Map.remove(s2.charAt(left));
                    //右指针偏移
                    left++;
                } else {
                    //如果到了最后一个字符，还没有满足要求的窗口，则不成立
                    return false;
                }
            }
            right++;
        }
        return false;
    }
}


class Solution03 {
    public boolean checkInclusion(String s1, String s2) {
        int l1Len = s1.length();
        int l2Len = s2.length();
        //目标子串长度大于主串长度
        if (l1Len > l2Len) return false;
        //记录目标子串所需各个元素的数量
        Map<Character, Integer> s1Map = new HashMap<>();
        for (int i = 0; i < l1Len; i++)
            s1Map.put(s1.charAt(i), s1Map.getOrDefault(s1.charAt(i), 0) + 1);
        Map<Character, Integer> s2Map = new HashMap<>();
        //维护一个index，1：窗口扩展时，下一个元素的位置。2：不要Solution02中对if (right + 1 < l2Len) {}的判断条件
        int index = 0;
        //初始化窗口中元素
        for (int i = 0; i < l1Len; i++) {
            s2Map.put(s2.charAt(i), s2Map.getOrDefault(s2.charAt(i), 0) + 1);
            index++;
        }
        while (index < l2Len) {
            //当两个map相同时，达到目的
            if (s1Map.equals(s2Map)) return true;
            int right = index;
            int left = index - l1Len;
            //窗口向右活动一个空间
            //添加新元素的数量到窗口
            s2Map.put(s2.charAt(right), s2Map.getOrDefault(s2.charAt(right), 0) + 1);
            //减去旧窗口中元素的数量
            s2Map.put(s2.charAt(left), s2Map.getOrDefault(s2.charAt(left), 0) - 1);
            //移除窗口中不再使用的元素
            if (s2Map.get(s2.charAt(left)) == 0) s2Map.remove(s2.charAt(left));
            //右指针偏移
            index++;
        }
        return s1Map.equals(s2Map);
    }
}
