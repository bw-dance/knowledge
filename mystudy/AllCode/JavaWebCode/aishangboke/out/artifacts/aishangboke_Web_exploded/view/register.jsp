<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <!-- 引入bootstrape框架 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
    <!-- 引入layDate框架 -->
    <script src="/aishangboke/frame/layui-master/src/layui.js"></script>
<%--    引入jq框架--%>
    <script src="/aishangboke/frame/jquery-3.4.1.min压缩文档.js"></script>
    <link rel="stylesheet" href="/aishangboke/css/register.css">
    <script src="/aishangboke/js/register.js">
    </script>
</head>

<body>
    <header>
        <h1 class="title">爱尚博客欢迎您注册</h1>
    </header>
    <article>
        <form method="post" action="/aishangboke/registerUserServlet">
            <div class="form-group">
                <label >姓名：</label>
                <input type="text" class="form-control" name="name">

            </div>
            <fieldset class="form-group">
                <div class="row">
                    <legend class="col-form-label col-sm-2 pt-0">性别</legend>
                    <div class="col-sm-10">
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="gridRadios1" value="男" checked>
                            <label class="form-check-label" for="gridRadios1">
              男
              </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="gridRadios2" value="女">
                            <label class="form-check-label" for="gridRadios2">
               女
              </label>
                        </div>

                    </div>
                </div>
            </fieldset>
            <div>
                <label>日历：</label>
                <input type="text" id="test1" class="form-control" name="birth">
            </div>

            <div class="form-group">
                <label>身份证号：</label>
                <input type="text" class="form-control" name="idcard">
                <small class="form-text judge" >您已有申请账号</small>

            </div>
            <div class="form-group">
                <label>用户名：</label>
                <input type="text" class="form-control" name="username" id="username">
                <small class="form-text judge" id="username-judge"></small>
            </div>
            <div class="form-group">
                <label for="exampleInputPassword1">密码：</label>
                <input type="password" class="form-control" id="exampleInputPassword1" name="password">

            </div>



            <div class="form-group row">
                <div class="col-sm-10">
                    <button type="submit" class="btn btn-primary">注册</button>
                </div>
            </div>
        </form>

    </article>
    <footer>

    </footer>

</body>

</html>