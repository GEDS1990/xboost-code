var doc = document;

$(function  () {
	
	/**
	 * SimualtValidate.jsp =-= ValidateController
	 * 
	 */
	(function  () {
		//点击校验
		$("#sim-check").click(function  () {
			var _val = $('#sim-model').val();
			if (_val == 0) {
				$('#sim-error-check').show();
				return false;
			}else{
				$('#sim-error-check').hide();
				$.post("/simualte/Validate").done(function  (result) {
					console.log(result);
				});
			}
		});
		
		//点击运行run
		$('#sim-run').click(function  () {
			var runTime = $('#sim-run-time').val();
			var runCount = $('#sim-run-count').val();
			window.location.href = "/Simualte?run=yes";
		});
	})(),
	
	
	/**
	 * Simualte.jsp =-= SimualteController
	 * 
	 */
	(function  () {
		//获取地址栏中的数据函数
		var simStop = doc.getElementById("sim-stop");
		if (simStop) {
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
			if (Request.run == "yes") {
				alert("开始执行算法");
				//执行算法
				document.getElementById('sim-run-info').innerHTML="";
	            ws = new SockJS("http://127.0.0.1:8080/webSocketServer/sockjs");
	            ws.onopen = function () {
	                log('Info: connection opened.');
	            };
	
	            ws.onmessage = function (event) {
	                log(event.data);
	            };
	            ws.onclose = function (event) {
	                log('Info: connection closed.');
	                log(event);
	            };
	

	            $.post("/cascade/runSilumate","json").done(function(result){
	                alert("success");
	            }).fail(function(){
	                alert("fail");
	            });
		        function log(message) {
		            var console = document.getElementById('sim-run-info');
		            var p = document.createElement('p');
		            p.style.wordWrap = 'break-word';
		            p.appendChild(document.createTextNode(message));
		            console.appendChild(p);
		            console.scrollTop = console.scrollHeight;
		        }
				
				
				
			}
		}
		
	})()
	
	
	
	
	
	
	
	
});
