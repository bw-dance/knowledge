package likou.z_suanfa_miji.数组和链表.g链表;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname 删除链表的倒数第N个结点_19
 * @Description TODO
 * @Date 2022/2/13 22:25
 * @Created by zhq
 */
public class 删除链表的倒数第N个结点_19 {
}

class Solution10 {
    //    public ListNode removeNthFromEnd(ListNode head, int n) {
//        ListNode cur = head;
//        int num = 0;
//        while (cur != null) {
//            num++;
//            cur = cur.next;
//        }
//        cur = head;
//        while (cur.next != null) {
//
//        }
//        for (int i = 0; i < num; i++) {
//            if (i == (num - n - 1)) {
//                cur.next = cur.next.next;
//            }
//            cur = cur.next;
//        }
//        return head;
//    }
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode cur = dummy;
        ListNode slow = dummy;
        int num = 0;
        while (cur.next != null) {
            num++;
            if (num - n - 1 >= 0) {
                //要被删除得元素的前一个元素
                slow = slow.next;
            }
            cur = cur.next;
        }
        slow.next = slow.next.next;
        return dummy.next;
    }
}
