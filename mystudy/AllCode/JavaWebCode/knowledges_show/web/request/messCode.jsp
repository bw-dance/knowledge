<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/19
  Time: 21:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
</head>
<body>
<h1>乱码问题：</h1>
<form action="/servlet/mess-code/demo1" method="get">
    <h1>发送get请求</h1>
    用户名：<input name="username" type="text">
    <br>
    密码：<input name="password" type="text">
    <br>
    <input type="submit" value="提交">
</form>
<br>

<form action="/servlet/mess-code/demo1" method="post" accept-charset="UTF-8">
    <h1>发送post请求</h1>
    用户名：<input name="username" type="text">
    <br>
    密码：<input name="password" type="text">
    <br>
    <input type="submit" value="提交">
</form>


</body>
</html>
