package JavaCollection.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StudyHashMap {
    public static void main(String[] args) {
//        Map<String,String> map = new HashMap<>();
//        map.put("1","张三");
//        map.put("1","李四");
//        System.out.println(map.get("2"));
//        map.remove("1");
//        System.out.println(Integer.MAX_VALUE);
//        ConcurrentHashMap map1 = new ConcurrentHashMap();
//        System.out.println(map1.contains(123));
//        String a = new String("1")+new String("2");
//        System.out.println(a=="12");
//        String intern = a.intern();
//        System.out.println(intern=="12");
//        System.out.println(a=="12");
        String s  = new String("a")+new String("b");
        System.out.println(s=="ab");//false s引用的是堆中的new String("ab"),ab编译后能够确定，运行的时候已放入常量池
        String s2 = s.intern();//此时常量池已有ab，s2指向字符串常量池ab
        System.out.println(s=="ab");//false
        System.out.println(s2=="ab");//true


    }
}
