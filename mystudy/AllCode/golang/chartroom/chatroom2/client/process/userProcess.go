package process

import (
	"charroom2/common/message"
	"charroom2/server/utils"
	"encoding/json"
	"fmt"
	"net"
)

type UserProcess struct {
	//暂时不需要属性
}

//关联一个用户登陆的方法

func (this *UserProcess) Login(userId int,userPwd string) (err error) {
	//1.链接到服务器端
	conn,err:=	net.Dial("tcp","localhost:8889")
	if err!=nil {
		fmt.Println("net.Dial err = ",err)
		return
	}
	//延时关闭
	defer conn.Close()
	//2.通过conn发送给消息给服务
	var mes message.Message
	mes.Type=message.LoginMesType
	//3.创建loginMessage
	var loginMes message.LoginMes
	loginMes.UserId=userId
	loginMes.UserPwd=userPwd
	//4.将loginMessage序列化
	data,err:=json.Marshal(loginMes)
	if err!=nil {
		fmt.Println("json.marshal err=",err)
		return
	}
	//5.把data赋值给mes.Data字段并封装消息
	mes.Data=string(data)
	data,err=json.Marshal(mes)
	if err!=nil {
		fmt.Println("json.marshal err=",err)
		return
	}
	//6.发送消息给服务器
	tf:=&utils.Transfer{Conn: conn}
	err=tf.WritePkg(data)
	//7.处理服务器返回的消息
	var loginResMes message.LoginResMes
	mes,err= tf.ReadPkg()
	json.Unmarshal([]byte(mes.Data),&loginResMes)
	fmt.Println("返回结果",loginResMes)
	if loginResMes.Code==200 {
		fmt.Println("登录成功")
		//在客户端启动一个协程，该协程保持和服务器端的通讯，如果服务器有数据推送给客户端（如张三给你说话，或者李四上线），则接收并显示在客户端的终端
		go serverProcessMes(conn)
		//跳转二级菜单
		//1.循环显示我们登录成功的菜单。。。
		for  {
			ShowMenu()
		}

	}else if loginResMes.Code==500 {
		fmt.Println(loginResMes.Error)
	}
	return
}

//保持客户端与服务器的通话
func serverProcessMes(Conn net.Conn) {
	//使用for循环，不停的读。
	ts:=&utils.Transfer{Conn: Conn}
	for true {
		//只要服务器不关闭链接，客户端一直读取
		fmt.Printf("客户端正在读取服务器发送的消息")
		mes,err:=ts.ReadPkg()
		if err!=nil{
			fmt.Println("ts.readPlg err=",err)
			return
		}
		fmt.Printf("mes =%v\n",mes)
	}
}

