package likou.z_suanfa_miji.数组和链表.g链表;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname 环形链表_141
 * @Description TODO
 * @Date 2022/2/12 11:48
 * @Created by zhq
 */
public class 环形链表_141 {
}


//方式一：使用集合记录已遍历的元素，每获取一个链表元素判断集合中有无此元素  时间复杂度O（n2）
class Solution03 {
    public boolean hasCycle(ListNode head) {
        //记录已排查的指针
        List<ListNode> listNodes = new ArrayList<>();
        ListNode cur = head;
        while (cur != null) {
            //判断当前节点的下一个节点，是否指向之前的节点。
            for (ListNode node : listNodes
            ) {
                if (cur.next == node)
                    return true;
            }
            listNodes.add(cur);
            cur = cur.next;
        }
        return false;
    }
}

class Solution04 {
    public boolean hasCycle(ListNode head) {
        //记录已排查的指针
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode cur = dummy;
        while (dummy.next != null) {
            if (cur == cur.next) return true;
            cur = cur.next;
        }
        return false;
    }
}

