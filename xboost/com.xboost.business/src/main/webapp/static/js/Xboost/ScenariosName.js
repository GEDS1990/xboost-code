$(function  () {
	
	/*
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
	*/
	
	
	/*
	 * 选择上传文件
	 */
	$("body").on("click",".cond-file-btn",function  () {
		var _file = $(this).parent(".cond-file-box").prev();
		var _nextP = $(this).next();
		_file.trigger("click");
		_file.change(function  () {
			_nextP.text(_file.val())
		});
	});
	/*
	 *上传文件 函数
	 * formId 表单id ,
	 * inpClass 对应的文件的input类名,
	 * url 请求地址，
	 * modId ,模态框id
	 * */
	function UploadFile (formID,inpClass,url,modId) {
		var doc = document;
		var form = [];//创建对象储存文件信息
		var inp_class = doc.getElementById(formID).getElementsByClassName(inpClass);
		var len = inp_class.length;
		for (var i=0;i<len;i++) {
			FileTest(inp_class[i],form);
		}
		for (var j=0,_len=form.length;j<_len;j++) {
			if (form[j] == false) {
				return false;
			}
		}
		var form = new FormData(document.getElementById(formID));
         $.ajax({
             url:url,
             type:"post",
             data:form,
             processData:false,
             contentType:false,
             success:function(data){
                 alert("Import information to complete!");
                 $(modId).modal("hide");
                 window.location.reload(); 
             },
             error:function(e){
                 alert("Mistake!!");
                 window.clearInterval(timer);
             }
         });
         //此处为上传文件的进度条get();
	}
	
	
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
	    var _zeng = /^(\.xlsx)$/i;
	    if (!_zeng.test(_key)) {
	    	form.push(false)
	    	alert("The file format of"+" "+fileName+" "+"is inconsistent  .xlsx")
	    }else{
	    	//将数据封装对象
	    	form.push(true);
	    }
	}
	
	
	/*
	 *Conditions.jsp == siteInfoController
	 * 
	 * */
	(function  () {
		var Depots_Info = $("#Depots_Info");
		if (Depots_Info) {
			var dt =$("#Depots_Info").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/siteInfo/siteInfo.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"siteCode","name":"site_code"},
	                {"data":"siteLongitude","name":"longitude"},
	                {"data":"siteLatitude","name":"latitude"},
	                {"data":"siteName","name":"site_name"},
	                {"data":"siteAddress","name":"site_address"},
	                {"data":"siteArea","name":"site_area"},
	                {"data":"siteType","name":"site_type"},
	                {"data":"siteNightDelivery","name":"site_nightDelivery"},
	                {"data":"carNum","name":"car_num"},
	                {"data":"largeCarModel","name":"large_carModle"},
	                {"data":"maxOperateNum","name":"max_operate_num"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='editLink' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink' data-id='"+row.id+"'>Del</a>";
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	                {
	                    "targets":[3],
	                    "orderable":false
	                },
	                {
	                    "targets":[0,1,2,3,4,5,6,7,8,9,10,11,12],
	                    "orderable":false
	                }
	            ],
	            "language":{
	                "lengthMenu":"Show _MENU_ Record",
	                "search":"Search:",
	                "info": "There are  _TOTAL_ records From _START_ To _END_",
	                "processing":"Loading...",
	                "zeroRecords":"No Data",
	                "infoEmpty": "There are 0 records from 0 to 0",
	                "infoFiltered":"(Read from _MAX_ record)",
	                "paginate": {
	                    "first":      "First",
	                    "last":       "Last",
	                    "next":       "Next",
	                    "previous":   "Prev"
	                }
	            }
	        });
        

	        //添加新用户
	        $("#addNewUser").click(function(){
	            $("#newUserModal").modal('show');
	        });
	        $("#saveBtn").click(function(){
	            $.post("/siteInfo/add",$("#newUserForm").serialize())
	                    .done(function(result){
	                        if("success" == result) {
	                            $("#newUserForm")[0].reset();
	                            $("#newUserModal").modal("hide");
	                            dt.ajax.reload();
	                            window.location.reload(); 
	                        }
	                    }).fail(function(){
	                        alert("Exception occurs when adding");
	                    });
	
	        });
	
	        //删除用户
	        $(document).delegate(".delLink","click",function(){
	            var id = $(this).attr("data-id");
	            if(confirm("Are you sure you want to delete this data?")) {
	                $.post("/siteInfo/del",{"id":id}).done(function(result){
	                    if("success" == result) {
	                        dt.ajax.reload();
	                        window.location.reload(); 
	                    }
	                }).fail(function(){
	                    alert("Delete exception");
	                });
	
	            }
	        });
	
	        //编辑用户
	        $(document).delegate(".editLink","click",function(){
	            $("#editUserForm")[0].reset();
	            var id = $(this).attr("data-id");
	            $.get("siteInfo/site.json",{"id":id}).done(function(result){
	                $("#siteId").val(result.id);
	                $("#siteCode").val(result.siteCode);
	                $("#siteLongitude").val(result.siteLongitude);
	                $("#siteLatitude").val(result.siteLatitude);
	                $("#siteName").val(result.siteName);
	                $("#siteAddress").val(result.siteAddress);
	                $("#siteArea").val(result.siteArea);
	                $("#siteType").val(result.siteType);
	                $("#siteNightDelivery").val(result.siteNightDelivery);
	                $("#carNum").val(result.carNum);
	                $("#largeCarModel").val(result.largeCarModel);
	                $("#maxOperateNum").val(result.maxOperateNum);
	                $("#editUserModal").modal("show");
	
	            }).fail(function(){
	
	            });
				$("#editUserModal").modal("show");
	            
	        });
	
	        $("#editBtn").click(function(){
	        alert($("#editUserForm").serialize())
	            $.post("/siteInfo/edit",$("#editUserForm").serialize()).done(function(result){
	                if(result == "success") {
	                    $("#editUserModal").modal("hide");
	                    dt.ajax.reload();
	                    window.location.reload(); 
	                }
	            }).fail(function(){
	                alert("Modify user exception");
	            });
	
	        });
	        
	        //提交表单上传
	        $('#cond-file-upload').click(function  () {
				UploadFile("cond-input-form","cond_file","/siteInfo/addByExcel",'.bs-example-modal-input')
			});
		}
	})(),
	/*
	 *Distance.jsp == siteDistController
	 * 
	 * */
	(function  () {
		var Depots_Distance = $("#Depots_Distance");
		if (Depots_Distance) {
			var dt =$("#Depots_Distance").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/siteDist/siteDist.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"siteCollect","name":"site_collect"},
	                {"data":"siteDelivery","name":"site_delivery"},
	                {"data":"carDistance","name":"car_distance"},
	                {"data":"durationNightDelivery","name":"duration_night_delivery"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='editLink' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink' data-id='"+row.id+"'>Del</a>";
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	            	{
	                    "targets":[0],
	                    "visible":false
	                },
	                {
	                    "targets":[0,1,2,3,4,5],
	                    "orderable":false
	                }
	            ],
	            "language":{
	                "lengthMenu":"Show _MENU_ Record",
	                "search":"Search:",
	                "info": "There are  _TOTAL_ records From _START_ To _END_",
	                "processing":"Loading...",
	                "zeroRecords":"No Data",
	                "infoEmpty": "There are 0 records from 0 to 0",
	                "infoFiltered":"(Read from _MAX_ record)",
	                "paginate": {
	                    "first":      "First",
	                    "last":       "Last",
	                    "next":       "Next",
	                    "previous":   "Prev"
	                }
	            }
	        });
        

        //添加新用户
        $("#addNewUser").click(function(){
            $("#newUserModal").modal('show');
        });
        $("#saveBtn").click(function(){
            $.post("/siteInfo/add",$("#newUserForm").serialize())
                    .done(function(result){
                        if("success" == result) {
                            $("#newUserForm")[0].reset();
                            $("#newUserModal").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        alert("Exception occurs when adding");
                    });

        });

        //删除用户
        $(document).delegate(".delLink","click",function(){
            var id = $(this).attr("data-id");
            if(confirm("Are you sure you want to delete this data?")) {
                $.post("/siteInfo/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                        window.location.reload(); 
                    }
                }).fail(function(){
                    alert("Delete exception");
                });

            }
        });

        //编辑用户
        $(document).delegate(".editLink","click",function(){
            $("#editUserForm")[0].reset();
            var id = $(this).attr("data-id");
            $.get("siteInfo/site.json",{"id":id}).done(function(result){
                $("#siteId").val(result.id);
                $("#siteCode").val(result.siteCode);
                $("#siteLongitude").val(result.siteLongitude);
                $("#siteLatitude").val(result.siteLatitude);
                $("#siteName").val(result.siteName);
                $("#siteAddress").val(result.siteAddress);
                $("#siteArea").val(result.siteArea);
                $("#siteType").val(result.siteType);
                $("#siteNightDelivery").val(result.siteNightDelivery);
                $("#carNum").val(result.carNum);
                $("#largeCarModel").val(result.largeCarModel);
                $("#maxOperateNum").val(result.maxOperateNum);
                $("#editUserModal").modal("show");

            }).fail(function(){

            });
			$("#editUserModal").modal("show");
            
        });

        $("#editBtn").click(function(){
        alert($("#editUserForm").serialize())
            $.post("/siteInfo/edit",$("#editUserForm").serialize()).done(function(result){
                if(result == "success") {
                    $("#editUserModal").modal("hide");
                    dt.ajax.reload();
                    window.location.reload(); 
                }
            }).fail(function(){
                alert("Modify user exception");
            });

        });

         $("#cond-file-upload").click(function(){
             var form = new FormData(document.getElementById("cond-input-form"));
                 $.ajax({
                     url:"${pageContext.request.contextPath}/siteInfo/addByExcel",
                     type:"post",
                     data:form,
                     processData:false,
                     contentType:false,
                     success:function(data){
                         alert("Import information to complete!");
                         window.location.reload(); 
                     },
                     error:function(e){
                         alert("Mistake!!");
                         window.clearInterval(timer);
                     }
                 });
                 //此处为上传文件的进度条get();
         });
		}
		
	})(),
	/*
	 *Demands.jsp == DemandInfoController
	 * 
	 * */
	(function  () {
		var Demands = $("#Demands");
		if (Demands) {
			var dt =$("#Demands").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/demandInfo/demandInfo.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"date","name":"date"},
	                {"data":"siteCodeCollect","name":"site_code_collect"},
	                {"data":"siteCodeDelivery","name":"site_code_delivery"},
	                {"data":"productType","name":"product_type"},
	                {"data":"durationStart","name":"duration_start"},
	                {"data":"durationEnd","name":"duration_end"},
	                {"data":"weight","name":"weight"},
	                {"data":"votes","name":"votes"},
	                {"data":"ageing","name":"ageing"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='editLink' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink' data-id='"+row.id+"'>Del</a>";
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	                {
	                    "targets":[0],
	                    "visible":false
	                },
	                {
	                    "targets":[0,1,2,3,4,5,6,7,8,9,10],
	                    "orderable":false
	                }
	            ],
	            "language":{
	                "lengthMenu":"Show _MENU_ Record",
	                "search":"Search:",
	                "info": "There are  _TOTAL_ records From _START_ To _END_",
	                "processing":"Loading...",
	                "zeroRecords":"No Data",
	                "infoEmpty": "There are 0 records from 0 to 0",
	                "infoFiltered":"(Read from _MAX_ record)",
	                "paginate": {
	                    "first":      "First",
	                    "last":       "Last",
	                    "next":       "Next",
	                    "previous":   "Prev"
	                }
	            }
	        });
        

        //添加新用户
        $("#addNewUser").click(function(){
            $("#newUserModal").modal('show');
        });
        $("#saveBtn").click(function(){
            $.post("/siteInfo/add",$("#newUserForm").serialize())
                    .done(function(result){
                        if("success" == result) {
                            $("#newUserForm")[0].reset();
                            $("#newUserModal").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        alert("Exception occurs when adding");
                    });

        });

        //删除用户
        $(document).delegate(".delLink","click",function(){
            var id = $(this).attr("data-id");
            if(confirm("Are you sure you want to delete this data?")) {
                $.post("/siteInfo/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                        window.location.reload(); 
                    }
                }).fail(function(){
                    alert("Delete exception");
                });

            }
        });

        //编辑用户
        $(document).delegate(".editLink","click",function(){
            $("#editUserForm")[0].reset();
            var id = $(this).attr("data-id");
            $.get("siteInfo/site.json",{"id":id}).done(function(result){
                $("#siteId").val(result.id);
                $("#siteCode").val(result.siteCode);
                $("#siteLongitude").val(result.siteLongitude);
                $("#siteLatitude").val(result.siteLatitude);
                $("#siteName").val(result.siteName);
                $("#siteAddress").val(result.siteAddress);
                $("#siteArea").val(result.siteArea);
                $("#siteType").val(result.siteType);
                $("#siteNightDelivery").val(result.siteNightDelivery);
                $("#carNum").val(result.carNum);
                $("#largeCarModel").val(result.largeCarModel);
                $("#maxOperateNum").val(result.maxOperateNum);
                $("#editUserModal").modal("show");

            }).fail(function(){

            });
			$("#editUserModal").modal("show");
            
        });

        $("#editBtn").click(function(){
        alert($("#editUserForm").serialize())
            $.post("/siteInfo/edit",$("#editUserForm").serialize()).done(function(result){
                if(result == "success") {
                    $("#editUserModal").modal("hide");
                    dt.ajax.reload();
                    window.location.reload(); 
                }
            }).fail(function(){
                alert("Modify user exception");
            });

        });

         $("#cond-file-upload").click(function(){
             var form = new FormData(document.getElementById("cond-input-form"));
                 $.ajax({
                     url:"${pageContext.request.contextPath}/siteInfo/addByExcel",
                     type:"post",
                     data:form,
                     processData:false,
                     contentType:false,
                     success:function(data){
                         alert("Import information to complete!");
                         window.location.reload(); 
                     },
                     error:function(e){
                         alert("Mistake!!");
                         window.clearInterval(timer);
                     }
                 });
                 //此处为上传文件的进度条get();
         });
		}
	})()
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*
	
	//导出表格数据   Depots_Info,Depots_Distance,Transportation,Demands,Patameters
	$('.export-btn').click(function  () {
		var $this = $(this);
		$("#Depots_Info").tableExport({
		    // 导出文件的名称
		    filename: name+'_%DD%-%MM%-%YY%',
		    // 导出文件的格式：csv, xls, txt, sql
		    format: 'xlsx'
		});
//		var _id ="#"+$this.attr("data-xls");
//		console.log(_id)
//		var _name = $this.attr("data-xls");
//		Export_xls (_id,_name)
	});
	
	function Export_xls (id,name) {
		$(id).tableExport({
		    // 导出文件的名称
		    filename: name+'_%DD%-%MM%-%YY%',
		    // 导出文件的格式：csv, xls, txt, sql
		    format: 'xls'
		});
	}

*/































});

