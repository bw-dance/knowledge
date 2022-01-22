package JVM.内存划分.常量池;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2022/1/21 21:04
 * @Created by zhq
 */
public class Demo1_26 {
    static int _1Gb = 1024 * 1024 * 1024;
    public static void main(String[] args) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(_1Gb);//分配一个G的内存
        System.out.println("分配完毕...");
        System.in.read();//按回车可继续往下执行。
        System.out.println("开始释放...");
        byteBuffer = null;
        System.gc(); // 显式的垃圾回收，Full GC
        System.in.read();
    }
}