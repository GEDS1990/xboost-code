$(document).ready(function  () {
	var doc = document;
	//判断是否含有场景名称
	var scenName  = $('#scenName').val();
	var secenId = $('#scenName').attr("data-id"); 
	if (scenName) {
		var add = "";
		add+='<li class="xb-hover" id="scen-name" data-scenariosid ='+secenId+'>';
		add+='<a href="/ScenariosName" class="nav_xb" id="xb-nav-xb">';
		add+='<span id="xb_nav_span" class="glyphicon glyphicon-triangle-bottom"></span>';
		//add+='<span class="icon alt1 alt icon-file-text-o"></span>'+scenName+'</a><span id="scen-name-close" class="icon alt2 glyphicon glyphicon-remove"></span></li>';
		add+='<span class="icon alt1 alt icon-file-text-o"></span>'+scenName+'</a></li>';
		add+='<li class="scenInfo-list"><ul class="xb-nav_ul" id="scen-class">';
		add+='<li id="nav-Conditions"><a href="/siteInfo"><span class="icon-item alt icon-document-add"></span>Input</a></li>';
		add+='<li id="nav-Simualt"><a href="/simualte"><span class="icon-item alt icon-play"></span>Simulation</a></li>';
		add+='<li id="nav-Results"><a href="/depots"><span class="icon-item alt icon-document-checked"></span>Output</a></li></ul></li>';
		$('#after-content').after(add);
	}
	
	/*
	//点击X删除结构
	$('body').on("click","#scen-name-close",function  () {
		$('#scen-name-close').off("click");
		$(this).parent("#scen-name").remove().next("li").remove();
		//$(this).parent("#scen-name");
		window.location.href = "/MyScenarios";
		return false;
	});
	*/
	
	/*login out*///href="/logout"
	$('#loginout').click(function (){
		$('#modal-loginout').modal("show");
		$('#modal-loginBtn').click(function (){
			window.location.href = "/logout";
		});
	});
	
	
	
	
	
});	
