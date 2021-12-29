package stack_queue_02;

import stack_queue_02.stack.ArrayStack;

public class ArrayStackMain {
	public static void main(String[] args) {
		ArrayStack<Integer> arrayStrack = new ArrayStack<>();
		for(int a = 0;a<5;a++) {
			arrayStrack.push(a);
			System.out.println(arrayStrack);
		}
        //栈中的元素数量
		System.out.println(arrayStrack.getSize());//5
		//移除栈顶的元素
		System.out.println(arrayStrack.pop());//4
		//查看栈中的第一个元素
		System.out.println(arrayStrack.peek());//0
		//判断栈是否为空
		System.out.println(	arrayStrack.isEmpty());//false
	}
}
