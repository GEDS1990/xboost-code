$(function  () {
	//------------Conditions 页面tab切换
	$('.cond-top-ul>li').click(function  () {
		var $this = $(this);
		var i = $this.index();
		var _nextLi = $this.siblings("li");
		var _nextA = _nextLi.find("a");
		_nextLi.removeClass("active");//去除相邻的li的active
		_nextA.removeClass("active");//去除相邻的a的active
		$this.addClass("active");
		$this.find('a').addClass("active");
		$('.table-responsive').eq(i).addClass("active").siblings().removeClass("active");
	});	
	
	//----------选择上传文件
	$(".cond-file-btn").click(function  () {
		var _file = $(this).parent(".cond-file-box").prev();
		var _nextP = $(this).next();
		_file.trigger("click");
		_file.change(function  () {
			_nextP.text(_file.val())
		});
	});
	/*
	 *上传文件
	 * 
	 * */
	$('#cond-file-upload').click(function  () {
		var doc = document;
		var form = [];//创建对象储存文件信息
		var inp_class = doc.getElementById("cond-input-form").getElementsByClassName("cond_file");
		var len = inp_class.length;
		for (var i=0;i<len;i++) {
			FileTest(inp_class[i],form);
		}
		for (var j=0,_len=form.length;j<_len;j++) {
			if (form[j] == false) {
				return false;
			}
		}
		$('.bs-example-modal-input').modal("hide");
		/*
		var formElement = doc.querySelector("cond-input-form");
		$.ajax({
			type:"get",
			url:"",
			async:true,
			success:function(res){
				
			}
		});*/
	});
	
	/*
	 * 检测上传文件是否符合要求
	 */
	function FileTest (objid,form) {
		var res = objid.files[0];
		if (res != undefined) {
			var val = objid.value;
			var _fileName = res.name
			FileFormat(val,_fileName,form);
		}
	}
	/*
	 * 判断文件格式
	 */
	function FileFormat (val,fileName,form) {
		var _index = val.lastIndexOf(".");
	    var _key = val.slice(_index);
	    var _zeng = /^(\.xls|\.xlsx)$/i;
	    if (!_zeng.test(_key)) {
	    	form.push(false)
	    	alert("The file format of"+" "+fileName+" "+"is inconsistent .xls and .xlsx")
	    }else{
	    	//将数据封装对象
	    	form.push(true);
	    }
	}
	
	//导出表格数据   Depots_Info,Depots_Distance,Transportation,Demands,Patameters
	$('.export-btn').click(function  () {
		var $this = $(this);
		$("#Depots_Info").tableExport({
		    // 导出文件的名称
		    filename: name+'_%DD%-%MM%-%YY%',
		    // 导出文件的格式：csv, xls, txt, sql
		    format: 'xls'
		});
//		var _id ="#"+$this.attr("data-xls");
//		console.log(_id)
//		var _name = $this.attr("data-xls");
//		Export_xls (_id,_name)
	});
	/*
	 * 分装导出数据方法
	 */
	function Export_xls (id,name) {
		$(id).tableExport({
		    // 导出文件的名称
		    filename: name+'_%DD%-%MM%-%YY%',
		    // 导出文件的格式：csv, xls, txt, sql
		    format: 'xls'
		});
	}

































});

