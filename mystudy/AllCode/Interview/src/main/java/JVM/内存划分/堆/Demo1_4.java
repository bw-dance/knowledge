package JVM.内存划分.堆;

/**
 * @Classname 堆溢出案例1
 * @Description TODO
 * @Date 2022/1/21 10:48
 * @Created by zhq
 */
public class Demo1_4 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("1...");
        Thread.sleep(30000);
        byte[] array = new byte[1024 * 1024 * 10]; // 10 Mb
        System.out.println("2...");
        Thread.sleep(20000);
        array = null;
        System.gc();
        System.out.println("3...");
        Thread.sleep(1000000L);
    }
}