package main //声明文件所在的包，每个go文件必须有所属的包
import "fmt" //引入程序中需要的包，为了使用包下的函数  比如：Println
func main(){ // main 主函数 程序的入口
	fmt.Println("Hello Golang!") // 在控制台打印

	var a  = "Runoob"
    fmt.Println(a)

    var b, c  = 1, 2
    fmt.Println(b, c)
}