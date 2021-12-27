package web.filiter;

import web.domain.MessageState;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/addBlogServlet")
public class SensitivityFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

           //实体对象为req
            ServletRequest  servletRequest =(ServletRequest) Proxy.newProxyInstance(req.getClass().getClassLoader(), req.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //増强方法
                if (method.getName().equals("getParameter")){
                    //获取返回值
                    String values = (String)method.invoke(req, args);

                        if (values!=null){
                            for (String str:list){
                                if (values.contains(str)){ values=values.replace(str,"***");
                                }
                            }

                    }
                    return values;
                }
                //正常返回值
                return method.invoke(req,args);
            }
        });
        //2.放行,放行的为代理对象
        chain.doFilter(servletRequest, resp);


    }
private  List<String> list = new ArrayList<String>();
    public void init(FilterConfig config) throws ServletException {

        //获取文件路径
        try {
        ServletContext servletContext =config.getServletContext();
        String realPath = servletContext.getRealPath("/WEB-INF/classes/SensitiviteWords");
        System.out.println(realPath);
        BufferedReader bf = new BufferedReader(new FileReader(realPath));
        String line =null;
        while ((line=bf.readLine())!=null){
           list.add(line);
            System.out.println(line);
        }
        bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
