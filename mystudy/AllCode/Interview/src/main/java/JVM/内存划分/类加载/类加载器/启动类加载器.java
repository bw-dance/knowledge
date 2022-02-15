package JVM.内存划分.类加载.类加载器;

/**
 * @Classname 类加载器
 * @Description TODO
 * @Date 2022/2/15 8:55
 * @Created by zhq
 */
public class 启动类加载器 {

    public static void main(String[] args) throws ClassNotFoundException {
        //JAVA_HOME/jre/lib下
        System.out.println(String.class.getClassLoader()); //null
        //JAVA_HOME/jre/lib/ext下
        System.out.println(com.sun.crypto.provider.DESKeyFactory.class.getClassLoader()); //sun.misc.Launcher$ExtClassLoader@6e0be858
        // 应用类加载器
        Class<?> aClass = Class.forName("JVM.内存划分.类加载.类加载器.启动类加载器");
        System.out.println(aClass.getClassLoader());//sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(ClassLoader.getSystemClassLoader().getClass().getName());//sun.misc.Launcher$AppClassLoader
    }

}
