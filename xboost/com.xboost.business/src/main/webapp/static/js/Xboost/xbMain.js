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
			//console.log(res);
			if (res.data) {
				var result = res.data,
				len = result.length;
				$('#cateClass').empty();
				$('#cateClass>li').off("click");
				for (var i=0;i<len;i++) {
					var add="";
					add+='<li data-value='+result[i].id+'>'+result[i].categoryName+'</li>';
					$('#cateClass').prepend(add);
				}
				$('input[name="scenariosCategory"]').val(result[len-1].categoryName);
				$('#class-first').text(result[len-1].categoryName);
			}
		}
	}).fail(function  () {
		console.log("fail");
	})
}
//初始化 分类选项
(function  () {
	var category_out = doc.getElementById("category-out");
	if (category_out) {
		CategoryList();
		//点击显示分类下拉 下拉框
		$('#category-out').click(function  () {
			$(this).css({"border-color":"rgb(77, 144, 254)"})
			$('#Category-box').toggle();
		});
		//点 选项 进行选择
		$('body').on("click","#cateClass>li",function  () {
			var _val = $(this).attr("data-value");
			var _text = $(this).text();
			$('input[name="scenariosCategory"]').val(_text);
			$('#class-first').text(_text);
			$('#Category-box').hide();
		});
		//阻止冒泡
		$('.clearfix.classwrap').click(function  () {
			return false;
		});
		//点击add 增加选项
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
				return false;
			}
		});
	}
}());













});
