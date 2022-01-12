package likou.z_suanfa_miji.数组.b区间差;

/**
 * @Classname 区间加法_370
 * @Description TODO
 * @Date 2022/1/11 16:53
 * @Created by zhq
 */
public class 区间加法_370 {
}

//差分数组模板
class Solution {
    //差分数组
    private int[] diff;

    //初始化一个数组，区间操作在此数组上进行
    public void solution(int[] nums) {
        diff = new int[nums.length];
        diff[0] = nums[0];
        //构建差分数组
        for (int i = 1; i < diff.length; i++)
            diff[i] = nums[i] - nums[i - 1];
    }

    public void increment(int i, int j, int val) {
        diff[i] += val;
        //如果j+1==diff.length,说明j是数组的最后一个元素，则不需要再减去val了
        if (j + 1 < diff.length) diff[j + 1] -= val;
    }

    //结果返回
    public int[] result() {
        int[] res = new int[diff.length];
        for (int i = 1; i < res.length; i++)
            res[i] = res[i - 1] + diff[i];
        return res;
    }

    //业务处理
    public int[] getModifiedArray(int length, int[][] updates) {
        int[] nums = new int[length];
        //形成差分数组
        solution(nums);
        for (int l = 0; l < updates.length; l++) {
            int i = updates[l][0];
            int j = updates[l][1];
            int val = updates[l][2];
            //进行累加
            increment(i, j, val);
        }
        //返回结果
        return result();
    }
}


