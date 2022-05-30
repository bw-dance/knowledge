package main
import(
	"fmt"
	"strconv"
)
func main(){
        // var n1 int = 19
        // var n2 float32 = 4.78
        // var n3 bool = false
        // var n4 byte = 'a'
        // var s1 string = fmt.Sprintf("%d",n1)
        // fmt.Printf("s1对应的类型是:%T ,s1 = %q \n",s1, s1)
        // var s2 string = fmt.Sprintf("%f",n2)
        // fmt.Printf("s2对应的类型是:%T,s2 = %q \n",s2, s2)
        // var s3 string = fmt.Sprintf("%t",n3)
        // fmt.Printf("s3对应的类型是:%T ,s3 = %q \n",s3, s3)
        // var s4 string = fmt.Sprintf("%c",n4)
        // fmt.Printf("s4对应的类型是:%T ,s4 = %q \n",s4, s4)

		var n1 int = 18
        var s1 string = strconv.FormatInt(int64(n1),10)  //参数：第一个参数必须转为int64类型 ，第二个参数指定字面值的进制形式为十进制
        fmt.Printf("s1对应的类型是：%T ，s1 = %q \n",s1, s1)
        var n2 float64 = 4.29
        var s2 string = strconv.FormatFloat(n2,'f',9,64)
        //第二个参数：'f'（-ddd.dddd）  第三个参数：9 保留小数点后面9位  第四个参数：表示这个小数是float64类型
        fmt.Printf("s2对应的类型是：%T ，s2 = %q \n",s2, s2)
        var n3 bool = true
        var s3 string = strconv.FormatBool(n3)
        fmt.Printf("s3对应的类型是：%T ，s3 = %q \n",s3, s3)
}
