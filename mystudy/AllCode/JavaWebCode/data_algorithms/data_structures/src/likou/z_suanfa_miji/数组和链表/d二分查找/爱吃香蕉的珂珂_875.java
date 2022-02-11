package likou.z_suanfa_miji.数组和链表.d二分查找;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname 爱吃香蕉的珂珂_875
 * @Description TODO
 * @Date 2022/2/9 20:00
 * @Created by zhq
 */
public class 爱吃香蕉的珂珂_875 {
    public static void main(String[] args) {
        Solution06 solution06 = new Solution06();
        System.out.println(solution06.minEatingSpeed(new int[]{3, 6, 7, 11}, 8));
        System.out.println(solution06.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5));
        System.out.println(solution06.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6));
        System.out.println(solution06.minEatingSpeed(new int[]{312884470}, 312884469));
    }
}

class Solution06 {
    public int minEatingSpeed(int[] piles, int h) {
        //最慢速度
        int min = 1;
        //最快速度
        int max = Arrays.stream(piles).max().getAsInt();
        if (piles.length == h) return max;
        //取中间速度
        while (min <= max) {
            int speed = min + (max - min) / 2;
            if (eatBananaUseSpeed(speed, piles) > h) {
                min = speed + 1;
            } else {
                max = speed - 1;
            }
        }
        //返回最小min，因为题目要求返回最小速度。
        return min;
    }

    public int eatBananaUseSpeed(int speed, int[] piles) {
        //当前速度所用时间
        int time = 0;
        for (int i = 0; i < piles.length; i++) {
            time += piles[i] / speed + (piles[i] % speed == 0 ? 0 : 1);
        }
        return time;
    }
}

class Solution07 {
    public int minEatingSpeed(int[] piles, int h) {
        //最慢速度
        int min = 1;
        //最快速度
        int max = Arrays.stream(piles).max().getAsInt();
        //速度
        int speed = 0;
        if (piles.length == h) return max;
        List<Integer> list = new ArrayList<>();
        //取中间速度
        while (min <= max) {
            speed = min + (max - min) / 2;
            if (eatBananaUseSpeed(speed, piles) == h) {
                list.add(speed);
                max = speed - 1;
            } else if (eatBananaUseSpeed(speed, piles) > h) {
                min = speed + 1;
            } else if (eatBananaUseSpeed(speed, piles) < h) {
                max = speed - 1;
            }
        }
        if (list.size() != 0) {
            int minSpeed = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i) < minSpeed) minSpeed = list.get(i);
            }
            return minSpeed;
        }
        return min;
    }

    public int eatBananaUseSpeed(int speed, int[] piles) {
        //当前速度所用时间
        int time = 0;
        for (int i = 0; i < piles.length; i++) {
            time += piles[i] / speed + (piles[i] % speed == 0 ? 0 : 1);
        }
        return time;
    }
}
