$(function  () {
	var doc = document;
	/**
	 * index
	 */
	var code;//声明一个变量用于存储生成的验证码  
    function changeImg(){  
        //alert("换图片");  
        var arrays=new Array(  
            '1','2','3','4','5','6','7','8','9','0',  
            'a','b','c','d','e','f','g','h','i','j',  
            'k','l','m','n','o','p','q','r','s','t',  
            'u','v','w','x','y','z',  
            'A','B','C','D','E','F','G','H','I','J',  
            'K','L','M','N','O','P','Q','R','S','T',  
            'U','V','W','X','Y','Z'               
        );  
        code='';//重新初始化验证码  
        //alert(arrays.length);  
        //随机从数组中获取四个元素组成验证码  
        for(var i=0;i<4;i++){  
        //随机获取一个数组的下标  
            var r=parseInt(Math.random()*arrays.length);  
            code+=arrays[r];  
            //alert(arrays[r]);  
        }  
        //alert(code);  
        doc.getElementById('code').innerHTML=code;//将验证码写入指定区域  
    }
    (function  () {
    	var loginForm = doc.getElementById('loginForm');
    	if (loginForm) {
//  		keycode = doc.getElementById('code');
//  		keycode.onclick=changeImg;
//  		changeImg();
    		//点击登录
    		$("#index-body").keydown(function  () {
				keyLogin();
			});
			function keyLogin(){
				if (event.keyCode==13){//回车键的键值为13
					document.getElementById("loginBtn").click(); //调用登录按钮的登录事件
				}
					
			}
			$("#loginBtn").click(function(){
	            if(!$("#tel").val()) {
	                $("#tel").focus();
	                return;
	            }
	            if(!$("#password").val()) {
	                $("#password").focus();
	                return;
	            }
//	            var input_code=document.getElementById('vcode').value;
//	            if (input_code.toLowerCase() != code.toLowerCase()) {
//	            	$('#keyerro-info').text("The verification code is incorrect.");
//	            	$('#key-info').show();
//	            	return false;
//	            }
	            $("#loginForm").submit();
	        });
	        //
	        $('#key-info-close').click(function (){
	        	$('#key-info').hide();
	        });
	
	        //新用户注册
	        $("#registerBtn").click(function(){
	            $("#newUserModal").modal('show');
	        });
	        $("#saveBtn").click(function(){
	            debugger;
	            $.post("/account/new",$("#newUserForm").serialize())
	                .done(function(result){
	                    if("success" == result) {
	                        debugger;
	                        $("#newUserForm")[0].reset();
	                        $("#newUserModal").modal("hide");
	                        dt.ajax.reload();
	                    }
	                }).fail(function(){
	                alert("添加时出现异常");
	            });
	
	        });
    	}
    }());
    
	
	
	
/*
 *MyScenarios.jsp
 */
// new Category
//初始化 Category 列表函数
function CategoryList () {
	$.get("/MyScenarios/category.json").done(function  (res) {
		if (res) {
			//console.log(res);
			if (res.data.length !=0) {
				var result = res.data,
				len = result.length;
				$('#cateClass').empty();
				$('#cateClass>li').off("click");
				for (var i=0;i<len;i++) {
					var add="";
					add+='<li data-value='+result[i].id+'>'+result[i].categoryName+'</li>';
					$('#cateClass').prepend(add);
				}
				$('input[name="scenariosCategory"]').val(result[len-1].categoryName);
				$('#class-first').text(result[len-1].categoryName);
			}
		}
	}).fail(function  () {
		console.log("fail");
	})
}
//初始化 分类选项
(function  () {
	var category_out = doc.getElementById("category-out");
	if (category_out) {
		CategoryList();
		//点击显示分类下拉 下拉框
		$('#category-out').click(function  () {
			$(this).css({"border-color":"rgb(77, 144, 254)"})
			$('#Category-box').toggle();
		});
		//点 选项 进行选择
		$('body').on("click","#cateClass>li",function  () {
			var _val = $(this).attr("data-value");
			var _text = $(this).text();
			$('input[name="scenariosCategory"]').val(_text);
			$('#class-first').text(_text);
			$('#Category-box').hide();
		});
		//阻止冒泡
		$('.clearfix.classwrap').click(function  () {
			return false;
		});
		//点击add 增加选项
		$('#classbtn').click(function  () {
			var info = $('#classAdd').val();
			if (info) {
				$.post("/MyScenarios/addCategory",{"categoryName":info}).done(function  (res) {
					console.log(res);
					if (res == "success") {
						CategoryList();
					}
				})
			}else{
				$('#classAdd').focus();
				return false;
			}
		});
	}
}());
/*allscenarios.jsp*/
(function(){
    var user_out = doc.getElementById("user-out");
    if (user_out) {
        userList();
        //点击显示分类下拉 下拉框
        $('#user-out').click(function  () {
            $(this).css({"border-color":"rgb(77, 144, 254)"})
            $('#user-box').toggle();
        });
        //点 选项 进行选择
        $('body').on("click","#userClass>li",function  () {
            var _val = $(this).attr("data-value");
            var _text = $(this).text();
            $("#userId").val(_val);
            $('#class-user').text(_text);
            $('#user-box').hide();
        });
        //阻止冒泡
        $('.clearfix.classwrap').click(function  () {
            return false;
        });
    }
    //初始化 user 列表函数
    function userList () {
        $.get("/account/alluser.json").done(function  (res) {
            if (res) {
                //console.log(res);
                if (res) {
                    var result = res,
                    len = result.length;
                    $('#userClass').empty();
                    $('#userClass>li').off("click");
                    for (var i=0;i<len;i++) {
                        var add="";
                        add+='<li data-value='+result[i].id+'>'+result[i].username+'</li>';
                        $('#userClass').prepend(add);
                    }
                }
            }
        }).fail(function  () {
            console.log("fail");
        })
    }
}());




/*
 *ScenariosName.jsp
 * 
 */


//去除重复数组元素
	function unique(arr) {
		var result = [], hash = {};
		for (var i = 0, elem; (elem = arr[i]) != null; i++) {
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


(function  () {
	
	var editBtn = doc.getElementById("edit-create-info");
	if (editBtn) {
		//初始化地图
        var map = new BMap.Map("depots-map");
		var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
		var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
		var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
		map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
		map.addControl(top_right_navigation);
		
		function depotMapInit (listPoint,val) {
			map.clearOverlays();
			var point = new BMap.Point(listPoint[0].lng,listPoint[0].lat);
			map.centerAndZoom(point, 14);
			map.enableScrollWheelZoom(true);
			// 编写自定义函数,创建标注
			function addMarker(point,info){
			  var marker = new BMap.Marker(point);
			  map.addOverlay(marker);
			  marker.addEventListener("mouseover", function(){
			  	//this.openInfoWindow(info);
			  });
			  marker.addEventListener("mouseout", function(){
			  	//this.closeInfoWindow();
			  });
			}
			function addpPyline (pointA,pointB,infoWindowLine) {
				var polyline = new BMap.Polyline([pointA,pointB], {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.8});  //定义折线
				map.addOverlay(polyline);//添加折线到地图上
				polyline.addEventListener("mouseover", function(e){
					//console.log(e.point) //获取经过折线的当前坐标，触发覆盖物的事件返回值
					var point = new BMap.Point(e.point.lng,e.point.lat);
			  		//map.openInfoWindow(infoWindowLine,point);
			  	});
			  	polyline.addEventListener("mouseout", function(){
			  		//map.closeInfoWindow();
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
				addpPyline(pointA,pointB);//addpPyline(pointA,pointB,infoWindowLine);
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
				addMarker(points)//addMarker(points,infoWindow);
			}
			if (val == "") {
				console.log("a")
//				//初始化路线
//				for (var x = 0;x<p_len;x++) {
//					for (var y = 0 ;y<p_len;y++) {
//						var _curLoc = listPoint[x].curLoc,
//							_nextCurLoc = listPoint[y].nextCurLoc;
//						if (_curLoc == _nextCurLoc) {
//							depotPylineInfo(listPoint[x],listPoint[y]);
//						}
//					}
//				}
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
		
		
		
		
		var scenId = $('#scenName').attr("data-id");
		//获取场景信息
		$.get("/MyScenarios/scen.json",{"id":scenId}).done(function  (res) {
			$('#scen-cate').text(res.scenariosCategory);
			$('#scen-desc').text(res.scenariosDesc);
			$('#scenariosName').val(res.scenariosName);
			$('#class-first').text(res.scenariosCategory);
			$('#scenariosCategory').val(res.scenariosCategory);
			$('textarea[name="scenariosDesc"]').val(res.scenariosDesc);
		}).fail(function  () {
			console.log('fail');
		});
		//获取overview
		$.get("/ScenariosName/settingsOverview.json",{"id":scenId}).done(function  (res) {
            $('#depot-quantity').text(res.siteCounter);
            $('#vehicle-quantity').text(res.tranCounter);
            $('#demand-quantity').text(res.demandsCounter);
            $('#farthest-distance').text(Math.round(res.farthestDist * 100) / 100);
        }).fail(function  (e) {
            console.log('fail');
        });

        $.get("/ScenariosName/resultOverview1.json",{"id":scenId}).done(function  (res) {
        console.log(res)
            $('#title').text("Results Overview");
            if(res.scenario.scenariosModel=="1"){
            $('#simulation-method').text("串点模型");
            }else if(res.scenario.scenariosModel=="2"){
            $('#simulation-method').text("接力模型");}
            else if(res.scenario.scenariosModel=="3"){
            $('#simulation-method').text("综合模型");
            }
            $('#simulation-progress').text("100%");
            $('#simulation-finished').text(res.scenario.simulateFinishTime);
        }).fail(function  (e) {
            console.log('fail');
        });
        $.get("/ScenariosName/resultOverview2.json",{"id":scenId}).done(function  (res) {
            $('#staff-quantity').text(res.staffCount);
            $('#staff-cost').text(res.cost.sum2);
            $('#vehicle-quantity1').text(res.carCount);
            $('#vehicle-cost').text(res.cost.branchTransportCost);
            $('#total-cost').text(res.cost.totalCost);
        }).fail(function  (e) {
            console.log('fail');
        });
		
		//创建场景
        $("#edit-create-info").click(function(){
            $("#newUserModal-scen").modal('show');
        });
        $("#saveBtn-scen").click(function(){
        	var _val = $("input[name='scenariosName']").val(),
        	pattern = new RegExp("[`~!@#$^&*=|{}':;',\\[\\]<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]"),
			xss = pattern.test(_val);
        	if (_val && !xss) {
        		$.post("/MyScenarios/add",$("#newUserForm-scen").serialize()).done(function(result){
                        if("success" == result) {
                            $("#newUserForm-scen")[0].reset();
                            $("#newUserModal-scen").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        //alert("Exception occurs when adding");
                    });

        	}else{
        		$("input[name='scenariosName']").focus();
        	}
            
        });
        
		
		//用表格获取数据
		var dt =$("#ScenariosNamet").DataTable({
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
                {"data":"curLoc","name":"cur_loc"}
            ],
            "initComplete": function (settings, data) {
            	//console.log(data.data.length);
            	if (data.data.length !=0) {
            		$('#depots-map').show();
            		var result = data.data,
            		listPoint = [],
            		len = result.length;
            		var newResult = uniqeByKeys(result,["curLoc"]),
						newreslen = newResult.length;
	            		//console.log(newResult)
            		for (var f = 0;f<len;f++) {
            			var liser = {};
						liser["curLoc"] = result[f].curLoc;
						liser["siteType"] = result[f].siteType;
						liser["siteName"] = result[f].siteName;
						liser["calcDis"] = result[f].calcDis;
						liser["lng"] = result[f].siteLongitude;
						liser["lat"] = result[f].siteLatitude;
						liser["nextCurLoc"] = result[f].nextCurLoc;
						listPoint.push(liser);
            		}
					//查询所有网点坐标
					//console.log(listPoint)
//					listArry="";
//					listArry = listPoint;
						
					//百度地图
					depotMapInit(listPoint,"");

            	}else{
            		$('#depots-map').hide();
            	}

            	
            }
      });
        
        
	}
	
	
	
	
	
}());

















});
