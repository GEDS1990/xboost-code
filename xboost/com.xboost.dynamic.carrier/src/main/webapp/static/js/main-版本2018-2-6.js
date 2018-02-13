$(function (){
	var list_3 = "";
	var list_7 = "";
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
		clearInterval(timeTicket0);
		var timeTicket0 = setInterval(function(){
				fnDate('time');
			},1000);
		
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
		var status = fnDate_hours();
		var i=0;
		var colors = ["#2078d1","#53f5f3","#2078d1","#53f5f3"];
		var	option = {
				color: ["#2078d1","#53f5f3"],
			    tooltip: {
			        trigger: 'item',
			        formatter: "{a} <br/>{b}: {c} ({d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        x: 'left',
			        data:['SWOT Carrier','Reqular Carrier'],
			        textStyle : {
			            color: '#fff'
			        }
			    },
			    series: [
			        {
			            type:'pie',
			            radius: ['50%', '70%'],
			            avoidLabelOverlap: false,
			            itemStyle : {  
			                normal : {  
			                    label : {  
			                        show : true,
			                        formatter: '{b} : {c} ({d}%)' 
			                    },  
			                    labelLine : {  
			                        show : true  
			                    }  
			                },  
			                emphasis : {  
			                    label : {  
			                        show : true,  
			                        position : 'center',  
			                        textStyle : {  
			                            fontSize : '18',  
			                            fontWeight : 'bold'  
			                        }  
			                    }  
			                }  
			            }, 
			            label:{
			            	normal:{
			            		textStyle:{
			            			fontSize:10,
			            			color:"#fff"
			            		}
			            	}
			            },
			            data:[
			                {value:0, name:'SWOT Carrier'},
			                {value:0, name:'Reqular Carrier'}
			            ]
			        }
			    ]
			};
		var a = [48,50,52];
		var b = [850,850,850];
		myChart.setOption(option);
		clearInterval(timeTicket1);
		if (status) 
		{
			option.series[0].data[0].value = 48;
		    option.series[0].data[1].value = 850;
		    myChart.setOption(option, true);
			var timeTicket1 = setInterval(function (){
				var i =Math.floor(Math.random()*3);
			    option.series[0].data[0].value = a[i];
			    option.series[0].data[1].value = b[i];
			    
			    myChart.setOption(option, true);
			},30000);
			setTimeout(function (){
				
				clearInterval(timeTicket1);
				option.series[0].data[0].value = 0;
			    option.series[0].data[1].value = 0;
				myChart.setOption(option);
				
				clearInterval(timeTicket11_24);
				var timeTicket11_24 = setInterval(function  () {
					setTimeout(function  () {
						option.series[0].data[0].value = 48;
					    option.series[0].data[1].value = 802;
					    myChart.setOption(option, true);
						var timeTicket1 = setInterval(function (){
							var i =Math.floor(Math.random()*3);
						    option.series[0].data[0].value = a[i];
						    option.series[0].data[1].value = b[i];
						    
						    myChart.setOption(option, true);
						},30000);
						setTimeout(function (){
							clearInterval(timeTicket1);
							option.series[0].data[0].value = 0;
						    option.series[0].data[1].value = 0;
							myChart.setOption(option);
						},28800000);
					},57600000);
				},86400000);
				
			},i_time_18);
			
		}
		else
		{
			setTimeout(function (){
				option.series[0].data[0].value = 48;
			    option.series[0].data[1].value = 802;
			    myChart.setOption(option, true);
				var timeTicket1 = setInterval(function (){
					var i =Math.floor(Math.random()*3);
				    option.series[0].data[0].value = a[i];
				    option.series[0].data[1].value = b[i];
				    
				    myChart.setOption(option, true);
				},30000);
				setTimeout(function  () {
					clearInterval(timeTicket1);
					option.series[0].data[0].value = 0;
				    option.series[0].data[1].value = 0;
				    myChart.setOption(option, true);
				},28800000);
				clearInterval(timeTicket1_24);
				var timeTicket1_24 = setInterval(function  () {
			    	option.series[0].data[0].value = 48;
				    option.series[0].data[1].value = 802;
				    myChart.setOption(option, true);
				    clearInterval(timeTicket1);
				    var timeTicket1 = setInterval(function (){
						var i =Math.floor(Math.random()*3);
					    option.series[0].data[0].value = a[i];
					    option.series[0].data[1].value = b[i];
					    
					    myChart.setOption(option, true);
					},30000);
					setTimeout(function  () {
						clearInterval(timeTicket1);
						option.series[0].data[0].value = 0;
					    option.series[0].data[1].value = 0;
					    myChart.setOption(option, true);
					},28800000);
			    },86400000);
			},i_time);
		}
		
	}());
	
	//left-bar
	(function (){
		var myChart = echarts.init(document.getElementById('left-bar'));
		var status = fnDate_hours();
		var option = {
			    title : {
			        text: 'Top 5 Hot Area',
			        x:'left',
        			y:'top',
			        textStyle : {
			            color: '#fff',
			            fontSize:18
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
		var date_s=new Date();
		var status = fnDate_hours();
		var second=date_s.getSeconds();//秒
		var second_s=(60-second)*1000;//秒
		var option = {
			    title : {
			        text: 'transportation/demands',
			        textStyle : {
			            color: '#fff'
			        }
			    },
			    grid: {
				    left: '2%',
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
		var getTime = function  () {
			var arr = [];//
			var date=new Date();
  			var hours=date.getHours();//小时
			var minute=date.getMinutes();//分
			for (var i=6;i>-1;i--) 
			{
				var m = minute - i - 1;
				var h = hours - 1;
				if (m < 0) 
				{
					if (h == -1) 
					{
						var hh = 23;
					}
					else
					{
						var hh = add0(h);
					}
					var mm = 60 - Math.abs(m);
				}
				else
				{
					var mm = add0(m);
					var hh = add0(hours);
				}
				var add = hh + ":" + mm;
				arr.push(add);
			}
			return arr;
		};
		var getY = function  (arr,arrB) {
			var list = [];
			var len = arr.length;
			for (var i=0;i<len;i++) 
			{
				if (arr[i] < "10:00" || arr[i] >= "18:00") 
				{
					list.push(0);
				}
				else
				{
					list.push(arrB[i]);
				}
			}
			return list;
		};
		option.xAxis[0].data = getTime();
		myChart.setOption(option);
		var bb = [6.3, 6.2, 6.0, 6.1, 6.0, 6.3, 6.1];
		if (status) 
		{
			var aa = getTime();
			var b = getY(aa,bb);
			var timeTicket3;
			var timeTicket3_0;
			option.xAxis[0].data = getTime();
			option.series[0].data = b;
			myChart.setOption(option, true);
			setTimeout(function  () {
				var i =Math.floor(Math.random()*7);
				b.shift();
				b.push(bb[i]);
			    option.xAxis[0].data = getTime();
			    option.series[0].data = b;
			    myChart.setOption(option, true);
				clearInterval(timeTicket3);
				timeTicket3 = setInterval(function (){
					var i =Math.floor(Math.random()*7);
					b.shift();
					b.push(bb[i]);
				    option.xAxis[0].data = getTime();
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				},60000);
			},second_s);
			
			
			setTimeout(function () {
				var i =Math.floor(Math.random()*7);
				b.shift();
				b.push(bb[i]);
				clearInterval(timeTicket3);
				option.xAxis[0].data = getTime();
			    option.series[0].data = b;
			    myChart.setOption(option, true);
				clearInterval(timeTicket3_0);
				timeTicket3_0 = setInterval(function (){
					b.shift();
					b.push(0);
				    option.xAxis[0].data = getTime();
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				},60000);
				
				clearInterval(timeTicket33_24);
				var timeTicket33_24 = setInterval(function  () {
					setTimeout(function  () {
						var aa = getTime();
						var b = getY(aa,bb);
						var timeTicket3;
						var timeTicket3_0;
						option.xAxis[0].data = getTime();
						option.series[0].data = b;
						myChart.setOption(option, true);
						setTimeout(function  () {
							var i =Math.floor(Math.random()*7);
							b.shift();
							b.push(bb[i]);
						    option.xAxis[0].data = getTime();
						    option.series[0].data = b;
						    myChart.setOption(option, true);
							clearInterval(timeTicket3);
							timeTicket3 = setInterval(function (){
								var i =Math.floor(Math.random()*7);
								b.shift();
								b.push(bb[i]);
							    option.xAxis[0].data = getTime();
							    option.series[0].data = b;
							    myChart.setOption(option, true);
							},60000);
						},60000);
						
						setTimeout(function  () {
							clearInterval(timeTicket3);
							var i =Math.floor(Math.random()*7);
							b.shift();
							b.push(bb[i]);
							option.xAxis[0].data = getTime();
						    option.series[0].data = b;
						    myChart.setOption(option, true);
							clearInterval(timeTicket3_0);
							timeTicket3_0 = setInterval(function (){
								b.shift();
								b.push(0);
							    option.xAxis[0].data = getTime();
							    option.series[0].data = b;
							    myChart.setOption(option, true);
							},60000);
						},28800000);
					},57600000);
				},86400000)
			},i_time_18);
		}
		else
		{
			var aa = getTime();
			var b = getY(aa,bb);
			var timeTicket3_1;
			option.xAxis[0].data = getTime();
			option.series[0].data = b;
			myChart.setOption(option, true);
			setTimeout(function (){
				b.shift();
				b.push(0);
			    option.xAxis[0].data = getTime();
			    option.series[0].data = b;
			    myChart.setOption(option, true);
			    
			    clearInterval(timeTicket3_1);
				timeTicket3_1 = setInterval(function (){
					b.shift();
					b.push(0);
				    option.xAxis[0].data = getTime();
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				},60000);
			},second_s);
			
			
			setTimeout(function (){
				clearInterval(timeTicket3_1);
				var aa = getTime();
				var b = getY(aa,bb);
				var timeTicket3;
				var timeTicket3_0;
				option.xAxis[0].data = getTime();
				option.series[0].data = b;
				myChart.setOption(option, true);
				setTimeout(function  () {
					var i =Math.floor(Math.random()*7);
					b.shift();
					b.push(bb[i]);
				    option.xAxis[0].data = getTime();
				    option.series[0].data = b;
				    myChart.setOption(option, true);
					clearInterval(timeTicket3);
					timeTicket3 = setInterval(function (){
						var i =Math.floor(Math.random()*7);
						b.shift();
						b.push(bb[i]);
					    option.xAxis[0].data = getTime();
					    option.series[0].data = b;
					    myChart.setOption(option, true);
					},60000);
				},60000);
				
				setTimeout(function  () {
					clearInterval(timeTicket3);
					clearInterval(timeTicket3_0);
					var i =Math.floor(Math.random()*7);
					b.shift();
					b.push(bb[i]);
					option.xAxis[0].data = getTime();
				    option.series[0].data = b;
				    myChart.setOption(option, true);
					timeTicket3_0 = setInterval(function (){
						b.shift();
						b.push(0);
					    option.xAxis[0].data = getTime();
					    option.series[0].data = b;
					    myChart.setOption(option, true);
					},60000);
				},28800000);
				
				
				
				clearInterval(timeTicket3_24);
				var timeTicket3_24 = setInterval(function  () {
					var aa = getTime();
					var b = getY(aa,bb);
					var timeTicket3;
					var timeTicket3_0;
					option.xAxis[0].data = getTime();
					option.series[0].data = b;
					myChart.setOption(option, true);
					setTimeout(function  () {
						var i =Math.floor(Math.random()*7);
						b.shift();
						b.push(bb[i]);
					    option.xAxis[0].data = getTime();
					    option.series[0].data = b;
					    myChart.setOption(option, true);
						clearInterval(timeTicket3);
						timeTicket3 = setInterval(function (){
							var i =Math.floor(Math.random()*7);
							b.shift();
							b.push(bb[i]);
						    option.xAxis[0].data = getTime();
						    option.series[0].data = b;
						    myChart.setOption(option, true);
						},60000);
					},60000);
					
					setTimeout(function  () {
						clearInterval(timeTicket3);
						clearInterval(timeTicket3_0);
						var i =Math.floor(Math.random()*7);
						b.shift();
						b.push(bb[i]);
						option.xAxis[0].data = getTime();
					    option.series[0].data = b;
					    myChart.setOption(option, true);
						timeTicket3_0 = setInterval(function (){
							b.shift();
							b.push(0);
						    option.xAxis[0].data = getTime();
						    option.series[0].data = b;
						    myChart.setOption(option, true);
						},60000);
					},28800000);
				},86400000);
			},i_time);
		}
		
	}());
	
	//main
	(function (){
		var myChart = echarts.init(document.getElementById('main'));
		var status = fnDate_hours();
		var myCarrList;
		var myPartList;
		var myData;
		var myData1;
		var myLine_all;
		var myLine;
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
//			//console.log(hours)
//			//console.log(minutes)
//			//console.log(id)
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
		var app = {},
		option = null;
		function mapajax (timeId,s_time) {
//			$.ajax({
//				type:"get",
//				url:"http://182.254.216.232:80/main/calculate",
//				async:true,
//				success:function  (res) {
//					////console.log(res);
//				},
//				error:function  () {
//					////console.log("fail");
//				}
//			});
			$.ajax({
				type:"get",
				url:"http://182.254.216.232:80/main/dynamic",
				//url:"/echart/echart.json",
				//url:"point1.json",
				data:{"timeId":timeId},
				async:true,
				success:function  (data) {
					////console.log(data);
					clearInterval(timeTicketAjax);
					clearInterval(timeTicket444);
					count_line = 0;
					if (s_time) 
					{
						var res = {carrierList:[],parcelList:[]};
					}
					else
					{
						var res = data;
					}
					if (res.carrierList.length !=0 ) 
					{
						carrTimeId = carrTimeIdList(res.carrierList);
						myData = carrlist(res.carrierList,carrTimeId[0]);
					}
					else
					{
						myData = [];
					}
					if (res.parcelList.length !=0 ) 
					{
						partTimeId = partTimeIdList(res.parcelList);
						myData1 = parlist(res.parcelList,partTimeId[0]);
						myLine_all = parlist_line(res.parcelList,partTimeId[0]);
						lineCount =  Math.ceil( (myLine_all.length)/30 );
						myLine = lineFly(myLine_all,0,lineCount);
					}
					else
					{
						myData1 = [];
						myLine_all = [];
					}
					//console.log(res)
					
					/*
				    var myData = [
					    {value:[121.485615,31.215004]},
					    {value:[121.420446,31.260015]}
					  ];
					
					  */
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
				        /*
				        {
				            type: 'lines',
				            coordinateSystem: 'bmap',
				            polyline: true,
				            data: busLines,
				            silent: true,
				            lineStyle: {
				                normal: {
				                    // color: '#c23531',
				                    // color: 'rgb(200, 35, 45)',
				                    opacity: 0.2,
				                    width: 1
				                }
				            },
				            progressiveThreshold: 500,
				            progressive: 200
				        }, 
				        */
				        {
				            type: 'lines',
				            coordinateSystem: 'bmap',
				            polyline: true,
				            //data: busLines,
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
				        },
				        
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
				        }
				        
				        
				        
				        ]
				    };
				
					if (option && typeof option === "object") {
						myChart.setOption(option);
						timeTicket444 = setInterval(function () {
							count_line++
							var l_list = lineFly(myLine_all,count_line,lineCount);
							////console.log(l_list)
							option.series[0].data = l_list; 
							myChart.setOption(option);
						},10000);
						timeTicketAjax = setInterval(function () {
							var id = timenow();
							////console.log(id)
							if (id < 120 || id >215) 
							{
								mapajax(id,true);
							}
							else
							{
								mapajax(id);
							}
						},300000);
					}
		        
		        },
		        error:function  () {
		        	////console.log("fail");	
		        }
			});
		};
		//执行请求  
		//myChart.showLoading();
		if (status) 
		{
			var id = timenow();
			mapajax(id);
		}
		else
		{
			var id = timenow();
			mapajax(id,true);
			setTimeout(function  () {
				var id = timenow();
				mapajax(id);
			},i_time);
		}
		
	}());
	
	
	//right-dashboard
	(function (){
		var myChart = echarts.init(document.getElementById('right-dashboard'));
		var status = fnDate_hours();
		//myChart.showLoading();  //加载效果
		var option = {
				title : {
			        text: 'Realtime Delevery Rate',
			        x:'center',
			        textStyle : {
			            color: '#fff'
			        }
			    },
			    grid: {
				    left: '2%',
					right: '2%',
					bottom: '3%',
					containLabel: true
				},
			    tooltip : {
			        formatter: "{a} <br/>{b} : {c}%"
			    },
			    series: [{
			        type: 'liquidFill',
			        data: [],
			        radius: '75%'
			    }]
			};
			if (status) 
			{
				option.series[0].data= [1.0];
				myChart.setOption(option);
				setTimeout(function (){
					option.series[0].data= [];
					myChart.setOption(option);
				},i_time_18);
				
				clearInterval(timeTicket55_24);
				var timeTicket55_24 = setInterval(function  () {
					setTimeout(function  () {
						option.series[0].data= [1.0];
						myChart.setOption(option);
						setTimeout(function (){
							option.series[0].data= [];
							myChart.setOption(option);
						},28800000);
					},57600000);
				},86400000);
				
			}
			else
			{
				myChart.setOption(option);
				setTimeout(function (){
					option.series[0].data= [1.0];
					myChart.setOption(option,true);
					setTimeout(function (){
						option.series[0].data= [];
						myChart.setOption(option,true);
					},28800000);
					
					clearInterval(timeTicket5_24);
					var timeTicket5_24 = setInterval(function  () {
						option.series[0].data= [1.0];
						myChart.setOption(option,true);
						setTimeout(function  () {
							option.series[0].data= [];
							myChart.setOption(option,true);
						},28800000);
					},86400000);
					
				},i_time);
				
				
				
			}
	}());
         
    //right-bar
	(function (){
		var myChart = echarts.init(document.getElementById('right-bar'));
		var status = fnDate_hours();
		var option = {
			    title : {
			        text: 'Realtime Parcels Status',
			        textStyle : {
			            color: '#fff'
			        }
			    },
			    grid: {
				    left: '2%',
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
			            data : ['unattended','Pickup','Delivered'],
			            axisLabel:{  
		                    interval:0,  
		                    rotate:-45,//倾斜度 -90 至 90 默认为0  
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
			            max: function(value) {
						    return 10000;
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
			            data:["0",'0','0'],
			            itemStyle:{
			                normal:{color:'#358be3'}
			            }
			        }
			    ]
			};
		myChart.setOption(option);
		var b = [["2000",'2200','4232'],["2153",'2489','4015'],["2233",'2689','4215']];
		clearInterval(timeTicket6);
		if (status) 
		{
			option.series[0].data = ["2000",'2200','4232'];
			myChart.setOption(option);
			clearInterval(timeTicket6);
			var timeTicket6 = setInterval(function (){
				var i =Math.floor(Math.random()*3);
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
			    		option.series[0].data = ["2000",'2200','4232'];
						myChart.setOption(option);
						clearInterval(timeTicket6);
						var timeTicket6 = setInterval(function (){
							var i =Math.floor(Math.random()*3);
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
				option.series[0].data = ["2000",'2200','4232'];
				myChart.setOption(option);
				var timeTicket6 = setInterval(function (){
					var i =Math.floor(Math.random()*3);
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
					option.series[0].data = ["2000",'2200','4232'];
					myChart.setOption(option);
					clearInterval(timeTicket6);
					var timeTicket6 = setInterval(function (){
						var i =Math.floor(Math.random()*3);
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
	
	//right-line
	(function (){
		var myChart = echarts.init(document.getElementById('right-line'));
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
			            color: '#fff'
			        }
			    },
			    grid: {
				    left: '2%',
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
		var getTimeHour = function  () {
			var arr = [];//
			var date=new Date();
  			var hours=date.getHours();//小时
  			var h_h = hours - 10;
			if (h_h >= 0 && h_h <=3) 
			{
				for (var i=0;i<4;i++) 
				{
					var add = 10+ i + ":00";
					arr.push(add);
				}
			}
			else if (h_h >= 9 && h_h <=12)
			{
				for (var i=0;i<4;i++) 
				{
					var add = 19+ i + ":00";
					arr.push(add);
				}
			}
			else
			{
				for (var i=3;i>=0;i--)
				{
					var h = hours - i;
					if (h < 0) 
					{
						var hh = 24 - Math.abs(h);
					}
					else
					{
						var hh = add0(h);
					}
					var add = hh + ":00";
					arr.push(add);
				}
			}
			return arr;
		};
		var getY = function  (arr) {
			var list = [];
			var date=new Date();
			var hours = date.getHours();//时
			var hours_00 = hours + ":00"
			var minute=date.getMinutes();//分
			var hh = hours - 10;
			var len = arr.length;
			for (var i=0;i<len;i++) 
			{
				if (hours_00 == arr[i]) 
				{
					data_i = i;
				}
				if (arr[i] < "10:00" || arr[i] > "18:00") 
				{
					list.push(0);
				}
				else
				{
					if (arr[0] == "10:00") 
					{
						switch (hh) {
							case 0:
								var add = (minute+1) * 52;
								list = [add,0,0,0];
								break;
							case 1:
								var add =3120 + (minute+1) * 52;
								list = [3120,add,0,0];
								break;
							case 2:
								var add =3120*2 + (minute+1) * 52;
								list = [3120,6240,add,0];
								break;
							case 3:
								var add =3120*3 + (minute+1) * 52;
								list = [3120,6240,9360,add];
								break;
						}
					}
					else if (arr[0] > "10:00" && arr[3] < "19:00")
					{
						var a1 = 3120*(hh-2);
						var a2 = 3120*(hh-1);
						var a3 = 3120*(hh);
						var add =3120*hh + (minute+1) * 52;
						list.push(a1);
						list.push(a2);
						list.push(a3);
						list.push(add);
					}
				}
			}
			return list;
		};
		option.xAxis[0].data = getTimeHour();
		
		myChart.setOption(option);
		//var bb = [3140, 3000, 3100, 3119, 3034, 3028];
		if (status)
		{
			var aaaa = getTimeHour();
			var b = getY(aaaa);
			var timeTicket7;
			var timeTicket7_5;
			var timeTicket7_0;
			option.series[0].data = b;
			myChart.setOption(option);
			clearInterval(timeTicket7_5);
			timeTicket7_5 = setInterval(function  () {
				option.xAxis[0].data = getTimeHour();
				if (b[data_i] != 0) 
				{
					b[data_i] = b[data_i] + 5*52;
				}
				else
				{
					b = [0,0,0,0]
				}
			    option.series[0].data = b;
			    myChart.setOption(option, true);
			},300000);
			setTimeout(function (){
				clearInterval(timeTicket7_5);
				var aaaa = getTimeHour();
				var b = getY(aaaa);
			    option.xAxis[0].data = getTimeHour();
			    option.series[0].data = b;
			    myChart.setOption(option, true);
			    
			    clearInterval(timeTicket7_5);
				timeTicket7_5 = setInterval(function  () {
					option.xAxis[0].data = getTimeHour();
					if (b[data_i] != 0) 
					{
						b[data_i] = b[data_i] + 5*52;
					}
					else
					{
						b = [0,0,0,0]
					}
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				},300000);
				
				clearInterval(timeTicket7);
				timeTicket7 = setInterval(function (){
					clearInterval(timeTicket7_5);
					var aaaa = getTimeHour();
					var b = getY(aaaa);
				    option.xAxis[0].data = getTimeHour();
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				    
				    clearInterval(timeTicket7_5);
					timeTicket7_5 = setInterval(function  () {
						option.xAxis[0].data = getTimeHour();
						if (b[data_i] != 0) 
						{
							b[data_i] = b[data_i] + 5*52;
						}
						else
						{
							b = [0,0,0,0]
						}
					    option.series[0].data = b;
					    myChart.setOption(option, true);
					},300000);
				},3600000);
			},time_i);
			
			
			
			setTimeout(function (){
				var aaaa = getTimeHour();
				var b = getY(aaaa);
				var timeTicket7;
				var timeTicket7_5;
				var timeTicket7_0;
				option.series[0].data = b;
				myChart.setOption(option);
				clearInterval(timeTicket7_5);
				timeTicket7_5 = setInterval(function  () {
					option.xAxis[0].data = getTimeHour();
					if (b[data_i] != 0) 
					{
						b[data_i] = b[data_i] + 5*52;
					}
					else
					{
						b = [0,0,0,0]
					}
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				},300000);
				clearInterval(timeTicket7);
				timeTicket7 = setInterval(function (){
					clearInterval(timeTicket7_5);
					var aaaa = getTimeHour();
					var b = getY(aaaa);
				    option.xAxis[0].data = getTimeHour();
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				    
				    clearInterval(timeTicket7_5);
					timeTicket7_5 = setInterval(function  () {
						option.xAxis[0].data = getTimeHour();
						if (b[data_i] != 0) 
						{
							b[data_i] = b[data_i] + 5*52;
						}
						else
						{
							b = [0,0,0,0]
						}
					    option.series[0].data = b;
					    myChart.setOption(option, true);
					},300000);
				},3600000);
			},i_time);
		}
		else
		{
			var aaaa = getTimeHour();
			var b = getY(aaaa);
			var timeTicket7;
			var timeTicket7_5;
			var timeTicket7_0;
			option.series[0].data = b;
			myChart.setOption(option);
			setTimeout(function (){
				var aaaa = getTimeHour();
				var b = getY(aaaa);
				var timeTicket7;
				var timeTicket7_5;
				var timeTicket7_0;
				option.series[0].data = b;
				myChart.setOption(option);
				clearInterval(timeTicket7_5);
				timeTicket7_5 = setInterval(function  () {
					option.xAxis[0].data = getTimeHour();
					if (b[data_i] != 0) 
					{
						b[data_i] = b[data_i] + 5*52;
					}
					else
					{
						b = [0,0,0,0]
					}
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				},300000);
				clearInterval(timeTicket7);
				timeTicket7 = setInterval(function (){
					clearInterval(timeTicket7_5);
					var aaaa = getTimeHour();
					var b = getY(aaaa);
				    option.xAxis[0].data = getTimeHour();
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				    
				    clearInterval(timeTicket7_5);
					timeTicket7_5 = setInterval(function  () {
						option.xAxis[0].data = getTimeHour();
						if (b[data_i] != 0) 
						{
							b[data_i] = b[data_i] + 5*52;
						}
						else
						{
							b = [0,0,0,0]
						}
					    option.series[0].data = b;
					    myChart.setOption(option, true);
					},300000);
				},3600000);
			},time_i);
			
			
			setTimeout(function (){
				var aaaa = getTimeHour();
				var b = getY(aaaa);
				var timeTicket7;
				var timeTicket7_5;
				var timeTicket7_0;
				option.series[0].data = b;
				myChart.setOption(option);
				clearInterval(timeTicket7_5);
				timeTicket7_5 = setInterval(function  () {
					option.xAxis[0].data = getTimeHour();
					if (b[data_i] != 0) 
					{
						b[data_i] = b[data_i] + 5*52;
					}
					else
					{
						b = [0,0,0,0]
					}
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				},300000);
				clearInterval(timeTicket7);
				timeTicket7 = setInterval(function (){
					clearInterval(timeTicket7_5);
					var aaaa = getTimeHour();
					var b = getY(aaaa);
				    option.xAxis[0].data = getTimeHour();
				    option.series[0].data = b;
				    myChart.setOption(option, true);
				    
				    clearInterval(timeTicket7_5);
					timeTicket7_5 = setInterval(function  () {
						option.xAxis[0].data = getTimeHour();
						if (b[data_i] != 0) 
						{
							b[data_i] = b[data_i] + 5*52;
						}
						else
						{
							b = [0,0,0,0]
						}
					    option.series[0].data = b;
					    myChart.setOption(option, true);
					},300000);
				},3600000);
			},i_time);
		}
		
	}());
                                                   
 	
	
	
});