package JVM.内存划分.JMM;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Classname ThreadTest
 * @Description TODO
 * @Date 2022/2/15 15:38
 * @Created by zhq
 */
public class ThreadTest {
    static  boolean a = true;
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (a) {
               System.out.println(123456);
                int b  = 100;
            }
        }).start();
        Thread.sleep(1000L);
        a = false;
    }
}
