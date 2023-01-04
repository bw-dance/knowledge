package likou.面试.华为od;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt(); //矩阵行数
        int n = scanner.nextInt(); //矩阵列数
        char[][] matrix = new char[m][n]; //矩阵
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = scanner.next().charAt(0);
            }
        }
        //定义 dp 数组，dp[i][j] 表示 (i, j) 位置所在的最大单入口空闲区域大小
        int[][] dp = new int[m][n];

        //从矩阵最后一行开始往上遍历
        for (int i = m-1; i >= 0; i--) {
            for (int j = n-1; j >= 0; j--) {
                if (matrix[i][j] == 'O') {
                    //(i, j) 位置可以做为入口
                    dp[i][j] = 1;
                    //往下搜索
                    if (i < m-1) {
                        dp[i][j] += dp[i+1][j];
                    }
                    //往右搜索
                    if (j < n-1) {
                        dp[i][j] += dp[i][j+1];
                    }
                }
            }
        }

        //找到最大单入口空闲区域
        int maxArea = 0;
        int minI = -1;
        int minJ = -1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 'O') {
                    //找到入口
                    int area = dp[i][j];
                    //往上搜索
                    for (int k = i-1; k >= 0; k--) {
                        if (matrix[k][j] == 'O') {
                            area += dp[k][j];
                        } else {
                            //遇到障碍，退出循环
                            break;
                        }
                    }
                    //往左搜索
                    for (int k = j-1; k >= 0; k--) {
                        if (matrix[i][k] == 'O') {
                            area += dp[i][k];
                        } else {
                            //遇到障碍，退出循环
                            break;
                        }
                    }
                    //更新最大单入口空闲区域
                    if (area > maxArea) {
                        maxArea = area;
                        minI = i;
                        minJ = j;
                    }
                }
            }
        }
        //输出结果
        if (maxArea == 0) {
            System.out.println("null");
        } else if (minI == 0 || minJ == 0 || minI == m-1 || minJ == n-1) {
            System.out.println(minI + " " + minJ + " " + maxArea);
        } else {
            System.out.println(maxArea);
        }
    }
}
