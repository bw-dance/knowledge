package factory.simplefactory;

/**
 * @Classname Main
 * @Description TODO
 * @Date 2022/1/7 15:01
 * @Created by zhq
 */
public class Main {
    public static void main(String[] args) {
        HuaWeiPhoneFactory factory = new HuaWeiPhoneFactory();
        HuaWeiPhone p30 = factory.makePhone("P30");
        p30.call();
    }
}
