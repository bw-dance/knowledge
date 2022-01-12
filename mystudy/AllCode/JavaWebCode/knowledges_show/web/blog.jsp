<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/19
  Time: 13:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
    </script>
    <script>
        // var a = document.getElementById("del-blog");
        // a.onClick({
        //     fun()
        // });
        // function fun() {
        //     $.ajax({
        //         url: "/servlet/blog",//请求路径
        //         type: "PUT",//请求方式
        //         data: {"username": "jack", "password": "12"},
        //         //请求成功
        //         success: function (data) {
        //             alert(data);
        //         },
        //         //请求失败
        //         error: function () {
        //             alert("出错了")
        //         },
        //         //设置接收到的响应数据的格式
        //         dateType: "text"
        //     })
        //
        // }

    </script>
</head>
<body>


<form action="/servlet/blog" method="post">
    <h1>发送post请求</h1>
    用户名：<input name="username" type="text">
    <br>
    &nbsp&nbsp密码：<input name="password" type="text">
    <br>
    <input type="submit" value="提交">
</form>


<form action="/servlet/blog" method="delete">
    <h1>发送delete请求</h1>
    用户名：<input name="username" type="text">
    <br>
    &nbsp&nbsp密码：<input name="password" type="text">
    <br>
    <input type="submit" value="提交">
</form>


<form action="/servlet/blog" method="put">
    <h1>发送put请求</h1>
    用户名：<input name="username" type="text">
    <br>
    &nbsp&nbsp密码：<input name="password" type="text">
    <br>
    <input id="del-blog" type="submit" value="提交">
</form>


<form action="/servlet/blog" method="get">
    <h1>发送get请求</h1>
    用户名：<input name="username" type="text">
    <br>
    &nbsp&nbsp密码：<input name="password" type="text">
    <br>
    <input type="submit" value="提交">
</form>


</body>
</html>
