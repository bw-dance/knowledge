<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/17
  Time: 13:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
s
<body>
<h1>Hello servlet</h1>
<a href="/servlet/demo1">please enter</a>

<div>
    <a href="/servlet/blog/add">添加blog</a>
    <br>
    <a href="http://localhost:8899/servlet/blog/delete">删除blog</a>
    <br>
    <a href="/servlet/blog/update">修改blog</a>
    <br>
    <a href="/servlet/blog/query">查询blog</a>
</div>

<div>
    <h1>post请求</h1>
    <form action="/servlet/demo2" method="post">
        <input name="username">
        <input name="password">
        <input type="submit" value="提交">
    </form>
    <h1>get请求</h1>
    <form action="/servlet/demo2" method="get">
        <input name="username">
        <input name="password">
        <input type="submit" value="提交">
    </form>
</div>
</body>
</html>
