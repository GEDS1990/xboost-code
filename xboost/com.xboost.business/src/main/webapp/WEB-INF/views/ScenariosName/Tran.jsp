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
	        		<p>ScenariosName1(Conditions)</p>
	        	</div>
	        	<div class="xb-fr">
	        		<button id="btn-input" data-toggle="modal" data-target=".bs-example-modal-input"><span class="icon-download"></span>Input Conditions</button>
	        		<button id="btn-export" data-toggle="modal" data-target=".bs-example-modal-export"><span class="icon-upload"></span>Expot Conditions</button>
	        	</div>
	        </div>
	        <div class="cond-top">
	        	<ul class="cond-top-ul clearfix">
	        		<li><a href="/siteInfo">Depots Info</a></li>
	        		<li><a href="/siteDist">Depots Distance</a></li>
	        		<li class="active"><a  class="active" href="/transport">Vehicles</a></li>
	        		<li><a href="/demandInfo">Demands</a></li>
	        		<li><a href="/modelArg">Parameters</a></li>
	        	</ul>
	        </div>
	    	<div>
		        
		        <!--Transportation-->
		        <div class="table-responsive active">
		        	<a href="javascript:;" id="addNewUser-tran" class="btn btn-success btn-xs pull-right"><i class="fa fa-plus"></i>Add Info</a>
					<table id="Transportation" class="table table-striped table-bordered table-hover">
					    <thead>
					    	<tr>
					    		<th>Id</th>
					    		<th>type</th>
					    		<th>vehicle source</th>
					    		<th>maximum load(p)</th>
					    		<th>full load unload time (min)</th>
					    		<th>maxStop</th>
					    		<th>dimensions</th>
					    		<th>fixedRound</th>
					    		<th>fixedRoundFee</th>
					    		<th>startLocation</th>
					    		<th>endLocation</th>
					    		<th>maxDistance</th>
					    		<th>maxRunningTime</th>
					    		<th>costPerDistance</th>
					    		<th>costPerTime</th>
					    		<th>fixedCost</th>
					    		<th>velocity</th>
					    		<th>operation</th>
					    	</tr>
					    </thead>
					    <tbody>
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

<!--Model input-->
<div class="modal fade bs-example-modal-input" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" data-backdrop="static">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
		<div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	    </div>
	    
        <!--content s-->
        <form  method="post" id="cond-input-form" enctype="multipart/form-data">

        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Transportation"</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="file" class="cond_file"/>
	        		<div class="cond-file-box clearfix">
	        			<div class="cond-file-btn">Choose File</div>
	        			<p>No file chosen</p>
	        		</div>
	        	</div>
	        	<div class="col-sm-4">
	        		<div>
	        			<span class="icon-upload"></span>
	        			Download Template
	        		</div>
	        	</div>
        	</div>
  
        </form>
        <!--content e-->

        <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" id="cond-file-upload-tran">Upload</button>
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
    		<div class="col-sm-6 text-right">Export "Transportation"</div>
        	<div class="col-sm-4">
        		<div class="export-btn" data-xls="Transportation">
        			<span class="icon-upload"></span>
        			Export 
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







<div class="modal fade" id="newUserModal-tran">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Adding dot information</h4>
            </div>
            <div class="modal-body">
                <form id="newUserForm-tran" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">type</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="type" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">carSource</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="carSource" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">maxLoad</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="maxLoad" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">durationUnloadFull</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="durationUnloadFull" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">maxStop</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="maxStop" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">fixed_round</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="fixed_round" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">fixed_round_fee</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="fixed_round_fee" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">start_location</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="start_location" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">end_location</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="end_location" >
                        </div>
                    </div>
<!--
                    <div class="form-group">
                        <label class="col-sm-3 control-label">time_window</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="time_window_start" placeholder="start">
                        </div>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="time_window_end" placeholder="end">
                            <input type="hidden" name="tw" id="tw" value="" />
                        </div>
                    </div>
                    -->
                    <div class="form-group">
                        <label class="col-sm-3 control-label">skills</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="skills" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">max_distance</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="max_distance" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">max_running_time</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="max_running_time" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">cost_per_distance</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="cost_per_distance" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">cost_per_time</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="cost_per_time" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">fixed_cost</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="fixed_cost" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">velocity</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="velocity" >
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="saveBtn-tran" class="btn btn-primary">Save</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="editUserModal-tran">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit dot information</h4>
            </div>
            <div class="modal-body">
                <form id="editUserForm-tran" class="form-horizontal">
                    <input type="hidden" name="id" id="siteId" value="">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">ID</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteCode" id="siteCode">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">longitude</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteLongitude" id="siteLongitude">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">latitude</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteLatitude" id="siteLatitude">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">name</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteName" id="siteName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">address</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteAddress" id="siteAddress">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">area</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteArea" id="siteArea">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">type</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteType" id="siteType">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">distrib.center</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteNightDelivery" id="siteNightDelivery">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">truck quantity limit</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="carNum" id="carNum">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">truck weight limit</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="largeCarModel" id="largeCarModel">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">capacity</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="maxOperateNum" id="maxOperateNum">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="editBtn-tran" class="btn btn-primary">Save</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->




<div class="modal fade" id="modal-tran">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Delete Scenario</h4>
            </div>
            <div class="modal-body">
                <p>Are you sure want to delete this Scenario</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                <button type="button" id="modal-trandelBtn" class="btn btn-primary">Yes</button>
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
<script src="/static/js/tableExporter.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/Xboost/navMain.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/Xboost/ScenariosName.js" ></script>
<script type="text/javascript">
	$(function  () {
		(function  () {
			$('#nav-Conditions').addClass("active");
		})()
	});
</script>

</body>

</html>
