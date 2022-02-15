package JVM.内存划分.类加载.解析;

import java.io.IOException;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2022/2/14 17:25
 * @Created by zhq
 */
public class Demo3_5 {
    public static void main(String[] args) throws IOException {
//        Singleton.test(); // 打印test
//        // 只有显示地调用了getInstance才会去声明那个单例变量
//        Singleton.getInstance(); // 打印lazy holder init
        Singleton.test(); // Singleton会进行类的加载，链接，初始化。
//        Singleton.getInstance();
        System.in.read();
    }
}

class Singleton {
    public static void test() {
        System.out.println("test");
    }

    // 别的类调用不了构造方法，保证单例（可以通过反射破解）
    private Singleton() {
    }

    // 内部类中保存单例   静态内部类，可以访问外部类类的私有方法
    private static class LazyHolder {
        static final Singleton INSTANCE = new Singleton();

        static {
            System.out.println("lazy holder init");
        }
    }

    // 第一次调用 getInstance 方法，才会导致内部类加载和初始化其静态成员
    // 不调用就不会触发L啊这样Holder的加载链接初始化，保证懒加载
    public static Singleton getInstance() {
        return LazyHolder.INSTANCE;
    }
}
