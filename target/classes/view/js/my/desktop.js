$(document).ready(function(){
	//点击了导航栏里的按钮
	$(".my-nav-item").click(function(){
		bodyHides();//隐藏所有页面（div）
		//获取导航栏里的url
		var url = $(this).attr("url");
		if(undefined == url || "" == url){
			return;
		}
		//判断是否有对应的标签
		if($('div[url="'+url+'"]').length>0){
			//存在对应的标签直接显示
			$('div[url="'+url+'"]').show();
		}else{
			//不存在
			//创建一个div存当前页面
			var divBox = '<div url="'+url+'"></div>';
			$(".layui-body").append(divBox);
			// $(".layui-body").children("div").attr("url",url);
			//将指定的页面存入div
			$('div[url="'+url+'"]').show().load(url);
		}
	});
	//查看个人信息
	$(".my").click(function(){
		bodyHides();
		if($(".my-box").length>0){
			$(".my-box").show();
		}else{
			var divBox = '<div class="my-box"></div>';
			$(".layui-body").append(divBox);
			$(".my-box").show().load("/his/view/my/my.html");
		}
	});
	//修改密码
	$(".change").click(function(){
		bodyHides();
		if($(".change-box").length>0){
			$(".change-box").show();
		}else{
			var divBox = '<div class="change-box"></div>';
			$(".layui-body").append(divBox);
			$(".change-box").show().load("/his/view/my/change.html");
		}
	});
	
	//退出
	$(".exit").click(function(){
		$.get("/his/my/exit",function(e){
			if(e.msg == "退出成功"){
				//删除cookie
				$.cookie("auto", "", {expires: -1,path:"/"});
				window.location.replace("/his/view/login.html");
			}
		})
	})

	
	//隐藏主体的所有div
	function bodyHides(){
		var childrens = $(".layui-body").children().hide();
	};
});