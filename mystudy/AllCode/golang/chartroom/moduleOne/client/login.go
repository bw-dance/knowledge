package main

import (
	"encoding/json"
	"fmt"
	"net"
)

func login(userId int,userPwd string) (err error) {
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
     err= utils.WritePkg(conn,data)
	//7.处理服务器返回的消息
	var loginResMes message.LoginResMes
	mes,err= utils.ReadPkg(conn)
	json.Unmarshal([]byte(mes.Data),&loginResMes)
	fmt.Println("返回结果",loginResMes)
	return
}

