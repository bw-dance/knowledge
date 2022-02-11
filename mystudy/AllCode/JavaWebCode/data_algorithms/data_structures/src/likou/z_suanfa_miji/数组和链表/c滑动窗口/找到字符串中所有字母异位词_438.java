package likou.z_suanfa_miji.数组和链表.c滑动窗口;

import java.util.*;

/**
 * @Classname 找到字符串中所有字母异位词_438
 * @Description TODO
 * @Date 2022/1/13 11:27
 * @Created by zhq
 */
public class 找到字符串中所有字母异位词_438 {
    public static void main(String[] args) {
        Solution04 solution04 = new Solution04();
        System.out.println(solution04.findAnagrams("bb", "b"));
    }
}

class Solution04 {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> resList = new ArrayList<>();
        if (p.length() > s.length()) return resList;
        Map<Character, Integer> sMap = new HashMap<>();
        Map<Character, Integer> pMap = new HashMap<>();
        int sLen = s.length(), pLen = p.length(), index = 0;
        for (int i = 0; i < pLen; i++, index++) {
            sMap.put(s.charAt(i), sMap.getOrDefault(s.charAt(i), 0) + 1);
            pMap.put(p.charAt(i), pMap.getOrDefault(p.charAt(i), 0) + 1);
        }
        while (index < sLen) {
            int right = index;
            int left = index - pLen;
            if (sMap.equals(pMap)) resList.add(left);
            sMap.put(s.charAt(right), sMap.getOrDefault(s.charAt(right), 0) + 1);
            sMap.put(s.charAt(left), sMap.getOrDefault(s.charAt(left), 0) - 1);
            if (sMap.get(s.charAt(left)) == 0) sMap.remove(s.charAt(left));
            index++;
        }
        if (sMap.equals(pMap)) resList.add(index-pLen);//解决案例：“b”，“b”  或者 “bb”，“b”
        return resList;
    }
}
