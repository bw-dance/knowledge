package JVM.内存划分.类加载.类加载器;

import java.sql.DriverManager;

/**
 * @Classname JDBC
 * @Description TODO
 * @Date 2022/2/15 9:19
 * @Created by zhq
 */
public class JDBC {
    public static void main(String[] args) {
        System.out.println(DriverManager.class.getClassLoader());//null  因为java.sql.DriverManager在JAVA_HOME/jre/lib下，使用启动类加载器。
    }
}
