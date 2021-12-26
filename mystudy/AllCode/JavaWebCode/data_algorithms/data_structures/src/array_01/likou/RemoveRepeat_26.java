package array_01.likou;

/**
 * @Classname RemoveRepeat_26
 * @Description TODO
 * @Date 2021/12/26 15:30
 * @Created by zhq
 */
public class RemoveRepeat_26 {
    public static void main(String[] args) {
        int[] nums = {1,1,1,2};
        int length = removeDuplicates(nums);
        for (int i = 0; i < length; i++) {
            System.out.println(nums[i]);
        }
    }

    public static int removeDuplicates(int[] nums) {
        int pointer = 0;
        int var = 0;
        int length = 1;
        int repeat = 0;
        for (int i = pointer + 1; i < nums.length-repeat; i++) {
            //判断元素是否重复
            if (nums[pointer] == nums[i]) {
                //重复元素的个数
                var++;
                if (var == nums.length - 1)
                    //全是一个数
                    return 1;
            } else {
                length++;
                for (int j = pointer; j < nums.length - var; j++) {
                    nums[j] = nums[j + var];
                }
                pointer++;
                repeat+=var;
                i = pointer;
                var = 0;
            }
        }
        return length;
    }
}
