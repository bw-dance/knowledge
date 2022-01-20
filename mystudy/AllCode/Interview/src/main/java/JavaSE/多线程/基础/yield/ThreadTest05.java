package JavaSE.多线程.基础.yield;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2022/1/18 10:08
 * @Created by zhq
 */
public class ThreadTest05 {
    public static void main(String[] args) {
        //创建子线程
        MyThread05 myThread05 = new MyThread05();
        Thread childThread = new Thread(myThread05);
        childThread.start();
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        for (int i = 0; i <100 ; i++) {
            System.out.println(Thread.currentThread().getName()+"----"+i+"rank:"+Thread.currentThread().getPriority());
        }
    }
}

class MyThread05 implements Runnable {

    @Override
    public void run() {
        //对5取余，看是否实现线程让位
        for (int i = 0; i <100 ; i++) {
//            if (i%5==0){
                Thread.yield();
                System.out.println(Thread.currentThread().getName()+"----"+i+"-----"+"rank:"+Thread.currentThread().getPriority());
//            }
        }
    }
}