<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/aishangboke/frame/layui-master/src/css/layui.css">
    <script src="/aishangboke/frame/layui-master/src/layui.js"></script>
    <link rel="stylesheet" href="/aishangboke/css/success.css">
    <script src="/aishangboke/js/success.js"></script>
</head>

<body>
    <header>
        <h1 class="title">${URLDecoder.decode(requestScope.messageState.judge,"UTF-8")}<i class="layui-icon layui-icon-ok" style="font-size: 30px; color: #1E9FFF;"></i><a class="read-blog" href="${requestScope.messageState.url}" class="">${requestScope.messageState.urlName}</a></h1>
<c:if test="${requestScope.register!='register'}">
    <h2 class="title"><a href="/aishangboke/pageSearchServlet">查看我的所有博客</a></h2>
    <h2 class="title"><a href="/aishangboke/pageSearchAllServlet">查看更多博客</a></h2>
</c:if>

    </header>
    <footer></footer>

</body>

</html>