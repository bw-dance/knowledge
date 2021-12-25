package servlet;

import entity.Article;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname BlogServlet
 * @Description TODO
 * @Date 2021/12/25 16:42
 * @Created by DELL
 */
@WebServlet("/all-blog")
public class BlogServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Article article = new Article(i,"第"+i+"篇Blog","这是我的Blog"+i,null,null,null,null);
            articleList.add(article);
        }
        req.setAttribute("articles",articleList);
        req.getRequestDispatcher("/homepage.jsp").forward(req,resp);
    }
}
