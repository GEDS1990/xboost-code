<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Xboost</title>

    <!-- Bootstrap Core CSS -->
    <link rel="stylesheet" href="/static/css/bootstrap.min.css" />

    <!-- MetisMenu CSS -->
    <link href="/static/js/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/static/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/static/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/static/js/morris/morris.css">
    <link rel="stylesheet" href="/static/js/datatables/media/css/dataTables.bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/Xboost/icon.css" />
    <link rel="stylesheet" href="/static/css/Xboost/loaders.css" />
    <link rel="stylesheet" href="/static/css/Xboost/ladda-themeless.min.css" />
    <link rel="stylesheet" href="/static/css/Xboost/xb_main.css" />
    <link rel="stylesheet" href="/static/css/Xboost/ScenariosName.css" />

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="/static/js/Xboost/spin.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="/static/js/Xboost/ladda.min.js" type="text/javascript" charset="utf-8"></script>
    
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
	    	<div class="clearfix cond-wrap">
	        	<div class="xb-fl">
	        		<p>${sessionScope.openScenariosName}(Results)</p>
	        	</div>
				<div class="xb-fr">
					<button id="btn-input" data-toggle="modal" data-target=".bs-example-modal-input"><span class="icon-download"></span>Import Routes</button>
					<button id="btn-export" data-toggle="modal" data-target=".bs-example-modal-export"><span class="icon-upload"></span>Export Routes</button>
				</div>
	        </div>
	        <div class="cond-top">
	        	<ul class="cond-top-ul clearfix">
	        		<li><a href="/depots">Depots</a></li>
	        		<li class="active"><a class="active" href="/route">Route</a></li>
	        		<li><a href="/vehicles">Vehicles</a></li>
	        		<li><a href="/costs">Costs</a></li>
	        		<li><a href="/efficiency">Efficiency</a></li>
	        		<li><a href="/distribution">Distribution</a></li>
	        	</ul>
	        	<!--<ul class="cond-top-ul clearfix">
	        		<li class="active"><a class="active" href="/route">Solution Route</a></li>
	        		<li><a href="#">Solution Activity</a></li>
	        		<li><a href="#">Solution Stats</a></li>
	        		<li><a href="#">Solution Arrinfos</a></li>
	        		<li><a href="#">Solution Jobinfos</a></li>
	        	</ul>-->
	        </div>
	        
	        
	        
	    	<div id="route-wrap">
	    		<header class="clearfix result-header">
	    			<div class="xb-fl">
		    			<div class="select-depot">
		    				<span class="glyphicon glyphicon-align-left"></span>
		    				<select id="route-route" class="route-select">
		    					
		    				</select>
		    			</div>
	    			</div>
	    			<div class="xb-fr">
	    				<!--<button><span class="icon-upload"></span>Export this Sheet</button>-->
	    			</div>
	    		</header>
	    		<div class="scen-box clearfix">
	    			<div class="scen-itembox1" style="height: 420px;">
	    				<div id="depots-map" style="width: 100%; ">
	    					
	    				</div>
	    			</div>
	    			<div class="scen-itembox2">
	    				<h1 id="route-name">No Data</h1>
	    				<ul class="scen-cost">
	    					<li>total distance(km):<span id="total-distance"></span></li>
	    					<li>vehicle type requirement:<span id="vehicle-load-requirement"></span></li>
	    					<li>vehicle piece capacity requirement:<span id="vehicle-piece-capacity"></span></li>
	    					<li>speed requirement:<span id="speed-requirement"></span></li>
	    					<li style="color: blue;">Chosen Vehicle:<span style="color: blue;font-weight: inherit;" id="Chosen-Vehicle">--</span></li>
	    					<li class="route-choose">Choose vehicle:<span>--</span></li>
	    					<li>
	    						<form>
		    						<label >
		    							<input type="radio" name="type"  checked="checked"  value="1"/> Using Vehicle
		    						</label>
		    						<label style="margin-left: 10px;">
		    							<input type="radio" name="type"  value="0"/> Idle Vehicle
		    						</label>
	    						</form>
	    					</li>
	    					<li>
	    						<select id="us-vehicle" style="width: 50%;"></select>
	    						<select id="idle-vehicle" style="width: 50%;"></select>
	    					</li>
	    					<button id="vehicle-btn" class="btn btn-primary ladda-button" data-style="expand-right" >
	    						<span class="ladda-label">submit</span>
	    					</button>
	    				</ul>
	    			</div>
	    		</div>
	    	
	    	
	    	
	    		<!--Solution Route-->
			    <div class="table-responsive active result-style">
		            <table id="SolutionRoute" class="table table-striped table-bordered table-hover">
					    <thead>
					    	<tr>
					    		<th rowspan="1">id</th>
					    		<th rowspan="1">Route ID</th>
					    		<th rowspan="1">Depot Order</th>
					    		<th rowspan="1">Depot ID</th>
					    		<th rowspan="1">Depot Name</th>
					    		<th rowspan="1">Depot Address</th>
					    		<th rowspan="1">Arrival Time</th>
					    		<th rowspan="1">Operation</th>
					    		<th rowspan="1">Departure Time</th>
					    		<th rowspan="1">Next Depot & Distance</th>
					    	</tr>
					    </thead>
			            <tbody id="route-tbody">
			            	<tr>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
			            	</tr>
			            </tbody>
		            </table>
		        </div>
	    	</div>
	    </div>
	    <!-- /#page-wrapper -->
	</div>
</div>
<!-- /#wrapper -->

<!--  上传 导出 下载模板 -->
<!--Model input-->
<div class="modal fade bs-example-modal-input" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			</div>

			<!--content s-->
			<form  method="post" id="cond-input-form-info" enctype="multipart/form-data">

				<div class="model-input-content clearfix">
					<div class="col-sm-4 text-right">Import "Route Car"Settings:</div>
					<div class="col-sm-4">
						<input type="file" name="file" class="cond_file"/>
						<div class="cond-file-box clearfix">
							<div class="cond-file-btn">Choose File</div>
							<p>No file chosen</p>
						</div>
					</div>
					<div class="col-sm-4">
						<div>
							<a class="down-href" href="javascript:void(0);" >
								<span class="icon-upload"></span>
								Download Template
							</a>
						</div>
					</div>

				</div>
				<p class="text-center import-error"></p>

			</form>
			<!--content e-->

			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="cond-file-upload-info">Upload</button>
			</div>
		</div>
	</div>
</div>


<!--Model export-->
<div class="modal fade bs-example-modal-export" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" data-backdrop="static">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
		<div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	    </div>
	    
        <!--content s-->
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Routes"</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="Depots_Info">
        			<a href="javascript:void(0);">
        				<span class="icon-upload"></span>
        				Export
        			</a>
        		</div>
        	</div>
    	</div>
        <!--content e-->
        
        <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	    </div>
    </div>
  </div>
</div>






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
<script src="/static/js/datatables/media/js/jquery.dataTables.js"></script>
<script src="/static/js/datatables/media/js/dataTables.bootstrap.min.js"></script>
<script src="/static/js/tableExporter.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/flex.js"></script>
<script type="text/javascript" src="/static/js/Xboost/Solution.js" ></script>
<script type="text/javascript">
	$(function  () {
		(function  () {
			$('#nav-Results').addClass("active");
		}());
	});
	
</script>
</body>

</html>
