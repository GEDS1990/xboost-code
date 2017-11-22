$(function  () {
	var doc = document;
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
		var Depots_Info = doc.getElementById("Depots_Info");
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
	                    "targets":[0],
	                    "visible":false
	                },
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
		var Depots_Distance = doc.getElementById("Depots_Distance");
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
	                    return "<a href='javascript:;' class='editLink-dist' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-dist' data-id='"+row.id+"'>Del</a>";
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
        $("#addNewUser-dist").click(function(){
            $("#newUserModal-dist").modal('show');
        });
        $("#saveBtn-dist").click(function(){
            $.post("/siteDist/add",$("#newUserForm-dist").serialize())
                    .done(function(result){
                        if("success" == result) {
                            $("#newUserForm-dist")[0].reset();
                            $("#newUserModal-dist").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        alert("Exception occurs when adding");
                    });

        });

        //删除用户
        $(document).delegate(".delLink-dist","click",function(){
            var id = $(this).attr("data-id");
            if(confirm("Are you sure you want to delete this data?")) {
                $.post("/siteDist/del",{"id":id}).done(function(result){
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
        $(document).delegate(".editLink-dist","click",function(){
            $("#editUserForm-dist")[0].reset();
            var id = $(this).attr("data-id");
            $.get("siteDist/siteDistInfo.json",{"id":id}).done(function(result){
                $("#siteId-dist").val(result.id);
                $("#siteCollect").val(result.siteCollect);
                $("#siteDelivery").val(result.siteDelivery);
                $("#carDistance").val(result.carDistance);
                $("#durationNightDelivery").val(result.durationNightDelivery);
                $("#editUserModal-dist").modal("show");

            }).fail(function(){

            });
			$("#editUserModal-dist").modal("show");
            
        });

        $("#editBtn-dist").click(function(){
        
            $.post("/siteDist/edit",$("#editUserForm-dist").serialize()).done(function(result){
                if(result == "success") {
                    $("#editUserModal-dist").modal("hide");
                    dt.ajax.reload();
                    window.location.reload(); 
                }
            }).fail(function(){
                alert("Modify user exception");
            });

        });

         $("#cond-file-upload-dist").click(function(){
         	UploadFile("cond-input-form-dist","cond_file","/siteDist/addByExcel",'.bs-example-modal-input')
         });
		}
		
	})(),
	/*
	 *Demands.jsp == DemandInfoController
	 * 
	 * */
	(function  () {
		var Demands = doc.getElementById("Demands");
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
	                    return "<a href='javascript:;' class='editLink-dem' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-dem' data-id='"+row.id+"'>Del</a>";
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
        $("#addNewUser-dem").click(function(){
            $("#newUserModal-dem").modal('show');
        });
        $("#saveBtn-dem").click(function(){
            $.post("/demandInfo/add",$("#newUserForm-dem").serialize())
                    .done(function(result){
                        if("success" == result) {
                            $("#newUserForm-dem")[0].reset();
                            $("#newUserModal-dem").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        alert("Exception occurs when adding");
                    });

        });

        //删除用户
        $(document).delegate(".delLink-dem","click",function(){
            var id = $(this).attr("data-id");
            if(confirm("Are you sure you want to delete this data?")) {
                $.post("/demandInfo/del",{"id":id}).done(function(result){
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
        $(document).delegate(".editLink-dem","click",function(){
            $("#editUserForm-dem")[0].reset();
            var id = $(this).attr("data-id");
            $.get("demandInfo/demandInfoById.json",{"id":id}).done(function(result){
                $("#siteId-dem").val(result.id);
                $("#date").val(result.date);
                $("#siteCodeCollect").val(result.siteCodeCollect);
                $("#siteCodeDelivery").val(result.siteCodeDelivery);
                $("#productType").val(result.productType);
                $("#durationStart").val(result.durationStart);
                $("#durationEnd").val(result.durationEnd);
                $("#weight").val(result.weight);
                $("#votes").val(result.votes);
                $("#ageing").val(result.ageing);
                $("#editUserModal").modal("show");

            }).fail(function(){

            });
			$("#editUserModal-dem").modal("show");
            
        });

        $("#editBtn-dem").click(function(){
        
            $.post("/demandInfo/edit",$("#editUserForm-dem").serialize()).done(function(result){
                if(result == "success") {
                    $("#editUserModal-dem").modal("hide");
                    dt.ajax.reload();
                    window.location.reload(); 
                }
            }).fail(function(){
                alert("Modify user exception");
            });

        });

         $("#cond-file-upload-dem").click(function(){
             UploadFile("cond-input-form-dem","cond_file","/demandInfo/addByExcel",'.bs-example-modal-input')
         });
		}
	})(),
	/**
	 * patameters.jsp = modelController
	 * 
	 */
	(function  () {
		var Patameters = document.getElementById("Patameters");
		if (Patameters) {
			var dt =$("#Patameters").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/modelArg/modelArg.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"parameterName","name":"parameter_name"},
	                {"data":"data","name":"data"},
	                {"data":"note","name":"note"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='editLink-pata' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-pata' data-id='"+row.id+"'>Del</a>";
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	                {
	                    "targets":[0],
	                    "visible":false
	                },
	                {
	                    "targets":[0,1,2,3,4],
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
        $("#addNewUser-pata").click(function(){
            $("#newUserModal-pata").modal('show');
        });
        $("#saveBtn-pata").click(function(){
            $.post("/modelArg/add",$("#newUserForm-pata").serialize())
                    .done(function(result){
                        if("success" == result) {
                            $("#newUserForm-pata")[0].reset();
                            $("#newUserModal-pata").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        alert("Exception occurs when adding");
                    });

        });

        //删除用户
        $(document).delegate(".delLink-pata","click",function(){
            var id = $(this).attr("data-id");
            if(confirm("Are you sure you want to delete this data?")) {
                $.post("/modelArg/del",{"id":id}).done(function(result){
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
        $(document).delegate(".editLink-pata","click",function(){
            $("#editUserForm-pata")[0].reset();
            var id = $(this).attr("data-id");
            $.get("modelArg/modelArgById.json",{"id":id}).done(function(result){
                $("#siteId-pata").val(result.id);
                $("#parameterName").val(result.parameterName);
                $("#data").val(result.data);
                $("#note").val(result.note);
                
                $("#editUserModal-pata").modal("show");

            }).fail(function(){

            });
			
            
        });

        $("#editBtn-pata").click(function(){
        
            $.post("/modelArg/edit",$("#editUserForm-pata").serialize()).done(function(result){
                if(result == "success") {
                    $("#editUserModal-pata").modal("hide");
                    dt.ajax.reload();
                    window.location.reload(); 
                }
            }).fail(function(){
                alert("Modify user exception");
            });

        });

         $("#cond-file-upload-pata").click(function(){
             UploadFile("cond-input-form-pata","cond_file","/modelArg/addByExcel",'.bs-example-modal-input')
         });
		}
	})(),
	/**
	 * Tran.jsp = TransportationController
	 * 
	 */
	(function  () {
		var Transportation = document.getElementById("Transportation");
		if (Transportation) {
			var dt =$("#Transportation").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/transport/transport.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"carSource","name":"car_source"},
	                {"data":"carNum","name":"car_num"},
	                {"data":"carType","name":"car_type"},
	                {"data":"speed","name":"speed"},
	                {"data":"maxDistance","name":"max_distance"},
	                {"data":"maxLoad","name":"max_load"},
	                {"data":"durationUnloadFull","name":"duration_unload_full"},
	                {"data":"carCost1","name":"car_cost1"},
	                {"data":"carCost2","name":"car_cost2"},
	                {"data":"carCost3","name":"car_cost3"},
	                {"data":"singleVoteCost1","name":"single_vote_cost1"},
	                {"data":"singleVoteCost2","name":"single_vote_cost2"},
	                {"data":"singleVoteCost3","name":"single_vote_cost3"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='editLink-tran' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-tran' data-id='"+row.id+"'>Del</a>";
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	                {
	                    "targets":[0],
	                    "visible":false
	                },
	                {
	                    "targets":[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14],
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
        $("#addNewUser-tran").click(function(){
            $("#newUserModal-tran").modal('show');
        });
        $("#saveBtn-tran").click(function(){
            $.post("/transport/add",$("#newUserForm-tran").serialize())
                    .done(function(result){
                        if("success" == result) {
                            $("#newUserForm-tran")[0].reset();
                            $("#newUserModal-tran").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        alert("Exception occurs when adding");
                    });

        });

        //删除用户
        $(document).delegate(".delLink-tran","click",function(){
            var id = $(this).attr("data-id");
            if(confirm("Are you sure you want to delete this data?")) {
                $.post("/transport/del",{"id":id}).done(function(result){
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
        $(document).delegate(".editLink-tran","click",function(){
            $("#editUserForm-pata")[0].reset();
            var id = $(this).attr("data-id");
            $.get("transport/transpt.json",{"id":id}).done(function(result){
                $("#siteId-pata").val(result.id);
                $("#parameterName").val(result.parameterName);
                $("#data").val(result.data);
                $("#note").val(result.note);
                
                $("#editUserModal-pata").modal("show");

            }).fail(function(){

            });
			
            
        });

        $("#editBtn-pata").click(function(){
        
            $.post("/transport/edit",$("#editUserForm-pata").serialize()).done(function(result){
                if(result == "success") {
                    $("#editUserModal-pata").modal("hide");
                    dt.ajax.reload();
                    window.location.reload(); 
                }
            }).fail(function(){
                alert("Modify user exception");
            });

        });

         $("#cond-file-upload-pata").click(function(){
             UploadFile("cond-input-form-pata","cond_file","/transport/addByExcel",'.bs-example-modal-input')
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