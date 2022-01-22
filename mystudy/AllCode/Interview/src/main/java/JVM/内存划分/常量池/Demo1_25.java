package JVM.内存划分.常量池;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2022/1/21 20:14
 * @Created by zhq
 */
public class Demo1_25 {
    public static void main(String[] args) {
        int i = 0;
        try {
            for (int j = 0; j < 100000; j++) {
                String.valueOf(j).intern();
                i++;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            System.out.println(i);
        }
    }
}