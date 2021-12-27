<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/aishangboke/frame/layui-master/src/css/layui.css">
    <script src="/aishangboke/frame/layui-master/src/layui.js"></script>
    <%--    引入jq框架--%>
    <script src="/aishangboke/frame/jquery-3.4.1.min压缩文档.js"></script>
    <link rel="stylesheet" href="/aishangboke/css/writeblog.css">
    <script src="/aishangboke/js/writeblog.js"></script>

</head>

<body>
    <header>
        <c:if test="${requestScope.movement.equals('look')}">
            <h1 class="title">请查看您的爱尚博客</h1>
        </c:if>
        <c:if test="${requestScope.movement.equals('change')}">
            <h1 class="title">请修改您的爱尚博客</h1>
        </c:if>
        <c:if test="${requestScope.movement.equals('write')}">
            <h1 class="title">请编写您的爱尚博客</h1>
        </c:if>

    </header>

    <article class="arctile">

        <div class="buttons">
            <c:if test="${requestScope.movement.equals('look')&&requestScope.now=='myblog'}">
                <button class="layui-btn"  id="my-change">我要修改</button>
                <button class="layui-btn"  id="delete-now"><a class="delete-button" href="/aishangboke/deleteBlogServlet?id=${param.id}&now=myblog">立即删除</a></button>
            </c:if>


`

      </div>

        <c:if test="${requestScope.movement.equals('write')}">
               <form action="/aishangboke/addBlogServlet" method="post">
        </c:if>

        <c:if test="${requestScope.movement.equals('look')||requestScope.movement.equals('change')}">
            <form action="/aishangboke/updateBlogServlet" method="post">
                <input type="hidden" value="${blog.id}" name="id">
                <input type="hidden" value="${blog.author}" name="author">
                <input type="hidden" value=""${sessionScope.user.idcard} name="belong">
        </c:if>

                <c:if test="${requestScope.movement.equals('look')&&requestScope.now=='myblog'}">
                    <div class="layui-form-item">
                        <div class="layui-input-block submit-now">
                            <span>${requestScope.result}</span>
                            <button class="layui-btn" lay-submit lay-filter="formDemo" id="updatea">发布更新</button>
                        </div>
                    </div>
                </c:if>
                <c:if test="${requestScope.movement.equals('write')||requestScope.movement.equals('change')}">
                    <div class="layui-form-item">
                        <div class="layui-input-block submit-now">
                            <span>${requestScope.result}</span>
                            <button class="layui-btn" lay-submit lay-filter="formDemo" id="update">发布更新</button>
                        </div>
                    </div>
                </c:if>




            <div class="layui-form-item  text-content">
                <label class="layui-form-label">题目：</label>
                <div class="layui-input-block">
<%--                    判断请求是查看还是修改--%>
            <c:if test="${requestScope.movement.equals('look')}">
                <input id="title" disabled="disabled" type="text" name="title" required lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input" value="${blog.title}">
            </c:if>
                    <c:if test="${requestScope.movement.equals('change')}">
                        <input  type="text" name="title" required lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input" value="${blog.title}">
                        </c:if>
        <c:if test="${requestScope.movement.equals('write')}">
            <input  type="text" name="title" required lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input" value=${requestScope.title}>
        </c:if>

                </div>
            </div>

            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">文章内容：</label>
                <div class="layui-input-block">
                    <c:if test="${requestScope.movement.equals('look')}">
                        <textarea id="content" disabled="disabled" name="content" required lay-verify="required" placeholder="请输入" class="layui-textarea">${requestScope.blog.content}</textarea>
                    </c:if>
                    <c:if test="${requestScope.movement.equals('change')}">
                        <textarea name="content" required lay-verify="required" placeholder="请输入" class="layui-textarea">${requestScope.blog.content}</textarea>
                    </c:if>
                    <c:if test="${requestScope.movement.equals('write')}">
                        <textarea name="content" required lay-verify="required" placeholder="请输入" class="layui-textarea" >${requestScope.content}</textarea>
                    </c:if>

                </div>
            </div>

        </form>
    </article>
    <footer></footer>

</body>

</html>