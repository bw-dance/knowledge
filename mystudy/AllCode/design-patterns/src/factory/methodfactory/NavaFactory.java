package factory.methodfactory;

/**
 * @Classname NavaFactory
 * @Description TODO
 * @Date 2022/1/9 16:30
 * @Created by zhq
 */
public class NavaFactory implements Factory {
    @Override
    public HuaWeiPhone product() {
        return new Nava("Nava", 100, "red");
    }
}
