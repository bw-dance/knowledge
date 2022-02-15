package JavaSE.多线程.synchionzed原理.偏向锁;


/**
 * @Classname Demo01
 * @Description TODO
 * @Date 2022/1/17 14:25
 * @Created by zhq
 */
public class Demo01 {
    public static void main(String[] args) {
        Thread mt = new MyThread();
        mt.start();

    }

}
class  MyThread extends Thread{
    static  Object obj = new Object();
    @Override
    public void run() {
        synchronized (obj){
//            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
    }
}
