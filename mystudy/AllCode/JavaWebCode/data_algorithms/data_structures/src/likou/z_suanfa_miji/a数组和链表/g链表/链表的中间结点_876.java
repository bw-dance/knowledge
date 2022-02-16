package likou.z_suanfa_miji.a数组和链表.g链表;

/**
 * @Classname 链表的中间结点_876
 * @Description TODO
 * @Date 2022/2/13 21:09
 * @Created by zhq
 */
public class 链表的中间结点_876 {
}

class Solution07 {
    public ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast!=null&&fast.next != null) {//如果fast.next==null，说明就一个节点。直接返回即可。
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}
