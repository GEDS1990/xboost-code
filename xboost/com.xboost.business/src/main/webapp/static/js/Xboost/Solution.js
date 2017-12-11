$(function  () {
	var doc = document;
	
	/*
	 *deport.jsp == SolutionActivityController
	 * 
	 * */
	function add0(m){return m<10?'0'+m:m };
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
	                url:"/depots/operateInfo.json", //获取数据的URL
	                type:"get" //获取数据的方式
	                
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"curLoc","name":"cur_loc"},
	                {"data":"carType","name":"car_type"},
	                {"data":function  (res) {
	                	var result = parseInt(res.arrTime),
	                	h = parseInt(result/60),
	                	m = result%60;
	                	return add0(h)+":"+add0(m);
	                },"name":"arr_time"},
	                {"data":function  (res) {
	                	return "Unload "+res.unloadVol+" , "+"Load "+res.sbVol;
	                },"name":"unloadVol & sbVol"},
	                {"data":function  (res) {
	                	var result = parseInt(res.endTime),
	                	h = parseInt(result/60),
	                	m = result%60;
	                	return add0(h)+":"+add0(m);
	                },"name":"end_time"}
	                
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
	            	//console.log(data);
	            	if (data.data) {
	            		var result = data.data,
	            		len = result.length;
	            		$('#route-depot').empty();
	            		$('#route-depot').off("click");
	            		$('#route-depot').append('<option value="0">All Depots</option>');
	            		for (var i=0;i<len;i++) {
	            			var add='<option value='+result[i].curLoc+'>'+result[i].curLoc+'</option>';
							$('#route-depot').append(add);
	            		}
						//查询所有网点坐标
						$.get("/depots/allSite.json").done(function(res){
							console.log(res)
							//百度地图
							var map = new BMap.Map("depots-map");
							var point = new BMap.Point(120.98738,31.391479);
							map.centerAndZoom(point, 15);
							// 编写自定义函数,创建标注
							function addMarker(point){
							  var marker = new BMap.Marker(point);
							  map.addOverlay(marker);
							}
							// 随机向地图添加25个标注
							var bounds = map.getBounds();
							var sw = bounds.getSouthWest();
							var ne = bounds.getNorthEast();
							var lngSpan = Math.abs(sw.lng - ne.lng);
							var latSpan = Math.abs(ne.lat - sw.lat);
							for (var i = 0; i < 25; i ++) {
								var point = new BMap.Point(sw.lng + lngSpan * (Math.random() * 0.7), ne.lat - latSpan * (Math.random() * 0.7));
								addMarker(point);
							}
							
							
							
						}).fail(function(){
							console.log("fail")
						});
	            	}
	            	
	            },
	            "drawCallback":function  (settings, data) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        //console.log( api.rows( {page:'current'} ).data() );
	            }
	        });
	        //获取
	        //点击选项 来查询
	        var table = $('#SolutionDeport').DataTable();
			$(document).on("click","#route-depot",function  () {
				var val = $('#route-depot').val();
				if (val == 0) {
					table.search("").draw(false);
				}else{
					table.search(val).draw(false);
					$.get("/depots/baseInfo.json",{"siteCode":val}).done(function  (res) {
						console.log(res)
						if (res) {
							$('#depot').text("Depot "+res.siteCode);
	                        $('#east').text(res.siteLatitude);
	                        $('#north').text(res.siteLongitude);
	                        $('#name').text(res.siteName);
	                        $('#address').text(res.siteAddress);
	                        $('#type').text(res.siteType);
	                        $('#distrib-center').text(res.distribCenter);
	                        $('#area').text(res.siteArea);
	                        $('#vehicle-quantity-limit').text(res.carNum);
	                        $('#vehicle-weight-limit').text(res.largeCarModel);
	                        $('#piece-capacity').text(res.maxOperateNum);
						}else{
							$('#depot').text("No Data");
	                        $('#east').text("--");
	                        $('#north').text("--");
	                        $('#name').text("--");
	                        $('#address').text("--");
	                        $('#type').text("--");
	                        $('#distrib-center').text("--");
	                        $('#area').text("--");
	                        $('#vehicle-quantity-limit').text("--");
	                        $('#vehicle-weight-limit').text("--");
	                        $('#piece-capacity').text("--");
						}
                        
                    }).fail(function  (e) {
                        $('#depot').text("No Data");
                        $('#east').text("--");
                        $('#north').text("--");
                        $('#name').text("--");
                        $('#address').text("--");
                        $('#type').text("--");
                        $('#distrib-center').text("--");
                        $('#area').text("--");
                        $('#vehicle-quantity-limit').text("--");
                        $('#vehicle-weight-limit').text("--");
                        $('#piece-capacity').text("--");
                  });
				}
				
			});
	        
	        
	        
	        
		}
		
	}());
	
	/**
	 *route.jsp == SolutionRouteController
	 *
	 */
	//去除重复数组元素
	function unique(array){
        var r = [];
        for(var i = 0, l = array.length; i<l; i++){
            for(var j = i + 1; j < l; j++)
                if(array[i] == array[j]){
                	j == ++i;
                } 
                r.push(array[i]);
        }
        return r;
    }
	(function  () {
		var SolutionRoute = doc.getElementById("SolutionRoute");
		if (SolutionRoute) {
			//加载列表
			var dt =$("#SolutionRoute").DataTable({
	            "processing": true, //loading效果
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
	                	console.log(res)
	                	return "Route "+add0(res.id);
	                },"name":"route_count"},
	                {"data":function(res){return res.sequence;},"name":"sequence"},
	                {"data":function(res){return res.curLoc},"name":"cur_loc"},
	                {"data":function  (res) {
	                	//console.log(res)
	                	return res.siteName
	                },"name":"site_name"},
	                {"data":function  (res) {
	                	return res.siteAddress;
	                },"name":"site_address"},
	                {"data":function  (res) {
	                	var result = parseInt(res.arrTime),
	                	h = parseInt(result/60),
	                	m = result%60;
	                	return add0(h)+":"+add0(m);
	                },"name":"arr_time"},
	                {"data":function  (res) {
	                	return "Unload "+res.unloadVol+" , "+"Load "+res.carGoods;
	                },"name":"unload_vol&car_goods"},
	                {"data":function  (res) {
	                	var result = parseInt(res.endTime),
	                	h = parseInt(result/60),
	                	m = result%60;
	                	return add0(h)+":"+add0(m);
	                },"name":"end_time"},
	                {"data":function  (res) {
	                	return res.nextCurLoc+","+res.calcDis+"km";
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
	            	//console.log(data);
	            	if (data.data) {
	            		var result = data.data,
	            		arr = [],
	            		len = result.length;
	            		$('#route-route').empty();
	            		$('#route-route').off("click");
	            		for (var i=0;i<len;i++) {
	            			arr.push(result[i].id);
	            		}
	            		var Arr = unique(arr),
	            		A_len = Arr.length;
	            		for (var j=0;j<A_len;j++) {
	            			var add='<option value='+Arr[j]+'>'+"Route "+add0(Arr[j])+'</option>';
							$('#route-route').append(add);
	            		}
	            		var _val = $('#route-route').find("option").eq(0).val(),
	            		_text = $('#route-route').find("option").eq(0).text();
	            		$('#route-name').text(_text);
	            		
	            		var table = $('#SolutionRoute').DataTable();
	            		console.log(table)
	            		setTimeout(function(){
	            			table.search(_val).draw();
	            		},0)
	            	}
	            	
	            },
	            "drawCallback":function  (settings) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        var data = api.rows( {page:'current'} ).data();
			        //console.log(data);
//			        $('#route-name').text("Route 00"+data[0].routeCount);
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
                    $.get("/route/route.json",{"routeId":val}).done(function  (res) {
                        console.log(res)
                        if (res) {
                        debugger;
                            $('#route-name').text("Route "+res.id);
                            $('#total-distance').text(res.sbVol);
                            $('#vehicle-load-requirement').text(res.car_type);
                            $('#vehicle-piece-capacity').text(res.calcDis);
                            $('#speed-requirement').text(res.calcDis);
                        }else{
                            $('#route-name').text("No Data");
                            $('#total-distance').text("--");
                            $('#vehicle-load-requirement').text("--");
                            $('#vehicle-piece-capacity').text("--");
                            $('#speed-requirement').text("--");
                        }

                    }).fail(function  (e) {
                      console.log('fail');
                  });
				}

			});
		}
		
	}());
	


/**
	 *Vehicles.jsp == SolutionVehiclesController
	 *
	 */
	(function  () {
		var SolutionVehicles = doc.getElementById("SolutionVehicles");
		if (SolutionVehicles) {
			//加载列表
			var dt = $("#SolutionVehicles").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "destroy": true,
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/vehicles/vehicles.json", //获取数据的URL
	                type:"get" //获取数据的方式
	                
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":function(res) {
	                	console.log(res)
	                	return res.carType;
	                },"name":"car_type"},
	                {"data":function(res){return res.sequence;},"name":"sequence"},
	                {"data":function(res){return res.curLoc;},"name":"cur_loc"},
	                {"data":function(res) {return res.siteName;},"name":"site_name"},
	                {"data":function(res) {return res.siteAddress;},"name":"site_address"},
	                {"data":function(res) {
	                	var result = parseInt(res.arrTime),
	                	h = parseInt(result/60),
	                	m = result%60;
	                	return add0(h)+":"+add0(m);
	                },"name":"arr_time"},
	                {"data":function(res) {
	                	return "Unload "+res.unloadVol+" , "+"Load "+res.carGoods;
	                },"name":"unload_vol&car_goods"},
	                {"data":function(res) {
	                	var result = parseInt(res.endTime),
	                	h = parseInt(result/60),
	                	m = result%60;
	                	return add0(h)+":"+add0(m);
	                },"name":"end_time"},
	                {"data":function(res) {
	                	return res.nextCurLoc+","+res.calcDis+"km";
	                },"name":"nextCurLoc&calcDis"}
	                
	            ],
	            "columnDefs":[ //具体列的定义
	            	{
	                    "targets":[0],
	                    "visible":false
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
	            	var $this = this;
	            	console.log(data);
	            	if (data.data) {
	            		var result = data.data,
	            		arr = [],
	            		len = result.length;
	            		$('#route-vehicles').empty();
	            		$('#route-vehicles').off("click");
	            		for (var i=0;i<len;i++) {
	            			arr.push(result[i].carType);
	            		}
	            		var Arr = unique(arr),
	            		A_len = Arr.length;
	            		for (var j=0;j<A_len;j++) {
	            			var add='<option value='+Arr[j]+'>'+Arr[j]+'</option>';
							$('#route-vehicles').append(add);
	            		}
	            		var val = $('#route-vehicles').find("option").eq(0).val(),
	            		_text = $('#route-vehicles').find("option").eq(0).text();
	            		$('#route-name').text(_text);
	            		var table = $('#SolutionVehicles').DataTable();
	            		setTimeout(function(){
	            			table.search(val).draw();
	            		},0)
	            		
	            	}
	            	
	            },
	            "drawCallback":function  (settings) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        var data = api.rows( {page:'current'} ).data();
			        console.log(data);
	            }
	        });
	        //点击选项 来查询
	        var table = $('#SolutionVehicles').DataTable();
			$(document).on("change","#route-vehicles",function  () {
				console.log(this.value)
				table.search(this.value).draw();
				$('#route-name').text(this.value);
			});

		}
		
	}());

	
});