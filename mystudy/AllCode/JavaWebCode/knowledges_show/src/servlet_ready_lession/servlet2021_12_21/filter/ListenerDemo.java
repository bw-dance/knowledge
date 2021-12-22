package servlet_ready_lession.servlet2021_12_21.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @Classname ListenerDemo
 * @Description TODO
 * @Date 2021/12/22 9:09
 * @Created by DELL
 */
@WebListener
public class ListenerDemo implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //加载资源
        System.out.println("监听器，执行了。");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //释放资源
    }
}
