<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/aishangboke/frame/layui-master/src/css/layui.css">
    <script src="/aishangboke/frame/layui-master/src/layui.js"></script>
    <link rel="stylesheet" href="/aishangboke/css/changeblog.css">
    <script src="/aishangboke/js/changeblog.js"></script>
</head>

<body>
    <header>
        <h1 class="title">请编写您的爱尚博客</h1>

    </header>

    <article class="arctile">

        <form class="layui-form" action="">
            <div class="layui-form-item">
                <div class="layui-input-block submit-now">
                    <button class="layui-btn" lay-submit lay-filter="formDemo">立即修改</button>

                </div>
            </div>
            <div class="layui-form-item  text-content">
                <label class="layui-form-label">题目：</label>
                <div class="layui-input-block">
                    <input type="text" name="title" required lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">文章内容：</label>
                <div class="layui-input-block">
                    <textarea name="desc" placeholder="请输入内容" class="layui-textarea"></textarea>
                </div>
            </div>

        </form>
    </article>
    <footer></footer>

</body>

</html>