<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>登录页面</title>
  <style>
    *{
      padding: 0;
      margin: 0;
    }
    body{
      min-width: 1280px;
      min-height: 800px;
      overflow: hidden;
      background-image: url("./image/R-C.jfif");
      background-repeat: no-repeat;
      background-size: 100% 80%;


    }
    .body-div{

      margin: 0 auto;

    }
    .intro-div{
      margin-top: 150px;
      width: 100%;
      margin-bottom: 100px;
    }
    .login-div{
      width: 100%;
      margin: 0 auto;
      text-align: center;

    }
    .intro{
      text-align: center;
    }
    .from-div{
      display: inline-block;

    }
    ul{
      list-style-type: none;
    }
    li{
      display: block;
      width: 500px;
      height: 80px;
      position: relative;
    }
    .login-input{
      font-size: 25px;
      width: 120px;
      height: 80px;
      display: inline-block;
      text-align: right;
    }
    .login-verify{
      width: 80px;
    }
    .verify-img{
      width: 80px;
      height: 35px;
      vertical-align: middle;
    }
    .input-con{
      height: 30px;
    }
    #verift-update{
      font-size: 10px;
      right: 43px;
      top: 25px;
      position: absolute;
      color: blue;
      cursor: pointer;
      text-decoration: underline;
    }
    #verift-update:hover{
      color: purple;

    }
    #login-button{
      width: 100px;
      height: 40px;
      background: white;
      border: black solid 3px;
      color: skyblue;
      cursor: pointer;
      line-height: 40px;
      text-align: center;
    }
    #login-button:hover{
      color: burlywood;
    }

  </style>
</head>
<body>
<div class="body-div">
  <div class="intro-div">
    <h1 class="intro">欢迎登陆Blog系统</h1>
  </div>
  <div class="login-div">
    <form class="from-div">
      <ul>
        <li>
          <span class="login-input">账号：</span>
          <input type="text" class="input-con">
        </li>
        <li>
          <span class="login-input"> 密码：</span>
          <input type="password" class="input-con">
        </li>
        <li>
          <span class="login-input">验证码：</span>
          <input type="text" class="input-con login-verify">

          <img class="verify-img" src="./image/cat.jpeg">
          <span id="verift-update">点击切换</span>

        </li>
        <li>
          <input type="button"id="login-button" value="登录">
        </li>
      </ul>
    </form>
  </div>
</div>
</body>
</html>