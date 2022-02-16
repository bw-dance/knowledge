package likou.z_suanfa_miji.a数组和链表.a前缀和;

/**
 * @Classname f
 * @Description TODO
 * @Date 2022/1/10 14:55
 * @Created by zhq
 */

public class 二维区域和检索_304 {
    public static void main(String[] args) {
        int[][] matrix = {{3, 0, 1, 4, 2}, {5, 6, 3, 2, 1}, {1, 2, 0, 1, 5}, {4, 1, 0, 1, 7}, {1, 0, 3, 0, 5}};
        NumMatrix02 numArray = new NumMatrix02(matrix);
        System.out.println(numArray.sumRegion(1, 1, 3, 3));
    }
}

//方式一：前缀和
class NumMatrix01 {
    private int[][] preMatrix;

    public NumMatrix01(int[][] matrix) {
        preMatrix = new int[matrix.length + 1][matrix[0].length + 1];
        for (int i = 1; i < preMatrix.length; i++) {
            for (int j = 1; j < preMatrix[0].length; j++) {
                preMatrix[i][j] = preMatrix[i][j - 1] + matrix[i - 1][j - 1];
            }
        }
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        int result = 0;
        for (int i = row1; i <= row2; i++) {
            result += preMatrix[i + 1][col2 + 1] - preMatrix[i + 1][col1];
        }
        return result;
    }
}

//方式二：前缀和
class NumMatrix02 {
    private int[][] preMatrix;

    public NumMatrix02(int[][] nums) {
        preMatrix = new int[nums.length + 1][nums[0].length + 1];
        for (int i = 1; i < preMatrix.length; i++) {
            for (int j = 1; j < preMatrix[0].length; j++) {
                preMatrix[i][j] = preMatrix[i][j - 1]
                        + preMatrix[i - 1][j]
                        - preMatrix[i - 1][j - 1]
                        + nums[i - 1][j - 1];
            }
        }
    }


    public int sumRegion(int row1, int col1, int row2, int col2) {
        return preMatrix[row2 + 1][col2 + 1]
                - preMatrix[row2 + 1][col1]
                - preMatrix[row1][col2 + 1]
                + preMatrix[row1][col1];
    }
}