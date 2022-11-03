package JavaCollection.map;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyHashMap {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
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
//        String s  = new String("a")+new String("b");
//        System.out.println(s=="ab");//false s引用的是堆中的new String("ab"),ab编译后能够确定，运行的时候已放入常量池
//        String s2 = s.intern();//此时常量池已有ab，s2指向字符串常量池ab
//        System.out.println(s=="ab");//false
//        System.out.println(s2=="ab");//true

//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        executorService.submit()


//        final char[] a = {'1', '1', '1'};
//        a[0] = '0';
//        a[2] = '0';
//        System.out.println(Arrays.toString(a));
//        People p = new People();
//        System.err.println(p.str.hashCode());
//        Field field = People.class.getDeclaredField("str");
//        field.setAccessible(true);
//        field.set(p, "12345");
//        System.err.println(p.str.hashCode());


//        String str = new String("abc");
        String str ="abc";
        System.out.println(str+"的hashcode=	"+str.hashCode());
        System.out.println(str);
        Field f = str.getClass().getDeclaredField("value");
        f.setAccessible(true);
        char[] b=(char[]) f.get(str);
        b[0]='1';
        System.out.println(str+"的hashcode=	"+str.hashCode());
        System.out.println(str);



//        System.err.println(p.str);// 12345 修改String值成功
//        System.out.println(p.str.hashCode());
//        p.str="123123123";
//        System.out.println(p.str.hashCode());

    }
}

class People {
    String str = "123";
}
