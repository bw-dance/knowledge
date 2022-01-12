package factory.methodfactory;

/**
 * @Classname Mate20Factory
 * @Description TODO
 * @Date 2022/1/9 16:27
 * @Created by zhq
 */
public class Mate20Factory implements Factory {
    @Override
    public HuaWeiPhone product() {
        return new Mate20("Mate20", 100, "red");
    }
}
