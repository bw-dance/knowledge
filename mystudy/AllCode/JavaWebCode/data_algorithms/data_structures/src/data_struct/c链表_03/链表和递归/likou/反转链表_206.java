package data_struct.c链表_03.链表和递归.likou;

/**
 * @Classname 反转链表_206
 * @Description TODO
 * @Date 2022/1/23 21:04
 * @Created by zhq
 */
public class 反转链表_206 {
}

//迭代
class Solution07 {
    public ListNode reverseList(ListNode head) {
        //记录当前指针
        ListNode cur = head;
        //记录前一个元素
        ListNode pre = null;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }
}

//递归
class Solution08 {
    public ListNode reverseList(ListNode head) {
        if (head == null) return null;
        ListNode res = reverseList(head);
        return res.next = head;
    }
}