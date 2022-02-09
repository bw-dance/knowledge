package JVM.内存划分.类加载;

import java.io.IOException;

/**
 * @Classname Demo3_10
 * @Description TODO
 * @Date 2022/1/27 12:11
 * @Created by zhq
 */
public class Demo3_10 {

    public static void test(Animal animal) {
        animal.eat();
        System.out.println(animal);
    }

    public static void main(String[] args) throws IOException {
        test(new Cat());
        test(new Dog());
        System.in.read();
    }
}

abstract class Animal {
    public abstract void eat();

    @Override
    public String toString() {
        return "我是" + this.getClass().getSimpleName();
    }
}

class Dog extends Animal {

    @Override
    public void eat() {
        System.out.println("啃骨头");
    }
}

class Cat extends Animal {

    @Override
    public void eat() {
        System.out.println("吃鱼");
    }
}
