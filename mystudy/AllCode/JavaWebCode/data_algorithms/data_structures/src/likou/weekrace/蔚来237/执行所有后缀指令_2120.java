package likou.weekrace.蔚来237;

/**
 * @Classname 执行所有后缀指令_2120
 * @Description TODO
 * @Date 2022/2/15 20:58
 * @Created by zhq
 */
public class 执行所有后缀指令_2120 {
}

class Solution {
    public int[] executeInstructions(int n, int[] startPos, String s) {
        int[] res = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            int row = startPos[0];
            int column = startPos[1];
            int num = 0;
            int index = i;
            while ( index < s.length()) {
                char next = s.charAt(index);
                if (next == 'R') {
                    column++;
                } else if (next == 'L') {
                    column--;
                } else if (next == 'U') {
                    row--;
                } else if (next == 'D') {
                    row++;
                }
                if (row >= 0 && column >= 0 && row < n && column < n) {
                    num++;
                    index++;
                } else break;
            }
            res[i] = num;
        }
        return res;
    }
}