<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/19
  Time: 14:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/servlet/req/demo2" method="get">
  <input type="username" name="username" placeholder="用户名">
  <br>
  <input type="username" name="password" placeholder="密码">
  <input type="checkbox" name="hobby" value="篮球">篮球
  <input type="checkbox" name="hobby" value="足球">足球
  <input type="checkbox" name="hobby" value="橄榄球">橄榄球
  <br>
  <input type="submit" value="提交">
</form>

</body>
</html>
