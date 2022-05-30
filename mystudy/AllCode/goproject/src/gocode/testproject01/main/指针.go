package main
import(
        "fmt"
)
func main(){
        // var age int = 18
        // //&符号+变量 就可以获取这个变量内存的地址
        // fmt.Println(&age) //0xc0000a2058
        // //定义一个指针变量：
        // //var代表要声明一个变量
        // //ptr 指针变量的名字
        // //ptr对应的类型是：*int 是一个指针类型 （可以理解为 指向int类型的指针）
        // //&age就是一个地址，是ptr变量的具体的值
        // var ptr *int = &age
        // fmt.Println(ptr)
        // fmt.Println("ptr本身这个存储空间的地址为：",&ptr)
        // //想获取ptr这个指针或者这个地址指向的那个数据：
        // fmt.Printf("ptr指向的数值为：%v",*ptr) //ptr指向的数值为：18
	
			var num int = 10
			fmt.Println(num)  //10
			var ptr *int = &num //
			*ptr = 20
			fmt.Println(num) //20
			fmt.Println(*ptr)//20



			var fnum float32 = 3.14
			//cannot use &fnum (type *float32) as type *int in assignment
			// var fptr *int = &fnum;
			// fmt.Println(fnum) //20
			// fmt.Println(*fptr)//20

			var fptr *float32 = &fnum;
			fmt.Println(fnum) //20
			fmt.Println(*fptr)//20


}