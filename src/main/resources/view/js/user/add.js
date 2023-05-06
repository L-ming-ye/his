$(document).ready(function(){
	//重置表单
	$("#my-reset").click(function(){
		resetForm();
	});
	
	function resetForm(){
		$("#uname").val("");
		$("#zjm").val("");
		$("#email").val("");
		$("#age").val("");
		$('input[type="radio"][name="sex"][value="0"]').attr("checked",true);
		// 重新渲染
		layui.use('form', function() {
		    var form = layui.form;
		    form.render();
		});
	}
	
	//新增
	$("#my-btn").click(function(){
		if(!checkData()){//数据不完整结束
			return;
		}
		var uname = $("#uname").val();
		var zjm = $("#zjm").val();
		var email = $("#email").val();
		var age = $("#age").val();
		var sex = $(".layui-form-radioed").prev().val();
		$.ajax({
			url:"/his/user/add",
			method:"POST",
			data:{
				uname:uname,
				zjm:zjm,
				email:email,
				age:age,
				sex:sex
			},
			success:function(e){
				var msg = e.msg;
				if(msg == "请登录"){
					alert("请登录");
					window.location.replace("/his/view/login.html");
				}else if(msg == "创建用户成功"){
					alert(msg);
					resetForm();//重置表单
					$(".check").hide();//隐藏提示
				}else{
					//没有成功等等 显示提示
					$(".check").show();
					$("#check-text").text(msg);
				}
			}
		})
	})
	//检查数据完整性
	function checkData(){
		var uname = $("#uname").val();
		if(undefined == uname || uname == ""){
			$(".check").show();
			$("#check-text").text("请输入用户名");
			return false;
		}else{
			$(".check").hide();
		}
		var zjm = $("#zjm").val();
		if(undefined == zjm || zjm == ""){
			$(".check").show();
			$("#check-text").text("请输入助记码");
			return false;
		}else{
			$(".check").hide();
		}
		var email = $("#email").val();
		if(undefined == email || email == ""){
			$(".check").show();
			$("#check-text").text("请输入邮箱");
			return false;
		}else{
			$(".check").hide();
		}
		var age = $("#age").val();
		if(undefined == age || age == ""){
			$(".check").show();
			$("#check-text").text("请输入年龄");
			return false;
		}else{
			$(".check").hide();
		}
		
		return true;
	}
})