<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
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
                <input type="password" class="form-control" id="exampleInputPassword2" name="password" value="${sessionScope.password}">
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