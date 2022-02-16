package likou.z_suanfa_miji.a数组和链表.d二分查找;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname 在D天内送达包裹的能力_1011
 * @Description TODO
 * @Date 2022/2/10 8:17
 * @Created by zhq
 */
public class 在D天内送达包裹的能力_1011 {
    public static void main(String[] args) {
        Solution09 solution08 = new Solution09();
        System.out.println(solution08.shipWithinDays(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5));
        System.out.println(solution08.shipWithinDays(new int[]{3, 2, 2, 4, 1, 4}, 3));
        System.out.println(solution08.shipWithinDays(new int[]{1, 2, 3, 1, 1}, 4));
    }
}

//方法一
class Solution08 {
    public int shipWithinDays(int[] weights, int days) {
        if (weights.length == 1) return weights[0];
        //最小重量
        int min = 1;
        //最大重量:所有货物一次运完
        int max = Arrays.stream(weights).sum();
        List<Integer> list = new ArrayList<>();
        while (min <= max) {
            int weight = min + (max - min) / 2;
            int countDays = getDaysByWeight(weights, weight);
            if (countDays > days) {
                //说明每天最低运载能力太小
                min = weight + 1;
            } else if (countDays < days) {
                max = weight - 1;
            } else if (countDays == days) {
                list.add(weight);
                max = weight - 1;
            }
        }
        return list.size() == 0 ? min : list.stream().min(Integer::compareTo).get();
    }

    public int getDaysByWeight(int[] weights, int weight) {
        int days = 0;
        int sumWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            //如果某个包裹大于最小载重，直接返回
            if (weights[i] > weight) {
                days = Integer.MAX_VALUE;
                return days;
            }
            sumWeight += weights[i];
            if (sumWeight == weight) {
                days += 1;
                sumWeight = 0;
            } else if (sumWeight > weight) {
                days += 1;
                sumWeight = weights[i];
            } else if (sumWeight < weight) continue;
        }
        return sumWeight == 0 ? days : days + 1;
    }
}

//方法二：
class Solution09 {
    public int shipWithinDays(int[] weights, int days) {
        if (weights.length == 1) return weights[0];
        //最小重量
        int min = 1;
        //最大重量:所有货物一次运完
        int max = Arrays.stream(weights).sum();
        while (min <= max) {
            int weight = min + (max - min) / 2;
            int countDays = getDaysByWeight(weights, weight);
            if (countDays > days) {
                //说明每天最低运载能力太小
                min = weight + 1;
            } else  {
                max = weight - 1;
            }
        }
        return min;
    }

    public int getDaysByWeight(int[] weights, int weight) {
        int days = 0;
        int sumWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            //如果某个包裹大于最小载重，直接返回
            if (weights[i] > weight) {
                days = Integer.MAX_VALUE;
                return days;
            }
            sumWeight += weights[i];
            if (sumWeight == weight) {
                days += 1;
                sumWeight = 0;
            } else if (sumWeight > weight) {
                days += 1;
                sumWeight = weights[i];
            } else if (sumWeight < weight) continue;
        }
        return sumWeight == 0 ? days : days + 1;
    }
}