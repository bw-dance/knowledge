<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/20
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/students_system/css/login.css">
</head>
<body>
<form action="/students_system/loginThird" method="post">
    用户名：<input type="text" name="username">
    密码：<input type="text" name="password">
    <input class="sub-but" type="submit" value="登陆">
    <br>
    ${message}
</form>
</body>
</html>
