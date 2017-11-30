$(document).ready(function  () {
	var doc = document;
	//判断是否含有场景名称
	var scenName  = $('#scenName').val();
	if (scenName) {
		var add = "";
		add+='<li class="xb-hover" id="scen-name">';
		add+='<a href="/ScenariosName" class="nav_xb" id="xb-nav-xb">';
		add+='<span id="xb_nav_span" class="glyphicon glyphicon-triangle-bottom"></span>';
		add+='<span class="icon alt1 alt icon-file-text-o"></span>'+scenName+'</a></li>';
		add+='<li><ul class="xb-nav_ul" id="scen-class">';
		add+='<li id="nav-Conditions"><a href="/siteInfo"><span class="icon-item alt icon-document-add"></span>Settings</a></li>';
		add+='<li id="nav-Simualt"><a href="/simualte"><span class="icon-item alt icon-play"></span>Simulate</a></li>';
		add+='<li id="nav-Results"><a href="/route"><span class="icon-item alt icon-document-checked"></span>Results</a></li></ul></li>';
		$('#after-content').after(add);
	}
	
	/*
	//点击X删除结构
	$('body').on("click","#scen-name-close",function  () {
		$(this).parents("#scen-name").next("#scen-class").remove();
		$(this).parents("#scen-name").remove();
		window.location.href = "/MyScenarios";
		return false;
	});
	
	*/
	
	/**
	 *下载输入块模板 onclick='location.href = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Depots Info.xlsx"'
	 */
	(function  () {
		var cond_input_form = doc.getElementById("cond-input-form");
		if (cond_input_form) {
			var all = "http://"+document.location.host+"/static/excelTemplate/Template - All Settings.xlsx";
			var info = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Depots Info.xlsx";
			var dist = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Depots Distance.xlsx";
			var dem = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Demands.xlsx";
			var veh = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Vehicles.xlsx";
			var pata = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Parameters.xlsx";
			$('#template-all').attr("href",all);
			$('#template-info').attr("href",info);
			$('#template-dist').attr("href",dist);
			$('#template-dem').attr("href",dem);
			$('#template-veh').attr("href",veh);
			$('#template-pata').attr("href",pata);
		}
	})()
	
	
	
});	
