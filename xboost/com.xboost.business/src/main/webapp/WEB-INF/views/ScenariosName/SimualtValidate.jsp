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
	        		<p>${sessionScope.openScenariosName}(Simulate)</p>
	        	</div>
	        </div>
	        <div class="cond-top">
	        	<ul class="cond-top-ul clearfix">
	        		<li class="active"><a class="active" href="/simualte">Validate</a></li>
	        		<li><a href="/Simualte">Simulate</a></li>
	        	</ul>
	        </div>
	        
	    	<div class="sim-content">
	    		<header>
	    			<p>You must validate all settings before running the simulation</p>
	    		</header>
	    		<div class="sim-box">
	    			<div class="form-group sim-wrap clearfix">
	    				<p>Simulation Method :</p>
	    				<select id="sim-model" ><!--<option value="3">Mixed model</option>-->
	    					<!--<option value="0">Please choose</option>-->
		    			</select>
	    			</div>
	    			<div class="form-group sim-wrap clearfix">
	    				<p><button id="sim-check">Validate Settings</button></p>
	    				<p id="sim-error-check" class="sim-error">Please choise a Simulation Method first</p>
	    				<input type="hidden" id="modal-run"  />
	    			</div>
	    			
	    			<div class="form-group sim-ground" id="sim-check-info">
	    				${sessionScope.ValidateLog}
	    			</div>
	    			<div class="sim-settings">
	    				<div class="clearfix">
	    					<p>Optimization Finish Criteria :</p>
	    				</div>
	    				<div class="clearfix sim-input" style="display:none">
		    				<p>Time Limit(min)(模型运行时间)</p>
		    				<input type="number" id="sim-run-time" value="2" />
		    				<p id="timelimit" class="sim-error">You must set the time limit</p>
		    			</div>
		    			<div class="clearfix sim-input sim-v-serial">
		    				<p>No. of Loops (Model Optimization Loops):</p>
		    				<input type="number" id="sim-run-count" value="100" />
		    				<p id="loopslimit" class="sim-error">You must set the loops limit</p>
		    			</div>
		    			<div class="notes">
		    			    <p>Notes: This is model simulation loops, larger number means
                               longer time to run model and get better result.
		    			    </p>
		    			</div>
		    			<div class="clearfix sim-input sim-v-relay">
		    				<p>No. of Time (Model Optimization Time):</p>
		    				<input type="number" id="sim-run-t-limit" value="120" /><span>(s)</span>
		    				<p id="tlimit" class="sim-error">You must set the Time limit</p>
		    			</div>
		    			<div class="clearfix sim-input sim-v-relay">
		    				<p>No. of Optimization (Model Optimization Percentage):</p>
		    				<input type="number" id="sim-run-opt-limit" value="100" /><span>(%)</span>
		    				<p id="olimit" class="sim-error">You must set the Optimization limit</p>
		    			</div>
	    			</div>
	    			
	    			<div class="form-group  clearfix">
	    				<p class="sim-run-btn"><button id="sim-run">Run Simulation</button></p>
	    				<p id="sim-error-run" class="sim-error">You must pass the validation before running the simulation.If you have modified Scenario settings,you must validate them again</p>
	    			</div>
	    		</div>
	    	</div>
	    </div>
	    <!-- /#page-wrapper -->
	</div>
</div>
<!-- /#wrapper -->




<div class="modal fade" id="modal-siming">
    <div class="modal-dialog modal-del">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Restart Simulation</h4>
            </div>
            <div class="modal-body">
                <p>Are you sure want to restart simulation?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
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
<script src="/static/js/flex.js"></script>
<script src="/static/js/sockjs-0.3.min.js"></script>
<script src="/static/js/tableExporter.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/Xboost/ScenariosName.js" ></script>
<script src="/static/js/Xboost/simualte.js"></script>
<script type="text/javascript">
	$(function  () {
		(function  () {
			$('#nav-Simualt').addClass("active");
		})()
	});
</script>
</body>

</html>