package JavaSE.多线程.synchionzed原理.cas;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname 无cas操作
 * @Description TODO
 * @Date 2022/1/16 16:22
 * @Created by zhq
 */
public class 无cas操作 {
    //1.定义一个共享变量num
    private static int num = 0;

    public static void main(String[] args) throws InterruptedException {
        //2.对num进行1000次的++操作
        Runnable increment = () -> {
            for (int i = 0; i < 1000; i++) {
                num++;
            }
        };
        List<Thread> list = new ArrayList<>();
        //3.使用5个线程来运行
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(increment);
            t.start();
            list.add(t);
        }
        for (Thread t:list
        ) {
            t.join();
        }
        //最终结果<5000
        System.out.println(num);
    }
}
