package likou.z_suanfa_miji.数组和链表.c滑动窗口;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname 滑动窗口
 * @Description TODO 数组解法
 * @Date 2022/1/12 11:06
 * @Created by zhq
 */
public class 最小覆盖字串_76 {
    public static void main(String[] args) {
        Solution01 solution = new Solution01();
        System.out.println(solution.minWindow("aa", "aa"));
    }
}
class Solution01 {
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";
        //1.记录需要字串的数量
        Map<Character, Integer> tMap = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            tMap.put(t.charAt(i), tMap.getOrDefault(t.charAt(i), 0) + 1);
        }
        //2.窗口左边界，窗口右边界，窗口的起始，窗口长度
        int left = 0, right = 0, winStart = 0, windowLength = Integer.MAX_VALUE;
        //3.临界条件，右侧指针到尾
        while (right < s.length()) {
            char c = s.charAt(right);
            if (tMap.containsKey(c)) {
                //如果map中包含此索引，则说明当前位置含字串的元素，所需的字串元素数量-1.
                tMap.put(c, tMap.getOrDefault(c, 0) - 1);
            }
            //当当前串中包含字串时。判断能否缩短当前字串，如果移动左指针实现。
            while (checkNum(tMap) == true) {
                if (right - left < windowLength) {
                    winStart = left;
                    windowLength = right - left+1;
                }
                char leftNum = s.charAt(left);
                if (tMap.containsKey(leftNum)) {
                    //如果删除了字串的一个元素，但是这个元素时要求的字串中包含的元素，则需求的字串元素数量+1
                    tMap.put(leftNum, tMap.getOrDefault(leftNum, 0) + 1);
                }
                left++;
            }
            right++;
        }
        //防止出现主串和字串长度一样，但是又不符合条件。  如测试案例  "aa"  "bb"
        return windowLength == Integer.MAX_VALUE ? "" : s.substring(winStart, winStart + windowLength);
    }

    public boolean checkNum(Map<Character, Integer> tMap) {
        boolean flag = true;
        for (Character key : tMap.keySet()) {
            if (tMap.get(key) > 0) flag = false;
        }
        return flag;

    }
    
}