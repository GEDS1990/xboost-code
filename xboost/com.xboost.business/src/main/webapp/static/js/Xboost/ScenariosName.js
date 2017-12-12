$(function  () {
	var doc = document;
	
	
	
	
	
	
	/*
	 * 选择上传文件
	 */
	$("body").on("click",".cond-file-btn",function  () {
		var _file = $(this).parent(".cond-file-box").prev();
		var _nextP = $(this).next();
		_file.trigger("click");
		_file.change(function  () {
			var _index = _file.val().lastIndexOf("\\")+1;
	    	var _key = _file.val().slice(_index);
			_nextP.text(_key);
		});
	});
	/*
	 *上传文件 函数
	 * formId 表单id ,
	 * inpClass 对应的文件的input类名,
	 * url 请求地址，
	 * modId ,模态框id
	 * */
	function UploadFile (formID,inpClass,urls,modId) {
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
             url:urls,
             type:"post",
             data:form,
             processData:false,
             contentType:false,
             success:function(data){
                 //alert("Import information to complete!");
                 $(modId).modal("hide");
                 window.location.reload(); 
             },
             error:function(e){
                 //alert("Mistake!!");
                 window.clearInterval(timer);
             }
         });
         //此处为上传文件的进度条get();
	}
	
	/*
	 *检测form 表单的input 是否为空
	 */
	function FormInput (id,fun) {
		var _input = doc.getElementById(id).getElementsByTagName("input"),
		pattern = new RegExp("[`~!@#$^&*=|{}':;',\\[\\]<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]"),
		len = _input.length;
		for (var i=0;i<len;i++) {
			var xss = pattern.test(_input[i].value);
			console.log(xss)
			if (_input[i].value == "" || xss == true) {
				_input[i].focus();
				_input[i].classList.add("active");
				return false;
			}else{
				_input[i].classList.remove("active");
			}
		}
		fun();
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
			var href = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Depots Info.xlsx";
			$('.down-href').attr("href",href);
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
	                {"data":function  (row) {
	                	return "<span>"+(row.siteNightDelivery==1?'support':'not support')+"</span>"
	                },"name":"site_night_delivery"},
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
	                    "targets":[0,13],
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
        	
        	//阻止表单提交
			var formadd = doc.getElementById('newUserForm'),
			formedit = doc.getElementById('editUserForm');
			formadd.addEventListener("submit",function  (event) {
				event.preventDefault();
			});
			formedit.addEventListener("submit",function  (event) {
				event.preventDefault();
			});
			
			
	        //添加新用户
	        $("#addNewUser-info").click(function(){
	            $("#newUserModal").modal('show');
	        });
	        $("#saveBtn").click(function(){
	        	FormInput("newUserForm",function  () {
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
	        	FormInput("editUserForm",function  () {
	        		$.post("/siteInfo/edit",$("#editUserForm").serialize()).done(function(result){
		                if(result == "success") {
		                    $("#editUserModal").modal("hide");
		                    dt.ajax.reload();
		                    window.location.reload(); 
		                }
		            }).fail(function(){
		                //alert("Modify user exception");
		            });
	        	})
	            
	
	        });
	        
	        //导入excel 表格
	        $('#cond-file-upload-info').click(function  () {
				UploadFile("cond-input-form-info","cond_file","/siteInfo/addByExcel",'.bs-example-modal-input')
			});

			 //导出excel表格
            $('.export-btn').click(function  () {
                var _xls = $(this).attr('data-xls');
                if (_xls) {
                    window.location.href='/siteInfo/exportExcel';
                }
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
			var href = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Depots Distance.xlsx";
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
	                {"data":"carType","name":"car_type"},
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
	                    "visible":true
	                },
	                {
	                    "targets":[0,5],
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
	            },
	            "initComplete": function (settings, data) {
	            	//console.log(data)
	            },
	            "drawCallback":function  (settings, data) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        console.log( api.rows( {page:'current'} ).data() );
	            }
	        });
        
		//阻止表单提交
		var formadd = doc.getElementById('newUserForm-dist'),
		formedit = doc.getElementById('editUserForm-dist');
		formadd.addEventListener("submit",function  (event) {
			event.preventDefault();
		});
		formedit.addEventListener("submit",function  (event) {
			event.preventDefault();
		});
        //添加新用户
        $("#addNewUser-dist").click(function(){
            $("#newUserModal-dist").modal('show');
        });
        
        $("#saveBtn-dist").click(function(){
        	FormInput("newUserForm-dist",function  () {
        		$.post("/siteDist/add",$("#newUserForm-dist").serialize())
                    .done(function(result){
                        if("success" == result) {
                            $("#newUserForm-dist")[0].reset();
                            $("#newUserModal-dist").modal("hide");
                            dt.ajax.reload();
                            window.location.reload(); 
                        }
                    }).fail(function(){
                        console.log("fail");
                    });
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
        	
        	FormInput("editUserForm-dist",function  () {
        		
        		$.post("/siteDist/edit",$("#editUserForm-dist").serialize()).done(function(result){
	                if(result == "success") {
	                    $("#editUserModal-dist").modal("hide");
	                    dt.ajax.reload();
	                    window.location.reload(); 
	                }
	        	}).fail(function(){
	                console.log("fail");
	            });
        	})
            

        });
		
		//导入excel 表格
         $("#cond-file-upload-dist").click(function(){
         	UploadFile("cond-input-form-dist","cond_file","/siteDist/addByExcel",'.bs-example-modal-input')
         });	
         
        //导出excel表格
        $('.export-btn').click(function  () {
        	var _xls = $(this).attr('data-xls');
        	if (_xls) {
        	window.location.href='/siteDist/exportExcel';
        	}
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
			var href = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Demands.xlsx";
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
	                {"data":"durationStart","name":"duration_start"},
	                {"data":"siteCodeDelivery","name":"site_code_delivery"},
	                {"data":"durationEnd","name":"duration_end"},
	                {"data":"votes","name":"votes"},
	                {"data":"weight","name":"weight"},
	                {"data":"productType","name":"product_type"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='editLink-dem' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-dem' data-id='"+row.id+"'>Del</a>";
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	                {
	                    "targets":[0],
	                    "visible":true
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
	        
	        //阻止表单提交
			var formadd = doc.getElementById('newUserForm-dem'),
			formedit = doc.getElementById('editUserForm-dem');
			formadd.addEventListener("submit",function  (event) {
				event.preventDefault();
			});
			formedit.addEventListener("submit",function  (event) {
				event.preventDefault();
			});
	        
        

	        //添加新用户
	        $("#addNewUser-dem").click(function(){
	            $("#newUserModal-dem").modal('show');
	        });
	        $("#saveBtn-dem").click(function(){
	        	FormInput("newUserForm-dem",function  () {
	        		$.post("/demandInfo/add",$("#newUserForm-dem").serialize())
	                    .done(function(result){
	                        if("success" == result) {
	                            $("#newUserForm-dem")[0].reset();
	                            $("#newUserModal-dem").modal("hide");
	                            dt.ajax.reload();
	                            window.location.reload(); 
	                        }
	                    }).fail(function(){
	                        console.log("fail");
	                    });
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
	        	FormInput("editUserForm-dem",function  () {
	        		$.post("/demandInfo/edit",$("#editUserForm-dem").serialize()).done(function(result){
		                if(result == "success") {
		                    $("#editUserModal-dem").modal("hide");
		                    dt.ajax.reload();
		                    window.location.reload(); 
		                }
		            }).fail(function(){
		                console.log("fail");
		            });
	        	})
	            
	
	        });
	
	        $("#cond-file-upload-dem").click(function(){
	             UploadFile("cond-input-form-dem","cond_file","/demandInfo/addByExcel",'.bs-example-modal-input')
	         });


         //导出excel表格
         $('.export-btn').click(function  () {
             var _xls = $(this).attr('data-xls');
             if (_xls) {
             window.location.href='/demandInfo/exportExcel';
             }
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
			var href = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Parameters.xlsx";
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
	                    "visible":true
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
	     
        	//阻止表单提交
			var formadd = doc.getElementById('newUserForm-pata'),
			formedit = doc.getElementById('editUserForm-pata');
			formadd.addEventListener("submit",function  (event) {
				event.preventDefault();
			});
			formedit.addEventListener("submit",function  (event) {
				event.preventDefault();
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
	        //导入excel表格
	         $("#cond-file-upload-pata").click(function(){
	             UploadFile("cond-input-form-pata","cond_file","/modelArg/addByExcel",'.bs-example-modal-input')
	         });

	         //导出excel表格
             $('.export-btn').click(function  () {
                var _xls = $(this).attr('data-xls');
                if (_xls) {
                window.location.href='/modelArg/exportExcel';
                }
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
			var href = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Vehicles.xlsx";
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
	                
	                {"data":"maxStop","name":"max_stop"},
	                {"data":"dimensions","name":"dimensions"},
	                
	                {"data":"max_distance","name":"max_distance"},
	                {"data":"max_running_time","name":"max_running_time"},
	                {"data":"velocity","name":"velocity"},
	                {"data":"durationUnloadFull","name":"duration_unload_full"},
	                {"data":"start_location","name":"start_location"},
	                {"data":"end_location","name":"end_location"},
	                {"data":"a1","name":"a1"},
	                {"data":"a2","name":"a2"},
                    {"data":"costa1","name":"costa1"},
                    {"data":"costa2","name":"costa2"},
                    {"data":"costa2","name":"costa2"},
	                {"data":"b1","name":"b1"},
	                {"data":"b2","name":"b2"},
                    {"data":"costb1","name":"costb1"},
                    {"data":"costb2","name":"costb2"},
                    {"data":"costb2","name":"costb2"},
	                {"data":"c1","name":"c1"},
	                {"data":"c2","name":"c2"},
                    {"data":"costc1","name":"costc1"},
                    {"data":"costc2","name":"costc2"},
                    {"data":"costc2","name":"costc2"},
                    {"data":"d1","name":"d1"},
                    {"data":"d2","name":"d2"},
	                {"data":"costd1","name":"costd1"},
	                {"data":"costd2","name":"costd2"},
	                {"data":"costd2","name":"costd2"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='editLink-tran' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-tran' data-id='"+row.id+"'>Del</a>";
	                }},
	            ],
	            "columnDefs":[ //具体列的定义
	                {
	                    "targets":[0],
	                    "visible":true
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
        
        	//阻止表单提交
			var formadd = doc.getElementById('newUserForm-tran'),
			formedit = doc.getElementById('editUserForm-tran');
			formadd.addEventListener("submit",function  (event) {
				event.preventDefault();
			});
			formedit.addEventListener("submit",function  (event) {
				event.preventDefault();
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
	                $("#carType").val(result.type);
	                $("#dimensions").val(result.dimensions);
	                $("#maxLoad").val(result.maxLoad);
	                $("#durationUnloadFull").val(result.durationUnloadFull);
	                $("#maxStop").val(result.maxStop);
	                $("#fixedRound").val(result.fixed_round);
	                $("#fixedRoundFee").val(result.fixed_round_fee);
	                $("#startLocation").val(result.start_location);
	                $("#endLocation").val(result.end_location);
	                $("#maxDistance").val(result.max_distance);
	                $("#maxRunningTime").val(result.max_running_time);
	                $("#costPerDistance").val(result.cost_per_distance);
	                $("#costPerTime").val(result.cost_per_time);
	                $("#fixedCost").val(result.fixed_cost);
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
	        //导入excel表格
	         $("#cond-file-upload-tran").click(function(){
	             UploadFile("cond-input-form-tran","cond_file","/car/addByExcel",'.bs-example-modal-input')
	         });

	         //导出excel表格
             $('.export-btn').click(function  () {
                var _xls = $(this).attr('data-xls');
                if (_xls) {
                window.location.href='/car/exportExcel';
                }
             });
		}
	})(),
	
	
	
	/*
	 *MyScenarios.jsp == MyScenariosController
	 * 
	 * */
	(function  () {
		
		var MyScenarios = doc.getElementById("MyScenarios");
		if (MyScenarios) {
			var href = "http://"+document.location.host+"/static/excelTemplate/Template - Scenario.xlsx";
			$('.down-href').attr("href",href);
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
	                    "targets":[1,5,6],
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
	        
	        //阻止表单提交
			var formCreate = doc.getElementById('newUserForm-scen');
			formCreate.addEventListener("submit",function  (event) {
				event.preventDefault();
			});
	    
        
	        //点击open 打开场景
	        $("body").on("click",".openLink-scen",function  () {
	        	var $this = $(this);
	        	var openScenariosId = $this.attr("data-scenariosid");
	        	var scenName = $this.parent("td").parent("tr").find("td").eq(1).text();
	        	$.post("/MyScenarios/open",{"openScenariosId":openScenariosId,"openScenariosName":scenName}).done(function  (res) {
	        		if (res == "success") {
	        			$('#scen-name').remove();
	        			$('#scen-class').remove();
	        			var add = "";
						add+='<li class="xb-hover" id="scen-name">';
						add+='<a href="/ScenariosName" class="nav_xb" id="xb-nav-xb">';
						add+='<span id="xb_nav_span" class="glyphicon glyphicon-triangle-bottom"></span>';
						//add+='<span class="icon alt1 alt icon-file-text-o"></span>'+scenName+'</a><span id="scen-name-close" class="icon alt2 glyphicon glyphicon-remove"></span></li>';
						add+='<span class="icon alt1 alt icon-file-text-o"></span>'+scenName+'</a></li>';
						add+='<li><ul class="xb-nav_ul" id="scen-class">';
						add+='<li id="nav-Conditions"><a href="/siteInfo"><span class="icon-item alt icon-document-add"></span>Settings</a></li>';
						add+='<li id="nav-Simualt"><a href="/simualte"><span class="icon-item alt icon-play"></span>Simulate</a></li>';
						add+='<li id="nav-Results"><a href="/depots"><span class="icon-item alt icon-document-checked"></span>Results</a></li></ul></li>';
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
	                    //alert("Delete exception");
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
    	 *AllScenarios.jsp == MyScenariosController
    	 *
    	 * */
    	(function  () {

    		var AllScenarios = doc.getElementById("AllScenarios");
    		if (AllScenarios) {
//    			var href = "http://"+document.location.host+"/static/excelTemplate/Template - Scenario.xlsx";
//    			$('.down-href').attr("href",href);
    			//加载列表
    			var dt =$("#AllScenarios").DataTable({
    	            "processing": true, //loding效果
    	            "serverSide":true, //服务端处理
    	            "searchDelay": 1000,//搜索延迟
    	            "order":[[0,'desc']],//默认排序方式
    	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
    	            "ajax":{
    	                url:"MyScenarios/allscenarios.json", //获取数据的URL
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
    	                    return "<a href='javascript:;' class='openLink-scen' data-scenariosid='"+row.id+"'>Open</a> <a href='javascript:;' class='sendToLink' data-scenariosId='"+row.id+"'>SendTo</a> <a href='javascript:;' class='delLink-scen' data-scenariosid='"+row.id+"'>Delete</a>";
    	                }}
    	            ],
    	            "columnDefs":[ //具体列的定义
    	            	{
    	                    "targets":[1,5,6],
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

    	        //阻止表单提交
    			var formCreate = doc.getElementById('newUserForm-scen');
    			formCreate.addEventListener("submit",function  (event) {
    				event.preventDefault();
    			});

    			//sendTo modal
    			$("body").on("click",".sendToLink",function  () {
    	        	var $this = $(this);
    	        	var scenariosId = $this.attr("data-scenariosId");
    	        	$("#scenariosId").val(scenariosId);
                    $("#sendToModal").modal('show');
                });
                //sendTo controller
    			$("body").on("click","#sendToBtn",function  () {
                    debugger;
                    var userId = $("#userId").val();
                    var scenariosId = $("#scenariosId").val();
                    $.post("/MyScenarios/sendto",{"scenariosId":scenariosId,"userId":userId}).done(function(result){
                    debugger;
                    window.location.href = "/MyScenarios/AllScenarios";
                    }).fail(function(e){
debugger;
                    });
debugger;
                    $("#sendToModal").modal('hide');
                });


    	        //点击open 打开场景
    	        $("body").on("click",".openLink-scen",function  () {
    	        	var $this = $(this);
    	        	var openScenariosId = $this.attr("data-scenariosid");
    	        	var scenName = $this.parent("td").parent("tr").find("td").eq(1).text();
    	        	$.post("/MyScenarios/open",{"openScenariosId":openScenariosId,"openScenariosName":scenName}).done(function  (res) {
    	        		if (res == "success") {
    	        			$('#scen-name').remove();
    	        			$('#scen-class').remove();
    	        			var add = "";
    						add+='<li class="xb-hover" id="scen-name">';
    						add+='<a href="/ScenariosName" class="nav_xb" id="xb-nav-xb">';
    						add+='<span id="xb_nav_span" class="glyphicon glyphicon-triangle-bottom"></span>';
    						//add+='<span class="icon alt1 alt icon-file-text-o"></span>'+scenName+'</a><span id="scen-name-close" class="icon alt2 glyphicon glyphicon-remove"></span></li>';
    						add+='<span class="icon alt1 alt icon-file-text-o"></span>'+scenName+'</a></li>';
    						add+='<li><ul class="xb-nav_ul" id="scen-class">';
    						add+='<li id="nav-Conditions"><a href="/siteInfo"><span class="icon-item alt icon-document-add"></span>Settings</a></li>';
    						add+='<li id="nav-Simualt"><a href="/simualte"><span class="icon-item alt icon-play"></span>Simulate</a></li>';
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
    	                    //alert("Delete exception");
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
	
	
	    /**
    	 * account.jsp
    	 *
    	 */
    	(function  () {
        var dt = $("#userTable").DataTable({
            "processing": true, //loding效果
            "serverSide":true, //服务端处理
            "searchDelay": 1000,//搜索延迟
            "order":[[0,'desc']],//默认排序方式
            "lengthMenu":[5,10,25,50,100],//每页显示数据条数菜单
            "ajax":{
                url:"/account/users.json", //获取数据的URL
                type:"get" //获取数据的方式
            },
            "columns":[  //返回的JSON中的对象和列的对应关系
                {"data":"id","name":"id"},
                {"data":"username"},
                {"data":"tel"},
                {"data":function(row){
                    if(row.state == "禁用") {
                        return "<span class='label label-danger'>"+row.state+"</span>";
                    } else {
                        return row.state;
                    };
                }},
                {"data":function(row){
                    var roleName = "";
                    for(var i = 0;i < row.roleList.length;i++) {
                        var role = row.roleList[i];
                        roleName = roleName + role.rolename + "&nbsp&nbsp";
                    }
                    return roleName;
                }},
                {"data":function(row){
                    return "<a href='javascript:;' class='editLink' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink' data-id='"+row.id+"'>Delete</a>";
                }}
            ],
            "columnDefs":[ //具体列的定义
                {
                    "targets":[0],
                    "visible":true
                },
                {
                    "targets":[3],
                    "orderable":false
                },
                {
                    "targets":[1,2,4,5],
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
        $("#addNewUser-user").click(function(){
            $("#newUserModal-user").modal('show');
        });
        $("#saveBtn").click(function(){
            $.post("/account/new",$("#newUserForm-user").serialize())
                    .done(function(result){
                        if("success" == result) {
                        debugger;
                            $("#newUserForm-user")[0].reset();
                            $("#newUserModal-user").modal("hide");
                            dt.ajax.reload();
                        }
                    }).fail(function(){
                        alert("添加时出现异常");
                    });

        });

        //删除用户
        $(document).delegate(".delLink","click",function(){
            var id = $(this).attr("data-id");
            if(confirm("确定要删除该数据吗?")) {
                $.post("/account/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                    }
                }).fail(function(){
                    alert("删除出现异常");
                });

            }
        });

        //编辑用户
        $(document).delegate(".editLink","click",function(){
            $("#editUserForm-user")[0].reset();
            var id = $(this).attr("data-id");
            $.get("/account/user.json",{"id":id}).done(function(result){
                $("#id").val(result.id);
                $("#userName").val(result.username);
                $("#tel").val(result.tel);

                $(".role").each(function(){
                    var roleList = result.roleList;
                    for(var i = 0;i < roleList.length;i++) {
                        var role = roleList[i];
                        if($(this).val() == role.id) {
                            $(this)[0].checked = true;
                        }
                    }
                });

                if(result.state == "正常") {
                    $("#ok")[0].checked = true;
                } else {
                    $("#disable")[0].checked = true;
                }



            }).fail(function(){

            });

            $("#editUserModal-user").modal("show");
        });

        $("#editBtn").click(function(){

            $.post("/account/edit",$("#editUserForm-user").serialize()).done(function(result){
                if(result == "success") {
                    $("#editUserModal-user").modal("hide");
                    dt.ajax.reload();
                }
            }).fail(function(){
                alert("修改用户异常");
            });

        });
    })()


});