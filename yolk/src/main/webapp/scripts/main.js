"use strict";
$("#signupBtn").click(function () {
    var n = $("form").serialize(), e = $.ajax({
        type: "POST", data: n, url: "/user/signup.json", success: function (n) {
            console.log(n), alert("Signup Succeed!")
        }
    });
    e.error(function () {
        alert("Something went wrong")
    })
}), $("#loginBtn").click(function () {
    var n = $("form").serialize(), e = $.ajax({
        type: "POST", data: n, url: "/user/login.json", success: function (n) {
            console.log(n), alert("login Succeed")
        }
    });
    e.error(function () {
        alert("Something went wrong")
    })
});