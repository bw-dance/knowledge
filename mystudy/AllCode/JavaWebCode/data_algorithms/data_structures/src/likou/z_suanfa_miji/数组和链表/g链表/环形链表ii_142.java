package likou.z_suanfa_miji.数组和链表.g链表;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Classname 环形链表_141
 * @Description TODO
 * @Date 2022/2/12 11:48
 * @Created by zhq
 */
public class 环形链表ii_142 {
}


//方式一：使用集合记录已遍历的元素，每获取一个链表元素判断集合中有无此元素  时间复杂度O（n2）
class Solution05 {
    public boolean detectCycle(ListNode head) {
        //记录已排查的指针
        Set<ListNode> listNodes = new HashSet<>();
        ListNode cur = head;
        while (cur != null) {
            //判断当前节点的下一个节点，是否指向之前的节点。
            if (listNodes.contains(cur)) {
                return true;
            }
            listNodes.add(cur);
            cur = cur.next;
        }
        return false;
    }
}

//方式二：快慢指针。慢指针一次走一步，快指针一次走两步。如果快指针到null，说明没环状。如果有环，满指针=快指针。
class Solution06 {
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        //fast!=null 用于fast.next!=null的辅助判断。
        //因为fast=fast.next.next，因此必须判断fast.next!=null
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast){
                slow = head;
                while (slow!=fast){
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }
}

