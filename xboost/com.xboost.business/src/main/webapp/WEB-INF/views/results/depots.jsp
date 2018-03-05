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
	    	<div class="clearfix cond-wrap">
	        	<div class="xb-fl">
	        		<p>${sessionScope.openScenariosName}(Results)</p>
	        	</div>
	        	<div class="xb-fr">
	        		<button id="btn-export" data-toggle="modal" data-target=".bs-example-modal-export"><span class="icon-upload"></span>Export Results</button>
	        	</div>
	        </div>
	        <div class="cond-top">
	        	<ul class="cond-top-ul clearfix">
	        		<li class="active"><a class="active" href="/depots">Depots</a></li>
	        		<li><a href="/route">Route</a></li>
	        		<!--<li><a href="/vehicles">Vehicles</a></li>-->
	        		<li><a href="/vehiclesPlan">Vehicle Scheduling</a></li>
	        		<li><a href="/costs">Costs</a></li>
	        		<li><a href="/efficiency">Depot Efficiency</a></li>
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
		    				<select id="route-depot" class="route-select">
		    					<option value="0">All Depots</option>
		    				</select>
		    			</div>
	    			</div>
	    			<div class="xb-fr">
	    				<!--<button><span class="icon-upload"></span>Export this Sheet</button>-->
	    			</div>
	    		</header>
	    		<div class="scen-box clearfix">
	    			<div class="scen-itembox1" style="height: 456px;">
	    				<div id="depots-map" style="width: 100%; ">
	    					
	    				</div>
	    			</div>
	    			<div class="scen-itembox2" style="height: 456px;">
	    				<h1><span id="depot">No Data</span></h1>
	    				<ul class="scen-result">
	    					<li>E:<span id ="east">--</span>&nbsp;&nbsp;N:<span id ="north">--</span></li>
	    					<li id = "name">--</li>
	    					<li id = "address">--</li>
	    				</ul>
	    				<ul class="scen-cost">
	    					<li>depot type:<span id ="type">--</span></li>
	    					<!--<li>to distrib.center:<span id="distrib-center">--</span></li>-->
	    					<li>depot area:<span id="area">--</span></li>
	    					<li>vehicle quantity limit:<span id="vehicle-quantity-limit">--</span><span>(per 10mins)</span></li>
	    					<li>vehicle weight limit:<span id="vehicle-weight-limit">--</span><span>(per 10mins)</span></li>
	    					<li>piece capacity:<span id="piece-capacity">--</span><span>(per 10mins)</span></li>
	    					<li><i class="depotline alt_last"></i>&nbsp;&nbsp;<i>The previous location</i></li>
	    					<li><i class="depotline alt_next"></i>&nbsp;&nbsp;<i>The next location</i></li>
	    					<li><i class="depotline alt_each"></i>&nbsp;&nbsp;<i>Locations of each other</i></li>
	    				</ul>
	    			</div>
	    		</div>
	    	
	    	
	    	
	    		<!--Solution Route-->
			    <div class="table-responsive active result-style">
		            <table id="SolutionDeport" class="table table-striped table-bordered table-hover">
					    <thead>
					    	<tr>
					    		<th rowspan="1">id</th>
					    		<th rowspan="1">Depot ID</th>
					    		<th rowspan="1">Incoming Vehicle</th>
					    		<th rowspan="1">Arrival Time</th>
					    		<th rowspan="1">Operation</th>
					    		<th rowspan="1">Departure Time</th>
					    		<!--<th rowspan="1">cur loc</th>
					    		<th rowspan="1">type</th>
					    		<th rowspan="1">sb_loc</th>
					    		<th rowspan="1">sb_vol</th>
					    		<th rowspan="1">arr_time</th>
					    		<th rowspan="1">end_time</th>
					    		<th rowspan="1">unload_loc</th>
					    		<th rowspan="1">unload_vol</th>
					    		<th rowspan="1">next_cur_loc</th>
					    		<th rowspan="1">calc_dis</th>
					    		<th rowspan="1">car_goods</th>-->
					    	</tr>
					    </thead>
			            <tbody id="depot-tbody">
			            	<tr>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <!--<td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>-->
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



<!--Model export-->
<div class="modal fade bs-example-modal-export" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" data-backdrop="static">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
		<div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	    </div>
	    
        <!--content s-->
        <!--<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export All Data to Single File</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="ALL_Data">
        			<span class="icon-upload"></span>
        			Export All Data
        		</div>
        	</div>
    	</div>-->
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Depots Info"</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="Depots_Info">
        			<a href="javascript:void(0);">
        				<span class="icon-upload"></span>
        				Export
        			</a>
        		</div>
        	</div>
    	</div>
    	<!--<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Depots Distance"</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="Depots_Distance">
        			<span class="icon-upload"></span>
        			Export 
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Transportation"</div>
        	<div class="col-sm-4">
        		<div class="export-btn" data-xls="Transportation">
        			<span class="icon-upload"></span>
        			Export 
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Demands"</div>
        	<div class="col-sm-4" data-xls="Demands">
        		<div class="export-btn">
        			<span class="icon-upload"></span>
        			Export 
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Patameters"</div>
        	<div class="col-sm-4">
        		<div class="export-btn" data-xls="Patameters">
        			<span class="icon-upload"></span>
        			Export 
        		</div>
        	</div>
    	</div>-->
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
		})()
	});
</script>
</body>

</html>
