package cn.chasing.jvm.t3.bytecode;

import java.util.Arrays;

/**
 * @Classname f
 * @Description TODO
 * @Date 2022/2/9 16:35
 * @Created by zhq
 */
public class Candy4 {
    public static void foo(String... args) {
        String[] array = args; // 直接赋值
        System.out.println(Arrays.toString(array));
    }

    public static void main(String[] args) {
        foo("hello", "world");
        foo();
    }
}