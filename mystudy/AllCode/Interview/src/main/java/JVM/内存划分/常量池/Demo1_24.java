package JVM.内存划分.常量池;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2022/1/21 16:11
 * @Created by zhq
 */
public class Demo1_24 {

    //  ["a", "b","ab" ]
    public static void main(String[] args) {
//
//        String s = new String("a") + new String("b");
//
//        // 堆  new String("a")   new String("b") new String("ab")
//        String s2 = s.intern(); // 将这个字符串对象尝试放入串池，如果有则并不会放入，如果没有则放入串池， 会把串池中的对象返回
//
//        System.out.println(s2 == "ab"); //true
//        System.out.println(s == "ab"); //true

        //判断创建了几个对象  只在字符串常量池创建了一个对象
        String a = "123" + "456" + "789";

    }

}
