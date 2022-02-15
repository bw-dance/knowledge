package JVM.内存划分.JMM;

/**
 * @Classname f
 * @Description TODO
 * @Date 2022/2/15 15:14
 * @Created by zhq
 */
public class f {
    static int i = 0;
    static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                i++;
            }
        });

        Thread t2 = new Thread(() -> {
            // 推荐把synchronized写在for外层，这样加锁解锁只会执行一次
            for (int j = 0; j < 5000; j++) {
                i--;
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(i);
    }
}
