package JavaCollection.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StudyHashMap {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("1","张三");
        map.put("1","李四");
        System.out.println(map.get("2"));
        map.remove("1");
        System.out.println(Integer.MAX_VALUE);
        ConcurrentHashMap map1 = new ConcurrentHashMap();
        System.out.println(map1.contains(123));
    }
}
