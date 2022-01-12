package factory.simplefactory;

/**
 * @Classname Mate20
 * @Description TODO
 * @Date 2022/1/7 14:58
 * @Created by zhq
 */
public class Mate20 implements HuaWeiPhone {

    private String name;
    private double price;
    private String color;

    public Mate20(String name, double price, String color) {
        this.name = name;
        this.price = price;
        this.color = color;
    }


    @Override
    public void call() {
        System.out.println("this is HuaWei Mate20");
    }
}
