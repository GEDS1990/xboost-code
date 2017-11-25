$(document).ready(function  () {

	
	//点击X删除结构
	$('body').on("click","#scen-name-close",function  () {
		console.log(11)
		$(this).parents("#scen-name").next("#scen-class").remove();
		$(this).parents("#scen-name").remove();
		window.location.href = "/MyScenarios";
		return false;
	});
});	
