<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Xboost-物流规划管理系统</title>

    <!-- Bootstrap Core CSS -->
    <link rel="stylesheet" href="../../static/css/bootstrap.min.css" />

    <!-- MetisMenu CSS -->
    <link href="/static/js/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/static/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/static/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/static/js/morris/morris.css">
    <link rel="stylesheet" href="/static/js/datatables/media/css/dataTables.bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/Xboost/icon.css" />
    <link rel="stylesheet" href="/static/css/Xboost/xb_main.css" />
    <link rel="stylesheet" href="/static/css/Xboost/ScenariosName.css" />

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
    	
        <!--main-->
	    <div id="page-wrapper">
	    	<!--top-->
	    	<div class="clearfix cond-wrap">
	        	<div class="xb-fl">
	        		<p><shiro:principal property="username"/>′s Research</p>
	        	</div>
	        </div>
	        
	        <!--main-->
	        <div class="sim-content" style="margin-top: 30px;">
	    		<header class="clearfix">
	    			<div class="xb-fl">
		    			<div class="select-depot">
		    				<span class="glyphicon glyphicon-align-left"></span>
		    				<select id="research-name1" class="route-select"></select>
		    			</div>
	    			</div>
	    			<div class="xb-fl" style="margin: 0 10px;">
		    			<p>Compare with</p>
	    			</div>
	    			<div class="xb-fl">
		    			<div class="select-depot">
		    				<span class="glyphicon glyphicon-align-left"></span>
		    				<select id="research-name2" class="route-select"></select>
		    			</div>
	    			</div>
	    		</header>
	    		<div class="sim-box">
	    			<div style="display: none;">
	    				<table id="research-table" style="display: none;">
	    					<thead>
	    						<tr>
	    							<th>id</th>
	    							<th>scenariosName</th>
	    						</tr>
	    					</thead>
	    					<tbody>
	    						<tr>
	    							<td></td>
	    							<td></td>
	    						</tr>
	    					</tbody>
	    				</table>
	    			</div>
	    			
	    			<div id="collect-reserach-echarts" style="border:1px solid #ccc;padding:10px;">
	    				
	    			</div>
	    			<div id="branch-reserach-echarts" style="border:1px solid #ccc;padding:10px;margin-top: 20px;">
	    				
	    			</div>
	    		</div>
	    	</div>
	        
	        
	    </div>
	    
	    
	</div>
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
<%-- DataTables JS--%>
<script src="/static/js/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="/static/js/datatables/media/js/dataTables.bootstrap.min.js"></script>
<script src="/static/js/tableExporter.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/flex.js"></script>
<script src="/static/js/echarts.js"></script>
<script src="/static/js/Xboost/xbMain.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/Xboost/ScenariosName.js" ></script>
<script src="/static/js/Xboost/xb_echart.js"></script>
<script type="text/javascript">
	$(function  () {
		(function  () {
			$('#nav-Research').addClass("active");
		})()
	});
</script>
</body>

</html>
