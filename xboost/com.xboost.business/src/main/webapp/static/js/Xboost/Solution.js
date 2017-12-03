$(function  () {
	var doc = document;
	
	/*
	 *route.jsp == SolutionRouteController
	 * 
	 * */
	(function  () {
		var SolutionRoute = doc.getElementById("SolutionRoute");
		if (SolutionRoute) {
			//加载列表
			var dt =$("#SolutionRoute").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/route/route.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"scenariosId","name":"scenariosId"},
	                {"data":"routeCount","name":"route_count"},
	                {"data":"carType","name":"car_type"},
	                {"data":"location","name":"location"},
	                {"data":"sequence","name":"sequence"},
	                {"data":"curLoc","name":"cur_loc"},
	                {"data":"type","name":"type"},
	                {"data":"sbLoc","name":"sb_loc"},
	                {"data":"sbVol","name":"sb_vol"},
	                {"data":"arrTime","name":"arr_time"},
	                {"data":"endTime","name":"end_time"},
	                {"data":"unloadLoc","name":"unload_loc"},
	                {"data":"unloadVol","name":"unload_vol"},
	                {"data":"nextCurLoc","name":"next_cur_loc"},
	                {"data":"calcDis","name":"calc_dis"},
	                {"data":"carGoods","name":"car_goods"}
	            ],
	            "columnDefs":[ //具体列的定义
	            	{
	                    "targets":[0],
	                    "visible":false
	                },
	                {
	                    "targets":[2],
	                    "orderable":false
	                },
	                {
	                    "targets":[3,4],
	                    "orderable":true
	                }
	            ],
	            "language":{
	                "lengthMenu":"Show _MENU_ Record",
	                "search":"Search:",
	                "info": "There are  _TOTAL_ records From _START_ To _END_",
	                "processing":"Loading...",
	                "zeroRecords":"No Data",
	                "infoEmpty": "There are 0 records from 0 to 0",
	                "infoFiltered":"(Read from _MAX_ record)",
	                "paginate": {
	                    "first":      "First",
	                    "last":       "Last",
	                    "next":       "Next",
	                    "previous":   "Prev"
	                }
	            }
	        });
		}
		
	})()
});