$(document).ready(function(){
	
	//重置表单
	function reset(){
		$("#rname").val("");
	}
	
	$("#my-reset").click(function(){
		reset();
	})
	
	//点击新增按钮 发送请求
	$("#my-btn").click(function(){
		//判断是否输入了角色名字
		var rname = $("#rname").val();
		if(rname == null || rname == ""){
			$(".check").show();
			$("#check-text").text("请输入角色名字");
			return;
		}
		//有名字发送请求
		$.post({
			url:"/his/role/add",
			data:{
				rname
			},
			success:function(e) {
				if(e.msg == "创建成功"){
					alert("创建成功");
					//清空内容
					reset();
					$(".check").hide();
					$("#check-text").text("");
				}else{
					$(".check").show();
					$("#check-text").text(e.msg);
				}
			}
		})
	})
})