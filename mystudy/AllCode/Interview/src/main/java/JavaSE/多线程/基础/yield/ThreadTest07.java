package JavaSE.多线程.基础.yield;

/**
 * @Classname ee
 * @Description TODO
 * @Date 2022/1/18 10:35
 * @Created by zhq
 */
public class ThreadTest07 {
    public static void main(String[] args) throws InterruptedException {
        //创建子线程
//        MyThread07 myThread07 = new MyThread07();
//        Thread childThread = new Thread(myThread07);
//        childThread.start();
//        try {
//            //合并线程，合并后childThread执行完才会执行主线程
//            childThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < 100; i++) {
//            System.out.println(Thread.currentThread().getName() + "----" + i);
//        }

        Thread t1 = new Thread(() -> {

            for (int i = 1; i < 4; i++) {

                System.out.println("线程A ： " + i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 1; i < 4; i++) {
                System.out.println("线程B ： " + i);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("爬取结束");
    }
}

class MyThread07 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "----" + i);
            }
        }
    }
}
