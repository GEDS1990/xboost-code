$(function  () {
	var doc = document;
	
	/*
	 *deport.jsp == SolutionActivityController
	 * 
	 * */
	(function  () {
		var SolutionDeport = doc.getElementById("SolutionDeport");
		if (SolutionDeport) {
			//加载列表
			var dt =$("#SolutionDeport").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "destroy": true,
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/activity/activity.json", //获取数据的URL
	                type:"get" //获取数据的方式
	                
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"pickupLoc","name":"pickup_loc"},
	                {"data":function  (res) {
	                	return "type"
	                }},
	                {"data":"arrTime","name":"Arr_time"},
	                {"data":"vol","name":"vol"},
	                {"data":"endTime","name":"end_time"}
	                
	            ],
	            "columnDefs":[ //具体列的定义
	            	{
	                    "targets":[0],
	                    "visible":false
	                },
	                {
	                    "targets":[1,2,3,4,5],
	                    "orderable":false
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
	            },
	            "initComplete": function (settings, data) {
	            	console.log(data);
	            	if (data.data) {
	            		var result = data.data,
	            		len = result.length;
	            		$('#route-depot').empty();
	            		$('#route-depot').off("click");
	            		$('#route-depot').append('<option value="0">All Depots</option>');
	            		for (var i=0;i<len;i++) {
	            			var add='<option value='+result[i].pickupLoc+'>'+result[i].pickupLoc+'</option>';
							$('#route-depot').append(add);
	            		}
	            	}
	            	
	            }
	        });
	        //点击选项 来查询
	        var table = $('#SolutionDeport').DataTable();
			$(document).on("click","#route-depot",function  () {
				var val = $('#route-depot').val();
				if (val == 0) {
					table.search("").draw(false);
				}else{
					table.search(val).draw(false);
				}
				
			});
	        
	        
	        
	        
		}
		
	}());
	
	/**
	 *route.jsp == SolutionRouteController
	 *
	 */
	(function  () {
		var SolutionRoute = doc.getElementById("SolutionRoute");
		if (SolutionRoute) {
			//加载列表
			var dt =$("#SolutionRoute").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "destroy": true,
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/route/route.json", //获取数据的URL
	                type:"get" //获取数据的方式
	                
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":function  (res) {
	                	return "Route 00"+res.routeCount;
	                },"name":"route_count"},
	                {"data":"sequence","name":"sequence"},
	                {"data":"curLoc","name":"cur_loc"},
	                {"data":function  (res) {
	                	return "name";
	                }},
	                {"data":function  (res) {
	                	return "address";
	                }},
	                {"data":"arrTime","name":"arr_time"},
	                {"data":function  (res) {
	                	return "Unload "+res.unloadVol+" , "+"Load "+res.carGoods;
	                },"name":"unload_vol&car_goods"},
	                {"data":"endTime","name":"end_time"},
	                {"data":function  (res) {
	                	return res.nextCurLoc+","+res.calcDis;
	                },"name":"nextCurLoc&calcDis"}
	                
	            ],
	            "columnDefs":[ //具体列的定义
	            	{
	                    "targets":[0],
	                    "visible":false
	                },
	                {
	                    "targets":[1,2,3,4,5],
	                    "orderable":false
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
	            },
	            "initComplete": function (settings, data) {
	            	console.log(data);
	            	if (data.data) {
	            		var result = data.data,
	            		len = result.length;
	            		$('#route-route').empty();
	            		$('#route-route').off("click");
	            		for (var i=0;i<len;i++) {
	            			var add='<option value='+result[i].curLoc+'>'+result[i].curLoc+'</option>';
							$('#route-route').append(add);
	            		}
	            	}
	            	
	            }
	        });
	        //点击选项 来查询
	        var table = $('#SolutionRoute').DataTable();
			$(document).on("click","#route-route",function  () {
				var val = $('#route-route').val();
				if (val == 0) {
					table.search("").draw(false);
				}else{
					table.search(val).draw(false);
				}
				
			});
	        
	        
	        
	        
		}
		
	}());
	
});