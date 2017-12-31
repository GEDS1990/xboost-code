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
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=Sjbn4XELTZZZKwunDx4AGqblQ2aViy3Z"></script>
    <style>
    	#depots-map {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
    </style>

</head>

<body>

<div id="wrapper">
    <%@ include file="../include/nav.jsp"%>
    <!-- Page Content -->
    <div class="xb_page_content">
    	
        <!--main-->
	    <div id="page-wrapper">
	    	<!--top-->
	    	<div class="cond-wrap">
	    		<div class="clearfix ">
		        	<div class="xb-fl">
		        		<p>${sessionScope.openScenariosName}(Overview)</p>
		        	</div>
		        	<div class="xb-fr">
		        		<button id="edit-create-info" ><span class="glyphicon glyphicon-leaf"></span>Edit Basic Info</button>
		        	</div>
	        	</div>
	        	<p>Category: <span id="scen-cate"></span></p>
	        	<p id="scen-desc"></p>
	        </div>
	        
	        
	    	<div class="sim-content scen">
	    		<header class="clearfix">
	    			<p>Settings Overview</p>
	    			<a href="/siteInfo">See details</a>
	    		</header>
	    		<div class="sim-box">
	    			<ul id="scen-ul" class="clearfix">
	    				<li>
	    					<p>Depot Quantity:</p>
	    					<p id="depot-quantity">--</p>
	    				</li>
	    				<li>
	    					<p>Vehicle Quantity:</p>
	    					<p id="vehicle-quantity">--</p>
	    				</li>
	    				<li>
	    					<p>Demand Quantity:</p>
	    					<p id="demand-quantity">--</p>
	    				</li>
	    				<li>
	    					<p>The Farthest Distance(km):</p>
	    					<p id="farthest-distance">--</p>
	    				</li>
	    			</ul>
	    		</div>
	    	</div>
	    	<div class="sim-content scen">
	    		<header class="clearfix">
	    			<p>Simulation & Results Overview</p>
	    			<a>See details</a>
	    		</header>
	    		<table id="ScenariosNamet" style="display: none;">
	    			<thead>
	    				<tr>
	    					<th>id</th>
	    					<th>Depot ID</th>
	    				</tr>
	    			</thead>
	    			<tbody></tbody>
	    		</table>
	    		<div class="scen-box clearfix">
	    			<div class="scen-itembox1" style="height: 380px;">
	    				<div id="depots-map" style="width: 100%; ">
	    					
	    				</div>
	    			</div>
	    			<div class="scen-itembox2">
	    				<h1 id="title">No Data</h1>
	    				<ul class="scen-result">
	    					<li>Simulation Method:<span id ="simulation-method">--</span></li>
	    					<li>Simulation Progress:<span id ="simulation-progress">--</span></li>
	    					<li>Simulation Finished:<span id ="simulation-finished">--</span></li>
	    				</ul>
	    				<ul class="scen-cost">
	    					<li>Staff Quantity:<span id ="staff-quantity">--</span></li>
	    					<li>Staff Cost:<span id ="staff-cost">--</span></li>
	    					<li>Vehicle Quantity:<span id ="vehicle-quantity1">--</span></li>
	    					<li>Vehicle Cost:<span id ="vehicle-cost">--</span></li>
	    					<li class="totalcost">Total Cost:<span id="total-cost">--</span></li>
	    				</ul>
	    			</div>
	    		</div>
	    	</div>
	    	
	    </div>
	    <!-- /#page-wrapper -->
	</div>
</div>
<!-- /#wrapper -->



<!--Edit info create-->

<div class="modal fade" id="newUserModal-scen">
    <div class="modal-dialog creat">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Create Scenarios</h4>
            </div>
            <form id="newUserForm-scen" class="form-horizontal">
	            <div class="modal-body">
	                <div class="form-group">
	                    <label class="col-sm-4 control-label">Scenarios Name*:</label>
	                    <div class="col-sm-7">
	                        <input type="text" class="form-control" name="scenariosName" id="scenariosName"  required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');" >
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-4 control-label">Category*:</label>
	                    <div class="col-sm-7">
	                    	<input type="hidden" name="scenariosCategory"  id="scenariosCategory"/>
	                        <div  class="form-control Category" id="category-out">
	                        	<span class="iconcate glyphicon glyphicon-triangle-bottom"></span>
	                        	<p id="class-first"></p>
	                        	<div id="Category-box">
	                    			<div class="clearfix classwrap" >
	                    				<input type="text" id="classAdd" />
	                        			<a href="javascript:void(0);" id="classbtn" >Add</a>
	                        		</div>
	                        		<ul id="cateClass"></ul>
	                        	</div>
	                        </div>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-4 control-label">Description</label>
	                    <div class="col-sm-7">
	                        <textarea type="text" class="form-control" name="scenariosDesc" ></textarea>
	                    </div>
	                </div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                <input type="submit" id="saveBtn-scen" class="btn btn-primary" value="Save">
	            </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->















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
<script src="/static/js/sockjs-0.3.min.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/vue.min.js"></script>
<script src="/static/js/Xboost/ScenariosName.js" ></script>
<script src="/static/js/Xboost/xbMain.js"></script>
<script type="text/javascript">
	$(function  () {
		(function  () {
			$('#scen-name').addClass("active");
		})()
	});
</script>
</body>

</html>