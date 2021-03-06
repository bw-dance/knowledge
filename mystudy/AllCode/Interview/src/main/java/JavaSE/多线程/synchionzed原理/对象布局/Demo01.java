package JavaSE.多线程.synchionzed原理.对象布局;



/**
 * @Classname Demo01
 * @Description TODO
 * @Date 2022/1/17 12:07
 * @Created by zhq
 */
public class Demo01 {
    static int i = 0;
    static Object obj = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                synchronized (obj) {
                    i++;
                }
            }
        });

        Thread t2 = new Thread(() -> {
            // 推荐把synchronized写在for外层，这样加锁解锁只会执行一次
            synchronized (obj) {
                for (int j = 0; j < 5000; j++) {
                    i--;
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(i);
    }
}
