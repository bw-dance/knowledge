$(document).ready(function() {
    var a = document.getElementById("my-home");
    a.onclick = function() {
        console.log("153132121")
    }

    $("#my-home").hover(function() {
        $("#my-child").css("display", "block");
    }, function() {
        $("#my-child").css("display", "none");
    })

    $("#my-child").hover(function() {
        $("#my-child").css("display", "block");
    }, function() {
        $("#my-child").css("display", "none");
    })

    $("#select-all").click(function() {
        var messageArray = $(".select-message");
        console.log(messageArray)
        for (let a = 0; a < messageArray.length; a++) {
            messageArray[a].checked = this.checked;
        }
    })
    $(".delete-other").click(function () {
        alert("违规操作，请确定该blog是否为您的blog");
    })
    $(".delete-blog").click(function () {
        alert("删除成功");
    })
});