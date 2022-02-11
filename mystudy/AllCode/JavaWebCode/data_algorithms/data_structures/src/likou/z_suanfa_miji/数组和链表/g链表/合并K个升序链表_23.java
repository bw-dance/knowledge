package likou.z_suanfa_miji.数组和链表.g链表;

import java.util.Arrays;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @Classname 合并K个升序链表_23
 * @Description TODO
 * @Date 2022/2/11 11:03
 * @Created by zhq
 */
public class 合并K个升序链表_23 {
}

class Solution01 {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) return null;
        ListNode dummy = new ListNode(-1);
        ListNode cur = dummy;
        while (true) {
            //遍历获取最小的那个元素的值和索引
            int min = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int i = 0; i < lists.length; i++) {
                ListNode node = lists[i];
                if (node != null) {
                    if (node.val <= min) {
                        min = node.val;
                        minIndex = i;
                    }
                }
            }
            //如果minIndex是-1，说明没有节点
            if (minIndex == -1)
                break;
            //最小的那个节点的元素指针切换。
            lists[minIndex] = lists[minIndex].next;
            //将符合要求的元素的值作为新的节点
            cur.next = new ListNode(min);
            cur = cur.next;
        }
        return dummy.next;
    }
}

class Solution02 {
    public ListNode mergeKLists(ListNode[] lists) {
        Queue<ListNode> pq = new PriorityQueue<>((v1, v2) -> v1.val - v2.val);
        //链表放入队列，以链表中的第一个值进行大小排序
        for (ListNode node : lists) {
            if (node != null) pq.offer(node);
        }
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        while (!pq.isEmpty()) {
            //出队获取第一个元素
            ListNode minNode = pq.poll();
            tail.next = minNode;
            //将之后的链表重新放入队列
            if (minNode.next != null) pq.offer(minNode.next);
        }
        return dummy.next;
    }
}