package scoket.chat_01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @Classname MyClient
 * @Description TODO
 * @Date 2022/3/4 15:57
 * @Created by zhq d
 */
public class MyClient
{
    public static void main(String[] args)throws Exception
    {
        Socket s = new Socket("127.0.0.1" , 30000);
        // 客户端启动ClientThread线程不断读取来自服务器的数据
        new Thread(new ClientThread(s)).start();   // ①


        // 获取该Socket对应的输出流,向服务端发送数据
        PrintStream ps = new PrintStream(s.getOutputStream());
        String line = null;
        // 不断读取键盘输入,键盘录入的数据发送至服务器
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        while ((line = br.readLine()) != null)
        {
            // 将用户的键盘输入内容写入Socket对应的输出流
            ps.println(line);
        }
    }
}
