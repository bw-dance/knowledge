package web.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageSearch {
    private int page;//当前页数
    private int pageNumber;//每页有多少条数据
    private List<Blog> blogs;//返回的数据
    private  int totalContent;//一共有多少条数据
    private  int totalPage; //一共有多少页

    public PageSearch() {
    }

    public PageSearch(int page, int pageNumber, List<Blog> blogs, int totalContent, int totalPage) {
        this.page = page;
        this.pageNumber = pageNumber;
        this.blogs = blogs;
        this.totalContent = totalContent;
        this.totalPage = totalPage;
    }


    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }



    public int getTotalContent() {
        return totalContent;
    }

    public void setTotalContent(int totalContent) {
        this.totalContent = totalContent;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "PageSearch{" +
                "page=" + page +
                ", pageNumber=" + pageNumber +
                ", blogs=" + blogs +
                ", totalContent=" + totalContent +
                ", totalPage=" + totalPage +
                '}';
    }
}
