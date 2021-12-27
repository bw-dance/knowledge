$(function () {
    window.onload = function () {
        //点击图片切换
        //获取图片
        $("img").click(function () {
            let date = new Date().getTime();
            $(this).attr("src","/aishangboke/checkCodeServlet?date="+date);
        })
        $(".identifying-change").click(function () {
            let date = new Date().getTime();
            $("img").attr("src","/aishangboke/checkCodeServlet?date="+date);
        })
    }
})