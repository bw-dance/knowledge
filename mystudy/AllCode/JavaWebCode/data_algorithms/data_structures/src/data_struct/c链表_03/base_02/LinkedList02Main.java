package data_struct.c链表_03.base_02;

/**
 * @Classname LinkedList02Main
 * @Description TODO
 * @Date 2021/12/30 16:07
 * @Created by zhq
 */
public class LinkedList02Main {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
//        for (int i = 0; i < 10; i++) {
//            list.addFirst(i);
//            System.out.println(list);
//        }
//        list.add(1,100);
//        System.out.println(list);
//        list.addLast(200);
//        System.out.println(list);
//        list.remove(2);
//        System.out.println(list);
//        list.removeFist();
//        System.out.println(list);
//        list.removeLast();
//        System.out.println(list);
//        list.set(5,-1);
//        System.out.println(list);
//        System.out.println(list.contains(5));
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(2);
        list.removeElements(2);
        System.out.println(list);
    }
}
