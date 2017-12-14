$(function  () {
	var doc = document,
	listArry = '';
	var map = new BMap.Map("depots-map");
	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
	var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
	map.addControl(top_left_control);        
	map.addControl(top_left_navigation);     
	map.addControl(top_right_navigation);   
	
	
	//初始化地图
	function depotMapInit (listPoint,val) {
		map.clearOverlays();
		var point = new BMap.Point(listPoint[0].lng,listPoint[0].lat);
		map.centerAndZoom(point, 11);
		map.enableScrollWheelZoom(true);
		// 编写自定义函数,创建标注
		function addMarker(point,info){
		  var marker = new BMap.Marker(point);
		  map.addOverlay(marker);
		  marker.addEventListener("mouseover", function(){
		  	this.openInfoWindow(info);
		  });
		  marker.addEventListener("mouseout", function(){
		  	this.closeInfoWindow();
		  });
		}
		function addpPyline (pointA,pointB,infoWindowLine) {
			var polyline = new BMap.Polyline([pointA,pointB], {strokeColor:"blue", strokeWeight:6, strokeOpacity:0.8});  //定义折线
			map.addOverlay(polyline);//添加折线到地图上
			polyline.addEventListener("mouseover", function(e){
				//console.log(e.point) //获取经过折线的当前坐标，触发覆盖物的事件返回值
				var point = new BMap.Point(e.point.lng,e.point.lat);
		  		map.openInfoWindow(infoWindowLine,point);
		  	});
		  	polyline.addEventListener("mouseout", function(){
		  		map.closeInfoWindow();
		  	});
		}
		function depotPylineInfo (listPointX,listPointY) {
			var pointA = new BMap.Point(listPointX.lng,listPointX.lat),
				pointB = new BMap.Point(listPointY.lng,listPointY.lat);					
			var sContentLine = "";
			sContentLine +='<div class="clearfix">';
			sContentLine +='<p style="float: left;">Distance:</p>';
			sContentLine +='<div style="float: left;">';
			sContentLine +='<p>'+listPointX.curLoc+' to '+listPointY.curLoc+" "+listPointX.calcDis+'km'+'</p>';
			sContentLine +='<p>'+listPointY.curLoc+' to '+listPointX.curLoc+" "+listPointX.calcDis+'km'+'</p>';
			sContentLine +='</div></div>';
			var infoWindowLine = new BMap.InfoWindow(sContentLine); // 创建信息窗口对象
			addpPyline(pointA,pointB,infoWindowLine);
		}
		//初始化坐标
		var p_len = listPoint.length;
		for (var j = 0;j<p_len;j++) {
			var points = new BMap.Point(listPoint[j].lng,listPoint[j].lat);
			//console.log(points)
			var sContent = "";
			sContent += '<p>ID: '+listPoint[j].curLoc+'</p>';
			sContent += '<p>Type: '+listPoint[j].siteType+'</p>';
			sContent += '<p>Name: '+listPoint[j].siteName+'</p>';
			var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
			//console.log(infoWindow)
			addMarker(points,infoWindow);
		}
		if (val == "") {
			//初始化路线
			for (var x = 0;x<p_len;x++) {
				for (var y = 0 ;y<p_len;y++) {
					var _curLoc = listPoint[x].curLoc,
						_nextCurLoc = listPoint[y].nextCurLoc;
					if (_curLoc == _nextCurLoc) {
						depotPylineInfo(listPoint[x],listPoint[y]);
					}
				}
			}
		}else{
			//根据网点 规划线路
			for (var a=0;a<p_len;a++) {
				if (listPoint[a].curLoc == val) {
					var index = a;
				}
			}
			for (var y=0;y<p_len;y++) {
				for (var x=0;x<p_len;x++) {
					var _nextCurLoc = listPoint[y].nextCurLoc,
						indexNextCurLoc = listPoint[index].nextCurLoc,
						_xCurLoc = listPoint[x].curLoc;
					if (val == _nextCurLoc) {
						depotPylineInfo(listPoint[index],listPoint[y]);
					}else if (indexNextCurLoc == _xCurLoc){
						depotPylineInfo(listPoint[index],listPoint[x]);
					}
				}
				
			}
		}
		
	}
	//排序函数
	function sortNumber(a,b) {
		return a.sequence - b.sequence;
	}
	
	function routeMapInit (listPoint,val) {
		map.clearOverlays();
		var point = new BMap.Point(listPoint[0].lng,listPoint[0].lat);
		map.centerAndZoom(point, 11);
		map.enableScrollWheelZoom(true);
		// 编写自定义函数,创建标注
		function addMarker(point,info){
		  var marker = new BMap.Marker(point);
		  map.addOverlay(marker);
		  marker.addEventListener("mouseover", function(){
		  	this.openInfoWindow(info);
		  });
		  marker.addEventListener("mouseout", function(){
		  	this.closeInfoWindow();
		  });
		}
		function addpPyline (pointA,pointB,infoWindowLine) {
			var polyline = new BMap.Polyline([pointA,pointB], {strokeColor:"blue", strokeWeight:6, strokeOpacity:0.8});  //定义折线
			map.addOverlay(polyline);//添加折线到地图上
			
			addArrow(polyline,10,Math.PI/7);
			
			polyline.addEventListener("mouseover", function(e){
				//console.log(e.point) //获取经过折线的当前坐标，触发覆盖物的事件返回值
				var point = new BMap.Point(e.point.lng,e.point.lat);
		  		map.openInfoWindow(infoWindowLine,point);
		  	});
		  	polyline.addEventListener("mouseout", function(){
		  		map.closeInfoWindow();
		  	});
		}
		function depotPylineInfo (listPointX,listPointY) {
			var pointA = new BMap.Point(listPointX.lng,listPointX.lat),
				pointB = new BMap.Point(listPointY.lng,listPointY.lat);					
			var sContentLine = "";
			sContentLine +='<div class="clearfix">';
			sContentLine +='<p style="float: left;">Distance:</p>';
			sContentLine +='<div style="float: left;">';
			sContentLine +='<p>'+listPointX.curLoc+' to '+listPointY.curLoc+" "+listPointX.calcDis+'km'+'</p>';
			sContentLine +='<p>'+listPointY.curLoc+' to '+listPointX.curLoc+" "+listPointX.calcDis+'km'+'</p>';
			sContentLine +='</div></div>';
			var infoWindowLine = new BMap.InfoWindow(sContentLine); // 创建信息窗口对象
			addpPyline(pointA,pointB,infoWindowLine);
		}
		//初始化坐标
		var p_len = listPoint.length;
		for (var j = 0;j<p_len;j++) {
			var points = new BMap.Point(listPoint[j].lng,listPoint[j].lat);
			//console.log(points)
			var sContent = "";
			sContent += '<p>ID: '+listPoint[j].curLoc+'</p>';
			sContent += '<p>Type: '+listPoint[j].siteType+'</p>';
			sContent += '<p>Name: '+listPoint[j].siteName+'</p>';
			sContent += '<p>Arrival Time: '+listPoint[j].arrTime+'</p>';
			sContent += '<p>Unload: '+listPoint[j].unloadVol+'</p>';
			sContent += '<p>Load: '+listPoint[j].sbVol+'</p>';
			sContent += '<p>Departure Time: '+listPoint[j].endTime+'</p>';
			var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
			//console.log(infoWindow)
			addMarker(points,infoWindow);
		}
		if (val) {
			//规划线路
			var routelist = [];
			for (var q=0;q<p_len;q++) {
				var routeNum = listPoint[q].routeCount;
				if (val == routeNum) {
					routelist.push(listPoint[q]);
				}
			}
			routelist.sort(sortNumber);
			console.log(routelist[3]);
			for (var i=0,rl_len = routelist.length;i<rl_len;i++) {
				if (i==rl_len-1) {
					continue;
				}
				depotPylineInfo(routelist[i],routelist[i+1]);
				//console.log(i)
			}
		}

		
	}
	
	//绘制箭头函数
	function addArrow(polyline,length,angleValue){ //绘制箭头的函数  

		var linePoint=polyline.getPath();//线的坐标串  
		
		var arrowCount=linePoint.length;  
		
		for(var i =1;i<arrowCount;i++){ //在拐点处绘制箭头  
		
		var pixelStart=map.pointToPixel(linePoint[i-1]);  
		
		var pixelEnd=map.pointToPixel(linePoint[i]);  
		
		var angle=angleValue;//箭头和主线的夹角  
		
		var r=length; // r/Math.sin(angle)代表箭头长度  
		
		var delta=0; //主线斜率，垂直时无斜率  
		
		var param=0; //代码简洁考虑  
		
		var pixelTemX,pixelTemY;//临时点坐标  
		
		var pixelX,pixelY,pixelX1,pixelY1;//箭头两个点  
		
		if(pixelEnd.x-pixelStart.x==0){ //斜率不存在是时  
		
		    pixelTemX=pixelEnd.x;  
		
		    if(pixelEnd.y>pixelStart.y)  
		
		    {  
		
		    pixelTemY=pixelEnd.y-r;  
		
		    }  
		
		    else  
		
		    {  
		
		    pixelTemY=pixelEnd.y+r;  
		
		    }     
		
		    //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法  
		
		    pixelX=pixelTemX-r*Math.tan(angle);   
		
		    pixelX1=pixelTemX+r*Math.tan(angle);  
		
		    pixelY=pixelY1=pixelTemY;  
		
		}  
		
		else  //斜率存在时  
		
		{  
		
		    delta=(pixelEnd.y-pixelStart.y)/(pixelEnd.x-pixelStart.x);  
		
		    param=Math.sqrt(delta*delta+1);  
		
		  
		
		    if((pixelEnd.x-pixelStart.x)<0) //第二、三象限  
		
		    {  
		
		    pixelTemX=pixelEnd.x+ r/param;  
		
		    pixelTemY=pixelEnd.y+delta*r/param;  
		
		    }  
		
		    else//第一、四象限  
		
		    {  
		
		    pixelTemX=pixelEnd.x- r/param;  
		
		    pixelTemY=pixelEnd.y-delta*r/param;  
		
		    }  
		
		    //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法  
		
		    pixelX=pixelTemX+ Math.tan(angle)*r*delta/param;  
		
		    pixelY=pixelTemY-Math.tan(angle)*r/param;  
		
		  
		
		    pixelX1=pixelTemX- Math.tan(angle)*r*delta/param;  
		
		    pixelY1=pixelTemY+Math.tan(angle)*r/param;  
		
		}  
		
		  
		
		var pointArrow=map.pixelToPoint(new BMap.Pixel(pixelX,pixelY));  
		
		var pointArrow1=map.pixelToPoint(new BMap.Pixel(pixelX1,pixelY1));  
		
		var Arrow = new BMap.Polyline([  
		
		    pointArrow,  
		
		 linePoint[i],  
		
		    pointArrow1  
		
		], {strokeColor:"blue", strokeWeight:8, strokeOpacity:0.8});  
		
		map.addOverlay(Arrow);  
		
		}  

}
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
	            "lengthMenu":[100000],//每页显示数据条数菜单
	            "ajax":{
	                url:"/depots/depots.json", //获取数据的URL
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
	                    "visible":true
	                },
	                {
	                    "targets":[1,2,3,4,5],
	                    "orderable":false
	                }
	            ],
	            "language":{
	                "lengthMenu":"Show _MENU_ Record",
	                "search":"Search:",
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
	            	//console.log(data.data.length);
	            	if (data.data.length !=0) {
	            		var result = data.data,
	            		listPoint = [],
	            		len = result.length;
	            		$('#route-depot').empty();
	            		$('#route-depot').off("click");
	            		$('#route-depot').append('<option value="0">All Depots</option>');
	            		for (var i=0;i<len;i++) {
	            			var liser = {};
	            			var add='<option value='+result[i].curLoc+'>'+result[i].curLoc+'</option>';
							$('#route-depot').append(add);
							liser["curLoc"] = result[i].curLoc;
							liser["siteType"] = result[i].siteType;
							liser["siteName"] = result[i].siteName;
							liser["calcDis"] = result[i].calcDis;
							liser["lng"] = result[i].siteLongitude;
							liser["lat"] = result[i].siteLatitude;
							liser["nextCurLoc"] = result[i].nextCurLoc
							listPoint.push(liser);
	            		}
						//查询所有网点坐标
						//console.log(listPoint)
						listArry="";
						listArry = listPoint;
							
						//百度地图
						depotMapInit(listPoint,"");

	            	}

	            	
	            },
	            "drawCallback":function  (settings, data) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        var Datas = api.rows( {page:'current'} ).data();
			        //console.log(Datas.length);
			        var _len = Datas.length;
			        if (_len == 1) {
			        	var res = Datas[0];
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

	            }
	        });
	        //获取
	        //点击选项 来查询
	        var table = $('#SolutionDeport').DataTable();
			$(document).on("change","#route-depot",function  () {
				var val = $('#route-depot').val();
				//console.log(listArry)
				if (val == 0) {
					depotMapInit(listArry,"");
					table.search("").draw(false);
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
				}else{
					table.search(val).draw(false);
					depotMapInit(listArry,val);
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
	//求时间
	function operationTime (data) {
		var result = parseInt(data),
	    	h = parseInt(result/60),
	    	m = result%60;
	    	return add0(h)+":"+add0(m);
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
	            "lengthMenu":[100000],//每页显示数据条数菜单
	            "ajax":{
	                url:"/route/route.json", //获取数据的URL
	                type:"get" //获取数据的方式
	                
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":function  (res) {
	                	//console.log(res)
	                	return "Route "+add0(res.routeCount);
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
	                	return operationTime(res.arrTime);
	                },"name":"arr_time"},
	                {"data":function  (res) {
	                	return "Unload "+res.unloadVol+" , "+"Load "+res.sbVol;
	                },"name":"unload_vol&sb_vol"},
	                {"data":function  (res) {
	                	return operationTime(res.endTime);
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
	            	if (data.data.length != 0) {
	            		var result = data.data,
	            		arr = [],
	            		listPoint = [],
	            		len = result.length;
	            		$('#route-route').empty();
	            		$('#route-route').off("click");
	            		for (var i=0;i<len;i++) {
	            			arr.push(result[i].routeCount);
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
	            		
	            		for (var i=0;i<len;i++) {
	            			var liser = {};
	            			liser["routeCount"] = result[i].routeCount;
							liser["curLoc"] = result[i].curLoc;
							liser["siteType"] = result[i].siteType;
							liser["siteName"] = result[i].siteName;
							liser["calcDis"] = result[i].calcDis;
							liser["lng"] = result[i].siteLongitude;
							liser["lat"] = result[i].siteLatitude;
							liser["nextCurLoc"] = result[i].nextCurLoc;
							var arrTime = operationTime(result[i].arrTime);
							var endTime = operationTime(result[i].endTime);
							liser["arrTime"] = arrTime;
							liser["endTime"] = endTime;
							liser["sbVol"] = result[i].sbVol;
							liser["unloadVol"] = result[i].unloadVol;
							liser["sequence"] = result[i].sequence;
							listPoint.push(liser);
	            		}
	            		console.log(listPoint);
	            		listArry="";
						listArry = listPoint;
	            		
	            		var table = $('#SolutionRoute').DataTable();
	            		setTimeout(function(){
	            			table.search(_val).draw();
	            			routeMapInit(listPoint,_val)
	            		},0)
	            		
	            	}
	            	
	            },
	            "drawCallback":function  (settings) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        var data = api.rows( {page:'current'} ).data();
			        //console.log(data)
			        
	            }
	        });
	        //点击选项 来查询
	        var table = $('#SolutionRoute').DataTable();
			$(document).on("change","#route-route",function  () {
				var val = $('#route-route').val(),
					_text = "Route "+add0(val);
					console.log(val)
				table.search(val).draw(false);
	            $('#route-name').text(_text);
				routeMapInit(listArry,val);
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
	            "lengthMenu":[100000],//每页显示数据条数菜单
	            "ajax":{
	                url:"/vehicles/vehicles.json", //获取数据的URL
	                type:"get" //获取数据的方式
	                
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":function(res) {
	                	//console.log(res)
	                	return res.car_name;
	                },"name":"car_type"},
	                {"data":"sequence","name":"sequence"},
	                {"data":"curLoc","name":"cur_loc"},
	                {"data":"siteName","name":"site_name"},
	                {"data":"siteAddress","name":"site_address"},
	                {"data":function(res) {
	                	var result = parseInt(res.arrTime),
	                	h = parseInt(result/60),
	                	m = result%60;
	                	return add0(h)+":"+add0(m);
	                },"name":"arr_time"},
	                {"data":function(res) {
	                	return "Unload "+res.unloadVol+" , "+"Load "+res.sbVol;
	                },"name":"unload_vol&sbVol"},
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
	            	if (data.data.length != 0) {
	            		var result = data.data,
	            		arr = [],
	            		len = result.length;
	            		$('#route-vehicles').empty();
	            		$('#route-vehicles').off("click");
	            		for (var i=0;i<len;i++) {
	            			arr.push(result[i].car_name);
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