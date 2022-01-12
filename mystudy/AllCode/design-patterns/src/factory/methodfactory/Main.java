package factory.methodfactory;

import jdk.nashorn.internal.runtime.regexp.JoniRegExp;

/**
 * @Classname Main
 * @Description TODO
 * @Date 2022/1/7 15:01
 * @Created by zhq
 */
public class Main {
    public static void main(String[] args) {
        Factory mate20Factory = new Mate20Factory();
        HuaWeiPhone mate20 = mate20Factory.product();
        mate20.call();
        Factory p30Factory = new P30Factory();
        HuaWeiPhone p30 = p30Factory.product();
        p30.call();
        Factory navaFactory = new NavaFactory();
        HuaWeiPhone nava = navaFactory.product();
        nava.call();
    }
}
