$(function  () {
	var doc = document;
/*
 *MyScenarios.jsp
 */
// new Category
//初始化 Category 列表函数
function CategoryList () {
	$.get("/MyScenarios/category.json").done(function  (res) {
		if (res) {
			console.log(res);
			if (res.data) {
				$('#Category-class').show();
				$('#scenariosCategory').empty();
				var result = res.data,
				len = result.length;
				for (var i=0;i<len;i++) {
					var add="";
					add+='<option value='+result[i].id+'>'+result[i].categoryName+'</option>';
					$('#scenariosCategory').prepend(add);
				}
			}
			
		}
	}).fail(function  () {
		console.log("fail");
	})
}
(function  () {
	var scenariosCategory = doc.getElementById("scenariosCategory");
	if (scenariosCategory) {
		CategoryList();
	}
})(),
//$('#scenariosCategory').click(function  () {
//	
//});

$('#classbtn').click(function  () {
	var info = $('#classAdd').val();
	if (info) {
		$.post("/MyScenarios/addCategory",{"categoryName":info}).done(function  (res) {
			console.log(res);
			if (res == "success") {
				CategoryList();
			}
		})
	}else{
		$('#classAdd').focus();
	}
	return false;
});
//Category class 选项绑定事件
$('body').on("click","#Category-box>li",function  () {
	var id = $(this).attr("data-id");
	$('#scenariosCategory').val(id);
	console.log($('#scenariosCategory').val())
});












});
