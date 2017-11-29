$(function  () {
/*
 *MyScenarios.jsp
 */
// new Category
$('#scenariosCategory').click(function  () {
	$.get("/MyScenarios/category.json").done(function  (res) {
		if (res) {
			console.log(res)
			$('#Category-class').show();
		}
	}).fail(function  () {
		console.log("fail");
	})
});

$('#classbtn').click(function  () {
	var info = $('#classAdd').val();
	if (info) {
		$.post("/MyScenarios/addCategory",{"categoryName":info}).done(function  (res) {
			console.log(res)
			$('#class-li').after("<li>"+info+"</li>");
			$('#classAdd').val("");
		})
	}else{
		$('#classAdd').focus();
	}
	return false;
});













});
