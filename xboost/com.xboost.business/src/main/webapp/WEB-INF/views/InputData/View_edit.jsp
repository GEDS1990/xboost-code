<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Xboost-物流规划管理系统</title>

    <!-- Bootstrap Core CSS -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="/static/js/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/static/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/static/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/static/js/morris/morris.css">
    <link rel="stylesheet" href="../../../static/css/xb_main.css" />

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

	<div id="wrapper">
	
	    <%@ include file="../include/nav.jsp"%>
	    <!-- Page Content -->
	    <div class="xb_page_content">
		    <div id="page-wrapper">
				<div class="modal-content" id="newInput">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">核心算法参数输入页</h4>
					</div>
					<div class="modal-body">
						<form id="newInputForm" class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-label">dist_mode</label>
								<div class="col-sm-10">
									<div class="checkbox">
										<label>
											<input type="checkbox" name="dist_mode" value="1"> 串点模型
										</label>
										<label>
											<input type="checkbox" name="dist_mode" value="2"> 接力模型
										</label>
										<label>
											<input type="checkbox" name="dist_mode" value="3"> 混合模型
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">distance_file</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="distance_file">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">demand_file</label>
								<div class="col-sm-10">
									<input type="password" class="form-control" name="demand_file" value="">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">optimize_iterations</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="optimize_iterations">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">car_cost_mode</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="car_cost_mode">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">car_templates</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="car_templates">
								</div>
							</div>

						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" id="saveBtn" class="btn btn-primary">提交</button>
					</div>
				</div>
		    </div>
	    </div>
	    <!-- /#page-wrapper -->
	
	</div>
	<!-- /#wrapper -->
	
	<!-- jQuery -->
	<script src="/static/js/jquery.min.js"></script>
	
	<!-- Bootstrap Core JavaScript -->
	<script src="/static/js/bootstrap.min.js"></script>
	
	<!-- Metis Menu Plugin JavaScript -->
	<script src="/static/js/metisMenu/metisMenu.min.js"></script>
	
	<!-- Custom Theme JavaScript -->
	<script src="/static/js/sb-admin-2.js"></script>
	<script src="/static/js/morris/raphael-min.js"></script>
	<script src="/static/js/morris/morris.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var $this = $(".xb_alt>li:nth-child(2)")
			$this.find("ul").css("display","block");
			$this.find("span").attr("class","glyphicon glyphicon-triangle-bottom");
			$this.find("li").eq(0).addClass("active");
		});
        //添加新输入
        $("#saveBtn").click(function(){
            $.post("/cascade/newInput",$("#newInput").serialize())
                .done(function(result){
                    if("success" == result) {
                        alert("success");
                    }
                }).fail(function(){
                alert("出现异常");
            });

        });
	</script>

</body>

</html>
