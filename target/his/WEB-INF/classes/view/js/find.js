$(document).ready(function(){
	//检查表单完整性
	function checkForm(){
		var username = $("#username").val();
		var password = $("#password").val();
		var checkPassword = $("#check-password").val();
		var verify = $("#verify").val();
		
		var checkText = $("#check-text");
		var check = $(".check");
		
		if(username == ""){
			checkText.text("请输入用户名");
			check.show();
			return false;
		}else if(password == ""){
			checkText.text("请输入密码");
			check.show();
			return false;
		}else if(checkPassword == ""){
			checkText.text("请输入确认密码");
			check.show();
		}else if(verify == ""){
			checkText.text("请输入验证码");   
			check.show();
			return false;
		}else if(checkPassword != password){
			checkText.text("两次密码不一致");
			check.show();
			return false;
		}else{
			checkText.text("");
			check.hide();
			return true;
		}
	}
	
	//点击找回密码按钮
	$("#change-btn").click(function(){
		if(checkForm()){
			//表单内容没问题
			var username = $("#username").val();
			var password = $("#password").val();
			var checkPassword = $("#check-password").val();
			var verify = $("#verify").val();
			$.ajax({
				url:"/his/change",
				method:"POST",
				data:{
					"username":username,
					"password":password,
					"checkPassword":checkPassword,
					"verify":verify
				},
				success:function(e){
					var msg = e.msg;
					if(msg == "修改成功"){
						alert(msg);
						window.location.replace("login.html")
					}else{
						$("#check-text").text(msg);
						$(".check").show();
					}
				}
			})
		}
	});
	
	var verifyClickFlag = true;//可以点击获取验证码按钮
	//获取验证码的按钮
	$(".verify-btn").click(function(){
		if(verifyClickFlag == false) return;
		verifyClickFlag = false;
		//判断是否输入了账户
		var username = $("#username").val();
		if(username == ""){
			//为空 显示警告
			$(".check").show();
			$("#check-text").text("请输入账号/邮箱");
		}else{
			verifyClickFlag = false;//不可以点击获取验证码按钮
			$(".check").hide();
			//发送ajax给服务器发送返回密码的邮件
			$.ajax({
				url:"/his/find",
				method:"POST",
				data:{
					"username":username
				},
				success:function(e){
					var msg = e.msg;
					if(msg == "发送成功"){
						//发送成功
						$(".check").hide();//隐藏提示
						//发送成功后 发送验证码按钮倒计时60秒
						verifyTimer();
					}else{
						//没有发送成功 显示提示
						$(".check").show();
						$("#check-text").text(msg);
						verifyClickFlag = true;//发送失败 可以重新发送验证码
					}
				},
			});
		}
	});
	//获取验证码按钮发送成功后计时60秒后才可以使用
	function verifyTimer(){
		var count = 60;//60次 每秒执行一次 相当于60秒
		var timer = setInterval(function(){
			if(count == 0){
				clearInterval(timer);
				var verifyBtn = $(".verify-btn");
				verifyBtn.text("重新获取验证码");
				verifyBtn.attr("disabled",false);
			}else{
				count--;
				var verifyBtn = $(".verify-btn");
				verifyBtn.text(count);
				verifyBtn.attr("disabled",true);
			}
		},1000)
	}

});