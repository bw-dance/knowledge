package factory.methodfactory;


/**
 * @Classname P30Factory
 * @Description TODO
 * @Date 2022/1/9 16:23
 * @Created by zhq
 */
public class P30Factory implements Factory{
    @Override
    public HuaWeiPhone product() {
        return new P30("P30", 100, "red");
    }
}
