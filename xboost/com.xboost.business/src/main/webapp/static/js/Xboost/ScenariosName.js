$(function  () {
	var doc = document;
	
	/*
	 *MyScenarios.jsp == MyScenariosController
	 * 
	 * */
	(function  () {
		
		var MyScenarios = doc.getElementById("MyScenarios");
		if (MyScenarios) {
			//加载列表
			var dt =$("#MyScenarios").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"MyScenarios/scenarios.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"userId","name":"user_id"},
	                {"data":"scenariosName","name":"scenarios_name"},
	                {"data":"scenariosCategory","name":"scenarios_category"},
	                {"data":"scenariosDesc","name":"scenarios_desc"},
	                {"data":"scenariosModel","name":"scenarios_model"},
	                {"data":"scenariosOut","name":"scenarios_out"},
	                {"data":"lastOpenTime","name":"last_open_time"},
	                {"data":"scenariosStatus","name":"scenarios_status"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='openLink-scen' data-scenariosid='"+row.id+"'>Open</a> <a href='javascript:;' class='editLink-scen' data-scenariosid='"+row.id+"'>Export</a> <a href='javascript:;' class='delLink-scen' data-scenariosid='"+row.id+"'>Delete</a>";
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	            	{
	                    "targets":[0,1,5,6],
	                    "visible":false
	                },
	                {
	                    "targets":[9],
	                    "orderable":false
	                },
	                {
	                    "targets":[1,2,3,4,5],
	                    "orderable":true
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
	    
        
        //点击open 打开场景
        $("body").on("click",".openLink-scen",function  () {
        	var $this = $(this);
        	var openScenariosId = $this.attr("data-scenariosid");
        	var scenName = $this.parent("td").parent("tr").find("td").eq(0).text();
        	$.post("/MyScenarios/open",{"openScenariosId":openScenariosId,"openScenariosName":scenName}).done(function  (res) {
        		if (res == "success") {
        			$('#scen-name').remove();
        			$('#scen-class').remove();
        			var add = "";
					add+='<li class="xb-hover" id="scen-name">';
					add+='<a href="/ScenariosName" class="nav_xb" id="xb-nav-xb">';
					add+='<span id="xb_nav_span" class="glyphicon glyphicon-triangle-bottom"></span>';
					add+='<span class="icon alt1 alt icon-file-text-o"></span>'+scenName+'</a></li>';
					add+='<li><ul class="xb-nav_ul" id="scen-class">';
					add+='<li id="nav-Conditions"><a href="/siteInfo"><span class="icon-item alt icon-document-add"></span>Settings</a></li>';
					add+='<li id="nav-Simualt"><a href="/simualte"><span class="icon-item alt icon-play"></span>Simualt</a></li>';
					add+='<li id="nav-Results"><a href="#"><span class="icon-item alt icon-document-checked"></span>Results</a></li></ul></li>';
					$('#after-content').after(add);
					window.location.href = "/ScenariosName";
        		}
        	});
        })
       

        //创建场景
        $("#addNewUser-scen").click(function(){
            $("#newUserModal-scen").modal('show');
        });
        $("#saveBtn-scen").click(function(){
            $.post("/MyScenarios/add",$("#newUserForm-scen").serialize())
                    .done(function(result){
                        if("success" == result) {
                            $("#newUserForm-scen")[0].reset();
                            $("#newUserModal-scen").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        alert("Exception occurs when adding");
                    });

        });

        //删除用户
        $(document).delegate(".delLink-scen","click",function(){
            var id = $(this).attr("data-scenariosid");
            $('#modal-del').modal("show")
            $('#modal-delBtn').click(function  () {
            	$.post("/MyScenarios/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                        window.location.reload(); 
                    }
                }).fail(function(){
                    alert("Delete exception");
                });
            }) 
                

            
        });

        //导出文件
        

		//上传excel文件
         $("#cond-file-upload-dist").click(function(){
         	UploadFile("cond-input-form-dist","cond_file","/MyScenarios/addByExcel",'.bs-example-modal-input')
         });
		}
		
	})(),
	
	
	
	
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
			var href = "http://"+doc.location.host+"/static/excelTemplate/deport.xlsx"
			$('.down-href').attr("href",href)
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
	                {"data":"distribCenter","name":"distrib_center"},
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
	            $('#modal-info').modal("show");
	            $("#modal-infodelBtn").click(function  () {
	            	$.post("/siteInfo/del",{"id":id}).done(function(result){
	                    if("success" == result) {
	                        dt.ajax.reload();
	                        window.location.reload(); 
	                    }
	                }).fail(function(){
	                    alert("Delete exception");
	                });
	            }) 
	            
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
	                $("#distribCenter").val(result.distribCenter);
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
			var href = "http://"+doc.location.host+"/static/excelTemplate/distances.xlsx";
			$('.down-href').attr("href",href);
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
            $('#modal-dist').modal("show");
            $('#modal-distdelBtn').click(function  () {
            	$.post("/siteDist/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                        window.location.reload(); 
                    }
                }).fail(function(){
                    alert("Delete exception");
                });
            });
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
			var href = "http://"+doc.location.host+"/static/excelTemplate/demands.xlsx";
			$('.down-href').attr("href",href);
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
            $('#modal-dem').modal("show");
            $('#modal-demdelBtn').click(function  () {
            	$.post("/demandInfo/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                        window.location.reload(); 
                    }
                }).fail(function(){
                    alert("Delete exception");
                });
            });
            
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
			var href = "http://"+doc.location.host+"/static/excelTemplate/marg.xlsx";
			$('.down-href').attr("href",href);
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
            $('#modal-pata').modal("show");
            $('#modal-patadelBtn').click(function  () {
            	$.post("/modelArg/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                        window.location.reload(); 
                    }
                }).fail(function(){
                    alert("Delete exception");
                });
            });
                
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
			var href = "http://"+doc.location.host+"/static/excelTemplate/car.xlsx";
			$('.down-href').attr("href",href);
			var dt =$("#Transportation").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'desc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/car/transport.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"type","name":"type"},
	                {"data":"carSource","name":"car_source"},
	                {"data":"maxLoad","name":"max_load"},
	                {"data":"durationUnloadFull","name":"duration_unload_full"},
	                {"data":"maxStop","name":"max_stop"},
	                {"data":"dimensions","name":"dimensions"},
	                {"data":"fixed_round","name":"fixed_round"},
	                {"data":"fixed_round_fee","name":"fixed_round_fee"},
	                {"data":"start_location","name":"start_location"},
	                {"data":"end_location","name":"end_location"},
	                {"data":"max_distance","name":"max_distance"},
	                {"data":"max_running_time","name":"max_running_time"},
	                {"data":"cost_per_distance","name":"cost_per_distance"},
	                {"data":"cost_per_time","name":"cost_per_time"},
	                {"data":"fixed_cost","name":"fixed_cost"},
	                {"data":"velocity","name":"velocity"},
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
	                    "targets":[0,17],
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
        	var tw_start = $('#time_window_start').val();
        	var tw_end = $('#time_window_end').val();
        	var twData = tw_start+"."+tw_end;
        	$('#tw').val(twData);
        	console.log($("#newUserForm-tran").serialize())
            $.post("/car/add",$("#newUserForm-tran").serialize())
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
            $('#modal-tran').modal("show");
            $('#modal-trandelBtn').click(function  () {
            	$.post("/car/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                        window.location.reload(); 
                    }
                }).fail(function(){
                    alert("Delete exception");
                });
            });
                
        });

        //编辑用户
        $(document).delegate(".editLink-tran","click",function(){
            $("#editUserForm-tran")[0].reset();
            var id = $(this).attr("data-id");
            $.get("/car/transpt.json",{"id":id}).done(function(result){
                $("#siteId-tran").val(result.id);
                $("#carSource").val(result.carSource);
                $("#dimensions").val(result.dimensions);
                $("#maxLoad").val(result.maxLoad);
                $("#durationUnloadFull").val(result.durationUnloadFull);
                $("#maxStop").val(result.maxStop);
                $("#fixedRound").val(result.fixedRound);
                $("#fixedRoundFee").val(result.fixedRoundFee);
                $("#startLocation").val(result.startLocation);
                $("#endLocation").val(result.endLocation);
                $("#maxDistance").val(result.maxDistance);
                $("#maxRunningTime").val(result.maxRunningTime);
                $("#costPerDistance").val(result.costPerDistance);
                $("#costPerTime").val(result.costPerTime);
                $("#fixedCost").val(result.fixedCost);
                $("#velocity").val(result.velocity);
                
                $("#editUserModal-tran").modal("show");

            }).fail(function(){

            });
			
            
        });

        $("#editBtn-tran").click(function(){
        
            $.post("/car/edit",$("#editUserForm-tran").serialize()).done(function(result){
                if(result == "success") {
                    $("#editUserModal-tran").modal("hide");
                    dt.ajax.reload();
                    window.location.reload(); 
                }
            }).fail(function(){
                alert("Modify user exception");
            });

        });

         $("#cond-file-upload-tran").click(function(){
             UploadFile("cond-input-form-tran","cond_file","/car/addByExcel",'.bs-example-modal-input')
         });
		}
	})()
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
































});