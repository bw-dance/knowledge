package data_struct.d二分搜索树_04.base;

import com.sun.javaws.IconUtil;

import java.util.*;

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


    //前序遍历：非递归实现(有问题  todo)
//    public void preOrderTree() {
//        Node node = root;
//        while (node != null) {
//            System.out.println(node.e);
//            Node left = node.left;
//            Node right = node.right;
//            while (left != null) {
//                System.out.println(left);
//                left = node.left;
//            }
//            while (right != null) {
//                System.out.println(right);
//                right = node.left;
//            }
//
//        }
//    }

    //前序遍历的非递归写法
    public void preOrderND() {
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            System.out.println(cur.e);
            if (cur.right != null) stack.push(cur.right);
            if (cur.left != null) stack.push(cur.left);
        }
    }

    //中序遍历的非递归写法
    public void inOrderND() {
        Stack<Node> stack = new Stack<>();
        Node cur = root;
        //将根元素的所有节点添加到栈中：因为中序遍历，左根右，即先获取的是最左侧的元素。
        addLeftNodeToStack(stack,cur);
        while (!stack.isEmpty()) {
            cur = stack.pop();
            System.out.println(cur.e);
            //出栈元素，判断其包含是否有右节点，如果有右节点，则下一次应该打印的是右节点的最左侧元素
            if (cur.right != null) {
                cur = cur.right;
                addLeftNodeToStack(stack,cur);
            }
        }
    }
    //中序遍历，将当前节点的所有左侧元素放入栈中
    public void addLeftNodeToStack(Stack<Node> stack,Node cur){
        while (cur != null) {
            stack.push(cur);
            if (cur.left==null){
                break;
            }
            cur = cur.left;
        }
    }

    public void postOrderND() {
        Stack<Node> stack = new Stack<>();
        Node cur = root;
        while (cur!=null){
            stack.add(cur);
            cur = cur.right;
        }
        cur = root.left;
        while (cur!=null){
            stack.add(cur);
            cur = cur.left;
        }
        while (!stack.isEmpty()){
            Node node = stack.pop();
            System.out.println(node.e);
        }


    }


    //广度优先遍历（层次遍历）
    public void rankOrder() {
        Deque<Node> deque = new ArrayDeque<>();
        deque.push(root);
        while (!deque.isEmpty()) {
            Node cur = deque.remove();
            System.out.println(cur.e);
            if (cur.left != null) deque.add(cur.left);
            if (cur.right != null) deque.add(cur.right);
        }
    }


    //寻找二分搜索树的最小值
    public E minimum() {
        if (size == 0)
            throw new IllegalArgumentException("BST is empty");
        return minimum(root).e;
    }

    //返回以node为根的二分搜索树的最小值所在的节点。
    private Node minimum(Node node) {
        if (node.left == null) return node;
        return minimum(node.left);
    }

    //寻找二分搜索树的最大值
    public E maximum() {
        if (size == 0)
            throw new IllegalArgumentException("BST is empty");
        return maximum(root).e;
    }

    //返回以node为根的二分搜索树的最大值所在的节点。
    private Node maximum(Node node) {
        if (node.right == null) return node;
        return maximum(node.right);
    }


    //删除树的最小值
    public E removeMin() {
        //获取最小值
        E ret = minimum();
        //删除以node为根的最小值节点
        root = removeMin(root);
        //返回删除的最小值
        return ret;
    }


    //删除以node为根的二分搜索树中的最小节点
    //返回删除节点后新的二分搜索树的根。
    private Node removeMin(Node root) {
        //临界，说明当前节点是最小值
        if (root.left == null) {
            //保存当前节点的右子树
            Node right = root.right;
            root.right = null;
            size--;
            //返回当前节点的右子树
            return right;
        }
        //如果当前节点的左侧不是null，说明还有更小的节点，删除更小的节点。
        root.left = removeMin(root.left);
        //返回最新的根节点。
        return root;
    }


    //删除树的最大值
    public E removeMax() {
        //获取最大值
        E ret = maximum();
        //删除以node为根的最大值节点
        root = removeMax(root);
        //返回删除的最大值
        return ret;
    }


    //删除以node为根的二分搜索树中的最大节点
    //返回删除节点后新的二分搜索树的根。
    private Node removeMax(Node root) {
        //临界，说明当前节点是最大值
        if (root.right == null) {
            //保存当前节点的右子树
            Node left = root.left;
            root.left = null;
            size--;
            //返回当前节点的右子树
            return left;
        }
        //如果当前节点的右侧不是null，说明还有更大的节点，删除其更大的节点。
        root.right = removeMax(root.right);
        //返回最新的根节点。
        return root;
    }

    //    public void remove(E e) {
//        root = remove(root, e);
//    }
    //删除节点：情况1
//    private Node remove(Node node, E e) {
//        //节点不存在
//        if (node == null) throw new IllegalArgumentException("Remove Error： node is not exist");
//        if (e.compareTo(node.e) == 0) {
//            Node left = node.left;
//            Node right = node.right;
//            //要删除的节点左右两树都有
//            if (right != null && left != null) {
//                //左侧树拼接到右侧树的左侧底部。
//                //获取右侧树的左侧底部
//                Node cur = right;
//                while (cur.left != null) {
//                    cur = cur.left;
//                }
//                cur.left = left;
//                //返回右侧树
//                return right;
//            }
//            //被删除的节点只有左子树
//            if (left != null) return left;
//            //被删除的节点只有右子树
//            if (right != null) return right;
//        }
//        if (e.compareTo(node.e) > 0) {
//            node.right = remove(node.right, e);
//        } else {
//            node.left = remove(node.left, e);
//        }
//        return node;
//    }


    //删除节点：情况2  替换目标值为右侧子树的最小值
//    public void remove(E e) {
//        root = remove(root, e);
//    }
//
//    private Node remove(Node node, E e) {
//        //节点不存在
//        if (node == null) return null;
//        if (e.compareTo(node.e) == 0) {
//
//            //被删除的节点只有左子树
//            if (node.left == null) {
//                Node rightNode = node.right;
//                node.right = null;
//                size--;
//                return rightNode;
//            }
//            //被删除的节点只有右子树
//            if (node.right == null) {
//                Node leftNode = node.left;
//                node.left = null;
//                size--;
//                return leftNode;
//            }
//            //要删除的节点左右两树都有
//            //1.获取右侧子树的最小值
//            Node successor = minimum(node.right);
//            //2.删除右侧子树的最小值,并成为该节点的右子树
//            //注意，removeMin的时候，已经做了size--
//            successor.right = removeMin(node.right);
//            //左侧子树称为该节点的左子树
//            successor.left = node.left;
//            node.left = node.right = null;
//            //将该节点返回
//            return successor;
//        } else if (e.compareTo(node.e) > 0) {
//            node.right = remove(node.right, e);
//            return node;
//        } else {
//            node.left = remove(node.left, e);
//            return node;
//        }
//    }


    //删除节点：情况3  替换目标值为左侧子树的最大值。
    public void remove(E e) {
        root = remove(root, e);
    }

    private Node remove(Node node, E e) {
        //节点不存在
        if (node == null) return null;
        if (e.compareTo(node.e) == 0) {

            //被删除的节点只有左子树
            if (node.left == null) {
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }
            //被删除的节点只有右子树
            if (node.right == null) {
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            }
            //要删除的节点左右两树都有
            //1.获取右侧子树的最大值
            Node successor = maximum(node.left);
            //2.删除右侧子树的最大值,并成为该节点的左子树
            successor.left = removeMax(node.left);
            //右侧子树称为该节点的右子树
            successor.right = node.right;
            node.left = node.right = null;
            //将该节点返回
            return successor;
        } else if (e.compareTo(node.e) > 0) {
            node.right = remove(node.right, e);
            return node;
        } else {
            node.left = remove(node.left, e);
            return node;
        }
    }
}
