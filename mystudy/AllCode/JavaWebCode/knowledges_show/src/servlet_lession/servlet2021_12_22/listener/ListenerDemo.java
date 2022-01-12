package servlet_lession.servlet2021_12_22.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ListenerDemo implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //加载资源
        System.out.println("服务器启动了。监听器，执行了。");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //释放资源.
        System.out.println("服务器关闭了。监听器，执行了。");
    }
}
