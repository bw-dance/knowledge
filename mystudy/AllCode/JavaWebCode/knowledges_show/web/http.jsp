<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/19
  Time: 10:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/servlet/css/http.css">
</head>

<body>
<a href="/servlet/sep">发送请求</a>
<form action="/servlet/sep" method="get">
    <h1>发送get请求</h1>
    用户名：<input name="username" type="text">
    <br>
    密码：<input name="password" type="text">
    <br>
    <input type="submit" value="提交">
</form>
<br>

<form action="/servlet/sep" method="post">
    <h1>发送post请求</h1>
    用户名：<input name="username" type="text">
    <br>
    密码：<input name="password" type="text">
    <br>
    <input type="submit" value="提交">
</form>

</body>
</html>
