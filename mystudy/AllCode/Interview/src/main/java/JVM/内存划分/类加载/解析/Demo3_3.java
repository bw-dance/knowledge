package JVM.内存划分.类加载.解析;

import java.io.IOException;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2022/2/14 16:48
 * @Created by zhq
 */
// 解析的含义
public class Demo3_3 {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
//        ClassLoader classLoader = Demo3_3.class.getClassLoader();
//        // loadClass 方法不会导致类的解析和初始化，只会加载（类的字节码载入方法区）
//        Class<?> c = classLoader.loadClass("JVM.内存划分.类加载.解析.C");
//        // new C() 加载、解析、初始化
       C c = new C();
        System.in.read();
    }
}

class C {
    D d = new D();
}

class D {

}