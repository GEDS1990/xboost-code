$(function(){
/**
 * Research.jsp = ResearchController
 * 
 */
var doc = document;
var width;  
var height;  
var branchChart;  
var collectChart;
var distributionEchart;

(function (){
	var collect_reserach_echarts = doc.getElementById('collect-reserach-echarts');
	if (collect_reserach_echarts) {
		//自适应设置  
	    width = $(window).width();  
	    height = $(window).height();  
	    //$("#distribution-echarts").css("width",width-40);  
	    $("#collect-reserach-echarts").css("height",height-40);
	    //$("#distribution-echarts").css("width",width-40);  
	    $("#branch-reserach-echarts").css("height",height-40);
	    
	}

}());

(function  () {
	var collect_reserach_echarts = doc.getElementById('collect-reserach-echarts');
	if (collect_reserach_echarts) {
		//加载列表
		var dt =$("#research-table").DataTable({
            "serverSide":true, //服务端处理
            "searchDelay": 1000,//搜索延迟
            "destroy": true,
            "order":[[0,'desc']],//默认排序方式
            "lengthMenu":[100000],//每页显示数据条数菜单
            "ajax":{
                url:"/MyScenarios/scenarios.json", //获取数据的URL
                type:"get" //获取数据的方式
                
            },
            "columns":[  //返回的JSON中的对象和列的对应关系
                {"data":"id","name":"id"},
                {"data":"scenariosName","name":"scenarios_name"}
                
            ],
            "columnDefs":[ //具体列的定义
            	{
                    "targets":[0,1],
                    "visible":false
                }
            ],
            "initComplete": function (settings, data) {
            	//console.log(data);
            	if (data.data) {
            		var result = data.data,
            		len = result.length;
            		$('#research-name1').empty();
            		$('#research-name2').empty();
            		$('#research-name1').off("click");
            		$('#research-name2').off("click");
            		for (var i=0;i<len;i++) {
            			var add='<option value='+result[i].scenariosName+'>'+result[i].scenariosName+'</option>';
						$('#research-name1').append(add);
						$('#research-name2').append(add);
            		}
            	}
            	var data1 = {
            		"name":$('#research-name1').val()
            	},
            	data2 = {
            		"name":$('#research-name2').val()
            	}
            	collectEcharts(data1,data2);
	    		branchEcharts(data1,data2);//data1,data2
            	
            }
        });
	}
}());



	
function collectEcharts(){  
    collectChart = echarts.init(document.getElementById('collect-reserach-echarts'));  
    //自适应  
    window.onresize = collectChart.resize;  
    collectChart.setOption({ 
    	title : {
	        text: '收端到达集散点时间分布',
	        x:'center',
        	y:'top'
	    },
        tooltip : {
            trigger: 'axis'  
        },  
        legend: { 
        	x:'center',
        	y:'bottom',
            data:['1','2']  
        },  
        toolbox: {  
            show : true,  
            feature : {  
                mark : {show: true},  
                dataView : {show: true, readOnly: false},  
                magicType : {show: true, type: ['line', 'bar']},  
                restore : {show: true},  
                saveAsImage : {show: true}  
            }  
        },  
        calculable : true,  
        xAxis : [  
            {  
                type : 'category',  
                data : ['提早70','提早60','提早50','提早40','提早30','提早20','提早10','准时到'],  
                //设置字体倾斜  
                axisLabel:{  
                    interval:0,  
                    rotate:0,//倾斜度 -90 至 90 默认为0  
                    margin:5,  
                    textStyle:{  
                        fontWeight:"bolder",  
                        color:"#000000"  
                    }  
                },    
            }  
        ],  
        yAxis : [  
            {  
                type : 'value',  
                splitArea : {show : true}  
            }  
        ],  
        series : [  
            {  
                name:'1',  
                type:'bar',  
                data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2],
                barWidth : 30,//柱图宽度
                //顶部数字展示pzr  
                itemStyle: {  
                    normal: {
                    	color:"#5b9bd5",
                        label: {  
                            show: true,//是否展示 
                            position: 'top',
                            textStyle: {  
                                fontWeight:'bolder',  
                                fontSize : '12',  
                                fontFamily : '微软雅黑',  
                            }  
                        }
                    }  
                }
                
            },  
            {  
                name:'1',  
                type:'bar',  
                data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2], 
                barWidth : 30,//柱图宽度
                //顶部数字展示pzr  
                itemStyle: {  
                    normal: { 
                    	color:'#ed7d31',
                        label: {  
                            show: true,//是否展示 
                            position: 'top',
                            textStyle: {  
                                fontWeight:'bolder',  
                                fontSize : '12',  
                                fontFamily : '微软雅黑',  
                            }  
                        }  
                    }  
                }
                //barGap:'10%'
            }  
        ]  
    });  
}  	
function branchEcharts(data1,data2){  
    branchChart = echarts.init(document.getElementById('branch-reserach-echarts'));  
    //自适应  
    window.onresize = branchChart.resize;  
    branchChart.setOption({ 
    	title : {
	        text: '支线到达目的地集散点时间分布',
	        x:'center',
        	y:'top'
	    },
        tooltip : {
            trigger: 'axis'  
        },  
        legend: { 
        	x:'center',
        	y:'bottom',
            data:[data1.name,data2.name]  
        },  
        toolbox: {  
            show : true,  
            feature : {  
                mark : {show: true},  
                dataView : {show: true, readOnly: false},  
                magicType : {show: true, type: ['line', 'bar']},  
                restore : {show: true},  
                saveAsImage : {show: true}  
            }  
        },  
        calculable : true,  
        xAxis : [  
            {  
                type : 'category',  
                data : ['提早60','提早50','提早40','提早30','提早20','提早10','准时到'],  
                //设置字体倾斜  
                axisLabel:{  
                    interval:0,  
                    rotate:0,//倾斜度 -90 至 90 默认为0  
                    margin:5,  
                    textStyle:{  
                        fontWeight:"bolder",  
                        color:"#000000"  
                    }  
                },    
            }  
        ],  
        yAxis : [  
            {  
                type : 'value',  
                splitArea : {show : true}  
            }  
        ],  
        series : [  
            {  
                name:data1.name,  
                type:'bar',  
                data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6],
                barWidth : 30,//柱图宽度
                //顶部数字展示pzr  
                itemStyle: {  
                    normal: {
                    	color:"#5b9bd5",
                        label: {  
                            show: true,//是否展示  
                            position: 'top',
                            textStyle: {  
                                fontWeight:'bolder',  
                                fontSize : '12',  
                                fontFamily : '微软雅黑',  
                            }  
                        }
                    }  
                },  
            },  
            {  
                name:data2.name,  
                type:'bar',  
                data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6], 
                barWidth : 30,//柱图宽度
                //顶部数字展示pzr  
                itemStyle: {  
                    normal: {  
                    	color:'#ed7d31',
                        label: {  
                            show: true,//是否展示  
                            position: 'top',
                            textStyle: {  
                                fontWeight:'bolder',  
                                fontSize : '12',  
                                fontFamily : '微软雅黑',  
                            }  
                        }  
                    }  
                },  
            }  
        ]  
    });  
}	
	
//点击选项 切换图表信息
(function  () {
	var collect_reserach_echarts = doc.getElementById('collect-reserach-echarts');
	if (collect_reserach_echarts) {
		$('#research-name1').click(function (){
			
		});
		$('#research-name2').click(function (){
			
		});
		
	}
}());
	

/**
 *distribution.jsp == SolutionDistributionController
 *
 */
function startNum (a,b) {
	return a.start-b.start;
}
//求时间
function operationTime (data) {
	var result = parseInt(data),
    	h = parseInt(result/60),
    	m = result%60;
    	return add0(h)+":"+add0(m);
}
function add0(m){return m<10?'0'+m:m };
(function  () {
	var distribution_echarts = doc.getElementById('distribution-echarts');
	if (distribution_echarts) {
		//自适应设置  
	    width = $(window).width();  
	    height = $(window).height();  
	    $("#distribution-echarts").css("height",height-300); 
	    var _val = $('#distribution-choose').val();
	    $.get("/distribution/getMaxMix.json",{"type":_val}).done(function (res){
	    	////console.log(res);
	    	$('.echarts-error-info').hide();
	    	var arr = [];
	    	for (var i in res) {
	    		var obj = {}
	    		var s = i.split("-");
	    		s.push(res[i]);
	    		////console.log(s)
				obj["start"] = s[0];
				obj["end"] = s[1];
				obj["num"] = s[2];
				arr.push(obj);
	    	}
	    	arr.sort(startNum);
	    	////console.log(arr);
	    	var xinfo = [];
	    	var seriesinfo = [];
	    	for (var j=0,len=arr.length;j<len;j++) {
	    		var time = operationTime(arr[j].start) +"-"+operationTime(arr[j].end);
	    		xinfo.push(time);
	    		var val = arr[j].num;
	    		seriesinfo.push(val);
	    	}
	    	var data = {
		    	"xinfo":xinfo,
		    	"seriesinfo":seriesinfo
		    }
			distributionEcharts(data);
	    	
	    }).fail(function  () {
	    	//$('.echarts-error-info').show();
	    });
	    $('#distribution-choose').change(function  () {
	    	var _val = $(this).val();
	    	$.get("/distribution/getMaxMix.json",{"type":_val}).done(function (res){
		    	////console.log(res);
		    	$('.echarts-error-info').hide();
		    	if (_val == 0 || _val== 1) {
		    		var arr = [];
			    	for (var i in res) {
			    		var obj = {}
			    		var s = i.split("-");
			    		s.push(res[i]);
			    		////console.log(s)
						obj["start"] = s[0];
						obj["end"] = s[1];
						obj["num"] = s[2];
						arr.push(obj);
			    	}
			    	arr.sort(startNum);
			    	////console.log(arr);
			    	var xinfo = [];
			    	var seriesinfo = [];
			    	for (var j=0,len=arr.length;j<len;j++) {
			    		var time = operationTime(arr[j].start) +"-"+operationTime(arr[j].end);
			    		xinfo.push(time);
			    		var val = arr[j].num;
			    		seriesinfo.push(val);
			    	}
			    	var data = {
				    	"xinfo":xinfo,
				    	"seriesinfo":seriesinfo
				    }
					distributionEcharts(data);
		    	}else{
		    		var xinfo = ['提早60','提早50以上','提早40以上','提早30以上','提早20以上','提早10以上','准时到'];
		    		var arr = [res.tiqian60,res.tiqian50,res.tiqian40,res.tiqian30,res.tiqian20,res.tiqian10,res.zunshi];
		    		////console.log(arr)
		    		var arrlen = arr.length;
		    		var seriesinfo = [];
		    		for (var x=0;x<arrlen;x++) {
		    			var sum = "";
		    			for (var y=0;y<x+1;y++) {
		    				sum = (Number(sum) + Number(arr[y])).toFixed(2);
		    			}
		    			if(sum>100){
		    			sum = 100;
		    			}
		    			seriesinfo.push(sum);
		    		}
		    		var slen = seriesinfo.length;
		    		seriesinfo[slen-1] = 100;
		    		var data = {
		    			"xinfo":xinfo,
				    	"seriesinfo":seriesinfo
		    		}
		    		distributionEcharts(data);
		    	}
		    	
		    	
		    	
		    }).fail(function  () {
		    	//$('.echarts-error-info').show();
		    });
	    });
	    
	    
		
		
		
	}
}());	
function distributionEcharts(datas){  
    distributionEchart = echarts.init(document.getElementById('distribution-echarts'));  
    //自适应  
    window.onresize = distributionEchart.resize;  
    distributionEchart.setOption({ 
        tooltip : {
            trigger: 'axis'  
        },  
        toolbox: {  
            show : true,  
            feature : {  
                mark : {show: true},  
                dataView : {show: true, readOnly: false},  
                magicType : {show: true, type: ['line', 'bar']},  
                restore : {show: true},  
                saveAsImage : {show: true}  
            }  
        },
        calculable : true,  
        xAxis : [  
            {  
                type : 'category',  
                data : datas.xinfo,  
                //设置字体倾斜  
                axisLabel:{  
                    interval:0,  
                    rotate:45,//倾斜度 -90 至 90 默认为0  
                    margin:5,  
                    textStyle:{  
                        fontWeight:"bolder",  
                        color:"#000000"  
                    }  
                },    
            }  
        ],  
        yAxis : [  
            {  
                type : 'value',  
                axisLabel: {  
                	formatter: '{value} %',  //显示百分比
               		show: true,  
                    interval: 'auto',  
                    
                },  
                splitArea : {show : true}  
            }  
        ],  
        series : [  
            {  
                name:'1',  
                type:'bar',  
                data:datas.seriesinfo,
                barWidth : 30,//柱图宽度
                //顶部数字展示pzr  
                itemStyle: {  
                    normal: {
                    	color:"#5b9bd5",
                        label: {  
                            show: true,//是否展示 
                            position: 'top',
                            //formatter:'{b}\n{c}%',
                            formatter:'{c}%', //显示百分比
                            textStyle: {  
                                fontWeight:'bolder',  
                                fontSize : '12',  
                                fontFamily : '微软雅黑',  
                            }  
                        }
                    }  
                }
                
            }
        ]  
    });  
}

//导出excel表格
    $('.export-btn').click(function  () {
        var _xls = $(this).attr('data-xls');
        if (_xls) {
            window.location.href="/distribution/exportResult";
        }
        $(".modal-header span").trigger('click');
    });
	
	
	
	
	
	
	
	
	
});