$(document).ready(function  () {
	//侧边栏点击切换
	$(".nav_xb").click(function  () {
		var $this = $(this);
		var _span = $this.find(".glyphicon");//当前点击下的span
		var _spans = $this.parent("li").siblings("li").find(".glyphicon");//相邻下的span
		var span_class = _span.attr("class");
		var _ul = $this.parent("li").siblings("li").find("ul");
		_ul.slideUp();//将相邻的列表收起来
		_spans.attr("class","glyphicon glyphicon-triangle-right");//将相邻的小箭头设置向右
		if(span_class == "glyphicon glyphicon-triangle-right"){ //判断小图标是否是向右的
			_span.attr("class","glyphicon glyphicon-triangle-bottom");
			$this.next("ul").slideDown()
		}else{
			_span.attr("class","glyphicon glyphicon-triangle-right");
			$this.next("ul").slideUp()
		}
	});
});	
//上传
$(document).ready(function  () {
	var xhr;  
	var doc = document;
	var fileId = doc.getElementById("file"),
		nameId = doc.getElementById("fileName"),
		sizeId = doc.getElementById("fileSize"),
		typeId = doc.getElementById("fileType"),
		xb_file = doc.getElementById("xb_file");
	
	$("#xb_btn_up").click(function  () {//点击choice 相当于点击input file
		$("#file").trigger("click");
	});
	$("#file").change(function  () {//#file发送改变就把改变的值赋给#xb_file
		var res = $("#file").val();
		$("#xb_file").val(res);
		fileSelected(fileId,nameId,sizeId,typeId)
	});
	$("#xb_btn_upload").click(function  () {
		UpladFile(fileId);
	})
});
function createXMLHttpRequest() {  //ajax兼容
    if (window.ActiveXObject) {  
        xhr = new ActiveXObject("Microsoft.XMLHTTP");  
    } else if (window.XMLHttpRequest) {  
        xhr = new XMLHttpRequest();  
    }  
}  
function fileSelected(fileId,nameId,sizeId,typeId) { // 获取显示文件信息
	var file = fileId.files[0];
	if (file) {
	  var fileSize = 0;
		if (file.size > 1024 * 1024){
			fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
		}else{
			fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
		}
		  nameId.innerHTML = 'Name: ' + file.name;
		  sizeId.innerHTML = 'Size: ' + fileSize;
		  typeId.innerHTML = 'Type: ' + file.type;
	}
}
function UpladFile(fileId) {  
    var fileObj = fileId.files[0]; 
    var vals = fileId.value;
    var _index = vals.lastIndexOf(".");
    var _key = vals.slice(_index);
    var _zeng = /^(\.xls|\.xlsx|\.xlsm|\.xltx|\.xltm|\.xlam|\.xlsb)$/i
    if(_zeng.test(_key)){
    	var FileController = 'https://www.zhukaijie.site/in.php';  //改为接受表格数据接口
        var form = new FormData(); 
        $.ajax({
        	type:"post",
        	url:FileController,
        	async:true,
        	data:{myfile:fileObj},
        	success:function (res) {
        		alert("Upload success");
        	}
        });
    }else{
    	alert("The file format must be in the form of Excel")
    }
}
//导出
$(document).ready(function  () {
	$("#key").click(function  () {
		$('.table').tableExport({
	    // 导出文件的名称
	    filename: 'table_%DD%-%MM%-%YY%',
	    // 导出文件的格式：csv, xls, txt, sql
	    format: 'xls',
	    // 导出指定的表格列
	    cols: '1,2,3,4,5'
		});
	});
});
function ExportTable (type) {
	$('.table').tableExport({
	    // 导出文件的名称
	    filename: 'table_%DD%-%MM%-%YY%',
	    // 导出文件的格式：csv, xls, txt, sql
	    format: 'xls',
	    // 导出指定的表格列
	    cols: '1,2,3,4,5'
	});
}
