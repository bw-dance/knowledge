package main

import "fmt"

//定义一个函数：
func test(num int) {
	fmt.Println(num)
}

//定义一个函数，把另一个函数作为形参：
func test02(num1 int, num2 float32, testFunc func(int)) {
	fmt.Println("-----test02")
	a := num1 + int(num2)
	fmt.Println(a)
	fmt.Printf("testFunc的类型是：%T,", testFunc)
	fmt.Println()
}
func main() {
	//函数也是一种数据类型，可以赋值给一个变量
	a := test                                        //变量就是一个函数类型的变量
	fmt.Printf("a的类型是：%T,test函数的类型是：%T \n", a, test) //a的类型是：func(int),test函数的类型是：func(int)
	//通过该变量可以对函数调用
	a(10) //等价于  test(10)
	//调用test02函数：
	test02(10, 3.19, test)
	test02(10, 3.19, a)
}
