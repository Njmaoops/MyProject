//放一些页面公共的代码
function getUrlValue(key) {
    // location.search 就是后缀内容
    // location.search = ?id&...
    let params = location.search;
    if (params.length > 1) {
        params = location.search.substring(1); // ? 被去掉了
        let paramArr = params.split("&"); // 每一个& 被去掉了 , 并且以 & 分割
        for (let i = 0; i < paramArr.length; i++) {
            let kv = paramArr[i].split("="); // 每个键值对 被分割
            if (kv[0] === key) {
                return kv[1];
            }
        }
    }
    return "";
}

//注销功能
function logout() {
    if (confirm("确认是否要注销?")) {
        jQuery.ajax({
            url: "user/logout",
            type: 'POST',
            data: {},
            success: function (result) {
                if (result != null && result.code === 1) {
                    alert("注销成功");
                    location.href = "/blog_login.html";
                } else {
                    alert("注销失败");
                }
            }
        });
    }
}
