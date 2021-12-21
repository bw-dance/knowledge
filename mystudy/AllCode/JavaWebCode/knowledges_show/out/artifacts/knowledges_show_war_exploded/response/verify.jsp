<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/20
  Time: 22:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script>
        window.onload = function () {
            //点击图片切换
            //获取图片
            let elementById = document.getElementById("img");
            //更改图片，其实就是更改图片的路径，因为我们的图片是随机生成的，所以只需要图片重新加载一次即可。
            elementById.onclick = function () {
                // elementById.src="/Response/checkCodeServlet";
                //这时你会发现点击图片没有变化，这是因为之前浏览器已经访问过这个路径了，此时路径的内容已经存储在了浏览器的内存中，所以访问的还是之前的图片
                // 解决措施：假意传参.   如果参数传入相同的值，那么就会造成只能切换一次。跟上面一个道理。所以要传一个随机的值
                //  elementById.src="/Response/checkCodeServlet?1";

                //因为随机数有时候也可能相同，所以遇到这种情况，我们可以通过把时间戳当做参数来解决。
                let date = new Date().getTime();
                elementById.src = "/servlet/reqs/verify?" + date;
                alert(data)
            }
            //点击文字切换
            let elementById1 = document.getElementById("change");
            elementById1.onclick = function () {
                let date = new Date().getTime();
                elementById.src = "/servlet/reqs/verify?" + date;
            }
        }
    </script>
</head>
<body>
<!--图片的点击切换-->
<img id="img" src="/servlet/reqs/verify">
<a id="change">看不清，换一张</a>
</body>
</html>
