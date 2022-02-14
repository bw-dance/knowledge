package likou.z_suanfa_miji.数组和链表.g链表;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Classname 相交链表_160
 * @Description TODO
 * @Date 2022/2/13 21:43
 * @Created by zhq
 */
public class 相交链表_160 {
}

class Solution08 {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        Set headASet = new HashSet();
        while (headA != null) {
            headASet.add(headA);
            headA = headA.next;
        }
        while (headB != null) {
            if (headASet.contains(headB)) return headB;
            headB = headB.next;
        }
        return null;
    }
}

class Solution09 {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode slow = headA;
        ListNode fast = headB;
        while (slow != fast) {
            slow = slow == null ? headB : slow.next;
            fast = fast == null ? headA : fast.next;
        }
        return slow;
    }
}
