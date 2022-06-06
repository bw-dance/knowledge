package main

import (
	"encoding/json"
	"fmt"
	"io"
	"moduleOne/common/message"
	"moduleOne/common/utils"
	"net"
)

func main() {
	//提示信息client
	fmt.Println("服务器在8889端口监听")
	listen, err := net.Listen("tcp", "0.0.0.0:8889")
	defer listen.Close()
	if err != nil {
		fmt.Println("net listen err = ", err)
		return
	}
	for {
		fmt.Println("等待客户端链接服务器")
		conn, err := listen.Accept()
		if err != nil {
			fmt.Println("链接失败")
		}
		//链接成功，则启动一个协程与客户端保持通讯
			go process(conn)
	}
}
//协程，处理消息
func process(conn net.Conn) {
	//需要延时关闭
	defer conn.Close()

	//读客户端发送的信息
	for {
	mes,err:=	utils.ReadPkg(conn)
	if err!=nil{
		if err ==io.EOF{
			fmt.Println("客户端退出，服务器也正常退出。。")
			return
		}else {
			fmt.Println("readPkg err = " ,err)
			return
		}
	}
	ServerProcessMes(&mes,conn)
	fmt.Println("mes=",mes)
	}
}
//消息处理中转
func ServerProcessMes(mes *message.Message, conn net.Conn) {
	switch mes.Type {
	case message.LoginMesType:
		serverProcessLogin(mes,conn)
		break
	case message.LoginResMesType:
		break
	case message.RegisterMesType:
		break
	}

}
//处理登陆消息
func serverProcessLogin(mes *message.Message, conn net.Conn) {
	var loginMes message.LoginMes
	var loginResMes message.LoginResMes
	//1.反序列化登陆信息
	err:=json.Unmarshal([]byte(mes.Data),&loginMes)
	if err!=nil {
		fmt.Println("序列化失败")
		return
	}
	//2.密码校验，登陆结果封装
	if loginMes.UserId==142742&&loginMes.UserPwd=="zhq1427421650"{
		fmt.Println("密码校验正确")
		loginResMes.Code=200
	}else {
		fmt.Println("密码错误，请重新输入")
		loginResMes.Code=500
		loginResMes.Error="密码错误，请重新输入"
		return
	}
	//3.登陆结果序列化及返回结果封装
	mes.Type=message.LoginResMesType
	data,err:=json.Marshal(loginResMes)
	if err!=nil {
		fmt.Println("序列化失败")
		return
	}
	mes.Data=string(data)
	data,err= json.Marshal(mes)
	//4.发送消息：消息长度+消息体
	utils.WritePkg(conn,data)
}

