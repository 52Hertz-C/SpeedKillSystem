$(function() {// 初始化内容
    $("#mobile").val("");
    $("#password").val("");
    // $("#verifyCodeImg").attr("src", "/codeImg/verifyCodeRegister");
    // $("#verifyCodeImg").show();

    //登录输入框效果
    $('.form_text_ipt input').focus(function(){
        $(this).parent().css({
            'box-shadow':'0 0 3px #bbb',
        });
    });
    $('.form_text_ipt input').blur(function(){
        $(this).parent().css({
            'box-shadow':'none',
        });
        //$(this).parent().next().hide();
    });

    //表单验证
    $('.form_text_ipt input').bind('input propertychange',function(){
        if($(this).val()==""){
            $(this).css({
                'color':'red',
            });
            $(this).parent().css({
                'border':'solid 1px red',
            });
            //$(this).parent().next().find('span').html('helow');
            $(this).parent().next().show();
        }else{
            $(this).css({
                'color':'#ccc',
            });
            $(this).parent().css({
                'border':'solid 1px #ccc',
            });
            $(this).parent().next().hide();
        }
    });
});

function refreshVerifyCode(){
    $("#verifyCodeImg").attr("src", "/login/verifyCodeRegister");
};

function checkPhone(phone){
    if(!(/^1[34578]\d{9}$/.test(phone))){
        alert("手机号码有误，请重填");
        return false;
    }
}

function register_go() {
    var mobile =$("#mobile").val();
    var inputPass = $("#password").val();
    var passwordrepeat = $("#repassword").val();
    var verifyCode = $("#verifyCode").val();
    var check = checkPhone(mobile);
    if(check){
        alert("手机号不符合要求");
        return ;
    }
    if(inputPass==""||inputPass==null||inputPass == undefined ){
        alert("密码不能为空！");
        return ;
    }
    if(mobile==""||mobile==null||mobile == undefined ){
        alert("手机号不能为空！");
        return ;
    }
    if(inputPass!=passwordrepeat){
        alert("密码不一致！");
    }
    if(verifyCode!=verifyCode){
        alert("验证码不能为空！");
    }
    var salt = g_passsword_salt;
    var str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
    var password = md5(str);
    g_showLoading();
    $.ajax({
        url: "/user/register",
        type: "POST",
        data:{
            mobile:mobile,
            password: password,
            salt:salt,
            verifyCode:verifyCode
        },
        success:function(data){
            layer.closeAll();
            if(data.code == 0){
                layer.msg("成功");
                window.location.href="/goods/to_list";
            }else{
                layer.msg(data.message);
            }
        },
        error:function(){
            layer.closeAll();
        }
    });
}