package factory.abstractfactory;

/**
 * @Classname HuaWeiFactory
 * @Description TODO
 * @Date 2022/1/9 17:16
 * @Created by zhq
 */
public class HuaWeiFactory extends AbstractFactory{
    @Override
    AbstractPhoneProduct createPhone() {
        return new Mate50();
    }

    @Override
    AbstractPcProduct createComputer() {
        return new MateBookPro();
    }
}
