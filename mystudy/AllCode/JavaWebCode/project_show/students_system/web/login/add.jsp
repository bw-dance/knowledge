<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/20
  Time: 12:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/students_system/add" method="post">
   username: <input type="text" name="username">
    password:  <input type="text" name="password">
    name: <input type="text" name="name">
<%--    dept_id:  <input type="text" name="dept_id">--%>
    age: <input type="text" name="age">
    height:   <input type="text" name="height">
    <input type="submit" value="登陆">
    </form>
</body>
</html>
