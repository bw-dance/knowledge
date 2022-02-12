package JVM.内存划分.堆;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname Demo1_5
 * @Description TODO
 * @Date 2022/2/11 20:02
 * @Created by zhq
 */
public class Demo1_5 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        String a = new String("a");
        while (true) {
            a = a + a;
            list.add(a);
        }
    }
}
