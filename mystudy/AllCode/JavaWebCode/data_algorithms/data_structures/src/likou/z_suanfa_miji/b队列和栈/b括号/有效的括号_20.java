package likou.z_suanfa_miji.b队列和栈.b括号;

import java.util.*;

/**
 * @Classname 有效的括号_20
 * @Description TODO
 * @Date 2022/2/15 20:19
 * @Created by zhq
 */
public class 有效的括号_20 {
}

class Solution {
    public boolean isValid(String s) {
        Map<Character, Character> map = new HashMap<Character, Character>() {{
            put('(', ')');
            put('[', ']');
            put('{', '}');
        }};
        Deque<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (map.containsKey(cur)) {
                stack.add(cur);
            } else if (!map.containsKey(cur)) {
                if (stack.isEmpty()) return false;
                Character last = stack.removeLast();
                if (map.get(last) != last) return false;
            }
        }
        return stack.size() == 0;
    }
}
