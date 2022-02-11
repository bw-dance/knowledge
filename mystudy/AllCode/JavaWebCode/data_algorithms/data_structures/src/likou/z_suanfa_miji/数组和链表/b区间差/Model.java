package likou.z_suanfa_miji.数组和链表.b区间差;

/**
 * @Classname Main
 * @Description TODO
 * @Date 2022/1/11 16:16
 * @Created by zhq
 */
public class Model {

    public static void main(String[] args) {
        //需求：将nums的索引1-3的元素值都+1
        int[] nums = {8, 2, 6, 3, 1};
        //方法一：O（N）
        for (int i = 1; i <= 3; i++) {
            nums[i] += 1;
        }
        //方法二：使用差分数组
        //1.将原数组转换为差分数组   即diff的某一项diff[i]，对应着nums的这一项nums[i]减去nums这一项他前一项的值nums[i-1]
        int[] diff = new int[nums.length];
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
        //2.通过差分数组反推原数组。
        int[] res = new int[diff.length];
        res[0] = diff[0];
        for (int i = 1; i < res.length; i++) {
            res[i] = res[i - 1] + diff[i];
        }
        //3.将nums的索引1-3的元素值都+1  我们只需要将diff1位置的元素+1  diff 3+1位置的元素-1即可。
        //详情见Difference


    }
}

//差分数组模板
class Difference {
    //差分数组
    private int[] diff;

    //初始化一个数组，区间操作在此数组上进行
    public void proDiff(int[] nums) {
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

    public int[] result() {
        int[] res = new int[diff.length];
        for (int i = 1; i < res.length; i++)
            res[i] = res[i - 1] + diff[i];
        return res;
    }

    //业务处理
    public int[] getModifiedArray(int length, int i, int j, int val) {
        int[] nums = new int[length];
        //形成差分数组
        proDiff(nums);
        //进行累加
        increment(i, j, val);
        //返回结果
        return result();
    }
}
