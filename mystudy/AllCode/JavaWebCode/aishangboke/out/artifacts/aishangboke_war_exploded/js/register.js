layui.use('laydate', function() {
    var laydate = layui.laydate;

    //执行一个laydate实例
    laydate.render({
        elem: '#test1' //指定元素
    });
});
$(function () {
    $("#username").blur(function () {

            let username = $(this).val();

        $.get("/aishangboke/findUserServlet",{username:username},function (data) {
            //判断
            var span = $("#username-judge");

            if(data.userExit){
                //用户存在
                span.css("color","green!importment")
                alert(span)
                span.text("该用户名可以使用")
            }else{
                //用户不存在
                span.css("color","red")
             console.log(span)
                span.text("该用户名不可使用")
            }
            //如果此处接收类型不写json，那么默认返回的还是字符串类型。所以一定要声明
        },"json")
    })



})