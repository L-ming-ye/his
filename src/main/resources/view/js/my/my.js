$(document).ready(function(){
	//获取用户信息
	function getUser(){
		$.get("/his/my/get",function(e){
			var user = e.data;
			$(".my-name").text(user.uname);
			$(".my-zjm").text(user.zjm);
			$(".my-age").text(user.age);
			$(".my-email").text(user.email);
			$(".my-sex").text(user.sex==0?'男':user.sex==1?'女':'未知');
			var time = formatTime(user.createTime);
			$(".my-time").text(time==null?'未知':time);
			
		})
	};
	getUser();
	
	//格式化时间
	function formatTime(time){
		if(undefined == time || null == time){
			return null;
		}
		var t = new Date(time);
		var year = compareNum(t.getFullYear());
		var month = compareNum(t.getMonth()+1);
		var day = compareNum(t.getDate());
		return year + "-" + month + "-" + day;
	}
	
	//比较是否大于十
	function compareNum(num){
		if(num < 10){
			return "0"+num;
		}
		return num;
	}
	
});