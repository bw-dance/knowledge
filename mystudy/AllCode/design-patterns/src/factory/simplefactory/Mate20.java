package factory.simplefactory;

/**
 * @Classname Mate20
 * @Description TODO
 * @Date 2022/1/7 14:58
 * @Created by zhq
 */
public class Mate20 implements HuaWeiPhone {
    public Mate20() {
    }

    @Override
    public void call() {
        System.out.println("this is HuaWei Mate20");
    }
}
