package main

import (
	"charroom2/common/message"
	process2 "charroom2/server/process"
	"charroom2/server/utils"
	"fmt"
	"io"
	"net"

)

type Processor struct {
Conn 	net.Conn
}

//消息处理中转
func (this *Processor) ServerProcessMes(mes *message.Message) {
	switch mes.Type {
	case message.LoginMesType:
		up:=&process2.UserProcess{Conn: this.Conn}
		up.ServerProcessLogin(mes)
		break
	case message.LoginResMesType:
		break
	case message.RegisterMesType:
		break
	}

}


func (this * Processor)  process2() (err error){
	//读客户端发送的信息
	for {
		tf:=&utils.Transfer{Conn: this.Conn}
		mes,err:=	tf.ReadPkg()
		if err!=nil{
			if err ==io.EOF{
				fmt.Println("客户端退出，服务器也正常退出。。")
				return err
			}else {
				fmt.Println("readPkg err = " ,err)
				return err
			}
		}
		this.ServerProcessMes(&mes)
		fmt.Println("mes=",mes)
	}

}
