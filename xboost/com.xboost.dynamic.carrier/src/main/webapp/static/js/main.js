$(function (){
	var list_3 = "";
	var timeNow = "";
	var url_main = "http://"+document.location.host+"/dynamic";
	var url_map = '/davav'; //地图
	var url_feixian = '/parcel_feixian';//飞线
	var url_one_hour = '/one_hour';//最近一小时新增包裹
	var url_yunli = '/pressure';//运力指数
	var url_parcel = '/realtime_parcel'; //实时包裹
	var url_time = '/date'; //实时包裹
	var sum = '';//实时增加包裹数叠加
	var time_5min = 300000;
	var time_c = 800;
	/*
	 * 
	 * modal time
	 * 
	 */
	(function (){
		var date=new Date();
		var year=date.getFullYear();//当前年份
		var month=date.getMonth();//当前月份
		var data=date.getDate();//天
		var start_time=year+"-"+add0((month+1))+"-"+add0(data)+" "+add0(10)+":"+add0(0)+":"+add0(0);
		var end_time=year+"-"+add0((month+1))+"-"+add0(data)+" "+add0(18)+":"+add0(0)+":"+add0(0);
		$('#para_start_time').text(start_time);
		$('#para_end_time').text(end_time);
		$('#para_normal_carrier_num').text(550);
		$('#para_SwoT_carrier_num').text(50);
		$('#eff_time').text("3 (hours)");
		$('#refresh_time').text("5 (mins)");
		//时间插件
		$("#start_time").jeDate({
			skinCell:"jedateblue", 
			isinitVal:false, 
			format:"hh:mm"
		});
		$("#end_time").jeDate({
			skinCell:"jedateblue", 
			isinitVal:false, 
			format:"hh:mm"
		});
		$("#cut_time").jeDate({
			skinCell:"jedateblue", 
			isinitVal:false, 
			format:"hh:mm"
		});
		//
		$('#save_para').click(function  () {
			$('#setModal').modal("hide");
		});
	}());
	
	//time
	window.onload=function(){
		//定时器每秒调用一次fnDate()
//		$.post(url_main+url_time).done(function  (obj) {
//			var oDiv=document.getElementById('time');
//			var time=obj[0].x;
//			timeNow = time;
//			oDiv.innerHTML=time;
//		}).fail(function  () {
//			console.log("fail");
//		});
		clearInterval(timeTicket0);
		var timeTicket0 = setInterval(function(){
				fnDate('time');
			},1000);
		
	};
	function fnDate_$(id,obj){
		var oDiv=document.getElementById(id);
		if (obj)
		{
			var date=new Date(obj);
		}
		else
		{
			var date=new Date();
		}
		var year=date.getFullYear();//当前年份
		var month=date.getMonth();//当前月份
		var data=date.getDate();//天
		var hours=date.getHours();//小时
		var minute=date.getMinutes();//分
		var second=date.getSeconds();//秒
		var time=year+"-"+add0((month+1))+"-"+add0(data)+" "+add0(hours)+":"+add0(minute)+":"+add0(second);
		oDiv.innerHTML=time;
		timeNow = time;
	};
	function fnDate(id){
		var oDiv=document.getElementById(id);
		var date=new Date();
		var year=date.getFullYear();//当前年份
		var month=date.getMonth();//当前月份
		var data=date.getDate();//天
		var hours=date.getHours();//小时
		var minute=date.getMinutes();//分
		var second=date.getSeconds();//秒
		var time=year+"-"+add0((month+1))+"-"+add0(data)+" "+add0(hours)+":"+add0(minute)+":"+add0(second);
		oDiv.innerHTML=time;
	};
	function status_time () {
		var i_time;
		var date=new Date();
		var year=date.getFullYear();//当前年份
		var month=date.getMonth();//当前月份
		var data=date.getDate();//天
		var hours=date.getHours();//小时
		var minute=date.getMinutes();//分
		var second=date.getSeconds();//秒
		var time=year+"-"+add0((month+1))+"-"+add0(data)+" "+add0(hours)+":"+add0(minute)+":"+add0(second);
		var time_10 = year+"-"+add0((month+1))+"-"+add0(data)+" "+add0(10)+":"+add0(0)+":"+add0(0);
		var time_10_w = year+"-"+add0((month+1))+"-"+add0(data+1)+" "+add0(10)+":"+add0(0)+":"+add0(0);
		if (time <= time_10) 
		{
			i_time = ((new Date(time_10)).getTime()) - ((new Date(time)).getTime());
		}
		else
		{
			i_time = ((new Date(time_10_w)).getTime()) - ((new Date(time)).getTime());
		}
		return i_time;
	};
	function status_time_18 () {
		var i_time;
		var date=new Date();
		var year=date.getFullYear();//当前年份
		var month=date.getMonth();//当前月份
		var data=date.getDate();//天
		var hours=date.getHours();//小时
		var minute=date.getMinutes();//分
		var second=date.getSeconds();//秒
		var time=year+"-"+add0((month+1))+"-"+add0(data)+" "+add0(hours)+":"+add0(minute)+":"+add0(second);
		var time_18 = year+"-"+add0((month+1))+"-"+add0(data)+" "+add0(18)+":"+add0(0)+":"+add0(0);
		if (time <= time_18) 
		{
			i_time = ((new Date(time_18)).getTime()) - ((new Date(time)).getTime());
		}
		return i_time;
	};
	var i_time = status_time();
	var i_time_18 = status_time_18();
	////console.log(i_time_18);
	//js 获取当前时间
	function fnDate_hours(){
		var date=new Date();
		var hours=date.getHours();//小时
		var minute=date.getMinutes();//分
		var second=date.getSeconds();//秒
		var time=add0(hours)+":"+add0(minute)+":"+add0(second);
		////console.log(time)
		if (time >= "10:00:00" && time < "18:00:00") 
		{
			return true;
		}
		else
		{
			return false;
		}
	};
	//补位 当某个字段不是两位数时补0
	function add0(m){return m<10?'0'+m:m };
	
	//去除重复数据 deport
	//将对象元素转换成字符串以作比较
	function obj2key(obj, keys){
	    var n = keys.length,
	        key = [];
	    while(n--){
	        key.push(obj[keys[n]]);
	    }
	    return key.join('|');
	};
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
	//left-pie
	(function (){
		var myChart = echarts.init(document.getElementById('left-pie'));
		window.onresize = myChart.resize; 
		var status = fnDate_hours();
		var option = {
			    title : {
			        text: 'Delay time distribution',
			        textStyle : {
			            color: '#fff',
			            fontSize:'14'
			        }
			    },
			    grid: {
				    left: '6%',
					right: '2%',
					bottom: '3%',
					containLabel: true
				},
			    tooltip : {
			        trigger: 'item',
			        formatter: function (a) {
			        	var add = '';
			        	add+='<ul class="e-tip">';
			        	add+='<li>'+a['name']+'</li>';
			        	add+='<li><span class="e-tip-point"></span>'+a['value']+'</li>';
			        	add+='<li><span class="e-tip-point"></span>'+a['data'].pp[0]+'</li>';
			        	add+='</ul>';
			        	
                        return add;
			        }
			    },
			    calculable : true,
			    xAxis : [
			        {
			        	nameTextStyle:{
			            	color:"#fff"
			            },
			            type : 'category',
			            data : ['0.5h','1h','1.5h'],
			            axisLabel:{  
		                    interval:0,  
		                    rotate:0,//倾斜度 -90 至 90 默认为0  
		                    margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		               }
			        }
			    ],
			    yAxis : [
			        {
			        	name:'No. of parcel',
			            type : 'value',
			            nameTextStyle:{
			            	color:"#fff",
			            },
			            splitLine :{
			            	show:true,
			            	lineStyle:{
								color: '#534d49',//网格线颜色
								width: 1,//网格线宽度
								type: 'dashed'//网格线样式
							},
			            },
			            axisLabel:{  
		                    margin:5,
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		                }
		                
			        }
			    ],
			    series : [
			        {
			            type:'bar',
			            barWidth:30,
			            data:[
							{name:'0.5h',pp:['0%'],value:0},
							{name:'1h',pp:['0%'],value:0},
							{name:'1.5h',pp:['0%'],value:0}
						],
			            itemStyle:{
			                normal:{color:'#358be3'}
			            }
			        }
			    ]
			};
		myChart.setOption(option);["4",'1','1'],["7",'1','1'],["4",'2','2'],["1",'2','0']
		var b = [
			[
				{name:'0.5h',pp:['60%'],value:10},
				{name:'1h',pp:['30%'],value:5},
				{name:'1.5h',pp:['10%'],value:1}
			],[
				{name:'0.5h',pp:['80%'],value:4},
				{name:'1h',pp:['10%'],value:1},
				{name:'1.5h',pp:['10%'],value:1}
			],[
				{name:'0.5h',pp:['90%'],value:7},
				{name:'1h',pp:['5%'],value:1},
				{name:'1.5h',pp:['5%'],value:1}
			],[
				{name:'0.5h',pp:['70%'],value:4},
				{name:'1h',pp:['15%'],value:2},
				{name:'1.5h',pp:['15%'],value:2}
			],[
				{name:'0.5h',pp:['34%'],value:1},
				{name:'1h',pp:['66%'],value:2},
				{name:'1.5h',pp:['0%'],value:0}
			]
		];
		clearInterval(timeTicket6);
		if (status) 
		{
			option.series[0].data = [
				{name:'0.5h',pp:['60%'],value:10},
				{name:'1h',pp:['30%'],value:5},
				{name:'1.5h',pp:['10%'],value:1}
			];
			myChart.setOption(option);
			clearInterval(timeTicket6);
			var timeTicket6 = setInterval(function (){
				var i =Math.floor(Math.random()*5);
			    option.series[0].data = b[i];
			    myChart.setOption(option, true);
			},60000);
			
			setTimeout(function  () {
				clearInterval(timeTicket6);
				option.series[0].data = [
					{name:'0.5h',pp:['0%'],value:0},
					{name:'1h',pp:['0%'],value:0},
					{name:'1.5h',pp:['0%'],value:0}
				];
			    myChart.setOption(option, true);
			    
			    clearInterval(timeTicket66_24);
			    var timeTicket66_24 = setInterval(function  () {
			    	setTimeout(function  () {
			    		option.series[0].data = [
							{name:'0.5h',pp:['60%'],value:10},
							{name:'1h',pp:['30%'],value:5},
							{name:'1.5h',pp:['10%'],value:1}
						];
						myChart.setOption(option);
						clearInterval(timeTicket6);
						var timeTicket6 = setInterval(function (){
							var i =Math.floor(Math.random()*5);
						    option.series[0].data = b[i];
						    myChart.setOption(option, true);
						},60000);
						
						setTimeout(function  () {
							clearInterval(timeTicket6);
							option.series[0].data = [
								{name:'0.5h',pp:['0%'],value:0},
								{name:'1h',pp:['0%'],value:0},
								{name:'1.5h',pp:['0%'],value:0}
							];
						    myChart.setOption(option, true);
						},28800000);
			    	},57600000);
			    },86400000);
			},i_time_18);
		}
		else
		{
			setTimeout(function (){
				option.series[0].data = [
					{name:'0.5h',pp:['60%'],value:10},
					{name:'1h',pp:['30%'],value:5},
					{name:'1.5h',pp:['10%'],value:1}
				];
				myChart.setOption(option);
				var timeTicket6 = setInterval(function (){
					var i =Math.floor(Math.random()*5);
				    option.series[0].data = b[i];
				    
				    myChart.setOption(option, true);
				},60000);
				setTimeout(function  () {
					clearInterval(timeTicket6);
					option.series[0].data = [
						{name:'0.5h',pp:['0%'],value:0},
						{name:'1h',pp:['0%'],value:0},
						{name:'1.5h',pp:['0%'],value:0}
					];
					myChart.setOption(option, true);
				},28800000);
				
				
				clearInterval(timeTicket6_24);
				var timeTicket6_24 = setInterval(function  () {
					option.series[0].data = [
						{name:'0.5h',pp:['60%'],value:10},
						{name:'1h',pp:['30%'],value:5},
						{name:'1.5h',pp:['10%'],value:1}
					];
					myChart.setOption(option);
					clearInterval(timeTicket6);
					var timeTicket6 = setInterval(function (){
						var i =Math.floor(Math.random()*5);
					    option.series[0].data = b[i];
					    
					    myChart.setOption(option, true);
					},60000);
					setTimeout(function  () {
						clearInterval(timeTicket6);
						option.series[0].data = [
							{name:'0.5h',pp:['0%'],value:0},
							{name:'1h',pp:['0%'],value:0},
							{name:'1.5h',pp:['0%'],value:0}
						];
						myChart.setOption(option, true);
					},28800000);
				},86400000);
			},i_time);
		}

	}());
	
	//left-bar
	(function (){
		var myChart = echarts.init(document.getElementById('left-bar'));
		window.onresize = myChart.resize; 
		var status = fnDate_hours();
		var option = {
			    title : {
			        text: 'Top 5 Hot Area',
			        x:'left',
        			y:'top',
			        textStyle : {
			            color: '#fff',
			            fontSize:'14'
			        },
			    },
			    tooltip : {
			        trigger: 'axis',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
			    legend: {
			        data: ['send', 'collect'],
			        textStyle: {
			            fontSize: 14,
			            color: '#fff'
			        },
			        right:"4%"
			    },
			    grid: {
				    left: '2%',
					right: '2%',
					bottom: '3%',
					containLabel: true
				},
			   
			    calculable : true,
			    xAxis : [
			        {
			            type : 'value',
			            splitLine :{
			            	show:true,
			            	lineStyle:{
								color: '#534d49',//网格线颜色
								width: 1,//网格线宽度
								type: 'dashed'//网格线样式
							},
			            },
			            
			            boundaryGap : [0, 0.01],
			            axisLabel:{  
		                    margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		                }
			        }
			    ],
			    yAxis : [
			        {
			            type : 'category',
			            data : ['5','4','3','2','1'],
			            axisLabel:{  
		                    margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		                }
			        }
			    ],
			    series : [
			       
			        {
			        	name:"send",
			            type:'bar',
			            stack: '总量',
			            label: {
			                normal: {
			                    show: false,
			                    position: 'insideRight'
			                }
			            },
			            data:[0,0,0,0,0],
			            itemStyle:{
                    		normal:{color:'#126ff8'}
                		}
			            
			        },
			        {
			        	name:"collect",
			            type:'bar',
			            stack: '总量',
			            label: {
			                normal: {
			                    show: false,
			                    position: 'insideRight'
			                }
			            },
			            data:[0,0,0,0,0],
			            itemStyle:{
                    		normal:{color:'red'}
                		}
			            
			        }
			    ]
			};
		var a = [['5.五角场','4.徐家汇','3.火车站','2.新天地','1.陆家嘴'],['5.徐家汇','4.五角场','3.火车站','2.陆家嘴','1.新天地']];
		var b = [[12, 34, 50, 78, 100],[20, 30, 50, 90, 110],[23, 40, 55, 88, 105]];
		var bb = [[8, 12, 40, 53, 78],[9, 21, 34, 72, 85],[11, 28, 46, 77, 90]];
		myChart.setOption(option);
		clearInterval(timeTicket2);
		clearInterval(timeTicket22);
		if (status) 
		{
			option.series[0].data = [12, 34, 50, 78, 100];
			option.series[1].data = [8, 24, 47, 80, 91];
		    option.yAxis[0].data = ['5.五角场','4.徐家汇','3.火车站','2.新天地','1.陆家嘴'];
		    myChart.setOption(option, true);
		    clearInterval(timeTicket2);
			var timeTicket2 = setInterval(function (){
				var i =Math.floor(Math.random()*3);
			    option.series[0].data = b[i];
			    option.series[1].data = bb[i];
			    myChart.setOption(option, true);
			},5000);
			clearInterval(timeTicket22);
			var timeTicket22 = setInterval(function (){
				var i =Math.floor(Math.random()*2);
			    option.yAxis[0].data = a[i];
			    myChart.setOption(option, true);
			},30000);
			
			setTimeout(function (){
				clearInterval(timeTicket2);
				clearInterval(timeTicket22);
				option.yAxis[0].data = ['5','4','3','2','1'];
				option.series[0].data = [0,0,0,0,0];
				option.series[1].data = [0,0,0,0,0];
			    myChart.setOption(option, true);
			    
			    clearInterval(timeTicket22_24);
			    var timeTicket22_24 = setInterval(function  () {
			    	setTimeout(function  () {
			    		option.series[0].data = [12, 34, 50, 78, 100];
					    option.yAxis[0].data = ['5.五角场','4.徐家汇','3.火车站','2.新天地','1.陆家嘴'];
					    myChart.setOption(option, true);
					    clearInterval(timeTicket2);
						var timeTicket2 = setInterval(function (){
							var i =Math.floor(Math.random()*3);
						    option.series[0].data = b[i];
						    option.series[1].data = bb[i];
						    myChart.setOption(option, true);
						},5000);
						clearInterval(timeTicket22);
						var timeTicket22 = setInterval(function (){
							var i =Math.floor(Math.random()*2);
						    option.yAxis[0].data = a[i];
						    myChart.setOption(option, true);
						},30000);
						setTimeout(function  () {
							clearInterval(timeTicket2);
							clearInterval(timeTicket22);
							option.yAxis[0].data = ['5','4','3','2','1'];
							option.series[0].data = [0,0,0,0,0];
							option.series[1].data = [0,0,0,0,0];
						    myChart.setOption(option, true);
						},28800000);
			    	},57600000);
			    },86400000);
			    
			},i_time_18);
		}
		else
		{
			setTimeout(function (){
				option.series[0].data = [12, 34, 50, 78, 100];
				option.series[1].data = [8, 24, 47, 80, 91];
			    option.yAxis[0].data = ['5.五角场','4.徐家汇','3.火车站','2.新天地','1.陆家嘴'];
			    myChart.setOption(option, true);
			    clearInterval(timeTicket2);
				var timeTicket2 = setInterval(function (){
					var i =Math.floor(Math.random()*3);
				    option.series[0].data = b[i];
				    option.series[1].data = bb[i];
				    myChart.setOption(option, true);
				},5000);
				clearInterval(timeTicket22);
				var timeTicket22 = setInterval(function (){
					var i =Math.floor(Math.random()*2);
				    option.yAxis[0].data = a[i];
				    myChart.setOption(option, true);
				},30000);
				setTimeout(function  () {
					clearInterval(timeTicket2);
					clearInterval(timeTicket22);
					option.series[0].data = [0, 0, 0, 0, 0];
					option.series[1].data = [0, 0, 0, 0, 0];
			    	option.yAxis[0].data = ['5','4','3','2','1'];
			    	myChart.setOption(option, true);
				},28800000);
				
				
				clearInterval(timeTicket2_24);
				var timeTicket2_24 = setInterval(function  () {
					option.series[0].data = [12, 34, 50, 78, 100];
					option.series[1].data = [8, 24, 47, 80, 91];
				    option.yAxis[0].data = ['5.五角场','4.徐家汇','3.火车站','2.新天地','1.陆家嘴'];
				    myChart.setOption(option, true);
				    clearInterval(timeTicket2);
					var timeTicket2 = setInterval(function (){
						var i =Math.floor(Math.random()*3);
					    option.series[0].data = b[i];
					    option.series[1].data = bb[i];
					    myChart.setOption(option, true);
					},5000);
					clearInterval(timeTicket22);
					var timeTicket22 = setInterval(function (){
						var i =Math.floor(Math.random()*2);
					    option.yAxis[0].data = a[i];
					    myChart.setOption(option, true);
					},30000);
					setTimeout(function  () {
						clearInterval(timeTicket2);
						clearInterval(timeTicket22);
						option.series[0].data = [0, 0, 0, 0, 0];
						option.series[1].data = [0, 0, 0, 0, 0];
				    	option.yAxis[0].data = ['5','4','3','2','1'];
				    	myChart.setOption(option, true);
					},28800000);
				},86400000);
			},i_time);
		}
		
	}());
	
	//left-line
	(function  () {
		var myChart = echarts.init(document.getElementById('left-line'));
		window.onresize = myChart.resize; 
		var date_s=new Date();
		var status = fnDate_hours();
		var second=date_s.getSeconds();//秒
		var second_s=(60-second)*1000;//秒
		var option = {
			    title : {
			        text: 'transportation/demands',
			        textStyle : {
			            color: '#fff',
			            fontSize:'14'
			        }
			    },
			    grid: {
				    left: '8%',
					right: '2%',
					bottom: '3%',
					containLabel: true
				},
			    tooltip : {
			        trigger: 'axis'
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : [],
			            axisLabel:{  
		                    margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		                }
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            name:'Capacity index',
			            nameTextStyle:{
			            	color:'#fff'
			            },
			            splitLine :{
			            	show:true,
			            	lineStyle:{
								color: '#534d49',//网格线颜色
								width: 1,//网格线宽度
								type: 'dashed'//网格线样式
							},
			            },
			            axisLabel : {
			                formatter: '{value}',
			                margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
			            }
			        }
			    ],
			    series : [
			        {
			            type:'line',
			            data:[0,0,0,0,0,0,0],
			            itemStyle:{
			                normal:{color:'#58f4e6'}
			            }
			        }
			    ]
			};
		function getTime (arr) {
			var len = arr.length;
			var xlist = [];
			var ylist = [];
			var list = [];
			if (len != 0 && len < 7) 
			{
				xlist.push('09:55');
				ylist.push(0);
				for (var i=0;i<len;i++) 
				{
					var add_x = ( arr[i].x ).slice(0,5);
					var add_y = ( arr[i].y ).toFixed(3);
					xlist.push(add_x);
					ylist.push(add_y);
				}
				list.push(xlist);
				list.push(ylist);
			}
			else
			{
				var arr_last = arr.slice(-7);
				for (var i=0;i<7;i++) 
				{
					var add_x = ( arr_last[i].x ).slice(0,5);
					var add_y = ( arr_last[i].y ).toFixed(3);
					xlist.push(add_x);
					ylist.push(add_y);
				}
				list.push(xlist);
				list.push(ylist);
			}
			return list;
			
		};
		myChart.setOption(option);
		function Pressure () {
			$.post(url_main+url_yunli).done(function  (res) {
				//console.log(res);
				var result = getTime(res);
				//console.log(result)
				if (result.length != 0) 
				{
					option.xAxis[0].data = result[0];
					option.series[0].data = result[1];
				}
				myChart.setOption(option);
			}).fail(function  () {
				console.log("fail");
			});
		};
		setTimeout(function  () {
			Pressure();
			clearInterval(time33)
			var time33 = setInterval(function  () {
				Pressure();
			},time_5min);
		},time_c);
		
		/*
		 * option.xAxis[0].data = getTime();
			option.series[0].data = b;
			myChart.setOption(option, true);
		 
		 * */
		
		
	}());
	
	//main
	(function (){
		var myChart = echarts.init(document.getElementById('main'));
		window.onresize = myChart.resize; 
		var status = fnDate_hours();
		var myCarrList;
		var myPartList;
		var myData = [];
		var myData1 = [];
		var myLine_all;
		var myLine = [];
		var carrTimeId;
		var partTimeId;
		var count_carr = 0;
		var count_part = 0;
		var count_line = 0;
		var timeTicket4;
		var timeTicket44;
		var timeTicket444;
		var timeTicketAjax;
		var lineCount;
		var effect = {
		    show: true,
		    period: 10,             // 运动周期，无单位，值越大越慢
		    color: '#fff',
		    shadowColor: 'rgba(220,220,220,0.4)',
		    shadowBlur : 5 
		};
		function itemStyle(idx) {
		    return {
		        normal: {
		            color:'#fff',
		            borderWidth:1,
		            borderColor:['rgba(30,144,255,1)','lime'][idx],
		            lineStyle: {
		                //shadowColor : ['rgba(30,144,255,1)','lime'][idx], //默认透明
		                //shadowBlur: 10,
		                //shadowOffsetX: 0,
		                //shadowOffsetY: 0,
		                type: 'solid'
		            }
		        }
		    }
		};
		function carrTimeIdList (arr) {
			var arrlist = uniqeByKeys(arr,["timeID"]);
			var len = arrlist.length;
			var list = [];
			for (var i=0;i<len;i++) {
				list.push(arrlist[i].timeID);
			}
			return list;
		};
		function partTimeIdList (arr) {
			var arrlist = uniqeByKeys(arr,["timeId"]);
			var len = arrlist.length;
			var list = [];
			for (var i=0;i<len;i++) 
			{
				list.push(arrlist[i].timeId);
			}
			return list;
		};
		function carrlist (arr,id) {
			var len = arr.length;
			var list = [];
			if (len) 
			{
				for (var i=0;i<len;i++) 
				{
					if (arr[i].timeID == id)
					{
						var item = [],
							a = Number(arr[i].currentLong),
							b = Number(arr[i].currentLat);
						item.push(a);
						item.push(b);
						list.push(item);
					}
				}
			}
			return list;
		};
		function parlist (arr,id) {
			var len = arr.length;
			var list = [];
			if (len) 
			{
				for (var i=0;i<len;i++) 
				{
					if (arr[i].timeId == id)
					{
						var item_p = [],
							item_pp = [],
							a_p = Number(arr[i].origLong),
							b_p = Number(arr[i].origLat),
							a_pp = Number(arr[i].destLong),
							b_pp = Number(arr[i].destLat);
						item_p.push(a_p);
						item_p.push(b_p);
						item_pp.push(a_pp);
						item_pp.push(b_pp);
						list.push(item_p);
						list.push(item_pp);
					}
				}
			}
			return list;
		};
		function parlist_line (arr,id) {
			var len = arr.length;
			var list = [];
			if (len) 
			{
				for (var i=0;i<len;i++) 
				{
					if (arr[i].timeId == id)
					{
						var item_orig = [],
							item_dest = [],
							item_all = "";
							a_orig = Number(arr[i].origLong),
							b_orig = Number(arr[i].origLat),
							a_dest = Number(arr[i].destLong),
							b_dest = Number(arr[i].destLat);
						item_orig.push(a_orig);
						item_orig.push(b_orig);
						item_dest.push(a_dest);
						item_dest.push(b_dest);
						item_all = {
				    		"coords":[item_orig,item_dest],
				    		"lineStyle":{
				    			"normal":{
				    				color:"rgba(88,186,247,1)"
				    			}
				    		}
				    	}
						list.push(item_all);
					}
				}
			}
			return list;
		};
		//算当前时间
		function timenow  () {
			var data = new Date();
			var hours = data.getHours();
			var minutes = data.getMinutes();
			var id = '';
			//个位
			var a = minutes%10;
			if (a <= 5)
			{
				var m = minutes - a;
				id = ((hours*60) + m)/5;
			}
			else if (a > 5)
			{
				var m = minutes - a + 5;
				id = ((hours*60) + m)/5;
			}
			return id ;
		};
		//循环输出飞线
		function lineFly (arr,count,x) {
			var _len = arr.length;
			var list = [];
			var index = 0;
			var sum = Math.abs(count*x - _len);
			if (sum <= x) 
			{
				count = 0;
				count_line = -1;
				index = count*x;
				for (var i=0;i<sum;i++) 
				{
					list.push(arr[index+i]);
				}
			}
			else
			{
				index = count*x;
				for (var i=0;i<x;i++) 
				{
					list.push(arr[index+i]);
				}
			}
			
			return list;
		};
		function renderItem(params, api) {
		    var coords = [
		        [121.442293,31.280756],
		        [121.381352,31.265448],
		        [121.433094,31.235317],
		        [121.469889,31.239763],
		        [121.485986,31.2773]
		    ];
		    var points = [];
		    for (var i = 0; i < coords.length; i++) {
		        points.push(api.coord(coords[i]));
		    }
		    var color = api.visual('color');
		
		    return {
		        type: 'polygon',
		        shape: {
		            points: echarts.graphic.clipPointsByRect(points, {
		                x: params.coordSys.x,
		                y: params.coordSys.y,
		                width: params.coordSys.width,
		                height: params.coordSys.height
		            })
		        },
		        style: api.style({
		            fill: color,
		            stroke: echarts.color.lift(color)
		        })
		    };
		};
		function renderItem1(params, api) {
		    var coords = [
		        [121.438843,31.207647],
		        [121.446317,31.17206],
		        [121.507833,31.200729],
		        [121.529105,31.250136],
		        [121.473338,31.237293]
		    ];
		    var points = [];
		    for (var i = 0; i < coords.length; i++) {
		        points.push(api.coord(coords[i]));
		    }
		    var color = api.visual('color');
		
		    return {
		        type: 'polygon',
		        shape: {
		            points: echarts.graphic.clipPointsByRect(points, {
		                x: params.coordSys.x,
		                y: params.coordSys.y,
		                width: params.coordSys.width,
		                height: params.coordSys.height
		            })
		        },
		        style: api.style({
		            fill: color,
		            stroke: echarts.color.lift(color)
		        })
		    };
		};
		function Carr_list (arr) {
			var len = arr.length;
			var list = [];
			if (len) 
			{
				for (var i=0;i<len;i++) 
				{
					var item = [],
						a = Number(arr[i].currentLong),
						b = Number(arr[i].currentLat);
					item.push(a);
					item.push(b);
					list.push(item);
				}
			}
			return list; 
		};
		function Par_list (arr) {
			var len = arr.length;
			var list = [];
			if (len) 
			{
				for (var i=0;i<len;i++) 
				{
					
					var item_p = [],
						item_pp = [];
					var arr_x = (arr[i].x).split(",");
					var arr_y = (arr[i].y).split(",");
					//console.log(arr_x)
					//console.log(arr_y)
					item_p.push(arr_x[0],arr_x[1]);
					item_pp.push(arr_y[0],arr_y[1]);
					list.push(item_p);
					list.push(item_pp);
				}
			}
			return list;
		};
		function Par_list_line (arr) {
			var len = arr.length;
			var list = [];
			if (len) 
			{
				for (var i=0;i<len;i++) 
				{
					var item_p = [],
						item_all = '',
						item_pp = [];
					var arr_x = (arr[i].x).split(',');
					var arr_y = (arr[i].y).split(',');
					item_p.push(arr_x[0],arr_x[1]);
					item_pp.push(arr_y[0],arr_y[1]);
					item_all = {
			    		"coords":[item_p,item_pp],
			    		"lineStyle":{
			    			"normal":{
			    				color:"rgba(88,186,247,1)"
			    			}
			    		}
			    	}
					list.push(item_all);
				}
			}
			return list;
		};
		var app = {},
		option = null;
		option = {
	        bmap: {
	            center: [121.491280, 31.220435],
	            zoom: 12,
	            roam: true,
	            mapStyle: {
	              'styleJson': [
	                {
	                  'featureType': 'water',
	                  'elementType': 'all',
	                  'stylers': {
	                    'color': '#031628'
	                  }
	                },
	                {
	                  'featureType': 'land',
	                  'elementType': 'geometry',
	                  'stylers': {
	                    'color': '#000102'
	                  }
	                },
	                {
	                  'featureType': 'highway',
	                  'elementType': 'all',
	                  'stylers': {
	                    'visibility': 'off'
	                  }
	                },
	                {
	                  'featureType': 'arterial',
	                  'elementType': 'geometry.fill',
	                  'stylers': {
	                    'color': '#000000'
	                  }
	                },
	                {
	                  'featureType': 'arterial',
	                  'elementType': 'geometry.stroke',
	                  'stylers': {
	                    'color': '#0b3d51'
	                  }
	                },
	                {
	                  'featureType': 'local',
	                  'elementType': 'geometry',
	                  'stylers': {
	                    'color': '#000000'
	                  }
	                },
	                {
	                  'featureType': 'railway',
	                  'elementType': 'geometry.fill',
	                  'stylers': {
	                    'color': '#000000'
	                  }
	                },
	                {
	                  'featureType': 'railway',
	                  'elementType': 'geometry.stroke',
	                  'stylers': {
	                    'color': '#08304b'
	                  }
	                },
	                {
	                  'featureType': 'subway',
	                  'elementType': 'geometry',
	                  'stylers': {
	                    'lightness': -70
	                  }
	                },
	                {
	                  'featureType': 'building',
	                  'elementType': 'geometry.fill',
	                  'stylers': {
	                    'color': '#000000'
	                  }
	                },
	                {
	                  'featureType': 'all',
	                  'elementType': 'labels.text.fill',
	                  'stylers': {
	                    'color': '#857f7f'
	                  }
	                },
	                {
	                  'featureType': 'all',
	                  'elementType': 'labels.text.stroke',
	                  'stylers': {
	                    'color': '#000000'
	                  }
	                },
	                {
	                  'featureType': 'building',
	                  'elementType': 'geometry',
	                  'stylers': {
	                    'color': '#022338'
	                  }
	                },
	                {
	                  'featureType': 'green',
	                  'elementType': 'geometry',
	                  'stylers': {
	                    'color': '#062032'
	                  }
	                },
	                {
	                  'featureType': 'boundary',
	                  'elementType': 'all',
	                  'stylers': {
	                    'color': '#465b6c'
	                  }
	                },
	                {
	                  'featureType': 'manmade',
	                  'elementType': 'all',
	                  'stylers': {
	                    'color': '#022338'
	                  }
	                },
	                {
	                  'featureType': 'label',
	                  'elementType': 'all',
	                  'stylers': {
	                    'visibility': 'off'
	                  }
	                }
	              ]
	            }
	        },
	        
	        series: [
	        {
	            type: 'lines',
	            coordinateSystem: 'bmap',
	            polyline: true,
	            data:myLine,
	            lineStyle: {
	                normal: {
	                    width: 1
	                }
	            },
	            effect: {
	                constantSpeed: 40,
	                show: true,
	                trailLength: 0.5,
	                symbolSize: 5
	            },
	            zlevel: 1
	        },
	        
	        {
	        	type: 'scatter',
	            coordinateSystem: 'bmap',
	            data: myData,
	            itemStyle : {
                	normal:{
                		color:"#ddb926",
                	}
            	},
	            symbolSize:5
	        },
	        {
	        	type: 'scatter',
	            coordinateSystem: 'bmap',
	            data: myData1,
	            itemStyle : {
                	normal:{
                		color:"#ddb926",
                	}
            	},
            	symbolSize:5
	        }
	        /*
	        {
	            type: 'custom',
	            coordinateSystem: 'bmap',
	            renderItem: renderItem,
	            itemStyle: {
	                normal: {
	                    opacity: 0.5,
	                    color:'blue'
	                }
	            },
	            animation: false,
	            silent: true,
	            data: [0],
	            z: -10
	        },
	        {
	            type: 'custom',
	            coordinateSystem: 'bmap',
	            renderItem: renderItem1,
	            itemStyle: {
	                normal: {
	                    opacity: 0.5,
	                    color:'red'
	                }
	            },
	            animation: false,
	            silent: true,
	            data: [0],
	            z: -10
	        }*/
	        
	        
	        
	        ]
	    };
	    /*
	    var myData = [
		    {value:[121.485615,31.215004]},
		    {value:[121.420446,31.260015]}
		  ];
		
		*/
					
		function mapajax () {
			$.ajax({
				type:"post",
				url:url_main+url_map,
				async:true,
				success:function  (data) {
					var timeTicket55;
					clearInterval(timeTicket55);
					var $data = data;//地图
		        	$.post(url_main+url_feixian).done(function  (res) {
		        		var $res = res;//飞线
		        		var line = Par_list_line($res);
		        		var len = line.length;
		        		var count = 1;
		        		if (len !=0 ) 
		        		{
		        			option.series[0].data = [line[0]];
		        		}
		        		option.series[1].data = Carr_list($data);
		        		option.series[2].data = Par_list($res);
		        		myChart.setOption(option);
		        		if (len > 1)
		        		{
		        			clearInterval(timeTicket55);
			        		timeTicket55 = setInterval(function  () {
			        			if (count >= len) 
			        			{
			        				count = 0;
			        			}
			        			option.series[0].data = [line[count]];
			        			count++;
			        			myChart.setOption(option);
			        		},5000);
		        		}
		        		
		        		
		        	}).fail(function  () {
		        		console.log("fail");
		        	});
		        },
		        error:function  () {
		        	console.log("fail");
		        }
			});
		};
		//初始化
		mapajax();
		clearInterval(timeTicket5);
		var timeTicket5 = setInterval(function  () {
			mapajax();
		},time_5min);
	}());
	
	
	//right-dashboard
	(function (){
		var myChart = echarts.init(document.getElementById('right-dashboard'));
		window.onresize = myChart.resize; 
		var status = fnDate_hours();
		//myChart.showLoading();  //加载效果
		var status = fnDate_hours();
		var option = {
			    title : {
			        text: 'Carrier working hours distribution',
			        textStyle : {
			            color: '#fff',
			            fontSize:'14'
			        }
			    },
			    grid: {
				    left: '5%',
					right: '2%',
					bottom: '3%',
					containLabel: true
				},
			    tooltip : {
			        trigger: 'axis'
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            data : ['2h','4h','6h','8h'],
			            axisLabel:{  
		                    interval:0,  
		                    rotate:0,//倾斜度 -90 至 90 默认为0  
		                    margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		               }
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            name:'Total percentage',
			            nameTextStyle:{
			            	color:'#fff'
			            },
			            splitLine :{
			            	show:true,
			            	lineStyle:{
								color: '#534d49',//网格线颜色
								width: 1,//网格线宽度
								type: 'dashed'//网格线样式
							},
			            },
			            axisLabel:{ 
			            	formatter: '{value} %',  //显示百分比
		                    margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		               }
			        }
			    ],
			    series : [
			        {
			            type:'bar',
			            barWidth:30,
			            data:[0,0,0,0],
			            itemStyle:{
			                normal:{
			                	color:'#358be3',
			                	label:{
			                		show: false,//是否展示 
			                		formatter:'{c}%' //显示百分比
			                	}
			                }
			            }
			        }
			    ]
			};
		myChart.setOption(option);
		var b = [[0,3,20,77],[0,2,18,80],[0,5,12,83],[0,2,20,78],[0,4,10,86]];
		clearInterval(timeTicket6);
		if (status) 
		{
			option.series[0].data = [0,3,20,77];
			myChart.setOption(option);
			clearInterval(timeTicket6);
			var timeTicket6 = setInterval(function (){
				var i =Math.floor(Math.random()*5);
			    option.series[0].data = b[i];
			    myChart.setOption(option, true);
			},60000);
			
			setTimeout(function  () {
				clearInterval(timeTicket6);
				option.series[0].data = [0,0,0];
			    myChart.setOption(option, true);
			    
			    clearInterval(timeTicket66_24);
			    var timeTicket66_24 = setInterval(function  () {
			    	setTimeout(function  () {
			    		option.series[0].data = [0,3,20,77];
						myChart.setOption(option);
						clearInterval(timeTicket6);
						var timeTicket6 = setInterval(function (){
							var i =Math.floor(Math.random()*5);
						    option.series[0].data = b[i];
						    myChart.setOption(option, true);
						},60000);
						
						setTimeout(function  () {
							clearInterval(timeTicket6);
							option.series[0].data = [0,0,0];
						    myChart.setOption(option, true);
						},28800000);
			    	},57600000);
			    },86400000);
			},i_time_18);
		}
		else
		{
			setTimeout(function (){
				option.series[0].data = [0,3,20,77];
				myChart.setOption(option);
				var timeTicket6 = setInterval(function (){
					var i =Math.floor(Math.random()*5);
				    option.series[0].data = b[i];
				    
				    myChart.setOption(option, true);
				},60000);
				setTimeout(function  () {
					clearInterval(timeTicket6);
					option.series[0].data = ["0",'0','0'];
					myChart.setOption(option, true);
				},28800000);
				
				
				clearInterval(timeTicket6_24);
				var timeTicket6_24 = setInterval(function  () {
					option.series[0].data = [0,3,20,77];
					myChart.setOption(option);
					clearInterval(timeTicket6);
					var timeTicket6 = setInterval(function (){
						var i =Math.floor(Math.random()*5);
					    option.series[0].data = b[i];
					    
					    myChart.setOption(option, true);
					},60000);
					setTimeout(function  () {
						clearInterval(timeTicket6);
						option.series[0].data = ["0",'0','0'];
						myChart.setOption(option, true);
					},28800000);
				},86400000);
			},i_time);
		}
	}());
         
    //right-bar
	(function (){
		var myChart = echarts.init(document.getElementById('right-bar'));
		window.onresize = myChart.resize; 
		var status = fnDate_hours();
		var option = {
			    title : {
			        text: 'Realtime Parcels Status',
			        textStyle : {
			            color: '#fff',
			            fontSize:'14'
			        }
			    },
			    grid: {
				    left: '5%',
					right: '2%',
					bottom: '3%',
					containLabel: true
				},
			    tooltip : {
			        trigger: 'item',
			        formatter: function (a) {
			        	var add = '';
			        	add+='<ul class="e-tip">';
			        	add+='<li>'+a['name']+'</li>';
			        	add+='<li><span class="e-tip-point"></span>'+a['value']+'</li>';
			        	add+='<li><span class="e-tip-point"></span>'+a['data'].pp[0]+'</li>';
			        	add+='</ul>';
			        	
                        return add;
			        }
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            data : ['Unattended','Pickup','Delivered'],
			            axisLabel:{  
		                    interval:0,  
		                    rotate:30,//倾斜度 -90 至 90 默认为0  
		                    margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		                }
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            name:'No. of parcel',
			            nameTextStyle:{
			            	color:'#fff'
			            },
			            splitLine :{
			            	show:true,
			            	lineStyle:{
								color: '#534d49',//网格线颜色
								width: 1,//网格线宽度
								type: 'dashed'//网格线样式
							},
			            },
			            axisLabel:{  
		                    margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		                }
			        }
			    ],
			    series : [
			        {
			            type:'bar',
			            barWidth:30,
			            data:[
			            	{name:'Unattended',pp:['0%'],value:0},
							{name:'Pickup',pp:['0%'],value:0},
							{name:'Delivered',pp:['0%'],value:0}
						],
			            itemStyle:{
			                normal:{color:'#358be3'}
			            }
			        }
			    ]
			};
		myChart.setOption(option);
		function Parcels_status () {
			$.post(url_main+url_parcel).done(function  (res) {
				//console.log(res);
				if (res.length != 0)
				{
					var sum=0;
					var list = [];
					var len = res.length;
					for (var i=0;i<len;i++) 
					{
						sum = sum + Math.ceil(res[i].y);
					}
					
					var yy0 = Math.ceil(res[0].y);
					var yy1 = Math.ceil(res[1].y);
					var yy2 = sum - yy0 - yy1;
					var per0 = ( (yy0/sum)*100 ).toFixed(2) + "%";
					var per1 = ( (yy1/sum)*100 ).toFixed(2) + "%";
					var per2 = ( (yy2/sum)*100 ).toFixed(2) + "%";
					var add0 = {name:'Unattended',pp:[per0],value:yy0};
					var add1 = {name:'Pickup',pp:[per1],value:yy1};
					var add2 = {name:'Delivered',pp:[per2],value:yy2};
					list.push(add0);
					list.push(add1);
					list.push(add2);
					option.series[0].data = list;
					myChart.setOption(option);
				}
			});
		};
		setTimeout(function  () {
			Parcels_status();
			clearInterval(time6);
			var time6 = setInterval(function  () {
				Parcels_status();
			},time_5min);
		},time_c);
		


	}());
	
	//right-line
	(function (){
		var myChart = echarts.init(document.getElementById('right-line'));
		window.onresize = myChart.resize; 
		var status = fnDate_hours();
		var date=new Date();
		var minute=date.getMinutes();//分
		var second=date.getSeconds();//秒
		var time_i = ( (59-minute)*60+ (60-second) )*1000;
		var data_i='';
		var option = {
			    title : {
			        text: 'New Parcels occurred of accumulative total',
			        textStyle : {
			            color: '#fff',
			            fontSize:'14'
			        }
			    },
			    grid: {
				    left: '5%',
					right: '2%',
					bottom: '3%',
					containLabel: true
				},
			    tooltip : {
			        trigger: 'axis'
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : [],
			            axisLabel:{  
		                    margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
		                }
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            name:'Total parcel',
			            nameTextStyle:{
			            	color:'#fff'
			            },
			            splitLine :{
			            	show:true,
			            	lineStyle:{
								color: '#534d49',//网格线颜色
								width: 1,//网格线宽度
								type: 'dashed'//网格线样式
							},
			            },
			            axisLabel : {
			                formatter: '{value}',
			                margin:5,  
		                    textStyle:{  
		                        fontWeight:"bolder",  
		                        color:"#fff"  
		                    }  
			            }
			        }
			    ],
			    series : [
			
			        {
			            type:'line',
			            areaStyle: {normal: {}},
			            data:[0, 0, 0, 0, 0, 0],
			            itemStyle:{
			                normal:{color:'#2078d1'}
			            }
			            
			           
			        }
			    ]
			};
		function getTimeHour (arr) {
			var len = arr.length;
			var xlist = [];
			var ylist = [];
			var list = [];
			if (len != 0)
			{
				for (var i=0;i<len;i++)
				{
					var x =( arr[i].x ).slice(0,5);
					xlist.unshift(x);
					ylist.unshift(arr[i].y);
					list.push(xlist);
					list.push(ylist);
				}
				
			}
			return list;
			
		};
		
		/*
		 * option.xAxis[0].data = getTimeHour();
			    option.series[0].data = b;
			    myChart.setOption(option, true);
		 */
		myChart.setOption(option);
		function New_parcels () {
			$.post(url_main+url_one_hour).done(function  (res) {
				console.log(res);
				var result = getTimeHour(res);
				if (result[0] != []) 
				{
					option.xAxis[0].data = result[0];
			    	option.series[0].data = result[1];
				}
			    myChart.setOption(option);
			}).fail(function  () {
				console.log("fail");
			});
		};
		setTimeout(function  () {
			New_parcels();
			clearInterval(time77);
			var time77 = setInterval(function  () {
				New_parcels();
			},time_5min);
		},time_c);
		
		
		
	}());
                                                   
 	
	
	
});