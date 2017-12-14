var doc = document;



$(function  () {
	
	/**
	 * SimualtValidate.jsp =-= ValidateController
	 * 
	 */
	(function  () {
		var simCheck = doc.getElementById("sim-check");
		if (simCheck) {
			//点击校验
			$("#sim-check").click(function  () {
				var _val = $('#sim-model').val();
				if (_val == 0) {
					$('#sim-error-check').show();
					return false;
				}else{
					$('#sim-error-check').hide();
					//执行验证
					if(typeof(WebSocket) == "undefined") {
	                alert("您的浏览器不支持WebSocket");
	                return;
		            }
					document.getElementById('sim-check-info').innerHTML="";
		            socket  = new SockJS("http://"+document.location.host+"/webSocketServer/validate");
		            socket .onopen = function () {
		                console('Info: connection opened.');
		            };
		
		            socket .onmessage = function (event) {
		                logg(event.data);
		            };
		            socket .onclose = function (event) {
		                console('Info: connection closed.');
		                logg(event);
		            };
					
					$.post("/simualte/Validate").done(function  (result) {
					    ws.onclose();
						console.log("success");
					}).fail(function  () {
					    ws.onclose();
						console.log("fail");
					});
					
					function logg(messages) {
			            var consoleBox = document.getElementById('sim-check-info');
			            var p = document.createElement('p');
			            p.style.wordWrap = 'break-word';
			            p.appendChild(document.createTextNode(messages));
			            consoleBox.appendChild(p);
			            consoleBox.scrollTop = consoleBox.scrollHeight;
			        }
				}
			});
			
			//点击运行run
			$('#sim-run').click(function  () {
				var runTime = $('#sim-run-time').val();
				var runCount = $('#sim-run-count').val();
				var runModel = $('#sim-model').val();
				if (runModel == 0) {
					$('#sim-error-check').show();
					$('#sim-model').focus();
					return false;
				}
				if (runTime == "") {
					$('#timelimit').show();
					$('#sim-run-time').focus();
					return false;
				}
				if (runCount == "") {
					$('#loopslimit').show();
					$('#sim-run-count').focus();
					return false;
				}else{
					$('#sim-error-check').hide();
					$('#loopslimit').hide();
					$('#timelimit').hide();
					window.location.href = "/Simualte?run=yes&distMode="+runModel+"&loadTime="+runTime+"&loopLimit="+runCount;
				}
				
			});
		}
		
	})(),
	
	
	/**
	 * Simualte.jsp =-= SimualteController
	 * 
	 */
	(function  () {
		//获取地址栏中的数据函数
		var simStop = doc.getElementById("sim-stop");
		if (simStop) {
			var example = new Vue({
					el: '#sim-run-percent',
					data: {
						percent:""
					}
				});
			function UrlSearch() {
				var name,value; 
				var str=location.href; //取得整个地址栏
				var num=str.indexOf("?") 
				str=str.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]
				
				var arr=str.split("&"); //各个参数放到数组里
				for(var i=0;i < arr.length;i++){ 
					num=arr[i].indexOf("="); 
					if(num>0){ 
						name=arr[i].substring(0,num);
						value=arr[i].substr(num+1);
						this[name]=value;
					} 
				} 
			};
			//实例化对象
			var Request=new UrlSearch(); //实例化
			var _distMode = Request.distMode;
			var _loadTime = Request.loadTime;
			var _loopLimit = Request.loopLimit;
			if (Request.run == "yes") {
				//alert("开始执行算法");
				//执行算法
				if(typeof(WebSocket) == "undefined") {
	                alert("您的浏览器不支持WebSocket");
	                return;
	            }
				document.getElementById('sim-run-info').innerHTML="";
	            ws = new SockJS("http://"+document.location.host+"/webSocketServer/sockjs");
	            ws.onopen = function () {
	                console('Info: connection opened.');
	            };
	
	            ws.onmessage = function (event) {
	                log(event.data);
	            };
	            ws.onclose = function (event) {
	                console('Info: connection closed.');
	                log(event);
	            };
	
                $.ajaxSettings.async = false;//修改默认使用同步
	            $.post("/cascade/runSilumate",{"distMode":_distMode,"loadTime":_loadTime,"loopLimit":_loopLimit}).done(function(result){
	                ws.onclose();
	                console.log("success");
	            }).fail(function(){
	                ws.onclose();
	                console.log("fail");
	            });
	            $.ajaxSettings.async = true;//修改默认使用异步
	            //停止算法
				$('#sim-stop').click(function  () {
					$('#modal-sim').modal("show");
					$('#modal-simdelBtn').click(function  () {
						ws.close();
						$('#modal-sim').modal("hide");
					});
					
				});
		        function log(messages) {
		            var consoleBox = document.getElementById('sim-run-info');
		            var p = document.createElement('p');
		            p.style.wordWrap = 'break-word';
		            var _r = /\%/;
		            if (_r.test(messages)) {
		            	var i = messages.indexOf("%");
		            	var num = messages.substr(0,i);
		            	if (num == '99') {
		            		var res = messages.replace("99","100");
		            		example.percent = '('+res+')';
		            		$('#sim-percent').text('('+res+')');
		            	}else{
		            		example.percent = '('+messages+')';
		            		$('#sim-percent').text('('+messages+')');
		            	}
		            }
		            p.appendChild(document.createTextNode(messages));
		            consoleBox.appendChild(p);
		            consoleBox.scrollTop = consoleBox.scrollHeight;
		        }
			}
			
			
		}
		
	})()
	
	
	
	
	
	
	
	
});
