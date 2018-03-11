$(document).ready(function(){
	var map = new BMap.Map("depots-map");
	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
	var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
	map.addControl(top_left_control);        
	map.addControl(top_left_navigation);     
	map.addControl(top_right_navigation);
	
	var doc = document;
	var planType = false;
	//depot search 
	function depotSearch (selement,val){
		var table = $(selement).DataTable();
		table.search(val).draw(false);
	};
	//去除重复数组元素
	function unique(arr) {
		var result = [], hash = {};
		for (var i = 0, elem; (elem = arr[i]) != null; i++) {
			if (arr[i] == "") {
				continue;
			}
			if (!hash[elem]) {
				result.push(elem);
				hash[elem] = true;
			}
		}
		return result;
	}
	//去除重复数据 deport
	//将对象元素转换成字符串以作比较
	function obj2key(obj, keys){
	    var n = keys.length,
	        key = [];
	    while(n--){
	        key.push(obj[keys[n]]);
	    }
	    return key.join('|');
	}
	//去重操作
	function uniqeByKeys(array,keys){
	    var arr = [];
	    var hash = {};
	    for (var i = 0, j = array.length; i < j; i++) {
	        var k = obj2key(array[i], keys);
	        if (!(k in hash)) {
	            hash[k] = true;
	            arr .push(array[i]);
	        }
	    }
	    return arr ;
	};
	//排序 
	function Sort_sequence (arr) {
		arr.sort(function  (a,b) {
			return a.sequence - b.sequence;
		});
		return arr;
	};
	function Sort_rideid (arr) {
		arr.sort(function  (a,b) {
			return a.RideId - b.RideId;
		});
		return arr;
	};
	function RideId_List (result,rideidlist) {
		var len1 = result.length;
    	var len2 = rideidlist.length;
    	var list = [];
    	for (var i=0;i<len2;i++)
    	{
    		var item = {};
    		var l = [];
    		for (var j=0;j<len1;j++)
    		{
    			if (rideidlist[i].RideId == result[j].RideId) 
    			{
    				var items = {};
    				items.sequence = result[j].sequence;
    				items.curLoc = result[j].curLoc;
    				items.carType = result[j].carType;
    				l.push(items);
    			}
    		}
    		item.RideId = rideidlist[i].RideId;
    		item.carList = rideidlist[i].carList;
    		item.carType = rideidlist[i].carType;
    		item.val = Sort_sequence(l);
    		list.push(item);
    		Sort_rideid(list);
    	}
    	return list;
	};
	//求时间
	function add0(m){return m<10?'0'+m:m };
	function operationTime (data) {
		var reg = /^\d+(\.\d+)?$/;
		if (reg.test(data)) {
			var result = parseInt(data),
		    	h = parseInt(result/60),
		    	m = result%60;
	    	return add0(h)+":"+add0(m);
		}else{
			return data;
		}
		
		
	};
	function add00(m){
		var r;
		if (m<10) 
		{
			r = '00'+m;
		}
		else if (m>=10 && m<100)
		{
			r = '0'+m;
		}
		else
		{
			r=m;
		}
		return r;
	};
	function conjoin (arr) {
		var len = arr.length;
		var r = "";
		if (len !=0)
		{
			for (var i=0;i<len;i++)
			{
				var add = i==len-1?arr[i].curLoc:arr[i].curLoc + " >> ";
				r += add;
			}
		}
		return r;
	};
	function creatSelect (arr) {
		var len = arr.length;
		var add = '';
		add += '<option></option>'
		for (var i=0;i<len;i++)
		{
			add += '<option value='+arr[i]+'>'+arr[i]+'</option>';
		}
		return add;
	};
	/*
	function creatEle (id,data) {
		var p = doc.getElementById(id);
		var r_tbody = doc.createElement('tbody');
		var len = data.length;
		if (len !=0) 
		{
			for (var i=0;i<len;i++)
			{
				var add='';
				var r_tr = doc.createElement('tr');
				add += '<td>Ride '+add00(data[i].RideId)+'</td>';
				add += '<td><span class="plancar"><span>'+conjoin(data[i].val)+'</span><button class="btn btn-primary j-car-plan-btn" data-rideid='+data[i].RideId+'>View on Map</button>'+'</span></td>';
				add += '<td>'+data[i].carType+'</td>';
				add += '<td><span class="chosen">Chosen:</span><span class="chosen-data">--</span> <select style="width:30%">'+creatSelect(data[i].carList)+'</select> <button class="btn btn-primary" id="j-save-car">Submit</button></td>';
				r_tr.innerHTML = add;
				r_tbody.appendChild(r_tr);
			}
			p.appendChild(r_tbody);
		}
	};*/
	function creatEle (id,data) {
		var p = doc.getElementById(id);
		var r_tbody = doc.createElement('tbody');
		var len = data.length;
		//console.log(data)
		if (len !=0) 
		{
			$('.j-car-plan-btn').off('click');
			$('.j-save-car').off('click');
			for (var i=0;i<len;i++)
			{
				var add='';
				var r_tr = doc.createElement('tr');
				add += '<td>Ride '+add00(data[i].RideId)+'</td>';
				add += '<td><span class="plancar"><span>'+(data[i].depotOrder)+'</span><button data-style="slide-right" class="btn btn-primary j-car-plan-btn ladda-button" data-rideid='+data[i].RideId+'><span class="ladda-label">View on Map</span></button>'+'</span></td>';
				add += '<td>'+data[i].carType+'</td>';
				add += '<td><span class="plan-wrap"><span class="chosen">Chosen:</span><span class="chosen-data">'+(data[i].carName)+'</span> <select class="plan-select">'+creatSelect(data[i].carList)+'</select> <button data-rideid='+data[i].RideId+' class="btn btn-primary j-save-car ladda-button" data-style="slide-right"><span class="ladda-label">Submit</span></button></span></td>';
				r_tr.innerHTML = add;
				r_tbody.appendChild(r_tr);
			}
			p.appendChild(r_tbody);
		}
	};
	
	
	
	var dt = $("#SolutionVehiclesPlan").DataTable({
        "processing": true, //loding效果
        "serverSide":true, //服务端处理
        "searchDelay": 1000,//搜索延迟
        "destroy": true,
        "order":[[0,'desc']],//默认排序方式
        "lengthMenu":[1000000],//每页显示数据条数菜单
        "ajax":{
            url:"/vehiclesPlan/vehicles.json", //获取数据的URL
            type:"get", //获取数据的方式
            error:function (){
            	$('#SolutionVehiclesPlan_processing').show();
//          	var add = '<tr class="odd"><td valign="top" colspan="4" class="dataTables_empty">No Data</td></tr>';
//          	$('#vehicle-tbody').append(add);
            	$('#depots-map').hide();
            }
            
        },
        "columns":[  //返回的JSON中的对象和列的对应关系
        	{"data":"id","name":"id"},
            {"data":function (data){
            	return "Ride "+add00(data.RideId);
            },"name":"RideId"},
            {"data":function (data){
            	return '<span class="plancar"><span>'+(data.depotOrder)+'</span><button data-style="slide-right" class="btn btn-primary j-car-plan-btn ladda-button" data-rideid='+data.RideId+'><span class="ladda-label">View on Map</span></button>'+'</span>'
            }},
            {"data":"carType","name":"car_type"},
            {"data":function (data){
            	return '<span class="plan-wrap"><span class="chosen">Chosen:</span><span class="chosen-data">'+(data.carName)+'</span> <select class="plan-select">'+creatSelect(data.carList)+'</select> <button data-rideid='+data.RideId+' class="btn btn-primary j-save-car ladda-button" data-style="slide-right"><span class="ladda-label">Submit</span></button></span>';
            }}
            
        ],
        "columnDefs":[ //具体列的定义
        	{
                "targets":[0],
                "visible":false
            },
        	{
                "targets":[0,1,2,3,4],
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
        	var $this = this;
        	//console.log(data);
        	if (data.data.length !=0) 
        	{
        		var result = Sort_rideid(data.data);
//	        	var ridelist = uniqeByKeys(result,['RideId']);
//	        	var list = RideId_List(result,ridelist);
//	        	console.log(list);
				var rideId = result[0].RideId;
				//creatEle('VehiclesPlan',result);
				ViewMap(rideId);
        	}
        	
        },
        "drawCallback":function  (settings) {
        	var api = this.api();
	        // 输出当前页的数据到浏览器控制台
	        var datas = api.rows( {page:'current'} ).data();
	        //console.log(datas);
	        if (datas.length != 0) 
	        {
	        	var $this = doc.getElementsByClassName('j-save-car')[0];
	        	var l = Ladda.create($this);
	 			l.stop();
	        }
        }
    });
	
	function vehiclesPlanMapInit (listPoint) {
		map.clearOverlays();
		if (listPoint != undefined) {
			var point = new BMap.Point(listPoint[0].siteLongitude,listPoint[0].siteLatitude);
		}
		map.centerAndZoom(point, 13);
		map.enableScrollWheelZoom(true);
		// 编写自定义函数,创建标注
		function addMarker(point,info){
		  var myIcon = new BMap.Icon("/static/images/location.png", new BMap.Size(24,32),{
		  	anchor: new BMap.Size(10, 24)
		  });
			  var marker = new BMap.Marker(point,{icon:myIcon});
		  map.addOverlay(marker);
		  marker.addEventListener("mouseover", function(){
		  	this.openInfoWindow(info);
		  });
		  marker.addEventListener("mouseout", function(){
		  	this.closeInfoWindow();
		  });
		}
		function addpPyline (pointA,pointB,infoWindowLine) {
			var polyline = new BMap.Polyline([pointA,pointB], {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.8});  //定义折线
			map.addOverlay(polyline);//添加折线到地图上
			
			addArrow(polyline,15,Math.PI/7);
			
			polyline.addEventListener("mouseover", function(e){
				//////console.log(e.point) //获取经过折线的当前坐标，触发覆盖物的事件返回值
				var point = new BMap.Point(e.point.lng,e.point.lat);
		  		map.openInfoWindow(infoWindowLine,point);
		  		
		  	});
		  	polyline.addEventListener("mouseout", function(){
		  		map.closeInfoWindow();
		  	});
		}
		function depotPylineInfo (listPointX,listPointY) {
			var pointA = new BMap.Point(listPointX.siteLongitude,listPointX.siteLatitude),
				pointB = new BMap.Point(listPointY.siteLongitude,listPointY.siteLatitude);					
			var sContentLine = "";
			sContentLine +='<div class="clearfix">';
			sContentLine +='<p>'+listPointX.curLoc+' to '+listPointY.curLoc+':</p>';
			sContentLine +='<div>';
			sContentLine +='<p>Departure time: '+operationTime(listPointX.endTime)+'</p>';
			sContentLine +='<p>Arrival time: '+operationTime(listPointY.arrTime)+'</p>';
			sContentLine +='<p>Goods: '+listPointX.carGoods+'</p>';
			sContentLine +='</div></div>';
			var infoWindowLine = new BMap.InfoWindow(sContentLine); // 创建信息窗口对象
			addpPyline(pointA,pointB,infoWindowLine);
			//////console.log("v")
		}
		//初始化坐标
		var p_len = listPoint.length;
		for (var j = 0;j<p_len;j++) {
			var points = new BMap.Point(listPoint[j].siteLongitude,listPoint[j].siteLatitude);
			//////console.log(points)
			var sContent = "";
			sContent += '<p>ID: '+listPoint[j].curLoc+'</p>';
			sContent += '<p>Type: '+listPoint[j].siteType+'</p>';
			sContent += '<p>Name: '+listPoint[j].siteName+'</p>';
			sContent += '<p>Unload: '+listPoint[j].unloadVol+'</p>';
			sContent += '<p>Load: '+listPoint[j].sbVol+'</p>';
			var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
			//////console.log(infoWindow)
			addMarker(points,infoWindow);
		}
		if (listPoint != undefined) {
			//规划线路
			for (var i=0,rl_len = listPoint.length;i<rl_len;i++) {
				if (i==rl_len-1) {
					continue;
				}
				depotPylineInfo(listPoint[i],listPoint[i+1]);
				//////console.log(i)
			}
		}

		
	};
	
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
		
		], {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.8});  
		
		map.addOverlay(Arrow);  
		
		
		}  

	};
	function ViewMap (rideId,$this) {
		if ($this)
		{
			var l = Ladda.create($this);
	 		l.start();
		}
		$.get('/vehiclesPlan/vehiclesPlan.json',{"rideId":rideId}).done(function  (res) {
			if (res.data)
			{
				var result = Sort_sequence(res.data);
				vehiclesPlanMapInit(result);
				var route_name ="Ride " + add00(rideId) || "No Data",
					total = res.totalDistance || "--",
					piece_capacity = res.maxLoad || "--",
					type = res.carType || "--",
					chosen = res.carName || "--";
				$('#route-name').text(route_name);
				$('#route-name').attr('data-rideid',rideId);
				$('#total-distance').text(total);
				$('#vehicle-piece-capacity').text(piece_capacity);
				$('#type-requirement').text(type);
				$('#Chosen-Vehicle').text(chosen);
			}
			if ($this) 
			{
				l.stop();
			}
		}).fail(function  () {
			//console.log()
		});
	}
	//点击view on map
	$('body').on('click','.j-car-plan-btn',function  () {
		var $this = this;
		var rideId = $(this).attr('data-rideid');
		if (Boolean(rideId)) 
		{
			ViewMap(rideId,$this);
		}
		
	});
	//保存排车
	$('body').on('click','.j-save-car',function  () {
		var $this = $(this);
		var rideId = $(this).attr('data-rideid');
		var rideID_scenname = $('#route-name').attr('data-rideid');
		var carName = $(this).prev().val();
		if ( Boolean(rideId) && Boolean(carName) )
		{
			var l = Ladda.create(this);
		 	l.start();
			var data = {
				"rideId":rideId,
				"carName":carName
			};
			$.get('/vehiclesPlan/planCar',data).done(function  (res) {
				if (res.data === "success") {
					dt.ajax.reload();
					if (rideID_scenname === rideId) 
					{
						$('#Chosen-Vehicle').text(carName);
					}
					//l.stop();
				}
			}).fail(function  () {
				
			}).always(function  () {
				
			}); 
		}
	});

	//导出excel表格
    $('.export-btn').click(function  () {
        var _xls = $(this).attr('data-xls');
        if (_xls) {
            window.location.href="/vehiclesPlan/exportResult";
        }
        $(".modal-header span").trigger('click');
    });
	
	
	
	
});
