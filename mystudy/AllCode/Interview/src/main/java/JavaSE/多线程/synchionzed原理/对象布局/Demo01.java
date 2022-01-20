package JavaSE.多线程.synchionzed原理.对象布局;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Classname Demo01
 * @Description TODO
 * @Date 2022/1/17 12:07
 * @Created by zhq
 */
public class Demo01 {
    public static void main(String[] args) {
        LockObj obj = new LockObj();
        System.out.println("hashcode"+obj.hashCode());
        System.out.println("16进制的hashcode:"+Integer.toHexString(obj.hashCode()));
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }
}

class LockObj {
    private int x;
}
