<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>

    <link rel="stylesheet" href="/aishangboke/frame/layui-master/src/css/layui.css">
    <link rel="stylesheet" href="/aishangboke/css/myblog.css">
    <script src="/aishangboke/frame/layui-master/src/layui.js"></script>
    <script src="/aishangboke/frame/jquery-3.4.1.min压缩文档.js"></script>
    <script src="/aishangboke/js/myblog.js"></script>

</head>

<body>
    <header>
        <h1 class="title">我的爱尚博客</h1>
    </header>
    <article class="article">
        <form id="from-table" method="post" action="/aishangboke/deleteSelectServlet?source=myBlog">
<%--        博客表头--%>
        <ul class="layui-nav" lay-filter="">
            <li class="layui-nav-item"><input type="checkbox" class="all-select" name="select-all" id="select-all" /><span>全选</span></li>
            <li class="layui-nav-item layui-this"><span>博客名称</span></li>
            <li class="layui-nav-item"><span>发布人</span></li>
            <li class="layui-nav-item"><span>发布时间</span></li>
            <li class="layui-nav-item"><span>博客链接</span></li>
            <li class="layui-nav-item"><span>操作</span></li>
            <li class="layui-nav-item">
                <span id="my-home" class="my-blog">我的博客</span>
                <dl class="layui-nav-child" id="my-child">
                    <!-- 二级菜单 -->
                    <dd><a href="/aishangboke/lookBlogServlet?state=write">写博客</a></dd>
                    <dd><a href="/aishangboke/pageSearchAllServlet">更多博客</a></dd>
                </dl>
            </li>

        </ul>
<%--    数据条数展示--%>
        <h5>共搜到${pageSearch.totalContent}条数据，共${pageSearch.totalPage}页</h5>
        <div class="tables">
            <table class="layui-table">


                <tbody>
<%--根据搜索到的数据进行遍历，输入到表格中--%>
                <c:forEach items="${pageSearch.blogs}" var="blog" varStatus="s">
                    <tr>
                        <td><input type="checkbox" id="${blog.id}" name="blogId" class="select-message" value="${blog.id}">${s.count}</td>
                        <td>${blog.title}</td>
                        <td>${blog.author}</td>
                        <td>${blog.upDate}</td>
                        <td><a href="/aishangboke/lookBlogServlet?id=${blog.id}&state=look&now=myblog">查看博客</a></td>

<%--                        单条记录的删除修改--%>
                        <td>
                            <button type="button" class="layui-btn"><a href="/aishangboke/lookBlogServlet?id=${blog.id}&state=change">修改</a></button>
                            <button type="button" class="layui-btn" id="delete-button"  >
<%--                                <a href="javascript:deleteMessage()">删除</a>--%>

    <a href="/aishangboke/deleteBlogServlet?id=${blog.id}&page=${pageSearch.page}&now=myblog">删除</a>
                            </button>
                        </td>

                    </tr>

                </c:forEach>

                </tbody>
            </table>
        </div>



        </form>
    </article>
    <footer>
        <div class="foot-content">
            <div class="delete-selectAll">
                <button type="button" class="layui-btn layui-btn-sm delete-all-button">
                    <a class="delete-url" id="delete-submit">删除所选</a>
                </button>
            </div>
            <%--        数据的切换--%>
            <div class="button-array">
                <div class="layui-btn-group">
                    <button type="button" class="add-number layui-btn layui-btn-sm add-number">
                        <a class="layui-icon" href="/aishangboke/pageSearchServlet?page=${pageSearch.page-1>0?pageSearch.page-1:1}">&nbsp<&nbsp</a>
                    </button>
                    <c:forEach begin="1" end="${pageSearch.totalPage<=9?pageSearch.totalPage:10}" var="b" step="1" varStatus="s">
                        <button type="button" class="add-number layui-btn layui-btn-sm">
                            <a href="/aishangboke/pageSearchServlet?page=${b}" class="layui-icon">${b}</a>
                        </button>

                    </c:forEach>
                    <button type="button" class="add-number layui-btn layui-btn-sm">

                        <a class="layui-icon" href="/aishangboke/pageSearchServlet?page=${pageSearch.page+1<=pageSearch.totalPage?pageSearch.page+1:1}">&nbsp>&nbsp</a>

                    </button>


                </div>

            </div>
        </div>


    </footer>




</body>

</html>