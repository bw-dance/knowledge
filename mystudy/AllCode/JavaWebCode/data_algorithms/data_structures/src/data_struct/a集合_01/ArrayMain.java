package data_struct.aéå_01;

import data_struct.aéå_01.base.Array;

/**
 * @Classname ArrayMain
 * @Description TODO
 * @Date 2021/12/25 21:48
 * @Created by zhq
 */
public class ArrayMain {
    public static void main(String[] args) {
        Array array = new Array();
        for (int i = 0; i <5 ; i++) {
            array.add(i,i);
        }
        for (int i = 0; i <5 ; i++) {
            array.add(i,i);
        }
//        array.removeElements(1,3);
        array.removeSelIndex(-1,0,2,6,100);
        System.out.println(array);
    }
}
