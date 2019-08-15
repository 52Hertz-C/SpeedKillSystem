function doLogin(){
    g_showLoading();

    var inputPass = $("#password").val();
    var salt = g_passsword_salt;
    var str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
    var password = md5(str);

    $.ajax({
        url: "/login/doLogin",
        type: "POST",
        data:{
            mobile:$("#mobile").val(),
            password: password
        },
        success:function(data){
            layer.closeAll();
            if(data.code == 0){
                layer.msg("成功");
                window.location.href="/goods/toGoods";
            }else{
                layer.msg(data.message);
            }
        },
        error:function(){
            layer.closeAll();
        }
    });
}