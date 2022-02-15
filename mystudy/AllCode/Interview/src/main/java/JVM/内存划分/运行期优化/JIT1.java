package JVM.内存划分.运行期优化;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2022/2/15 9:51
 * @Created by zhq
 */
public class JIT1 {
    public static void main(String[] args) {
        for (int i = 0; i < 200; i++) {
            long start = System.nanoTime();
            for (int j = 0; j < 1000; j++) {
                new Object();
            }
            long end = System.nanoTime();
            System.out.printf("%d\t%d\n", i, (end - start));
        }
    }
}