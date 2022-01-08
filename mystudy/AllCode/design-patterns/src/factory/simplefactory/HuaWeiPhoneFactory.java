package factory.simplefactory;

/**
 * @Classname HuaWeiPhoneFactory
 * @Description TODO
 * @Date 2022/1/7 14:59
 * @Created by zhq
 */
public class HuaWeiPhoneFactory {
    public HuaWeiPhone makePhone(String name) {
        if (name.equalsIgnoreCase("P30")) {
            return new P30();
        } else if (name.equalsIgnoreCase("Mate20")) {
            return new Mate20();
        } else {
            throw new RuntimeException("we do not have this type phone");
        }
    }
}
