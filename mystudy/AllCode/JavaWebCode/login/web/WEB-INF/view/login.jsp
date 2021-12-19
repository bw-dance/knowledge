<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
<%--    引入jquery--%>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
        <script src="/aishangboke/js/login.js"></script>
    <link rel="stylesheet" href="/aishangboke/css/login.css">
</head>

<body>
<header>
    <h1 class="title">爱尚博客欢迎您的登陆</h1>
</header>
<article>
    <form action="/aishangboke/loginUserServlet">
        <div class="form-group">
            <label>用户名：</label>
            <input type="text" class="form-control" name="username" value="${sessionScope.username}">
        </div>
        <div class="form-group">
            <label for="exampleInputPassword2">密码：</label>
            <input type="password" class="form-control" id="exampleInputPassword2" name="password"
                   value="${sessionScope.password}">
            <small class="form-text text-muted">${requestScope.messageStatePass.judge}</small>
        </div>
        <div class="form-group">
            <label for="exampleInputPassword1">验证码：</label>
            <input type="password" class="form-control identifying-code" id="exampleInputPassword1" name="checkCode">
            <a href="javascript:void(0);"><img class="identifying-img" src="/aishangboke/checkCodeServlet" alt=""></a>
            <a href="javascript:void(0);" class="identifying-change">看不清？换一张</a>
            <a href="/aishangboke/view/register.jsp" class="register-user">注册账号</a>
            <small id="emailHelp" class="form-text text-muted">${requestScope.messageState.judge}</small>
        </div>
        <button type="submit" class="btn btn-primary">登录</button>
    </form>

</article>
<footer>

</footer>

</body>

</html>