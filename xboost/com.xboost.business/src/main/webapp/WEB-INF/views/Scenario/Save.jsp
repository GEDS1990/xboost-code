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
		        <h1>Save</h1>
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
			var $this = $(".xb_alt>li:first-child")
			$this.find("ul").css("display","block");
			$this.find("span").attr("class","glyphicon glyphicon-triangle-bottom");
			$this.find("li").eq(2).addClass("active");
		});
	</script>

</body>

</html>
