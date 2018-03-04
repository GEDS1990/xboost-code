$(function  () {
	var doc = document;
	
	
	
	
	
	
	/*
	 * 选择上传文件
	 */
	$("body").on("click",".cond-file-btn",function  () {
		$('.import-error').text("").hide();
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
		var _val = $('input[name="file"]').val();
		if ( !Boolean(_val) ) {
			return false;
		}
		$('.loading').show();
         $.ajax({
             url:urls,
             type:"post",
             data:form,
             processData:false,
             contentType:false,
             success:function(data){
                 //alert("Import information to complete!");
                 $(modId).modal("hide");
                 $('.loading').hide();
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
		pattern = new RegExp("[`~!@#$^&*=|{}':;',\\[\\]<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
		len = _input.length;
		for (var i=0;i<len;i++) {
			var xss = pattern.test(_input[i].value);
			var _inputType = _input[i].getAttribute("required");
			var _inputHid = _input[i].getAttribute("type");
			////console.log(_inputType+"----------"+_inputHid)
			////console.log(xss)
			if (Boolean(_inputType) == true  ) {
				if (_input[i].value == "" || xss == true) {
					_input[i].focus();
					_input[i].classList.add("active");
					return false;
				}else{
					_input[i].classList.remove("active");
				}
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
	    	form.push(false);
	    	$('.import-error').text("The file format of"+" "+fileName+" "+"is inconsistent  .xlsx").show();
	    	//alert("The file format of"+" "+fileName+" "+"is inconsistent  .xlsx")
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
	            "order":[[0,'asc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/siteInfo/siteInfo.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"siteCode","name":"site_code"},
	                {"data":function (res){
	                	return Number(res.siteLongitude).toFixed(6);
	                },"name":"longitude"},
	                {"data":function (res){
	                	return Number(res.siteLatitude).toFixed(6);
	                },"name":"latitude"},
	                {"data":"siteName","name":"site_name"},
	                {"data":function  (res) {
	                	var info = res.siteAddress;
	                	if (info.length > 26)
	                	{
	                		info = info.slice(0,26);
	                	}
	                	return info;
	                },"name":"site_address"},
	                {"data":function(res){
	                	if (res.siteArea == 0 || res.siteArea == "999") {
	                		return "--"
	                	}
	                	return Math.round(res.siteArea);
	                }
	                ,"name":"site_area"},
	                {"data":"siteType","name":"site_type"},
	                {"data":function (res){
	                	if (res.distribCenter == "" || $.trim(res.distribCenter) == "") {
	                		return "--"
	                	}
	                	return res.distribCenter;
	                },"name":"distrib_center"},
	                {"data":function  (row) {
	                	return "<span style='color:grey;'>"+row.siteNightDelivery+"</span>";
	                },"name":"site_night_delivery"},
	                {"data":function (res){
	                	if (res.noOfTruckLimitation >= 999 ) {
	                		return "∞";
	                	}
	                	return res.noOfTruckLimitation;
	                },"name":"no_of_truck_limitation"},
                    {"data":function (res){
                        if (res.noOfBaiduLimitation >= 999 ) {
                            return "∞";
                        }
                        return res.noOfBaiduLimitation;
                    },"name":"no_of_baidu_limitation"},
                    {"data":function (res){
                        if (res.noOfDidiLimitation >= 999 ) {
                            return "∞";
                        }
                        return res.noOfDidiLimitation;
                    },"name":"no_of_didi_limitation"},
                    {"data":function (res){
                        if (res.noOfDadaLimitation >= 999 ) {
                            return "∞";
                        }
                        return res.noOfDadaLimitation;
                    },"name":"no_of_dada_limitation"},
                    {"data":function (res){
                        if (res.reserve >= 999 ) {
                            return "∞";
                        }
                        return res.reserve;
                    },"name":"reserve"},
	                {"data":function (res){
	                	if (res.largeCarModel >= 999) {
	                		return "<span style='color:grey;'>∞</span>";
	                	}
	                	return "<span style='color:grey;'>"+res.largeCarModel+"</span>";
	                },"name":"large_carModle"},
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
	                $("#noOfTruckLimitation").val(result.noOfTruckLimitation);
	                $("#noOfBaiduLimitation").val(result.noOfBaiduLimitation);
	                $("#noOfDidiLimitation").val(result.noOfDidiLimitation);
	                $("#noOfDadaLimitation").val(result.noOfDadaLimitation);
	                $("#reserve").val(result.reserve);
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
                $(".modal-header span").trigger('click');
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
	            "order":[[0,'asc']],//默认排序方式
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
	                {"data":function(res){
	                	return res.carDistance.toFixed(2)
	                },"name":"car_distance"},
	                {"data":function (res){
	                	if ( !Boolean(res.durationNightDelivery) ) {
	                		return "--"
	                	}
	                	return Number(res.durationNightDelivery).toFixed(2);
	                },"name":"duration_night_delivery"},
	                {"data":function (res){
	                	if ( !Boolean(res.durationNightDelivery2) ) {
	                		return "--"
	                	}
	                	return Number(res.durationNightDelivery2).toFixed(2);
	                },"name":"duration_night_delivery2"},
	                {"data":function (res){
	                	if ( !Boolean(res.durationNightDelivery3) ) {
	                		return "--"
	                	}
	                	return Number(res.durationNightDelivery3).toFixed(2);
	                },"name":"duration_night_delivery3"},
	                {"data":function (res){
	                	if ( !Boolean(res.durationNightDelivery4) ) {
	                		return "--"
	                	}
	                	return Number(res.durationNightDelivery4).toFixed(2);
	                },"name":"duration_night_delivery3"},
	                {"data":function (res){
	                	if ( !Boolean(res.durationNightDelivery5) ) {
	                		return "<span style='color:grey;'>--</span>";
	                	}
	                	return "<span style='color:grey;'>"+Number(res.durationNightDelivery5).toFixed(2)+"</span>";
	                },"name":"duration_night_delivery3"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='editLink-dist' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-dist' data-id='"+row.id+"'>Del</a>";
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	            	{
	                    "targets":[0,1],
	                    "visible":false
	                },
	                {
	                    "targets":[0,10],
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
	            	////console.log(data)
	            },
	            "drawCallback":function  (settings, data) {
	            	var api = this.api();
			        // 输出当前页的数据到浏览器控制台
			        ////console.log( api.rows( {page:'current'} ).data() );
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
                        //console.log("fail");
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
            	console.log(result)
                $("#siteId-dist").val(result.id);
                $("#carType").val(result.carType);
                $("#siteCollect").val(result.siteCollect);
                $("#siteDelivery").val(result.siteDelivery);
                $("#carDistance").val(result.carDistance);
                $("#durationNightDelivery").val(result.durationNightDelivery);
                $("#durationNightDelivery2").val(result.durationNightDelivery2);
                $("#durationNightDelivery3").val(result.durationNightDelivery3);
                $("#durationNightDelivery4").val(result.durationNightDelivery4);
                $("#durationNightDelivery5").val(result.durationNightDelivery5);
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
	                //console.log("fail");
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
            $(".modal-header span").trigger('click');
        }); 
         
         
		}
		
	}());
	/*
	 *Demands.jsp == DemandInfoController
	 * 
	 * */
	//求时间
	function operationTime (data) {
		var result = parseInt(data),
	    	h = parseInt(result/60),
	    	m = result%60;
	    	return add0(h)+":"+add0(m);
	}
	function add0(m){return m<10?'0'+m:m };
	(function  () {
		var Demands = doc.getElementById("Demands");
		if (Demands) {
			var href = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Shipments.xlsx";
			$('.down-href').attr("href",href);
			var dt =$("#Demands").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'asc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/demandInfo/demandInfo.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"siteCodeCollect","name":"site_code_collect"},
	                {"data":function (res){
	                	return operationTime(res.durationStart);
	                },"name":"duration_start"},
	                {"data":"siteCodeDelivery","name":"site_code_delivery"},
	                {"data":function (res){
	                	return operationTime(res.durationEnd);
	                },"name":"duration_end"},
	                {"data":"votes","name":"votes"},
	                {"data":function (res){
	                	if (res.weight == null || res.weight == "" ||  res.weight == ' ' || res.weight == 0) {
	                		return "<span style='color:grey;'>-</span>";
	                	}
	                	return "<span style='color:grey;'>"+res.weight+"</span>";
	                },"name":"weight"},
	                {"data":function (res){
	                	if (res.productType == null || res.productType == "" ||  res.productType == ' ' ||  res.productType == 0) {
	                		return "<span style='color:grey;'>-</span>";
	                	}
	                	return "<span style='color:grey;'>"+res.productType+"</span>";
	                },"name":"product_type"},
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
	                    "targets":[0,8],
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
	                        //console.log("fail");
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
		                //console.log("fail");
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
             $(".modal-header span").trigger('click');
         });
		}
	})(),
	/**
	 * patameters.jsp = modelController
	 * 
	 */
	(function  () {
		var Patameters_section = document.getElementById("Patameters-section");
		if (Patameters_section) {
			var href = "http://"+document.location.host+"/static/excelTemplate/Template - Settings - Parameters.xlsx";
			$('.down-href').attr("href",href);
			/*
			var dt =$("#Patameters").DataTable({
	            "processing": true, //loding效果
	            "serverSide":true, //服务端处理
	            "searchDelay": 1000,//搜索延迟
	            "order":[[0,'asc']],//默认排序方式
	            "lengthMenu":[15,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/modelArg/modelArg.json", //获取数据的URL
	                type:"get" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":function (row){
	                	////console.log(row)
	                	if (row.modelType == 1 || row.modelType == 2) {
	                		if (row.parameterCode == "duration_relay" || row.parameterCode == "duration_peak" || row.parameterCode == "tansit_site_peak" || row.parameterCode == "duration_collect" || row.parameterCode == "duration_siteEnd_sort") {
		                		return "<span style='color:lightgrey;'>"+row.parameterName+"</span>";
		                	}
		                	return row.parameterName;
	                	}else if (row.modelType == 3) {
	                		if (row.parameterCode == "duration_peak" || row.parameterCode == "tansit_site_peak" || row.parameterCode == "duration_collect" || row.parameterCode == "duration_siteEnd_sort") {
		                		return "<span style='color:lightgrey;'>"+row.parameterName+"</span>";
		                	}
		                	return row.parameterName;
	                	}
	                	
	                },"name":"parameter_name"},
	                {"data":function  (row) {
	                	if (row.modelType == 1 || row.modelType == 2) {
	                		if (row.parameterCode == "duration_relay" || row.parameterCode == "duration_peak" || row.parameterCode == "tansit_site_peak" || row.parameterCode == "duration_collect" || row.parameterCode == "duration_siteEnd_sort") {
		                		return "<span style='color:lightgrey;'>"+row.data+"</span>";
		                	}
		                	return row.data;
	                	}else if (row.modelType == 3) {
	                		if (row.parameterCode == "duration_peak" || row.parameterCode == "tansit_site_peak" || row.parameterCode == "duration_collect" || row.parameterCode == "duration_siteEnd_sort") {
		                		return "<span style='color:lightgrey;'>"+row.data+"</span>";
		                	}
		                	return row.data;
	                	}
	                },"name":"data"},
	                {"data":function (row){
	                	if (row.modelType == 1 || row.modelType == 2) {
	                		if (row.parameterCode == "duration_relay" || row.parameterCode == "duration_peak" || row.parameterCode == "tansit_site_peak" || row.parameterCode == "duration_collect" || row.parameterCode == "duration_siteEnd_sort") {
		                		return "<span style='color:lightgrey;'>"+row.note+"</span>";
		                	}
		                	return row.note;
	                	}else if (row.modelType == 3) {
	                		if (row.parameterCode == "duration_peak" || row.parameterCode == "tansit_site_peak" || row.parameterCode == "duration_collect" || row.parameterCode == "duration_siteEnd_sort") {
		                		return "<span style='color:lightgrey;'>"+row.note+"</span>";
		                	}
		                	return row.note;
	                	}
	                },"name":"note"},
	                {"data":function(row){
	                	if (row.modelType == 1 || row.modelType == 2) {
	                		if (row.parameterCode == "duration_relay" || row.parameterCode == "duration_peak" || row.parameterCode == "tansit_site_peak" || row.parameterCode == "duration_collect" || row.parameterCode == "duration_siteEnd_sort") {
		                		return "";
		                	}
		                	return "<a href='javascript:;' class='editLink-pata' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-pata' data-id='"+row.id+"'>Del</a>";
	                	}else if (row.modelType == 3) {
	                		if (row.parameterCode == "duration_peak" || row.parameterCode == "tansit_site_peak" || row.parameterCode == "duration_collect" || row.parameterCode == "duration_siteEnd_sort") {
		                		return "";
		                	}
		                	return "<a href='javascript:;' class='editLink-pata' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-pata' data-id='"+row.id+"'>Del</a>";
	                	}
	                	
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	                {
	                    "targets":[0],
	                    "visible":false
	                },
	                {
	                    "targets":[4],
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
	            	var _val = $('#pata-model-type').val();
	            	var table = $('#Patameters').DataTable();
	            		setTimeout(function(){
	            			if (_val) {
	            				table.search(_val).draw();
	            			}
	            			
	            		},0)
	            }
	        });
	        */
	        
	        //点击选项搜索
	        $('#pata-model-type').change(function  () {
	        	var _val = $(this).val();
	        	var table = $('#Patameters').DataTable();
	        	table.search(_val).draw();
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
	        	FormInput("newUserForm-pata",function (){
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
	        	})
	            
	
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
					alert("fail");
	            });
				
	            
	        });
	
	        $("#editBtn-pata").click(function(){
	        	FormInput("editUserForm-pata",function (){
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
	            
	
	        });
	        
	        //查询Parameters参数
	        $.get('/modelArgs/modelArgs.json').done(function (row){
	        	//console.log(row)
	        	if (row.data.length != 0)
	        	{
	        		var res = row.data[0];
	        		$('#add-edit').val(1);
	        		$('#modelType').val(res.modelType);
		        	$('#durationTransfer').val(res.durationTransfer);
		        	$('#sitePeopleWork').val(res.sitePeopleWork);
		        	$('#distriPeopleWork').val(res.distriPeopleWork);
		        	$('#durationRelay1').val(res.durationRelay);
		        	$('#durationRelay').val(res.durationRelay);
	        	}
	        	else
	        	{
	        		$('#add-edit').val(0);
	        		$('#durationTransfer').val(10);
		        	$('#sitePeopleWork').val(300);
		        	$('#distriPeopleWork').val(500);
	        		$('#durationRelay1').val(120);
	        		$('#durationRelay').val(120);
	        	}
	        }).fail(function  () {
	        	console.log("fail");
	        });
	        
	        //检测是否输入
	        $('#form-para-setting input').blur(function  () {
        		var _val = $.trim($(this).val());
        		if (_val == "") 
        		{
        			$(this).parents('.form-group').find('p').css('display','block');
        		}
        		else
        		{
        			$(this).parents('.form-group').find('p').css('display','none');
        		}
        	});
	        
	        //用户手动输入
	        $('#js-save-para').click(function  () {
	        	var status = $('#add-edit').val();
	        	if (status == 0) 
	        	{
	        		var url_para = "/modelArgs/add";
	        	}
	        	else if (status == 1)
	        	{
	        		var url_para = "/modelArgs/edit";
	        	}
//	        	var modelType = $('#model-type').val();
//	        	var Waiting_Time = $('#durationTransfer').val();
//	        	var sort_depot = $('#sitePeopleWork').val();
//	        	var sort_dis_depot = $('#distriPeopleWork').val();
//	        	var Delivery_Window =  $('#durationRelay').val();
	        	var _input = doc.getElementById('form-para-setting').getElementsByTagName('input');
	        	var _p = doc.getElementById('form-para-setting').getElementsByTagName('p');
	        	var _len = _input.length;
	        	for (var i=0;i<_len;i++)
	        	{
	        		if ($.trim(_input[i].value) == "")
		        	{
		        		_p[i].style.display = "block";
		        		_input[i].focus();
		        		return false;
		        	}
	        	}
	        	//提交数据
	        	$.post(url_para,$('#form-para-setting').serialize()).done(function  (res) {
	        		if (res == "success")
	        		{
	        			window.location.reload();
	        		}
	        	}).fail(function  () {
	        		console.log("fail");
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
                 $(".modal-header span").trigger('click');
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
	            "order":[[0,'asc']],//默认排序方式
	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
	            "ajax":{
	                url:"/car/transport.json", //获取数据的URL
	                type:"post" //获取数据的方式
	            },
	            "columns":[  //返回的JSON中的对象和列的对应关系
	                {"data":"id","name":"id"},
	                {"data":"type","name":"type"},
	                {"data":"carSource","name":"car_source"},
	                {"data":function (res){
	                	if (res.num >= 99) {
	                		return "<span style='color:grey;'>∞</span>";
	                	}else{
	                		return "<span style='color:grey;'>"+res.num+"</span>";
	                	}
	                },"name":"num"},
	                {"data":function  (res) {
	                	if (res.maxStop >= 99) {
	                		return "∞";
	                	}else{
	                		return res.maxStop;
	                	}
	                },"name":"max_stop"},
	                {"data":"dimensions","name":"dimensions"},
	                
	                {"data":function(res){
		                if(res.max_distance > 998 ){
		                	return "∞";
		                }
		                return res.max_distance;
		            },"name":"max_distance"},
	                {"data":function(res){
	                    if(res.max_running_time > 998 ){
	                    	return "∞";
	                    }
	                    return res.max_running_time;
                    },"name":"max_running_time"},

	                {"data":function(res){
	                	return res.velocity;
	                },"name":"velocity"},
	                {"data":function(res){
	                	return res.velocity2;
	                },"name":"velocity2"},
	                {"data":function(res){
	                	return res.velocity3;
	                },"name":"velocity3"},
	                {"data":function (res){
	                	if (res.durationUnloadFull == null || res.durationUnloadFull == "" || res.durationUnloadFull == " " || res.durationUnloadFull == 0) {
	                		return "-";
	                	}
	                	return res.durationUnloadFull;
	                },"name":"duration_unload_full"},
	                {"data":function (res){
	                	if (res.start_location == null || res.start_location == "" || res.start_location == " " || res.start_location == 0) {
	                		return "<span style='color:grey;'>-</span>";
	                	}
	                	return "<span style='color:grey;'>"+res.start_location+"</span>";
	                },"name":"start_location"},
	                {"data":function (res){
	                	if (res.end_location == null || res.end_location == "" || res.end_location == " " || res.end_location == 0) {
	                		return "<span style='color:grey;'>-</span>";
	                	}
	                	return "<span style='color:grey;'>"+res.end_location+"</span>";
	                },"name":"end_location"},
	                {"data":"a1","name":"a1"},
	                {"data":function  (res) {
	                	if (res.a2 > 998) {
	                		return "∞";
	                	}
	                	return res.a2
	                },"name":"a2"},
                    {"data":"costa1","name":"costa1"},
                    {"data":"costa2","name":"costa2"},
                    {"data":function (res){
                    	var num = Number(res.costa3);
                    	if (num != 0) {
                    		return num.toFixed(2);
                    	}
                    	return res.costa3;
                    },"name":"costa3"},
	                {"data":"b1","name":"b1"},
	                {"data":function  (res) {
	                	if (res.b2 > 998) {
	                		return "∞";
	                	}
	                	return res.b2
	                },"name":"b2"},
                    {"data":"costb1","name":"costb1"},
                    {"data":"costb2","name":"costb2"},
                    {"data":function (res){
                    	var num = Number(res.costb3);
                    	if (num != 0) {
                    		return num.toFixed(2);
                    	}
                    	return res.costb3;
                    },"name":"costb3"},
	                {"data":"c1","name":"c1"},
	                {"data":function  (res) {
	                	if (res.c2 > 998) {
	                		return "∞";
	                	}
	                	return res.c2
	                },"name":"c2"},
                    {"data":"costc1","name":"costc1"},
                    {"data":"costc2","name":"costc2"},
                    {"data":function (res){
                    	var num = Number(res.costc3);
                    	if (num != 0) {
                    		return num.toFixed(2);
                    	}
                    	return res.costc3;
                    },"name":"costc3"},
                    {"data":"d1","name":"d1"},
                    {"data":function  (res) {
	                	if (res.d2 > 998) {
	                		return "∞";
	                	}
	                	return res.d2
	                },"name":"d2"},
	                {"data":"costd1","name":"costd1"},
	                {"data":"costd2","name":"costd2"},
	                {"data":function (res){
                    	var num = Number(res.costd3);
                    	if (num != 0) {
                    		return num.toFixed(2);
                    	}
                    	return res.costd3;
                    },"name":"costd3"},
	                {"data":"e1","name":"e1"},
                    {"data":function  (res) {
	                	if (res.e2 > 998) {
	                		return "∞";
	                	}
	                	return res.e2
	                },"name":"e2"},
	                {"data":"coste1","name":"coste1"},
	                {"data":"coste2","name":"coste2"},
	                {"data":function (res){
                    	var num = Number(res.coste3);
                    	if (num != 0) {
                    		return num.toFixed(2);
                    	}
                    	return res.coste3;
                    },"name":"coste3"},
	                {"data":"f1","name":"f1"},
                    {"data":function  (res) {
	                	if (res.f2 > 998) {
	                		return "∞";
	                	}
	                	return res.f2
	                },"name":"f2"},
	                {"data":"costf1","name":"costf1"},
	                {"data":"costf2","name":"costf2"},
	                {"data":function (res){
                    	var num = Number(res.costf3);
                    	if (num != 0) {
                    		return num.toFixed(2);
                    	}
                    	return res.costf3;
                    },"name":"costf3"},
	                {"data":function(row){
	                    return "<a href='javascript:;' class='editLink-tran' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-tran' data-id='"+row.id+"'>Del</a>";
	                }}
	            ],
	            "columnDefs":[ //具体列的定义
	                {
	                    "targets":[0,11],
	                    "visible":false
	                },
	                {
	                    "targets":[0,31],
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
	            "initComplete":function  (settings, data) {
	            	////console.log(data);
	            }
	        });
	        
	        //
        
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
	        //点击选框添加提示
	        function AddAttr ($this,type) {
	        	if (type == "/ride") {
	        		$this.parents('tr').find("td:first-child").find('input[type="number"]').attr({"required":"required","oninvalid":"setCustomValidity('Please enter information');","oninput":"setCustomValidity('');"});
	        		$this.parents('tr').find("td:nth-child(2)").find('span:first-child').find('input[type="number"]').attr({"required":"required","oninvalid":"setCustomValidity('Please enter information');","oninput":"setCustomValidity('');"});
	        		$this.parents('tr').find("td:nth-child(2)").find('.km-min').removeAttr('required').removeAttr('oninvalid').removeAttr('oninput');
	        		$this.parents('td').find(".km-min").attr("disabled","disabled").val("").addClass('active');
	        	}else if(type == "/km"){
	        		$this.parents('tr').find('input[type="number"]').attr({"required":"required","oninvalid":"setCustomValidity('Please enter information');","oninput":"setCustomValidity('');"});
	        		$this.parents('tr').nextAll().find('input[type="number"]').removeAttr('required').removeAttr('oninvalid').removeAttr('oninput');
	        		$this.parents('td').find(".km-min").removeAttr("disabled").removeClass('active');
	        	}
	        	
	        }
	        function rideFun ($this){
	        	var	dataname = $this.attr("data-name");
	        	$this.parents('span').next().find('input[value="/km"]').removeAttr("checked");
	        	$this.parents('tr').next().find('input[value="/ride"]').removeAttr('disabled').parent('label').css("color","#19a371");
	        	//$(this).parents('tr').nextAll().find('input[value="/km"]').removeAttr("checked");
	        	$this.parents("td").find("span:first-child").find("input").attr("name",dataname);
	        	AddAttr($this,"/ride");
	        };
	        function kmFun ($this){
	        	var	dataname = $this.attr("data-name");
	        	$this.parents('span').prev().find('input[value="/ride"]').removeAttr("checked");
	        	$this.parents('tr').nextAll().find('input[value="/ride"]').attr('disabled',"disabled").removeAttr("checked").parent('label').css("color","lightgrey");
	        	$this.parents("td").find("span:first-child").find("input").attr("name",dataname);
	        	AddAttr($this,"/km");
	        };
	        //点击选框
	        $('input[value="/ride"]').change(function(){
	        	var $this = $(this);
	        	rideFun($this);
	        });
	        $('input[value="/km"]').change(function(){
	        	var $this = $(this);
	        	kmFun($this);
	        });
	        
	        //点击 添加保存按钮
	        $("#saveBtn-tran").click(function(){
	        	FormInput("newUserForm-tran",function  () {
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
	            	////console.log(result)
	                $("#siteId-tran").val(result.id);
	                $("#type").val(result.type);
	                $("#carSource").val(result.carSource);
	                $("#num").val(result.num);
	                $("#maxStop").val(result.maxStop);
	                $("#maxLoad").val(result.maxLoad);
	                $("#dimensions").val(result.dimensions);
	                $("#max_distance").val(result.max_distance);
	                $("#max_running_time").val(result.max_running_time);
	                $("#velocity").val(result.velocity);
	                $("#velocity2").val(result.velocity2);
	                $("#velocity3").val(result.velocity3);
	                $("#durationUnloadFull").val(result.durationUnloadFull);
	                $("#start_location").val(result.start_location);
	                $("#end_location").val(result.end_location);
	                $("#a1").val(result.a1);
	                $("#a2").val(result.a2);
	                $("#costa1").val(result.costa1);
	                $("#b1").val(result.b1);
	                $("#b2").val(result.b2);
	                if (result.costb1 != " " && result.costb1 != "" && result.costb1 != null) {
	                	$('#costb').val(result.costb1);
	                	$('#costb').parents('td').find('input[value="/ride"]').attr("checked","checked");
	                	var $this = $('#costb').parents('td').find('input[value="/ride"]');
	                	rideFun($this);
	                	
	                }
	                if (result.costb2 != " " && result.costb2 != "" && result.costb2 != null) {
	                	$('#costb').val(result.costb2);
	                	$('#costb3').val(result.costb3);
	                	$('#costb').parents('td').find('input[value="/km"]').attr("checked","checked");
	                	var $this = $('#costb').parents('td').find('input[value="/km"]');
	                	kmFun($this);
	                }
	                $("#c1").val(result.c1);
	                $("#c2").val(result.c2);
	                if (result.costc1 != " " && result.costc1 != "" && result.costc1 != null) {
	                	$('#costc').val(result.costc1);
	                	$('#costc').parents('td').find('input[value="/ride"]').attr("checked","checked");
	                	var $this = $('#costc').parents('td').find('input[value="/ride"]');
	                	rideFun($this);
	                }
	                if (result.costc2 != " " && result.costc2 != "" && result.costc2 != null) {
	                	$('#costc').val(result.costc2);
	                	$('#costc3').val(result.costc3);
	                	$('#costc').parents('td').find('input[value="/km"]').attr("checked","checked");
	                	var $this = $('#costc').parents('td').find('input[value="/km"]');
	                	kmFun($this);
	                }
	                $("#d1").val(result.d1);
	                $("#d2").val(result.d2);
	                if (result.costd1 != " " && result.costd1 != "" && result.costd1 != null) {
	                	$('#costd').val(result.costd1);
	                	$('#costd').parents('td').find('input[value="/ride"]').attr("checked","checked");
	                	var $this = $('#costd').parents('td').find('input[value="/ride"]');
	                	rideFun($this);
	                }
	                if (result.costd2 != " " && result.costd2 != "" && result.costd2 != null) {
	                	$('#costd').val(result.costd2);
	                	$('#costd3').val(result.costd3);
	                	$('#costd').parents('td').find('input[value="/km"]').attr("checked","checked");
	                	var $this = $('#costd').parents('td').find('input[value="/km"]');
	                	kmFun($this);
	                }
	                $("#e1").val(result.e1);
	                $("#e2").val(result.e2);
	                if (result.coste1 != " " && result.coste1 != "" && result.coste1 != null) {
	                	$('#coste').val(result.coste1);
	                	$('#coste').parents('td').find('input[value="/ride"]').attr("checked","checked");
	                	var $this = $('#coste').parents('td').find('input[value="/ride"]');
	                	rideFun($this);
	                }
	                if (result.coste2 != " " && result.coste2 != "" && result.coste2 != null) {
	                	$('#coste').val(result.coste2);
	                	$('#coste3').val(result.coste3);
	                	$('#coste').parents('td').find('input[value="/km"]').attr("checked","checked");
	                	var $this = $('#coste').parents('td').find('input[value="/km"]');
	                	kmFun($this);
	                }
	                $("#f1").val(result.f1);
	                $("#f2").val(result.f2);
	                if (result.costf1 != " " && result.costf1 != "" && result.costf1 != null) {
	                	$('#costf').val(result.costf1);
	                	$('#costf').parents('td').find('input[value="/ride"]').attr("checked","checked");
	                	var $this = $('#costf').parents('td').find('input[value="/ride"]');
	                	rideFun($this);
	                }
	                if (result.costf2 != " " && result.costf2 != "" && result.costf2 != null) {
	                	$('#costf').val(result.costf2);
	                	$('#costf3').val(result.costf3);
	                	$('#costf').parents('td').find('input[value="/km"]').attr("checked","checked");
	                	var $this = $('#costf').parents('td').find('input[value="/km"]');
	                	kmFun($this);
	                }
	                
	                
	                $("#editUserModal-tran").modal("show");
	
	            }).fail(function(){
	
	            });
				
	            
	        });
	
	        $("#editBtn-tran").click(function(){
	        	//console.log($("#editUserForm-tran").serialize())
	        		FormInput("editUserForm-tran",function (){
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
                 $(".modal-header span").trigger('click');
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
	            "order":[[0,'asc']],//默认排序方式
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
                        var ulHtml = "";
                        ulHtml += "<a href='javascript:;' class='openLink-scen' data-scenariosid='"+row.id+"' data-scenariosname="+row.scenariosName+" data-status="+row.scenariosStatus+">Open</a> <a href='javascript:;' class='editLink-scen' data-scenariosid='"+row.id+"'>Export</a> <a href='javascript:;' class='delLink-scen' data-scenariosid='"+row.id+"'>Delete</a>";
//                        ulHtml += '<shiro:hasRole name="管理员">'
//                        ulHtml += " <a href='javascript:;' class='resLink-scen' data-scenariosid='"+row.id+"'>Restore</a>";
//                        ulHtml +='</shiro:hasRole>'
                        return ulHtml;
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
	            },
	            "initComplete": function (settings, data) {
	            	////console.log(data)
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
	        	var status = $this.attr("data-status");
	        	var openScenariosId = $this.attr("data-scenariosid");
	        	var scenName = $this.attr("data-scenariosname");
	        	if (status == "Simulating") 
	        	{
	        		$('#modal-siming').find('.modal-body p').text("The Simulation is running and can not restart the Simulation");
					$('#modal-siming').modal("show");
					return false;
	        	}
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
						add+='<li id="nav-Conditions"><a href="/siteInfo"><span class="icon-item alt icon-document-add"></span>Input</a></li>';
						add+='<li id="nav-Simualt"><a href="/simualte"><span class="icon-item alt icon-play"></span>Simulation</a></li>';
						add+='<li id="nav-Results"><a href="/depots"><span class="icon-item alt icon-document-checked"></span>Output</a></li></ul></li>';
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


	        //还原场景状态（管理员权限）
	        $(document).delegate(".resLink-scen","click",function(){
	            var id = $(this).attr("data-scenariosid");
	            $('#modal-res').modal("show")
	            $('#modal-resBtn').click(function  () {
	            	$.post("/MyScenarios/res",{"id":id}).done(function(result){
	                    if("success" == result) {
	                        dt.ajax.reload();
	                        window.location.reload();
	                    }
	                }).fail(function(){
	                    //alert("Delete exception");
	                });
	            })



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
    	            "order":[[0,'asc']],//默认排序方式
    	            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
    	            "ajax":{
    	                url:"/MyScenarios/allscenarios.json", //获取数据的URL
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
    	                    return "<a href='javascript:;' class='openLink-scen' data-scenariosid='"+row.id+"' data-scenariosname="+row.scenariosName+">Open</a> <a href='javascript:;' class='sendToLink' data-scenariosId='"+row.id+"'>SendTo</a> <a href='javascript:;' class='delLink-allscen' data-scenariosid='"+row.id+"'>Delete</a>";
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
                    var userId = $("#userId").val();
                    var scenariosId = $("#scenariosId").val();
                    $.post("/MyScenarios/sendto",{"scenariosId":scenariosId,"userId":userId}).done(function(result){
                    window.location.href = "/MyScenarios/AllScenarios";
                    }).fail(function(e){
                    });
                    $("#sendToModal").modal('hide');
                });


    	        //点击open 打开场景
    	        $("body").on("click",".openLink-scen",function  () {
    	        	var $this = $(this);
    	        	var openScenariosId = $this.attr("data-scenariosid");
    	        	var scenName = $this.attr("data-scenariosname");
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
    						add+='<li id="nav-Conditions"><a href="/siteInfo"><span class="icon-item alt icon-document-add"></span>Input</a></li>';
    						add+='<li id="nav-Simualt"><a href="/simualte"><span class="icon-item alt icon-play"></span>Simulation</a></li>';
    						add+='<li id="nav-Results"><a href="#"><span class="icon-item alt icon-document-checked"></span>Output</a></li></ul></li>';
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
    	        $(document).delegate(".delLink-allscen","click",function(){
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
    		var userTable_account = doc.getElementById('userTable-account');
    		if (userTable_account) 
    		{
		        var dt = $("#userTable-account").DataTable({
		            "processing": true, //loding效果
		            "serverSide":true, //服务端处理
		            "searchDelay": 1000,//搜索延迟
		            "order":[[0,'asc']],//默认排序方式
		            "lengthMenu":[10,25,50,100],//每页显示数据条数菜单
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
		                    return "<a href='javascript:;' class='editLink' data-id='"+row.id+"'>Edit</a> <a href='javascript:;' class='delLink-account' data-id='"+row.id+"'>Delete</a>";
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
		        $(document).delegate(".delLink-account","click",function(){
		            var id = $(this).attr("data-id");
		            $('#modal-user').modal("show");
		            $('#modal-userdelBtn').click(function  () {
		                $.post("/account/del",{"id":id}).done(function(result){
		                    if("success" == result) {
		                    $('#modal-user').modal("hide");
		                        dt.ajax.reload();
		                    }
		                }).fail(function(){
		                    alert("删除出现异常");
		                });
					});
		           	
		        });
		
		        //编辑用户
		        $(document).on(".editLink","click",function(){
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
		
						 $("#editUserModal-user").modal("show")
		
		            }).fail(function(){
		
		            });
		
		           ;
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
        
        	}
        
        
        
    })()


});