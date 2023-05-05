$(document).ready(function(){
	//重置表单
	$("#my-reset").click(function(){
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
	});
	//登录
	$("#my-btn").click(function(){
		var uname = $("#uname").val();
		var zjm = $("#zjm").val();
		var email = $("#email").val();
		var age = $("#age").val();
		var sex = $('input[name="sex"]:checked').val();
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
				console.log(e);
			}
		})
	})
})