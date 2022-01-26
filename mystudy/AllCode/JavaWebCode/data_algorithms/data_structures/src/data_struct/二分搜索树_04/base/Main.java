package data_struct.二分搜索树_04.base;

/**
 * @Classname HelloWorld
 * @Description TODO
 * @Date 2022/1/25 9:13
 * @Created by zhq
 */
public class Main {
    public static void main(String[] args) {
        BST bst = new BST();
        int[] arr = {5,3,6,8,4,2};
        for (int item: arr
             ) {
            bst.add(item);
        }
        System.out.println("前序遍历");
        bst.preOrder();
        System.out.println("中序遍历");
        bst.inOrder();
        System.out.println("后序遍历");
        bst.postOrder();
    }
}
