package likou.面试.软通;

public class StringTest {
    public static void main(String[] args) {
//        String a = new String("3")+new String("3");
//        String intern = a.intern();
//        String b = "33";
        int a = 1000;
        Integer b = new Integer(1000);
        Integer c = new Integer(1000);
        Integer d = 128;
        Integer f = 128;
        System.out.println(a == b);
//        System.out.println(a == c);
        System.out.println(f == d);
        System.out.println(b.equals(c));

    }
}
