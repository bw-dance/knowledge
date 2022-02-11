package likou.z_suanfa_miji.数组和链表.g链表;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2022/2/11 10:06
 * @Created by zhq
 */
public class 合并两个有序链表_21 {
    public static void main(String[] args) {

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


class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(-1);
        ListNode p1 = list1;
        ListNode p2 = list2;
        ListNode cur = dummy;
        while (p1 != null || p2 != null) {
            if (p1 != null && p2 != null) {
                if (p1.val > p2.val) {
                    cur.next = new ListNode(p2.val);
                    p2 = p2.next;
                } else {
                    cur.next = new ListNode(p1.val);
                    p1 = p1.next;
                }
                cur = cur.next;
            } else if (p1 == null) {
                cur.next = p2;
                break;
            } else if (p2 == null) {
                cur.next = p1;
                break;
            }
        }
        return dummy.next;
    }
}