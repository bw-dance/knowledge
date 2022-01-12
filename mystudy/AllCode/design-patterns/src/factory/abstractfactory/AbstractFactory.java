package factory.abstractfactory;

/**
 * @Classname AbstractFactory
 * @Description TODO
 * @Date 2022/1/9 17:11
 * @Created by zhq
 */
public abstract class AbstractFactory {
    /**
     * 生产手机的方法
     */
    abstract AbstractPhoneProduct createPhone();

    /**
     * 生产电脑的方法
     */
    abstract AbstractPcProduct createComputer();
}
