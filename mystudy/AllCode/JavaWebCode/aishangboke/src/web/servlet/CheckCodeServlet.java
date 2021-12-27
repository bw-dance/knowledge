package web.servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


@WebServlet("/checkCodeServlet")
public class CheckCodeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.创建一个对象，在内存中存图片（验证码图片对象）
        int width =100;
        int height= 50;
        BufferedImage image  =new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);//宽，高，格式
        //2.美化图片
        //2.1化背景颜色
        Graphics graphics = image.getGraphics();//画笔对象
        graphics.setColor(Color.pink);//设置画笔颜色
        graphics.fillRect(0,0,width,height);//填充一个蓝色的矩形  填充的位置和大小
        //2.2画边框
        graphics.setColor(Color.BLUE);//设置颜色
        graphics.drawRect(0,0,width-1,height-1);//画边框
        //2.3写验证码
        graphics.setFont(new Font("宋体",Font.BOLD,25));
        String str ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";  //验证码包含的所有字符数字
        StringBuffer sb= new StringBuffer();
        Random random = new Random();//画验证码验证符
        for (int i = 1; i < 5; i++) {
            int s = random.nextInt(str.length());//随机获取字符串的角标，长度在字符串长度的范围内
            char c = str.charAt(s);//获取随机的字符
            graphics.drawString(c+"",i*20,25);//字符串的内容和位置
            sb.append(c);
        }
        String checkCode = sb.toString();
        HttpSession session = request.getSession();
        //将验证码存储到session中，用于登录后的判断。
        session.setAttribute("checkCode",checkCode);
        //2.4画干扰线
        graphics.setColor(Color.black);
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(100);
            int x2 = random.nextInt(100);
            int y1 = random.nextInt(50);
            int y2 = random.nextInt(50);
            graphics.drawLine(x1,y1,x2,y2);
        }

        //3.将图片输入到页面展示
        ImageIO.write(image,"jpg",response.getOutputStream());//输出对象，后缀名，输出流输出
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

