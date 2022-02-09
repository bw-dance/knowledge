package data_struct.c链表_03.链表和递归.likou;

/**
 * @Classname 移除链表元素_203
 * @Description TODO
 * @Date 2022/1/20 16:44
 * @Created by zhq
 */
public class 移除链表元素_203 {
    public static void main(String[] args) {
        ListNode head = new ListNode();
        head.val = 0;
        ListNode preNode = head;
        for (int i = 1; i < 10; i++) {
            ListNode node = new ListNode(i);
            preNode.next = node;
            preNode = node;
        }
        for (ListNode listNode = head; listNode != null; listNode = listNode.next) {
            System.out.println("origin:" + listNode.val);
        }
//        Solution solution = new Solution();
//        ListNode listNode = solution.removeElements(head, 0);
//        for (ListNode listNode1 = listNode; listNode1 != null; listNode1 = listNode1.next) {
//            System.out.println("now" + listNode1.val);
//        }


        Solution02 solution02 = new Solution02();
        ListNode listNode02 = solution02.removeElements(head, 5);
        for (ListNode listNode1 = listNode02; listNode1 != null; listNode1 = listNode1.next) {
            System.out.println("now" + listNode1.val);
        }

    }
}


class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

/**
 * 思路1:不使用虚拟头结点
 * 1.删除链表元素时，必须要获取链表元素的头指针。然后让头指针指向删除元素之后的元素。
 * 2.在当前节点判断下一轮次节点的值，如果符合，则进行节点移除。
 * 3.头结点和最后一个节点特殊处理
 */
class Solution {
    public ListNode removeElements(ListNode head, int val) {
        if (head == null) return null;
        ListNode currentNode = head;
        for (ListNode listNode = currentNode; listNode != null; ) {
            //最后一个节点处理
            if (listNode.next == null) {
                if (listNode.val == val) listNode = null;
                break;
            } else {
                if (listNode.next.val == val) {
                    //节点替换
                    ListNode nextNode = listNode.next;
                    listNode.next = nextNode.next;
                    //切断删除节点的next指向的元素
                    nextNode.next = null;
                    //如果发生了节点移除，那么下一轮的节点还是本轮节点，否则下一轮节点指向next
                } else listNode = listNode.next;
            }
        }
        //头结点的特殊处理
        return currentNode.val == val ? currentNode.next : currentNode;
    }
}


/**
 * 思路2:上述结果的精简解法
 * 1.删除链表元素时，必须要获取链表元素的头指针。先对头结点进行处理，保证所有的头结点的val都不等于目标值
 * 2.如果头结点为null，直接返回。
 * 3. 子节点的处理
 */
class Solution03 {
    public ListNode removeElements(ListNode head, int val) {
        //1.处理头结点。
        while (head != null && head.val == val) head = head.next;
        //2.头结点为null
        if (head == null) return head;
        ListNode currentNode = head;
        //3.处理中间元素
        while (currentNode.next != null) {
            if (currentNode.next.val == val)
                currentNode.next = currentNode.next.next;
            else currentNode = currentNode.next;
        }
        return head;
    }
}


/**
 * 思路2:使用虚拟头结点
 * 1.删除链表元素时，必须要获取链表元素的头指针。然后让头指针指向删除元素之后的元素。
 * 2.在当前节点判断下一轮次节点的值，如果符合，则进行节点移除。
 * 3.头结点和最后一个节点特殊处理
 */
class Solution02 {
    public ListNode removeElements(ListNode head, int val) {
        if (head == null) return null;
        //1.创建虚拟头结点
        ListNode xuNiHead = new ListNode(0, head);
        //2.记录每一轮遍历的节点
        ListNode currentNode = xuNiHead;
        while (currentNode.next != null) {
            ListNode nextNode = currentNode.next;
            if (nextNode.val == val) {
                currentNode.next = nextNode.next;
                //切断删除节点的next指向的元素
                nextNode.next = null;
            } else currentNode = currentNode.next;
        }
        //3.返回虚拟节点的下一个节点
        return xuNiHead.next;
    }
}

/**
 * 思路4:使用虚拟头结点简化
 * 1.删除链表元素时，必须要获取链表元素的头指针。然后让头指针指向删除元素之后的元素。
 * 2.在当前节点判断下一轮次节点的值，如果符合，则进行节点移除。
 * 3.头结点和最后一个节点特殊处理
 */

class Solution04 {
    public ListNode removeElements(ListNode head, int val) {
        if (head == null) return null;
        //1.创建虚拟头结点
        ListNode dummyHead = new ListNode(-1, head);
        ListNode currentNode = dummyHead;
        while (currentNode.next != null) {
            if (currentNode.next.val == val)
                currentNode.next = currentNode.next.next;
            else currentNode = currentNode.next;
        }
        //2.返回真实的头结点
        return dummyHead.next;
    }
}

//思路3：使用递归
//1.将大问题划分为小问题。小问题：当前指针的下一点节点，如果节点与目标值相等，则返回当前节点的下一节点，如果不相等，则当前节点的下一节点保持不变
//2.终止的判断，如果下一节点的值为null，则保持不变即可

class Solution05 {
    //分析
    public ListNode removeElements(ListNode head, int val) {
        //1.对空节点的处理
        if (head == null) return head;
        //2.removeElements想象成一个子过程，删除值为value的节点.
        //我们将值为value的节点删除后，剩下的链表
        ListNode result = removeElements(head.next, val);
        //3.处理，如果当前的节点的值为value，返回删除当前节点后，剩余的链表。
        //如果当前节点的值不等于value，那么返回当前的节点即可。
        if (head.val == val) {
            return result;
        } else {
            head.next = result;
            return head;
        }
    }


    //精简
//    public ListNode removeElements(ListNode head, int val) {
//        if (head == null) return head;
//        head.next = removeElements(head.next, val);
//        return head.val == val ? head.next : head;
//    }
}

//递归扩展：求数组的和
class Solution06 {

    public static int sumArray(int[] nums) {
        return 0;
    }

    public static int sum(int[] nums, int len) {
        if (len == nums.length) return 0;
        return nums[len] + sum(nums, len + 1);
    }

}



