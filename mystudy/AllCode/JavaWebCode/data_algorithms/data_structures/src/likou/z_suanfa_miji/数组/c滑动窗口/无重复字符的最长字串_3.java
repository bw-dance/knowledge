package likou.z_suanfa_miji.数组.c滑动窗口;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Classname 无重复字符的最长字串_3
 * @Description TODO
 * @Date 2022/1/13 15:15
 * @Created by zhq
 */
public class 无重复字符的最长字串_3 {
    public static void main(String[] args) {
        Solution05 solution05 = new Solution05();
        System.out.println(solution05.lengthOfLongestSubstring("abcabcbb"));

        Solution06 solution06 = new Solution06();
        System.out.println(solution06.lengthOfLongestSubstring("abcabcbb"));
    }
}

class Solution05 {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> sSet = new HashSet<>();
        //winLength 记录窗口长度
        int left = 0, right = 0, winLength = 0;
        while (right < s.length()) {
            if (sSet.contains(s.charAt(right))) {
                int curWinLength = right - left;
                if (curWinLength > winLength) winLength = curWinLength;
                sSet.remove(s.charAt(left));
                left++;
            } else {
                sSet.add(s.charAt(right));
                right++;
            }
        }
        //为什么要使用right-left 与 winLength的比较呢？
        //1.right-left是为了防止出现
        return (right - left) > winLength ? right - left : winLength;
    }
}


class Solution06 {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> sSet = new HashSet<>();
        //winLength 记录窗口长度
        int left = 0, right = 0, winLength = 0;
        while (right < s.length()) {
            if (sSet.contains(s.charAt(right))) {
                int curWinLength = right - left;
                if (curWinLength > winLength) winLength = curWinLength;
                sSet.remove(s.charAt(left));
                left++;
            } else {
                sSet.add(s.charAt(right));
                right++;
            }
        }
        //为什么要使用right-left 与 winLength的比较呢？
        //1.right-left是为了防止出现
        return winLength;
    }
}
