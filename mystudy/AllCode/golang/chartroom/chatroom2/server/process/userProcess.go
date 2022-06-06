package process

import (
	"charroom2/common/message"
	"charroom2/server/utils"
	"encoding/json"
	"fmt"
	"net"
)

type UserProcess struct {
	Conn net.Conn

}

//处理登陆消息
func (this *UserProcess) ServerProcessLogin(mes *message.Message) {
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
	//使用了分层模式，需要穿件Transfer实例
	tf:=&utils.Transfer{Conn: this.Conn}
	tf.WritePkg(data)
}
