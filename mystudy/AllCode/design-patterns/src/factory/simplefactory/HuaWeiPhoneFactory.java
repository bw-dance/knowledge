package factory.simplefactory;

/**
 * @Classname HuaWeiPhoneFactory
 * @Description TODO
 * @Date 2022/1/7 14:59
 * @Created by zhq
 */
public class HuaWeiPhoneFactory {
    public HuaWeiPhone product(String name) {
        if (name.equalsIgnoreCase("P30")) {
            return new P30("P30", 100, "red");
        } else if (name.equalsIgnoreCase("Mate20")) {
            return new Mate20("Mate20", 100, "red");
        } else {
            throw new RuntimeException("we do not have this type phone");
        }
    }
}
