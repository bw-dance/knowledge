package factory.simplefactory;

/**
 * @Classname Main
 * @Description TODO
 * @Date 2022/1/7 15:01
 * @Created by zhq
 */
public class Main {
    public static void main(String[] args) {
        //使用工厂生产手机
        HuaWeiPhoneFactory factory = new HuaWeiPhoneFactory();
        HuaWeiPhone p30 = factory.product("P30");
        p30.call();
        HuaWeiPhone mate20 = factory.product("Mate20");
        mate20.call();
        //自己创建手机    问题：手机的参数有很多，生产不方便。不如我提供一个手机名称，交给工厂，工厂直接给我生产。
        P30 p301 = new P30("P30", 100, "red");
        Mate20 mate201 = new Mate20("Mate20", 100, "red");
    }
}
