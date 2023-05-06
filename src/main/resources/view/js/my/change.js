$(document).ready(function(){
	$("#my-btn").click(function(){
		changePass();
	})
	//修改密码
	function changePass () {
		if(!checkData()){//数据不完整直接结束
			return;
		}
		var oldPass = $("#old-pass").val();
		var newPass = $("#new-pass").val();
		var checkPass = $("#check-pass").val();
		$.post("/his/my/update",
		{
			oldPassword:oldPass,
			password:newPass,
			checkPassword:checkPass,
		},function(e){
			var msg = e.msg;
			if(msg == "请登录"){
				alert("请登录");
				window.location.replace("/his/view/login.html");
			}else if(msg == "修改成功"){
				alert("修改成功");
				$("#old-pass").val("");
				$("#new-pass").val("");
				$("#check-pass").val("");
				$(".check").hide();//隐藏提示
			}else{
				//没有成功等等 显示提示
				$(".check").show();
				$("#check-text").text(msg);
			}
		});
	}
	//检查数据完整性
	function checkData(){
		var oldPass = $("#old-pass").val();
		if(undefined == oldPass || oldPass == ""){
			$(".check").show();
			$("#check-text").text("请输入旧密码");
			return false;
		}else{
			$(".check").hide();
		}
		var newPass = $("#new-pass").val();
		if(undefined == newPass || newPass == ""){
			$(".check").show();
			$("#check-text").text("请输入新密码");
			return false;
		}else{
			$(".check").hide();
		}
		var checkPass = $("#check-pass").val();
		if(undefined == checkPass || checkPass == ""){
			$(".check").show();
			$("#check-text").text("请输入确认密码");
			return false;
		}else{
			$(".check").hide();
		}
		return true;
	}
})