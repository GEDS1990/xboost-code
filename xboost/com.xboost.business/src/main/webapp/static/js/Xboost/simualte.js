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
			}
		}
		
	})()
	
	
	
	
	
	
	
	
});
