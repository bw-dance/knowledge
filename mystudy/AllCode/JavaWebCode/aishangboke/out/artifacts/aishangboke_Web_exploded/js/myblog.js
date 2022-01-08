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
    var seleteMessage = $(".select-message");
    var judge=false;

        $("#select-all").click(function () {
               console.log( $(".select-message"))
            seleteMessage.attr("checked",this.checked);
    })
           $("#delete-submit").click(function () {
               for (let i = 0; i <seleteMessage.length ; i++) {
                        if (seleteMessage[i].checked==true){
                            judge=true;
                                     break;
                        }
               }
               if (judge==true){
                   $("#from-table").submit();
               }else {
                   alert("请选择您要删除的博客");
               }
           })




});