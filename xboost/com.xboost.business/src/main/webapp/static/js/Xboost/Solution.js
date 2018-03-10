$(function  () {
	var doc = document,
	listArry = '',
	listLen = '';
	var map = new BMap.Map("depots-map");
	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
	var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
	map.addControl(top_left_control);        
	map.addControl(top_left_navigation);     
	map.addControl(top_right_navigation);
	var  count = "";//储存变量
	
	
	//初始化地图
	function depotMapInit (listPoint,val) {
		map.clearOverlays();
		
		if (val) {
			var p_len = listPoint.length;
			for (var a=0;a<p_len;a++) {
				if (listPoint[a].curLoc == val) {
					var point = new BMap.Point(listPoint[a].lng,listPoint[a].lat);
				}
			}
		}else{
			var point = new BMap.Point(listPoint[0].lng,listPoint[0].lat);
		}
		
		
		map.centerAndZoom(point, 13);
		map.enableScrollWheelZoom(true);
		// 编写自定义函数,创建标注
		function addMarker(point,info,curLoc,myIcon){
		  	
		  	var marker = new BMap.Marker(point,{icon:myIcon});
		  map.addOverlay(marker);
		  marker.addEventListener("mouseover", function(e){
//		  	var point = new BMap.Point(e.point.lng,e.point.lat);
//		  	console.log(point)
		  	this.openInfoWindow(info);
		  });
		  marker.addEventListener("mouseout", function(){
		  	this.closeInfoWindow();
		  });
		  marker.addEventListener("click", function(){
		  	//////console.log(curLoc)
		  	$('#route-depot').val(curLoc);
		  	count = curLoc;
		  	depotMapInit(listPoint,curLoc);
		  	var table = $('#SolutionDeport').DataTable();
			table.search(curLoc).draw(false);
		  });
		}
		function addpPyline (point,infoWindowLine,color) {
			if (color == 1) { //上一个网点
				var polyline = new BMap.Polyline(point, {strokeColor:"black", strokeWeight:2, strokeOpacity:1});  //定义折线
			}else if (color == 2){ //下一个网点
				var polyline = new BMap.Polyline(point, {strokeColor:"blue", strokeWeight:2, strokeOpacity:1});  //定义折线
			}else{ // 彼此是网点
				var polyline = new BMap.Polyline(point, {strokeColor:"red", strokeWeight:2, strokeOpacity:1});  //定义折线
			}
			
			map.addOverlay(polyline);//添加折线到地图上
			polyline.addEventListener("mouseover", function(e){
				//////console.log(e.point) //获取经过折线的当前坐标，触发覆盖物的事件返回值
				var point = new BMap.Point(e.point.lng,e.point.lat);
		  		map.openInfoWindow(infoWindowLine,point);
		  		
		  	});
		  	polyline.addEventListener("mouseout", function(){
		  		map.closeInfoWindow();
		  		
		  	});
		}
		function depotPylineInfo (listPointX,listPointY,color,data) {
			var pointA = new BMap.Point(listPointX.lng,listPointX.lat),
				pointB = new BMap.Point(listPointY.lng,listPointY.lat);	
			var point = [pointA,pointB];
			var sContentLine = "";
			var res = data.list;
			var add = "";
			for (var i=0,listlen = res.length;i<listlen;i++) {
				var sbVol = res[i].sbVol;
				var unloadVol = res[i].unloadVol;
				if (sbVol == 0 && unloadVol != 0) {
					add +='<p>The previous location: '+data.curLoc+" , "+'Arrival Time: '+res[i].arrTime+" , "+'Type:unload '+" , "+'Unloadpiece: '+res[i].unloadVol+'</p>';
				}
				if (unloadVol == 0 && sbVol !=0) {
					add +='<p>The next location: '+data.nextCurLoc+" , "+'Departure Time:'+res[i].endTime+" , "+'Type:load'+" , "+'loadpiece:'+res[i].sbVol+'</p>';
				}else if (unloadVol != 0 && sbVol !=0){
					add +='<p>The previous location: '+data.curLoc+" , "+'Arrival Time: '+res[i].arrTime+" , "+'Type:unload '+" , "+'Unloadpiece: '+res[i].unloadVol+'</p>';
					add +='<p>The next location: '+data.nextCurLoc+" , "+'Departure Time:'+res[i].endTime+" , "+'Type:load'+" , "+'loadpiece:'+res[i].sbVol+'</p>';
				}
			}
			sContentLine +='<div class="clearfix">';
			sContentLine +='<div style="float: left;">';
			//sContentLine +='<p>'+listPointX.curLoc+' to '+(listPointY.nextCurLoc?listPointY.nextCurLoc:listPointY.prevCurLoc)+" :"+'</p>';
			sContentLine +=add;
			sContentLine +='</div></div>';
			var infoWindowLine = new BMap.InfoWindow(sContentLine); // 创建信息窗口对象
			addpPyline(point,infoWindowLine,color);
		}
		function depotPylineInfo1 (listPointX,listPointY,color,data) {
			var pointA = new BMap.Point(listPointX.lng,listPointX.lat),
				pointB = new BMap.Point(listPointY.lng,listPointY.lat);	
			var point = [pointA,pointB];
			var sContentLine = "";
			var res = data.list;
			var add = "";
			for (var i=0,listlen = res.length;i<listlen;i++) {
				var sbVol = res[i].sbVol;
				var unloadVol = res[i].unloadVol;
				if (sbVol == 0 && unloadVol != 0) {
					add +='<p>The previous location: '+data.prevCurLoc+" , "+'Arrival Time:'+res[i].arrTime+" , "+'Type:unload'+" , "+' Unloadpiece:'+res[i].unloadVol+'</p>';
				}
				if (unloadVol == 0 && sbVol !=0) {
					add +='<p>The next location: '+data.curLoc+" , "+'Departure Time:'+res[i].endTime+" , "+'Type:load'+" , "+'loadpiece:'+res[i].sbVol+'</p>';
				}else if (unloadVol != 0 && sbVol !=0){
					add +='<p>The previous location: '+data.prevCurLoc+" , "+'Arrival Time:'+res[i].arrTime+" , "+'Type:unload'+" , "+' Unloadpiece:'+res[i].unloadVol+'</p>';
					add +='<p>The next location: '+data.curLoc+" , "+' Departure Time:'+res[i].endTime+" , "+' Type:load'+" , "+' loadpiece:'+res[i].sbVol+'</p>';
					
				}
			}
			sContentLine +='<div class="clearfix">';
			sContentLine +='<div style="float: left;">';
			//sContentLine +='<p>'+listPointX.curLoc+' to '+(listPointY.nextCurLoc?listPointY.nextCurLoc:listPointY.prevCurLoc)+" :"+'</p>';
			sContentLine +=add;
			sContentLine +='</div></div>';
			var infoWindowLine = new BMap.InfoWindow(sContentLine); // 创建信息窗口对象
			addpPyline(point,infoWindowLine,color);
		}
		function depotPylineInfo2 (listPointX,listPointY,color,data) {
			var pointA = new BMap.Point(listPointX.lng,listPointX.lat),
				pointB = new BMap.Point(listPointY.lng,listPointY.lat);	
			var point = [pointA,pointB];
			var sContentLine = "";
			var res = data.list;
			var add = "";
			for (var i=0,listlen = res.length;i<listlen;i++) {
				var sbVol = res[i].sbVol;
				var unloadVol = res[i].unloadVol;
				if (sbVol == 0 && unloadVol != 0) {
					add +='<p>The previous location: '+data.prevCurLoc+" , "+' Arrival Time:'+res[i].arrTime+" , "+' Type:unload'+" , "+' Unloadpiece:'+res[i].unloadVol+'</p>';
				}
				if (unloadVol == 0 && sbVol !=0) {
					add +='<p>The next location: '+data.curLoc+" , "+' Departure Time:'+res[i].endTime+" , "+' Type:load'+" , "+' loadpiece:'+res[i].sbVol+'</p>';
				}else if (unloadVol != 0 && sbVol !=0){
					add +='<p>The previous location: '+data.prevCurLoc+" , "+' Arrival Time:'+res[i].arrTime+" , "+' Type:unload'+" , "+' Unloadpiece:'+res[i].unloadVol+'</p>';
					add +='<p>The next location: '+data.curLoc+" , "+' Departure Time:'+res[i].endTime+" , "+' Type:load'+" , "+' loadpiece:'+res[i].sbVol+'</p>';
				}
			}
			sContentLine +='<div class="clearfix">';
			sContentLine +='<div style="float: left;">';
			//sContentLine +='<p>'+listPointX.curLoc+' to '+(listPointY.nextCurLoc?listPointY.nextCurLoc:listPointY.prevCurLoc)+" :"+'</p>';
			sContentLine +=add;
			sContentLine +='</div></div>';
			var infoWindowLine = new BMap.InfoWindow(sContentLine); // 创建信息窗口对象
			addpPyline(point,infoWindowLine,color);
		}
		
		//初始化坐标
		var p_len = listPoint.length;
		for (var j = 0;j<p_len;j++) {
			if (listPoint[j].curLoc == val) {
				var points = new BMap.Point(listPoint[j].lng,listPoint[j].lat);
				var myIcon = new BMap.Icon("/static/images/locationB.png", new BMap.Size(30,40),{
					anchor: new BMap.Size(15,39)
				});
			}else{
				var points = new BMap.Point(listPoint[j].lng,listPoint[j].lat);
				var myIcon = new BMap.Icon("/static/images/location.png", new BMap.Size(24,32),{
					anchor: new BMap.Size(12, 25)
				});
			}
			
			//////console.log(points)
			var sContent = "";
			sContent += '<p>ID: '+listPoint[j].curLoc+'</p>';
			sContent += '<p>Type: '+listPoint[j].siteType+'</p>';
			sContent += '<p>Name: '+listPoint[j].siteName+'</p>';
			var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
			//////console.log(infoWindow)
			addMarker(points,infoWindow,listPoint[j].curLoc ,myIcon);
			//addMarkers(pointss);
		};
		if (val == "") {
			//初始化路线
			////console.log("a")
//			for (var x = 0;x<p_len;x++) {
//				for (var y = 0 ;y<p_len;y++) {
//					var _curLoc = listPoint[x].curLoc,
//						_nextCurLoc = listPoint[y].nextCurLoc;
//					if (_curLoc == _nextCurLoc) {
//						depotPylineInfo(listPoint[x],listPoint[y]);
//					}
//				}
//			}
		}else{
			//根据网点 规划线路
			for (var a=0;a<p_len;a++) {
				if (listPoint[a].curLoc == val) {
					var index = a;
					var indnext = listPoint[a].nextCurLoc;
					var indNlen = listPoint[a].nextCurLoc.length;
					var indprev = listPoint[a].prevCurLoc;
					var indPlen = listPoint[a].prevCurLoc.length;
					
				}
			}
			for (var bb=0;bb<indNlen;bb++) {
				var bblist = {
					"curLoc":listPoint[index].curLoc,
					"nextCurLoc":indnext[bb].nextCurLoc,
					"list":indnext[bb].list
				}
				//////console.log(bblist)
				depotPylineInfo(listPoint[index],indnext[bb],2,bblist);
			}
			for (var qq=0;qq<indPlen;qq++) {
				var qqlist = {
					"curLoc":listPoint[index].curLoc,
					"prevCurLoc":indprev[qq].prevCurLoc,
					"list":indprev[qq].list
				}
				depotPylineInfo1(listPoint[index],indprev[qq],1,qqlist);
			}
			for (var b=0;b<indNlen;b++) {
				for (var q=0;q<indPlen;q++) {
					var res1 = indnext[b].nextCurLoc;
					var res2 = indprev[q].prevCurLoc;
					if (res1 == res2) {
						var _list = indnext[b].list;
						for (var z=0,zlen=indprev[q].list.length;z<zlen;z++) {
							_list.push(indprev[q].list[z]);
						}
						var blist = {
							"curLoc":listPoint[index].curLoc,
							"prevCurLoc":res2,
							"nextCurLoc":res1,
							"list":_list
						}
						//////console.log(indnext[b].list)
						depotPylineInfo2(listPoint[index],indprev[q],3,blist);
					}
				}
				
			}

		}
		
	}
	//排序函数
	function sortNumber(a,b) {
		return a.sequence - b.sequence;
	}
	//route map
	function routeMapInit (listPoints,val,listPoint) {
		map.clearOverlays();
		if (listPoint != undefined) {
			var point = new BMap.Point(listPoint[0].lng,listPoint[0].lat);
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
			var pointA = new BMap.Point(listPointX.lng,listPointX.lat),
				pointB = new BMap.Point(listPointY.lng,listPointY.lat);					
			var sContentLine = "";
			sContentLine +='<div class="clearfix">';
			sContentLine +='<p>'+listPointX.curLoc+' to '+listPointY.curLoc+':</p>';
			sContentLine +='<div>';
			sContentLine +='<p>Departure time: '+listPointX.endTime+'</p>';
			sContentLine +='<p>Arrival time: '+listPointY.arrTime+'</p>';
			sContentLine +='<p>Goods: '+listPointX.carGoods+'</p>';
			sContentLine +='</div></div>';
			var infoWindowLine = new BMap.InfoWindow(sContentLine); // 创建信息窗口对象
			addpPyline(pointA,pointB,infoWindowLine);
			
		}
		//初始化坐标
		var p_len = listPoints.length;
		for (var j = 0;j<p_len;j++) {
			var points = new BMap.Point(listPoints[j].lng,listPoints[j].lat);
			//////console.log(points)
			var sContent = "";
			sContent += '<p>ID: '+listPoints[j].curLoc+'</p>';
			sContent += '<p>Type: '+listPoints[j].siteType+'</p>';
			sContent += '<p>Name: '+listPoints[j].siteName+'</p>';
			sContent += '<p>Unload: '+ listPoints[j].unloadVol+'</p>';
			sContent += '<p>Load: '+ listPoints[j].sbVol+'</p>';
			var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
			//////console.log(infoWindow)
			addMarker(points,infoWindow);
		}
		if (val && listPoint != undefined) {
			//规划线路
			//////console.log(listPoint)
			var list = [];
			var p_lens = listPoint.length;
			for (var q=0;q<p_lens;q++) {
				var routeNum = listPoint[q].routeCount;
				if (val == routeNum) {
					list.push(listPoint[q]);
				}
			}
			var routelist = uniqeByKeys(list,["sequence"]);
			routelist.sort(sortNumber);
			//////console.log(routelist);
			//////console.log(listPoint);
			for (var i=0,rl_len = routelist.length;i<rl_len;i++) {
				if (i==rl_len-1) {
					continue;
				}
				depotPylineInfo(routelist[i],routelist[i+1]);
				//////console.log(i)
			}
		}

		
	}
	//
	function vehiclesMapInit (listPoints,val,listPoint) {
		map.clearOverlays();
		if (listPoint != undefined) {
			var point = new BMap.Point(listPoint[0].lng,listPoint[0].lat);
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
			var pointA = new BMap.Point(listPointX.lng,listPointX.lat),
				pointB = new BMap.Point(listPointY.lng,listPointY.lat);					
			var sContentLine = "";
			sContentLine +='<div class="clearfix">';
			sContentLine +='<p>'+listPointX.curLoc+' to '+listPointY.curLoc+':</p>';
			sContentLine +='<div>';
			sContentLine +='<p>Departure time: '+listPointX.endTime+'</p>';
			sContentLine +='<p>Arrival time: '+listPointY.arrTime+'</p>';
			sContentLine +='<p>Goods: '+listPointX.carGoods+'</p>';
			sContentLine +='</div></div>';
			var infoWindowLine = new BMap.InfoWindow(sContentLine); // 创建信息窗口对象
			addpPyline(pointA,pointB,infoWindowLine);
			//////console.log("v")
		}
		//初始化坐标
		var p_len = listPoints.length;
		for (var j = 0;j<p_len;j++) {
			var points = new BMap.Point(listPoints[j].lng,listPoints[j].lat);
			//////console.log(points)
			var sContent = "";
			sContent += '<p>ID: '+listPoints[j].curLoc+'</p>';
			sContent += '<p>Type: '+listPoints[j].siteType+'</p>';
			sContent += '<p>Name: '+listPoints[j].siteName+'</p>';
			sContent += '<p>Unload: '+listPoints[j].unloadVol+'</p>';
			sContent += '<p>Load: '+listPoints[j].sbVol+'</p>';
			var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
			//////console.log(infoWindow)
			addMarker(points,infoWindow);
		}
		if (val && listPoint != undefined) {
			//规划线路
			//////console.log(listPoint)
			var list = [];
			var p_lens = listPoint.length;
			for (var q=0;q<p_lens;q++) {
				var carName = listPoint[q].carName;
				if (val == carName) {
					list.push(listPoint[q]);
				}
			}
			//////console.log(list)
			var routelist = uniqeByKeys(list,["sequence"]);
			routelist.sort(sortNumber);
			//////console.log(routelist);
			for (var i=0,rl_len = routelist.length;i<rl_len;i++) {
				if (i==rl_len-1) {
					continue;
				}
				depotPylineInfo(routelist[i],routelist[i+1]);
				//////console.log(i)
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
		
		], {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.8});  
		
		map.addOverlay(Arrow);  
		
		
		}  

	}
	
	//depot search 
	function depotSearch (val){
		var table = $('#SolutionDeport').DataTable();
		table.search(val).draw(false);
	}
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
	}
	//求时间
	function operationTime (data) {
		var result = parseInt(data),
	    	h = parseInt(result/60),
	    	m = result%60;
	    	return add0(h)+":"+add0(m);
	}
	function depotsNextList (arr){
		var allnewarr = []
		//////console.log(arr);
		var arrSite = uniqeByKeys(arr,["nextCurLoc"]);
		for (var j=0,alen = arrSite.length;j<alen;j++) {
			
			var newarr = {};
			var lista = [];
			for (var x=0,arrlen = arr.length;x<arrlen;x++) {
				var site1 = arrSite[j].nextCurLoc;
				var site2 = arr[x].nextCurLoc;
				if (site1 == site2) {
					var objj = {};
					objj["calcDis"] = arr[x].calcDis;
					objj["arrTime"] = arr[x].arrTime;
					objj["endTime"] = arr[x].endTime;
					objj["sbVol"] = arr[x].sbVol;
					objj["unloadVol"] = arr[x].unloadVol;
					lista.push(objj);
					
				}
			}
			newarr["nextCurLoc"]=arrSite[j].nextCurLoc;
			newarr["lng"]=arrSite[j].lng;
			newarr["lat"]=arrSite[j].lat;
			newarr["list"]=lista;
			allnewarr.push(newarr);
		}
		return allnewarr;
	}
	function depotsPrevList (arr){
		var allnewarr = []
		//////console.log(arr);
		var arrSite = uniqeByKeys(arr,["prevCurLoc"]);
		for (var j=0,alen = arrSite.length;j<alen;j++) {
			
			var newarr = {};
			var lista = [];
			for (var x=0,arrlen = arr.length;x<arrlen;x++) {
				var site1 = arrSite[j].prevCurLoc;
				var site2 = arr[x].prevCurLoc;
				if (site1 == site2) {
					var objj = {};
					objj["calcDis"] = arr[x].calcDis;
					objj["arrTime"] = arr[x].arrTime;
					objj["endTime"] = arr[x].endTime;
					objj["sbVol"] = arr[x].sbVol;
					objj["unloadVol"] = arr[x].unloadVol;
					lista.push(objj);
					
				}
			}
			newarr["prevCurLoc"]=arrSite[j].prevCurLoc;
			newarr["lng"]=arrSite[j].lng;
			newarr["lat"]=arrSite[j].lat;
			newarr["list"]=lista;
			allnewarr.push(newarr);
		}
		return allnewarr;
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
			$('#depot-tbody').empty();
			var dt =$("#SolutionDeport").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "destroy": true,
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[100000],//每页显示数据条数菜单
	            "ajax":{
	                url:"/depots/depots.json", //获取数据的URL
	                type:"get", //获取数据的方式
	                cache:true,
	                error:function (){
	                	$('#SolutionDeport_processing').hide();
	                	var add = '<tr class="odd"><td valign="top" colspan="5" class="dataTables_empty">No Data</td></tr>';
	                	$('#depot-tbody').append(add);
	                	$('#depots-map').hide();
	                }
	                
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"curLoc","name":"cur_loc"},
	                {"data":"carType","name":"car_type"},
	                {"data":function  (res) {
                        if (res.arrTime == "--") {
                            return res.arrTime;
                        }else {
                            return operationTime(res.arrTime);
                        }
	                },"name":"arr_time"},
	                {"data":function  (res) {
	                	if (res.unloadVol == "") {
	                		res.unloadVol = 0;
	                	}else if(res.sbVol == ""){
	                		res.sbVol = 0;
	                	}
	                	return "Unload "+res.unloadVol+" , "+"Load "+res.sbVol;
	                },"name":"unloadVol & sbVol"},
	                {"data":function  (res) {
                        if (res.endTime == "--") {
                            return res.endTime;
                        }else {
                            return operationTime(res.endTime);
                        }
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
	            	//////console.log("i");
	            	if (data.data.length !=0) {
	            		$('#depots-map').show();
	            		var result = data.data,
	            		listPoint = [],
	            		len = result.length;
	            		$('#route-depot').empty();
	            		$('#route-depot').off("change");
	            		$('#route-depot').append('<option value="0">All Depots</option>');
	            		//////console.log(result)
                        var Arr = uniqeByKeys(result,["curLoc"]),
                        A_len = Arr.length;
                        //////console.log(Arr)
	            		for (var i=0;i<A_len;i++) {
	            			
	            			var add='<option value='+Arr[i].curLoc+'>'+Arr[i].curLoc+'</option>';
							$('#route-depot').append(add);
						}
	            		
	            		for (var j=0;j<A_len;j++) {
	            			
	            			
	            			var nextlist = [];
	            			var liser = {};
	            			var prevlist = [];
	            			for (var f = 0;f<len;f++) {
	            				
	            				var liserN = {};
	            				var liserP = {};
	            				var s1 = Arr[j].curLoc;
	            				var s2 = result[f].curLoc;
	            				var s3 = result[f].nextCurLoc;
	            				if (s1 == s2) {
	            					if (result[f].nextCurLoc != '') {
	            						liserN["nextCurLoc"] = result[f].nextCurLoc;
	            						liserN["calcDis"] = result[f].calcDis;
	            						var arrTime = operationTime(result[f].arrTime);
										var endTime = operationTime(result[f].endTime);
										liserN["arrTime"] = arrTime;
										liserN["endTime"] = endTime;
										liserN["sbVol"] = result[f].sbVol;
										liserN["unloadVol"] = result[f].unloadVol;
										for (var w=0;w<A_len;w++) {
											if (result[f].nextCurLoc == Arr[w].curLoc) {
												liserN["lng"] = Arr[w].siteLongitude;
												liserN["lat"] = Arr[w].siteLatitude;
											}
										}
										
	            						nextlist.push(liserN);
	            	
	            					}
	            					
	            				}
	            				if (result[f].nextCurLoc != '' && s1 == s3) {
	            					liserP["prevCurLoc"] = result[f].curLoc;
            						liserP["calcDis"] = result[f].calcDis;
            						var arrTime = operationTime(result[f].arrTime);
									var endTime = operationTime(result[f].endTime);
									liserP["arrTime"] = arrTime;
									liserP["endTime"] = endTime;
									liserP["sbVol"] = result[f].sbVol;
									liserP["unloadVol"] = result[f].unloadVol;
									for (var e=0;e<A_len;e++) {
										if (result[f].curLoc == Arr[e].curLoc) {
											liserP["lng"] = Arr[e].siteLongitude;
											liserP["lat"] = Arr[e].siteLatitude;
										}
									}
            						prevlist.push(liserP);
	            				}
	            				
								
		            		}
	            			
	            			
	            			
	            			//处理返回数据
	
	            			//////console.log(rlist)
	            			liser["curLoc"] = Arr[j].curLoc;
							liser["siteType"] = Arr[j].siteType;
							liser["siteName"] = Arr[j].siteName;
							liser["lng"] = Arr[j].siteLongitude;
							liser["lat"] = Arr[j].siteLatitude;
							liser["nextCurLoc"] = depotsNextList(nextlist);
							liser["prevCurLoc"] = depotsPrevList(prevlist);
							listPoint.push(liser);
							
							
	            		}
	            		
							
	            		
						//查询所有网点坐标
						//////console.log(listPoint)
						listArry="";
						listArry = listPoint;
							
						//百度地图
						depotMapInit(listPoint,"");

	            	}else{
	            		$('#depots-map').hide();
	            	}

	            	
	            },
	            "drawCallback":function  (settings, data) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        var Datas = api.rows( {page:'current'} ).data();
			        //////console.log("d")
			        //////console.log(Datas);
			        var listPoint = [];
			        var _len = Datas.length;
			        if (_len != 0 && count != "") {
			        	var res = Datas[0];
						$('#depot').text("Depot "+res.siteCode);
                        $('#east').text(Number(res.siteLatitude).toFixed(6));
                        $('#north').text(Number(res.siteLongitude).toFixed(6));
                        $('#name').text(res.siteName);
                        $('#address').text(res.siteAddress);
                        $('#type').text(res.siteType);
                        //$('#distrib-center').text(res.distribCenter);
                        if (res.siteArea== 0) {
                        	var _area = '--';
                        }else{
                        	var _area = Math.ceil(res.siteArea);
                        }
                        $('#area').text(_area + " m²");
                        $('#vehicle-quantity-limit').text( (res.carNum >= 999?'∞':res.carNum) );
                        $('#vehicle-weight-limit').text( (res.largeCarModel >= 999?'∞':res.largeCarModel) );
                        $('#piece-capacity').text(res.maxOperateNum);
                        
//                      table.search(count).draw(false);
//						depotMapInit(listArry,count);
					}else if(_len == 0 || count == ""){
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
//                      depotMapInit(listArry,"");
//						table.search("").draw(false);
					}

	            }
	        });
	        //获取
	        //点击选项 来查询
	        var table = $('#SolutionDeport').DataTable();
			$(document).on("change","#route-depot",function  () {
				var val = $('#route-depot').val();
				count = val;
				if (val == 0) {
					depotMapInit(listArry,"");
					table.search("").draw(false);
					//////console.log(1)
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

            //导出excel表格
            $('.export-btn').click(function  () {
                var _xls = $(this).attr('data-xls');
                if (_xls) {
					window.location.href="/depots/exportResult";
                }
                $(".modal-header span").trigger('click');
            });

	        
	        
		}
		
	}());
	
	/**
	 *route.jsp == SolutionRouteController
	 *
	 */
	
	function rountNum (a,b){
		return a-b;
	}
	
	(function  () {
		var SolutionRoute = doc.getElementById("SolutionRoute");
		if (SolutionRoute) {
			var route_operation = false;
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
	                	//////console.log(res)
	                	return "Route "+add0(res.routeCount);
	                },"name":"route_count"},
	                {"data":"sequence","name":"sequence"},
	                {"data":"curLoc","name":"cur_loc"},
	                {"data":"siteName","name":"site_name"},
	                {"data":"siteAddress","name":"site_address"},
	                {"data":function  (res) {
                        if (res.arrTime == "--") {
                            return res.arrTime;
                        }else {
                            return operationTime(res.arrTime);
                        }
	                },"name":"arr_time"},
	                {"data":function  (res) {
	                	if (res.unloadVol == "") {
	                		res.unloadVol = 0;
	                	}else if(res.sbVol == ""){
	                		res.sbVol = 0;
	                	}
	                	return "Unload "+res.unloadVol+" , "+"Load "+res.sbVol;
	                },"name":"unload_vol&sb_vol"},
	                {"data":function  (res) {
                        if (res.endTime == "--") {
                            return res.endTime;
                        }else {
                            return operationTime(res.endTime);
                        }
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
	            	if (data.data.length != 0) {
	            		listLen = '';
	            		listLen = data.data.length;
	            		$('#depots-map').show();
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
	            		Arr.sort(rountNum);
	            		for (var j=0;j<A_len;j++) {
	            			var add='<option value='+Arr[j]+'>'+"Route "+add0(Arr[j])+'</option>';
							$('#route-route').append(add);
	            		}
	            		var _val = $('#route-route').find("option").eq(0).val(),
	            		_text = $('#route-route').find("option").eq(0).text();
	            		$('#route-name').text(_text);
	            		//创建安排车辆
	            		$.get("/route/planCar.json",{"routeCount":_val}).done(function (data){
	            			//////console.log(data);
	            			if (data.usingCar) {
		            			var useCar = data.usingCar,
		            			useCarLen = useCar.length;
		            			$('#us-vehicle').empty();
		            			$('#us-vehicle').append('<option>--Choose--</option>');
		            			for (var x=0;x<useCarLen;x++) {
		            				var useCarAdd = '<option value='+useCar[x]+'>'+useCar[x]+'</option>';
		            				$('#us-vehicle').append(useCarAdd);
		            			}
		            		}
		            		if (data.idleCar) {
		            			var idleCar = data.idleCar,
		            			idleCarLen = idleCar.length;
		            			$('#idle-vehicle').empty();
		            			$('#idle-vehicle').append('<option>--Choose--</option>');
		            			for (var y=0;y<idleCarLen;y++) {
		            				var idleCarAdd = '<option value='+idleCar[y]+'>'+idleCar[y]+'</option>';
		            				$('#idle-vehicle').append(idleCarAdd);
		            			}
	
		            		}
	            		}).fail(function (){
	            			////console.log("fail");
	            		})
	            		
	            		for (var f = 0;f<len;f++) {
	            			var liser = {};
	            			liser["routeCount"] = result[f].routeCount;
							liser["curLoc"] = result[f].curLoc;
							liser["siteType"] = result[f].siteType;
							liser["siteName"] = result[f].siteName;
							liser["calcDis"] = result[f].calcDis;
							liser["lng"] = result[f].siteLongitude;
							liser["lat"] = result[f].siteLatitude;
							liser["nextCurLoc"] = result[f].nextCurLoc;
							var arrTime = operationTime(result[f].arrTime);
							var endTime = operationTime(result[f].endTime);
							liser["arrTime"] = arrTime;
							liser["endTime"] = endTime;
							liser["sbVol"] = result[f].sbVol;
							liser["unloadVol"] = result[f].unloadVol;
							liser["sequence"] = result[f].sequence;
							
							listPoint.push(liser);
	            		}
	            		//////console.log(listPoint);
	            		listArry="";
	            		//listArry = listPoint;
						listArry = uniqeByKeys(listPoint,["curLoc"]);
	            		// ////console.log(listArry);
	            		var table = $('#SolutionRoute').DataTable();
	            		setTimeout(function(){
	            			if (_val) {
	            				table.search(_val).draw();
	            				routeMapInit(listPoint,_val);
	            				
	            				$.get("/route/totalDistance.json",{"routeCount":_val}).done(function (res){
	            					//////console.log(res)
									if (res) {
										$('#total-distance').text(res.totalDistance+" km");
										if (Boolean(res.carName) ) {
											$('#Chosen-Vehicle').text(res.carName);
										}else{
											$('#Chosen-Vehicle').text('--');
										}
									}
								}).fail(function (){
									alert("fail");
									////console.log("fail");
								});
	            			}
	            			
	            		},0)
	            		
	            	}else{
	            		$('#depots-map').hide();
	            	}
	            	
	            },
	            "drawCallback":function  (settings) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        var data = api.rows( {page:'current'} ).data();
			        //////console.log(data)
			        var data_len = data.length;
			        if (data_len != 0) {
			        	var result = data,
			        	listPoint = [];
			        	var res = data[0];
			        	//派车
			        	$.get("/route/planCar.json",{"routeCount":res.routeCount}).done(function (data){
	            			//////console.log(data);
	            			if (data.usingCar) {
		            			var useCar = data.usingCar,
		            			useCarLen = useCar.length;
		            			$('#us-vehicle').empty();
		            			$('#us-vehicle').append('<option>--Choose--</option>');
		            			for (var x=0;x<useCarLen;x++) {
		            				var useCarAdd = '<option value='+useCar[x]+'>'+useCar[x]+'</option>';
		            				$('#us-vehicle').append(useCarAdd);
		            			}
		            		}
		            		if (data.idleCar) {
		            			var idleCar = data.idleCar,
		            			idleCarLen = idleCar.length;
		            			$('#idle-vehicle').empty();
		            			$('#idle-vehicle').append('<option>--Choose--</option>');
		            			for (var y=0;y<idleCarLen;y++) {
		            				var idleCarAdd = '<option value='+idleCar[y]+'>'+idleCar[y]+'</option>';
		            				$('#idle-vehicle').append(idleCarAdd);
		            			}
	
		            		}
		            		for (var f = 0;f<data_len;f++) {
		            			var liser = {};
		            			liser["routeCount"] = result[f].routeCount;
								liser["curLoc"] = result[f].curLoc;
								liser["siteType"] = result[f].siteType;
								liser["siteName"] = result[f].siteName;
								liser["calcDis"] = result[f].calcDis;
								liser["lng"] = result[f].siteLongitude;
								liser["lat"] = result[f].siteLatitude;
								liser["nextCurLoc"] = result[f].nextCurLoc;
								var arrTime = operationTime(result[f].arrTime);
								var endTime = operationTime(result[f].endTime);
								liser["arrTime"] = arrTime;
								liser["endTime"] = endTime;
								liser["sbVol"] = result[f].sbVol;
								liser["unloadVol"] = result[f].unloadVol;
								liser["sequence"] = result[f].sequence;
								liser["carGoods"] = result[f].carGoods;
								listPoint.push(liser);
		            		}
		            		var val = $('#route-route').val();
		            		if (route_operation) 
		            		{
		            			var a_len = listArry.length;
		            			var r_len = listPoint.length;
		            			for (var a=0;a<a_len;a++) 
		            			{
		            				for (var b=0;b<r_len;b++) 
		            				{
		            					var a_cur = listArry[a].curLoc;
		            					var b_cur = listPoint[b].curLoc;
		            					if (a_cur == b_cur) 
		            					{
		            						listArry[a] = listPoint[b]
		            					}
		            				}
		            			}
		            		}
		            		route_operation = true;
	            			routeMapInit(listArry,val,listPoint);
//	            			////console.log(listArry)
//	            			////console.log(listPoint)
		            		
		            		//////console.log(listPoint)
		            		
	            		}).fail(function (){
	            			////console.log("fail");
	            		})
						$('#vehicle-load-requirement').text(res.carType);
						$('#vehicle-piece-capacity').text(res.max_load);
						$('#speed-requirement').text(res.velocity+" km/h");
			        }else{
			        	$('#route-name').text("No Data");
			        	$('#total-distance').text("--");
						$('#vehicle-load-requirement').text("--");
						$('#vehicle-piece-capacity').text("--");
						$('#speed-requirement').text("--");
			        }
			        
			        
	           }
	        });
	        //点击选项 来查询
	        var table = $('#SolutionRoute').DataTable();
			$(document).on("change","#route-route",function  () {
				var val = $('#route-route').val(),
					_text = "Route "+add0(val);
					//////console.log(val)
				table.search(val).draw(false);
	            $('#route-name').text(_text);
				
				$.get("/route/totalDistance.json",{"routeCount":val}).done(function (res){
					//////console.log(res)
					if (res) {
						$('#total-distance').text(res.totalDistance+" km");
						if (Boolean(res.carName) ) {
							$('#Chosen-Vehicle').text(res.carName);
						}else{
							$('#Chosen-Vehicle').text('--');
						}
						
					}
				}).fail(function (){
					////console.log("fail")
				});
			});
			//获取checked值paiche
			$('input[type="radio"]').click(function (){
				var _val = $("input[type='radio']:checked").val();
				//////console.log(_val)
				if (_val == 1) {
					$('#us-vehicle').show();
					$('#idle-vehicle').hide();
				}else if (_val == 0){
					$('#us-vehicle').hide();
					$('#idle-vehicle').show();
				}
			});
			//$('#idle-vehicle').change(function  () {
				$('#vehicle-btn').click(function  (e) {
					var routeNum = $('#route-route').val();
					//e.preventDefault();
					var _val = $('#idle-vehicle').val()
					var _text = $('#idle-vehicle').find("option:selected").text()
					//console.log(_val)
					if (_val != 0 && _val != null && _text != "--Choose--") 
					{
					 	var l = Ladda.create(this);
					 	l.start();
						$.post("/route/updateCarName",{"routeCount":routeNum,"carName":_val,}).done(function(res){
							if (res == "success") {
								//window.location.href = "/vehicles";
								var routeCount = $('#route-route').val();
					        	$.get("/route/planCar.json",{"routeCount":routeCount}).done(function (data){
			            			//////console.log(data);
			            			if (data.usingCar) {
				            			var useCar = data.usingCar,
				            			useCarLen = useCar.length;
				            			$('#us-vehicle').empty();
				            			$('#us-vehicle').append('<option value="0">--Choose--</option>');
				            			for (var x=0;x<useCarLen;x++) {
				            				var useCarAdd = '<option value='+useCar[x]+'>'+useCar[x]+'</option>';
				            				$('#us-vehicle').append(useCarAdd);
				            			}
				            		}
				            		if (data.idleCar) {
				            			var idleCar = data.idleCar,
				            			idleCarLen = idleCar.length;
				            			$('#idle-vehicle').empty();
				            			$('#idle-vehicle').append('<option value="0">--Choose--</option>');
				            			for (var y=0;y<idleCarLen;y++) {
				            				var idleCarAdd = '<option value='+idleCar[y]+'>'+idleCar[y]+'</option>';
				            				$('#idle-vehicle').append(idleCarAdd);
				            			}
										$('#idle-vehicle').val(0);
				            		}
				            		
			            		}).always(function() { 
			            			l.stop();
			            		}).fail(function  () {
			            			////console.log("fail");
			            		});
							}
						}).fail(function  () {
							alert("fail");
						})
					}
				});
			//});

            /*
			 * 选择上传文件
			 */
            $("body").on("click",".cond-file-btn",function  () {
                $('.import-error').text("").hide();
                var _file = $(this).parent(".cond-file-box").prev();
                var _nextP = $(this).next();
                _file.trigger("click");
                _file.change(function  () {
                    var _index = _file.val().lastIndexOf("\\")+1;
                    var _key = _file.val().slice(_index);
                    _nextP.text(_key);
                });
            });

            /*
			 *上传文件 函数
			 * formId 表单id ,
			 * inpClass 对应的文件的input类名,
			 * url 请求地址，
			 * modId ,模态框id
			 * */
            function UploadFile (formID,inpClass,urls,modId) {
                var doc = document;
                var form = [];//创建对象储存文件信息
                var inp_class = doc.getElementById(formID).getElementsByClassName(inpClass);
                var len = inp_class.length;
                for (var i=0;i<len;i++) {
                    FileTest(inp_class[i],form);
                }
                for (var j=0,_len=form.length;j<_len;j++) {
                    if (form[j] == false) {
                        return false;
                    }
                }
                var form = new FormData(document.getElementById(formID));
                var _val = $('input[name="file"]').val();
                if ( !Boolean(_val) ) {
                    return false;
                }
                $('.loading').show();
                $.ajax({
                    url:urls,
                    type:"post",
                    data:form,
                    processData:false,
                    contentType:false,
                    success:function(data){
                        //alert("Import information to complete!");
                        $(modId).modal("hide");
                        $('.loading').hide();
                        window.location.reload();
                    },
                    error:function(e){
                        //alert("Mistake!!");
                        window.clearInterval(timer);
                    }
                });
                //此处为上传文件的进度条get();
            }

            /*
			 * 检测上传文件是否符合要求
			 */
            function FileTest (objid,form) {
                var res = objid.files[0];
                if (res != undefined) {
                    var val = objid.value;
                    var _fileName = res.name
                    FileFormat(val,_fileName,form);
                }
            }
            /*
             * 判断文件格式
             */
            function FileFormat (val,fileName,form) {
                var _index = val.lastIndexOf(".");
                var _key = val.slice(_index);
                var _zeng = /^(\.xlsx)$/i;
                if (!_zeng.test(_key)) {
                    form.push(false);
                    $('.import-error').text("The file format of"+" "+fileName+" "+"is inconsistent  .xlsx").show();
                    //alert("The file format of"+" "+fileName+" "+"is inconsistent  .xlsx")
                }else{
                    //将数据封装对象
                    form.push(true);
                }
            }

            //导出excel表格进行排车
            $('.export-btn').click(function  () {
                var _xls = $(this).attr('data-xls');
                if (_xls) {
                    window.location.href="/route/exportResult";
                }
                $(".modal-header span").trigger('click');
            });

            //导入excel 表格
            $('#cond-file-upload-info').click(function  () {
                UploadFile("cond-input-form-info","cond_file","/route/inputRoutesExcel",'.bs-example-modal-input')
            });
		}
		
	}());
	


	/**
	 *Vehicles.jsp == SolutionVehiclesController
	 *
	 */
	function vehNum (a,b) {
		var indexa = a.search(/\d/),
		indexb = b.search(/\d/);
		return a.substr(indexa) - b.substr(indexb);
	}
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
	                {"data":"carName","name":"car_name"},
	                {"data":"sequence","name":"sequence"},
	                {"data":"curLoc","name":"cur_loc"},
	                {"data":"siteName","name":"site_name"},
	                {"data":"siteAddress","name":"site_address"},
	                {"data":function(res) {
	                	if (res.arrTime == "--") {
	                		return res.arrTime;
						}else {
                            return operationTime(res.arrTime);
						}
	                },"name":"arr_time"},
	                {"data":function(res) {
	                	if (res.unloadVol == "") {
	                		res.unloadVol = 0;
	                	}else if(res.sbVol == ""){
	                		res.sbVol = 0;
	                	}
	                	return "Unload "+Math.ceil(res.unloadVol)+" , "+"Load "+Math.ceil(res.sbVol);
	                },"name":"unload_vol&sbVol"},
	                {"data":function(res) {
                        if (res.endTime == "--") {
                            return res.endTime;
                        }else {
                            return operationTime(res.endTime);
                        }
	                },"name":"end_time"},
	                {"data":function(res) {
	                	return res.nextCurLoc+","+res.calcDis+"km";
	                },"name":"nextCurLoc&calcDis"}
	                
	            ],
	            "columnDefs":[ //具体列的定义
	            	{
	                    "targets":[0],
	                    "visible":false
	               },
	               {
	                    "targets":[0,1,2,3,4,5,6,7,8],
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
	            	//////console.log(data);
	            	if (data.data.length != 0) {
	            		listLen = '';
	            		listLen = data.data.length;
	            		$('#depots-map').show();
	            		var result = data.data,
	            		arr = [],
	            		listPoint = [],
	            		len = result.length;
	            		$('#route-vehicles').empty();
	            		$('#route-vehicles').off("click");
	            		for (var i=0;i<len;i++) {
	            			arr.push(result[i].carName);
	            		}
	            		var Arr = unique(arr),
	            		A_len = Arr.length;
	            		Arr.sort(vehNum);
	            		
	            		for (var j=0;j<A_len;j++) {
	            			var add='<option value='+Arr[j]+'>'+Arr[j]+'</option>';
							$('#route-vehicles').append(add);
	            		}
	            		var val = $('#route-vehicles').find("option").eq(0).val(),
	            		_text = $('#route-vehicles').find("option").eq(0).text();
	            		$('#route-name').text(_text);
	            		
	            		
	            		
	            		
//	            		var newResult = uniqeByKeys(result,["curLoc"]),
//						newreslen = newResult.length;
	            		//////console.log(newResult)
	            		for (var f = 0;f<len;f++) {
	            			var liser = {};
	            			liser["carName"] = result[f].carName;
							liser["curLoc"] = result[f].curLoc;
							liser["siteType"] = result[f].siteType;
							liser["siteName"] = result[f].siteName;
							liser["calcDis"] = result[f].calcDis;
							liser["lng"] = result[f].siteLongitude;
							liser["lat"] = result[f].siteLatitude;
							liser["nextCurLoc"] = result[f].nextCurLoc;
							var arrTime = operationTime(result[f].arrTime);
							var endTime = operationTime(result[f].endTime);
							liser["arrTime"] = arrTime;
							liser["endTime"] = endTime;
							liser["sbVol"] = result[f].sbVol;
							liser["unloadVol"] = result[f].unloadVol;
							liser["sequence"] = result[f].sequence;
							liser["carGoods"] = result[f].carGoods;
							listPoint.push(liser);
	            		}
	            		//////console.log(listPoint);
	            		listArry="";
						listArry = uniqeByKeys(listPoint,["curLoc"]);
	            		
	            		
	            		var table = $('#SolutionVehicles').DataTable();
	            		setTimeout(function(){
	            			table.search(val).draw();
	            			vehiclesMapInit(listPoint,val)
	            		},0)
	            		
	            	}else{
	            		$('#depots-map').hide();
	            	}
	            	
	            },
	            "drawCallback":function  (settings) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        var datas = api.rows( {page:'current'} ).data();
			        //////console.log(datas);
			        var datas_len = datas.length;
			        if (datas_len !=0) {
			        	var result = datas,
			        	listPoint = [];
			        	var res = datas[0];
			        	$('#veh-type').text(res.carType);
			        	$('#veh-source').text(res.carSource);
			        	$('#veh-limit').text(res.maxLoad);
			        	$('#veh-piece').text( (res.maxRunningTime >= 999?'∞' : res.maxRunningTime));
			        	$('#veh-unloadtime').text(res.durationUnloadFull);
			        	$('#veh-speed').text(res.velocity+" km/h");
			        	for (var f = 0;f<datas_len;f++) {
	            			var liser = {};
	            			liser["carName"] = result[f].carName;
							liser["curLoc"] = result[f].curLoc;
							liser["siteType"] = result[f].siteType;
							liser["siteName"] = result[f].siteName;
							liser["calcDis"] = result[f].calcDis;
							liser["lng"] = result[f].siteLongitude;
							liser["lat"] = result[f].siteLatitude;
							liser["nextCurLoc"] = result[f].nextCurLoc;
							var arrTime = operationTime(result[f].arrTime);
							var endTime = operationTime(result[f].endTime);
							liser["arrTime"] = arrTime;
							liser["endTime"] = endTime;
							liser["sbVol"] = result[f].sbVol;
							liser["unloadVol"] = result[f].unloadVol;
							liser["sequence"] = result[f].sequence;
							liser["carGoods"] = result[f].carGoods;
							listPoint.push(liser);
	            		}
			        	var _val = $('#route-vehicles').val();
			        	vehiclesMapInit(listArry,_val,listPoint);
			        	
			        }else{
			        	$('#route-name').text("No Data");
			        	$('#veh-type').text("--");
			        	$('#veh-source').text("--");
			        	$('#veh-limit').text("--");
			        	$('#veh-piece').text("--");
			        	$('#veh-unloadtime').text("--");
			        	$('#veh-speed').text("--");
			        }
	            }
	        });
	        //点击选项 来查询
	        var table = $('#SolutionVehicles').DataTable();
			$(document).on("change","#route-vehicles",function  () {
				//////console.log(this.value)
				table.search(this.value).draw();
				$('#route-name').text(this.value);
				vehiclesMapInit(listArry,this.value)
			});

            //导出excel表格
            $('.export-btn').click(function  () {
                var _xls = $(this).attr('data-xls');
                if (_xls) {
                    window.location.href="/vehicles/exportResult";
                }
                $(".modal-header span").trigger('click');
            });
		}
		
	}());

	
});