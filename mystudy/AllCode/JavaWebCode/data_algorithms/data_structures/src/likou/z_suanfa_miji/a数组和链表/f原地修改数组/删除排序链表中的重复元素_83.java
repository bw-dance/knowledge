package likou.z_suanfa_miji.a数组和链表.f原地修改数组;

/**
 * @Classname 删除排序链表中的重复元素_83
 * @Description TODO
 * @Date 2022/2/10 15:42
 * @Created by zhq
 */
public class 删除排序链表中的重复元素_83 {
    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 3, 3};
        ListNode head = new ListNode();
        head.val = arr[0];
        ListNode cur = head;
        for (int i = 1; i < arr.length; i++) {
            ListNode node = new ListNode();
            node.val = arr[i];
            cur.next = node;
            cur = cur.next;
        }

        Solution06 solution02 = new Solution06();
        solution02.deleteDuplicates(head);

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

//双指针（逻辑比较混乱）
class Solution02 {
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) return head;
        //记录慢指针（最后返回）
        ListNode slow = new ListNode(head.val);
        //记录慢指针最后一个元素的值
        int lastSlow = head.val;
        //记录快指针
        ListNode fast = head;
        while (fast != null) {
            if (lastSlow != fast.val) {
                ListNode sCur = slow;
                while (sCur.next != null) {
                    sCur = sCur.next;
                }
                //慢指针添加最后添加一个元素。
                sCur.next = new ListNode(fast.val);
                lastSlow = fast.val;
            }
            fast = fast.next;
        }
        return slow;
    }
}


//循环
class Solution03 {
    public ListNode deleteDuplicates(ListNode head) {
        ListNode cur = head;
        while (cur != null) {
            //如果当前元素的下一个节点不是null，且两个节点的值相等，进行节点替换。
            if (cur.next != null) {
                if (cur.val == cur.next.val) {
                    ListNode next = cur.next;
                    cur.next = next.next;
                    continue;
                }
                cur = cur.next;
            } else break;  //此时没有子节点，则为null。
        }
        return head;
    }
}

//循环
class Solution04 {
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) return null;
        ListNode cur = head;
        while (cur.next != null) {
            if (cur.val == cur.next.val) {
                cur.next = cur.next.next;
            } else cur = cur.next;
        }
        return head;
    }
}

class Solution06 {
    public ListNode deleteDuplicates(ListNode head) {
        if (head==null) return null;
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null) {
            if (slow.val == fast.val) {
                fast = fast.next;

            } else {
                slow.next = fast;
                slow = slow.next;
            }
        }
        slow.next = null;
        return head;
    }
}