package JVM.内存划分.常量池;

/**
 * @Classname Demo1_22
 * @Description TODO
 * @Date 2022/1/21 14:47
 * @Created by zhq
 */
public class Demo1_22 {
    public static void main(String[] args) {
        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";
        String s4  = s1+s2;
        String s5  =  "a"+"b";
        System.out.println(s3==s5);
    }
}
