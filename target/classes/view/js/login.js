$(document).ready(function(){
	
	$(".layui-form-switch").click(function(){
		var status = $(".auto-login").attr("checked");
		if(undefined == status){
			$(".auto-login").attr("checked","checked");
		}else{
			$(".auto-login").removeAttr("checked");
		}
	})
	
	//判断数据是否为空
	function checkData(){
		var username = $("#username").val();
		var password = $("#password").val();
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
		}else if(verify == ""){
			checkText.text("请输入验证码");   
			check.show();
			return false;
		}else{
			checkText.text("");
			check.hide();
			return true;
		}
		
	}
	
	//登录
	function login(){
		if(checkData()){
			var username = $("#username").val();
			var password = $("#password").val();
			var verify = $("#verify").val();
			var auto = undefined==$(".auto-login").attr("checked")?false:true;
			//检查完成开始发送请求
			$.ajax({
				url:"/his/user/login",
				method:"POST",
				data:{
					"username":username,
					"password":password,
					"verify":verify,
					"auto":auto
				},
				success:function(e){
					if(e.msg == "登录成功"){
						//判断是否有flag
						if(undefined != e.flag){
							$.cookie("auto",e.flag,{expires:7,path:"/"});
						}
						//跳转页面
						alert("登录成功");
						window.location.replace("/his/view/user/desktop.html");
					}else{
						//登录失败 将内容展示出来
						var checkText = $("#check-text");
						var check = $(".check");
						checkText.text(e.msg);
						check.show();
						refreshVerify();
					}
				}
			})
			
			

		}
	}
	
	var verifyCount = 1;
	//点击验证码刷新
	$(".verify-image").click(function(){
		refreshVerify();
		
	})
	
	function refreshVerify(){
		$(".verify-image").attr("src","/his/verify?id="+verifyCount++);
	}
  
	//监听登录按钮
	$("#my-btn").click(function(){
		login();
	})
});