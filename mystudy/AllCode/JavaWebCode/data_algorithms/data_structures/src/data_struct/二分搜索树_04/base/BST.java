package data_struct.二分搜索树_04.base;

/**
 * @Classname BST
 * @Description TODO
 * @Date 2022/1/24 17:12
 * @Created by zhq
 */


//存储的数据必须是有可比较性的数据。
public class BST<E extends Comparable<E>> {

    //定义内部节点类
    private class Node {
        public E e;
        public Node left, right;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
        }
    }

    //定义根节点
    private Node root;
    //定义节点的数量
    private int size;


    //构造器
    public BST() {
        //默认情况下，根节点为Null
        root = null;
        size = 0;
    }

    //获取节点数量
    public int size() {
        return size;
    }

    //判断树是否为null
    public boolean isEmpty() {
        return size == 0;
    }


//添加元素一：假设已有根节点，只添加子节点


//    //添加节点
//    public void add(E v) {
//        //当树为null时，添加根节点
//        if (root == null) {
//            root = new Node(v);
//            size++;
//        } else
//            add(root, v);
//    }
//
//    //向以node为根的二分搜索树中插入元素e，递归算法
//    private void add(Node node, E e) {
//        //终止条件
//        if (e.equals(node.e)) {
//            return;
//        } else if (e.compareTo(node.e) < 0 && node.left == null) {
//            node.left = new Node(e);
//            size++;
//            return;
//        } else if (e.compareTo(node.e) > 0 && node.right == null) {
//            node.right = new Node(e);
//            size++;
//            return;
//        }
//        //递归
//        if (e.compareTo(node.e) < 0)
//            add(node.left, e);
//        else add(node.right, e);
//    }

//添加元素方式二：所有的元素都一视同仁。

    //添加节点
    public void add(E e) {
        root = add(root, e);
    }

    //向以node为根的二分搜索树中插入元素e，递归算法
    private Node add(Node node, E e) {
        //临界值判断，如果要添加元素的节点为null，则直接返回一个新的元素
        if (node == null) {
            size++;
            return new Node(e);
        }
        //判断要添加的元素与当前节点的大小
        if (e.compareTo(node.e) > 0) {
            //说明在当前节点的右侧添加
            node.right = add(node.right, e);
        } else if (e.compareTo(node.e) < 0) {
            //说明在当前节点的左侧添加
            node.left = add(node.left, e);
        }
        //如果与当前节点相等，则不需要处理，直接返回即可。
        return node;
    }

    //看二分搜索树中是否包含元素e
    public boolean contains(E e) {
        return contains(root, e);
    }

    private boolean contains(Node node, E e) {
        if (node == null) return false;

        if (e.compareTo(node.e) > 0)
            return contains(node.right, e);
        else if (e.compareTo(node.e) < 0)
            return contains(node.left, e);
            //相等的情况下。
        else return true;
    }


    //前序遍历
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node node) {

        //方法一：
        //递归终止条件
        if (node == null) return;
        //父节点
        System.out.println(node.e);
        //遍历左子树
        preOrder(node.left);
        //遍历右子树
        preOrder(node.right);

        //方法二：
//        if (node!=null){
//            //遍历左子树
//            preOrder(node.left);
//            //遍历右子树
//            preOrder(node.right);
//        }
    }


    //中序遍历
    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(Node node) {
        //递归终止条件
        if (node == null) return;
        //遍历左子树
        inOrder(node.left);
        //父节点
        System.out.println(node.e);
        //遍历右子树
        inOrder(node.right);

    }


    //后序遍历
    public void postOrder() {
        postOrder(root);
    }

    private void postOrder(Node node) {
        //递归终止条件
        if (node == null) return;
        //遍历左子树
        postOrder(node.left);
        //遍历右子树
        postOrder(node.right);
        //父节点
        System.out.println(node.e);
    }


    //前序遍历：非递归实现
    public void preOrderTree() {
        Node node = root;
        while (node!=null){
            System.out.println(node.e);
            Node left = node.left;
            Node right = node.right;
            while (left!=null){
                System.out.println(left);
                left=node.left;
            }
            while (right!=null){
                System.out.println(right);
                right=node.left;
            }

        }
    }




}
