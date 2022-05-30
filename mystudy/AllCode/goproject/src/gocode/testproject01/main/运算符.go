package main

import "fmt"

func main() {
	//+加号：
	//1.正数 2.相加操作  3.字符串拼接
	dd := 10
	fmt.Println(dd)

	var n1 int = +10
	fmt.Println(n1)
	var n2 int = 4 + 7
	fmt.Println(n2)
	var s1 string = "abc" + "def"
	fmt.Println(s1)

	// /除号：
	fmt.Println(10 / 3)   //两个int类型数据运算，结果一定为整数类型
	fmt.Println(10.0 / 3) //浮点类型参与运算，结果为浮点类型
	// % 取模  等价公式： a%b=a-a/b*b
	fmt.Println(10 % 3) // 10%3= 10-10/3*3 = 1
	fmt.Println(-10 % 3)
	fmt.Println(10 % -3)
	fmt.Println(-10 % -3)
	//++自增操作：
	var a int = 10
	a++
	fmt.Println(a)
	a--
	fmt.Println(a)
	//++ 自增 加1操作，--自减，减1操作
	//go语言里，++，--操作非常简单，只能单独使用，不能参与到运算中去
	//go语言里，++，--只能在变量的后面，不能写在变量的前面 --a  ++a  错误写法
}
