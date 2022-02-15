package JVM.内存划分.JMM;

import java.lang.reflect.Parameter;

/**
 * @Classname ThreadTest
 * @Description TODO
 * @Date 2022/2/15 15:38
 * @Created by zhq
 */
public class ThreadTest2 {
    int num = 0;
    boolean ready = false;

    public static void main(String[] args) {
        new Thread(()->{

        }).start();
    }
}
