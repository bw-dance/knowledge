package cn.chasing.jvm.t3.bytecode;

/**
 * @Classname Demo3_2
 * @Description TODO
 * @Date 2022/1/27 10:41
 * @Created by zhq
 */
public class Demo3_2 {
    public static void main(String[] args) {
        int a = 10;
        int b = a++ + ++a + a--;
        System.out.println(a);
        System.out.println(b);
    }
}
