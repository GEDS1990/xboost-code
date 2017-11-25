$(document).ready(function  () {
	//判断是否含有场景名称
	var scenName  = $('#scenName').val();
	if (scenName) {
		var add = "";
		add+='<div class="xb-hover" id="scen-name"><div class="nav_xb" id="xb-nav-xb">';
		add+='<span id="xb_nav_span" class="glyphicon glyphicon-triangle-bottom"></span>';
		add+='<span class="icon alt1 alt icon-file-text-o"></span>';
		add+='<a href="/ScenariosName">'+scenName+'</a>';
		//add+='<a id="scen-name-close" class="glyphicon glyphicon-remove"></a>';
		add+='</div></div>';
		add+='<ul class="xb-nav_ul" id="scen-class">';
		add+='<li id="nav-Conditions"><a href="/siteInfo"><span class="icon-item alt icon-document-add"></span>Conditions</a></li>';
		add+='<li id="nav-Simualt"><a href="/Simualte"><span class="icon-item alt icon-play"></span>Simualt</a></li>';
		add+='<li id="nav-Results"><a href="#"><span class="icon-item alt icon-document-checked"></span>Results</a></li></ul>';
		$('#scen-info').append(add);
	}
	
	//点击X删除结构
	$('body').on("click","#scen-name-close",function  () {
		$(this).parents("#scen-name").next("#scen-class").remove();
		$(this).parents("#scen-name").remove();
		window.location.href = "/MyScenarios";
		return false;
	});
});	
