package servlet_ready_lession.servlet2021_12_21.filehandle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Classname dd
 * @Description TODO
 * @Date 2021/12/20 22:48
 * @Created by DELL
 */
@WebServlet("/file")
public class FileHandle extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取请求参数，文件名称
        String fileName = request.getParameter("filename");
        System.out.println(fileName); //photo

        //2.使用字节流，加载文件进内存
        //2.1找到文件所在的服务器路径
        ServletContext servletContext = request.getServletContext();
        String realPath = servletContext.getRealPath("/img/" + fileName);//图片的真是路径
        //2.2使用字节流关联
        FileInputStream fis = new FileInputStream(realPath);
        //自己理解为，通过此步骤，可以获取从浏览器把文件保存到本地的权限


        //3.设置response响应头
        //3.1设置响应头的数据类型：content-type  //因为我们也不知道文件的类型，所以需要设置。
        String mimeType = servletContext.getMimeType(fileName);
        response.setHeader("content-type", mimeType);
        //3.1.1解决中文文件名问题


        //获取user-agent请求头
        String agent = request.getHeader("user-agent");
        //3.2设置响应头的打开方式
        response.setHeader("content-disposition", "attachment;filename=" +  DownLoadUtils.getFileName(agent, fileName));//filename设置的为下载提示框的名字

        //4.将输入流的数据写出到输出流中
        ServletOutputStream sos = response.getOutputStream();
        byte[] buff = new byte[1024 * 8];
        int len = 0;
        while ((len = fis.read(buff)) != -1) {
            sos.write(buff, 0, len);
        }
        fis.close();//输入流关闭
    }
}
