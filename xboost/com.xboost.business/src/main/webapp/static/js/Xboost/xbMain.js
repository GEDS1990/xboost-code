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
/*allscenarios.jsp*/
(function(){
    var user_out = doc.getElementById("user-out");
    if (user_out) {
        userList();
        //点击显示分类下拉 下拉框
        $('#user-out').click(function  () {
            $(this).css({"border-color":"rgb(77, 144, 254)"})
            $('#user-box').toggle();
        });
        //点 选项 进行选择
        $('body').on("click","#userClass>li",function  () {
            var _val = $(this).attr("data-value");
            var _text = $(this).text();
            $("#userId").val(_val);
            $('#class-user').text(_text);
            $('#user-box').hide();
        });
        //阻止冒泡
        $('.clearfix.classwrap').click(function  () {
            return false;
        });
    }
    //初始化 user 列表函数
    function userList () {
        $.get("/account/alluser.json").done(function  (res) {
            if (res) {
                //console.log(res);
                if (res) {
                    var result = res,
                    len = result.length;
                    $('#userClass').empty();
                    $('#userClass>li').off("click");
                    for (var i=0;i<len;i++) {
                        var add="";
                        add+='<li data-value='+result[i].id+'>'+result[i].username+'</li>';
                        $('#userClass').prepend(add);
                    }
                }
            }
        }).fail(function  () {
            console.log("fail");
        })
    }
}());
/*
 *ScenariosName.jsp
 * 
 */

(function  () {
	
	var editBtn = doc.getElementById("edit-create-info");
	if (editBtn) {
		var scenId = $('#scenName').attr("data-id");
		//获取场景信息
		$.get("/MyScenarios/scen.json",{"id":scenId}).done(function  (res) {
			$('#scen-cate').text(res.scenariosCategory);
			$('#scen-desc').text(res.scenariosDesc);
			$('#scenariosName').val(res.scenariosName);
			$('#class-first').text(res.scenariosName);
			$('#scenariosCategory').val(res.scenariosCategory);
			$('textarea[name="scenariosDesc"]').val(res.scenariosDesc);
		}).fail(function  () {
			console.log('fail');
		});
		//获取overview
		$.get("/ScenariosName/settingsOverview.json",{"id":scenId}).done(function  (res) {
            $('#depot-quantity').text(res.siteCounter);
            $('#vehicle-quantity').text(res.tranCounter);
            $('#demand-quantity').text(res.demandsCounter);
            $('#farthest-distance').text(res.farthestDist);
        }).fail(function  (e) {
            console.log('fail');
        });
		
		//创建场景
        $("#edit-create-info").click(function(){
            $("#newUserModal-scen").modal('show');
        });
        $("#saveBtn-scen").click(function(){
        	var _val = $("input[name='scenariosName']").val(),
        	pattern = new RegExp("[`~!@#$^&*=|{}':;',\\[\\]<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]"),
			xss = pattern.test(_val);
        	if (_val && !xss) {
        		$.post("/MyScenarios/add",$("#newUserForm-scen").serialize()).done(function(result){
                        if("success" == result) {
                            $("#newUserForm-scen")[0].reset();
                            $("#newUserModal-scen").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        //alert("Exception occurs when adding");
                    });

        	}else{
        		$("input[name='scenariosName']").focus();
        	}
            
        });
	}
}());

















});
