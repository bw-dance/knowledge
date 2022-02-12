package JVM.内存划分.分享会;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname d
 * @Description TODO
 * @Date 2022/2/12 10:03
 * @Created by zhq
 */
public class DirectMemory {

    static int _100MB = 1024 * 1024 * 100;

    public static void main(String[] args) {
        List<ByteBuffer> list = new ArrayList<>();
        int i = 0;
        try {
            while (true) {
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(_100MB);
                list.add(byteBuffer);
                i++;
            }
        } finally {
            System.out.println(i);
        }
    }

}
