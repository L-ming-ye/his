$(document).ready(function(){
	
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
	
	layui.use('table', function(){
	  var table = layui.table;
	  //第一个实例
	  table.render({
		elem: '#role-data'
		,height: 312
		,url: 'http://localhost:8080/his/role/gets' //数据接口
		,method:"POST"
		,limit:5
		,limits:[5,10,20]
		,parseData: function(res){ // res 即为原始返回的数据
		    return{
				"code":res.code,
				"count":res.data.count,
				"data":res.data.data,
				"limit":res.data.limit,
				"page":res.data.page
			}
		  }
		,page: {
			layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'],
			groups:1,
		}
		,cols: [[ //表头
		  {type: 'checkbox',field:'rid',templet:
			function(d){
				var value = "<div stytle='display:none'>"+d.rid+"<div>";
				return value;
			}
		  }
		  ,{field: 'rname', title: '角色名字', minwidth:80}
		  ,{field: 'createTime', title: '创建时间', minwidth:80,templet:
			function(d){
				var time = formatTime(d.createTime);
				return time==null?'无':time;
			}
		  }
		  ,{field: 'createUid', title: '创建人', width:80,templet:
			function(d){
				return d.createUid==null?'无':d.createUid.uname==null?'无':d.createUid.uname;
			}
		  }
		  ,{field: 'updateTime', title: '更新时间', minwidth:80,templet:
			function(d){
				var time = formatTime(d.updateTime);
				return time==null?'无':time;
			}
		  }
		  ,{field: 'updateUid', title: '修改人', width:80,templet:
			function(d){
				return d.updateUid==null?'无':d.updateUid.uname==null?'无':d.updateUid.uname;
			}
		  }
		  ,{field: 'status', title: '状态', width:60,templet:
			function(d){
				var status = d.status == 0?"正常":d.status==1?"禁用":"异常";
				return status;
			}
		  },
		  ,{title:"操作",width:224,templet:'#operate-box'}
		]]
	  });
	});

})