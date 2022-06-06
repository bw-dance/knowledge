package utils

import (
	"charroom2/common/message"
	"encoding/binary"
	"encoding/json"
	"fmt"
	"net"
)


//封装发送消息的函数
func WritePkg(conn net.Conn,data []byte) (err error){
	//data是我们要发送的消息
	//先发送data的长度发送给服务器，告诉服务器接受的数据长度，防止丢包
	//先获取到data的长度-》转换成一个表示长度的切片。
	//使用binary包
	var pkgLen uint32
	pkgLen=uint32(len(data))
	var buf [4]byte
	binary.BigEndian.PutUint32(buf[0:4],pkgLen)
	//发送长度
	n,err:=conn.Write(buf[0:4])
	if n!=4||err!=nil {
		fmt.Println("conn.Write(bytes) fail",err)
		return
	}
	fmt.Printf("客户端，发送消息的长度ok，长度=%d，内容=%s",len(data),string(data))

	//发送消息本身
	_,err= conn.Write(data)
	if err!=nil {
		fmt.Println("conn.Write(data) fail",err)
		return
	}
	return
}
//封装接收消息的函数
func ReadPkg(conn net.Conn)(mes message.Message,err error) {
	buf := make([]byte, 8096)
	//将读取数据包，封装成一个函数readPkg(),返回message，Err
	_, err = conn.Read(buf[:4])
	if err != nil {
		fmt.Println("readPkg.Read err=", err)
		return
	}
	//根据buf[:4]转换成一个uint32类型
	var pkgLen uint32
	pkgLen =binary.BigEndian.Uint32(buf[:4])
	//根据pkgLen读取消息
	n,err :=conn.Read(buf[:pkgLen])//表示从conn中读取pkgLen字节的数据放入buf中。
	//pkgLen是客户端告诉服务端消息的长度，n表示实际读取的长度
	if n!=int(pkgLen)||err!=nil {
		fmt.Println("readPkg.body err=", err)
		return
	}
	//把pkgLen反序列化成  message.Message
	err = json.Unmarshal(buf[:pkgLen],&mes)
	if err!=nil {
		fmt.Println("反序列化失败",err)
		return
	}
	return
}
