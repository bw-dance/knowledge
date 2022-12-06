package likou.面试.软通;

public class Main {
    public static void main(String[] args) {
//       String a = "192.168.41.42";
//        String[] split = a.split(".");
//        for (int i = 0; i < split.length; i++) {
//
//        }1  3 5  2
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(6);
        ListNode l3 = new ListNode(5);
        ListNode l4 = new ListNode(2);
        ListNode l5 = new ListNode(4); //16524
        l1.next=l2;
        l2.next=l3;
        l3.next=l4;

        ListNode cur = l1;
        while (cur.next!=null){
            ListNode now = cur.next;
//            if (cur>no)
        }


    }
}
class  ListNode {
    ListNode next;
    int value;

    public ListNode(int value) {
        this.value = value;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ListNode(int value, ListNode next) {
        this.value = value;
        this.next = next;
    }
}