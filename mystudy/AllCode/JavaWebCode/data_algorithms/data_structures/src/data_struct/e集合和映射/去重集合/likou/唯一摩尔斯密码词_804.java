package data_struct.e集合和映射.去重集合.likou;

import data_struct.e集合和映射.去重集合.链表实现.BSTSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Classname 唯一摩尔斯密码词_804
 * @Description TODO
 * @Date 2022/1/27 21:59
 * @Created by zhq
 */
public class 唯一摩尔斯密码词_804 {
    public static void main(String[] args) {
//        Solution solution = new Solution();
//        String[] words = {"gin", "zen", "gig", "msg"};
//        String[] words2 = {};
//        System.out.println(solution.uniqueMorseRepresentations(words2));


        Solution02 solution = new Solution02();
        String[] words = {"gin", "zen", "gig", "msg"};
        String[] words2 = {};
        System.out.println(solution.uniqueMorseRepresentations(words2));
    }
}

class Solution {
    //方式一
    public int uniqueMorseRepresentations(String[] words) {
        //1.去重
        BSTSet bstSet = new BSTSet();
        for (String v : words)
            bstSet.add(v);
        if (bstSet.isEmpty()) return 0;
        //2.莫斯密码对照表
        String[] code = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
        //3.不重复的结果集
        BSTSet res = new BSTSet();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            char[] chars = word.toCharArray();
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < chars.length; j++) {
                str.append(code[chars[j] - 97]);
            }
            res.add(str.toString());
        }
        return res.getSize();
    }
}

class Solution02 {
    //方式一
    public int uniqueMorseRepresentations(String[] words) {
        //1.去重
        Set bstSet = new HashSet();
        for (String v : words)
            bstSet.add(v);
        if (bstSet.isEmpty()) return 0;
        //2.莫斯密码对照表
        String[] code = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
        //3.不重复的结果集
        Set res = new HashSet();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            char[] chars = word.toCharArray();
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < chars.length; j++) {
                str.append(code[chars[j] - 97]);
            }
            res.add(str.toString());
        }
        return res.size();
    }
}