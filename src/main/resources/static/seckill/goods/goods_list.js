function messageRegister() {
    var userid = $("#userid").val();
    window.location.href='/message/list?userid='+userid;
}