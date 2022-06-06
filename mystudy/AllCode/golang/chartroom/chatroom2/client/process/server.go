package process

import (
	"fmt"
	"os"
)

//显示登录成功后的界面..
func ShowMenu() {

	fmt.Println("-------恭喜xxx登录成功---------")
	fmt.Println("-------1. 显示在线用户列表---------")
	fmt.Println("-------2. 发送消息---------")
	fmt.Println("-------3. 信息列表---------")
	fmt.Println("-------4. 退出系统---------")
	fmt.Println("请选择(1-4):")
	var key int
	//var content string

	//因为，我们总会使用到SmsProcess实例，因此我们将其定义在swtich外部
	//smsProcess := &SmsProcess{}
	fmt.Scanf("%d\n", &key)
	switch key {
	case 1:
		fmt.Println("显示在线用户列表-")
		//outputOnlineUser()
	case 2:
		fmt.Println("发送消息")
		//fmt.Println("你想对大家说的什么:)")
		//fmt.Scanf("%s\n", &content)
		//smsProcess.SendGroupMes(content)
	case 3:
		fmt.Println("信息列表")
	case 4:
		fmt.Println("你选择退出了系统...")
		os.Exit(0)
	default :
		fmt.Println("你输入的选项不正确..")
	}

}
