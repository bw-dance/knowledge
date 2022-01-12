package factory.abstractfactory;

/**
 * @Classname XiaoMiFactory
 * @Description TODO
 * @Date 2022/1/9 17:17
 * @Created by zhq
 */
public class XiaoMiFactory extends AbstractFactory {
    @Override
    AbstractPhoneProduct createPhone() {
        return new Mi25();
    }

    @Override
    AbstractPcProduct createComputer() {
        return new XiaoMiBookPro();
    }
}
