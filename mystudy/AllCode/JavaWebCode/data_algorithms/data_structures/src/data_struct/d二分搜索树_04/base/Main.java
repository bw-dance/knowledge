package data_struct.d二分搜索树_04.base;

/**
 * @Classname HelloWorld
 * @Description TODO
 * @Date 2022/1/25 9:13
 * @Created by zhq
 */
public class Main {
    public static void main(String[] args) {

        //添加元素的遍历元素测试
//        BST bst = new BST();
//        int[] arr = {28,16,30,13,22,29,42};
//        for (int item: arr
//             ) {
//            bst.add(item);
//        }
//        System.out.println("前序遍历");
//        bst.preOrder();
//        System.out.println("中序遍历");
//        bst.inOrder();
//        System.out.println("后序遍历");
//        bst.postOrder();
//        System.out.println("前序遍历：非递归写法");
//        bst.preOrderND();
//        System.out.println("层序遍历");
//        bst.rankOrder();


        //移除最小值测试
//        BST bst1 = new BST();
//        //随机添加10000个数据
//        Random random = new Random();
//        for (int i = 0; i < 10000; i++) {
//            bst1.add(random.nextInt(10000));
//        }
//        //依次删除这些数据，查看删除数据的大小是否是从小到大
//        List<Integer> nums = new ArrayList<>();
//        while (!bst1.isEmpty()) {
//            nums.add((Integer) bst1.removeMin());
//        }
//        System.out.println(nums);
//        //检验数据是否从小到大
//        for (int i = 1; i <nums.size() ; i++) {
//            if (nums.get(i-1)>nums.get(i)) throw new IllegalArgumentException("removeMin error");
//        }
        //移除任意元素测试
        BST bst2 = new BST();
        int[] arr = {28,16,30,13,22,29,42};
        for (int i = 0; i < arr.length; i++) {
            bst2.add(arr[i]);
        }
//        System.out.println("---------------------前序--------------");
//        bst2.preOrder();
//        System.out.println("---------------------中序--------------");
//        bst2.inOrder();
//        System.out.println("-----------------------------------------");
//        bst2.inOrderND();
        bst2.postOrderND();
//        System.out.println("删除之前：");
//        bst2.rankOrder();
//        bst2.remove(50);
//        System.out.println("删除之后：");
//        bst2.rankOrder();
    }
}
