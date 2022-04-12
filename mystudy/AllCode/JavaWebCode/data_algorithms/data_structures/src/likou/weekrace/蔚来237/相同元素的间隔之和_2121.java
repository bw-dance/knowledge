package likou.weekrace.蔚来237;

import java.util.*;

/**
 * @Classname 相同元素的间隔之和_2121
 * @Description TODO
 * @Date 2022/2/16 8:43
 * @Created by zhq
 */
public class 相同元素的间隔之和_2121 {
    public static void main(String[] args) {
        Solution02 solution = new Solution02();
        System.out.println(Arrays.toString(solution.getDistances(new int[]{2,1,3,1,2,3,3})));
    }
}

//超时
class Solution01 {
    public long[] getDistances(int[] arr) {
        if (arr.length <= 1) return new long[1];
        int length = arr.length;
        int fast;
        long[] res = new long[length];
        for (int i = 0; i < length; i++) {
            fast = i + 1;
            long sum = 0;
            while (i != fast) {
                //如果fast==length，说明快指针到数组末尾，从头开始计算 fast = 0
                if (fast == length) {
                    //如果i==0的情况下,此时如果fast==0，往下执行程序的时候，fast会+1，这样i和slow永远无法在while的判断中相等，死循环。
                    if (i == 0) break;
                    fast = 0;
                }
                if (arr[i] == arr[fast]) {
                    sum += i - fast < 0 ? fast - i : i - fast;
                }
                fast++;
            }
            res[i] = sum;
        }
        return res;
    }
}

class Solution02 {
    public long[] getDistances(int[] arr) {
        if (arr.length <= 1) return new long[1];
        //使用map记录每个元素的坐标
        Map<Integer, List<Integer>> arrMap = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            List<Integer> list = arrMap.getOrDefault(arr[i], new ArrayList<>());
            list.add(i);
            arrMap.put(arr[i], list);
        }
        //创建返回元素的数组
        long[] res = new long[arr.length];
        //进行下标比较
        for (Integer key : arrMap.keySet()) {
            List<Integer> list = arrMap.get(key);
            for (int i = 0; i < list.size(); i++) {
                int sum = 0;
                int cur = list.get(i);
                for (int j = 0; j < list.size(); j++) {
                    int index = list.get(j);
                    if (cur != index) {
                        sum += cur - index < 0 ?index - cur : cur -index;
                    }
                }
                res[list.get(i)]=sum;
            }
        }
        return res;
    }
}
