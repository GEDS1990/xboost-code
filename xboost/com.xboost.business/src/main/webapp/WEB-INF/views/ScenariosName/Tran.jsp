<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Xboost System</title>

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
	        		<p>${sessionScope.openScenariosName}(Settings)</p>
	        	</div>
	        	<div class="xb-fr">
	        		<button id="btn-input" data-toggle="modal" data-target=".bs-example-modal-input"><span class="icon-download"></span>Import Settings</button>
	        		<button id="btn-export" data-toggle="modal" data-target=".bs-example-modal-export"><span class="icon-upload"></span>Export Settings</button>
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
					    <!--
                        <tr>
                            <th>fixed<br />Round</th>计算
                            <th>fixed<br />RoundFee</th>计算
                            <th>cost<br />PerDistance</th>计算
                            <th>cost<br />PerTime</th>计算
                            <th>fixed<br />Cost</th>计算
                        </tr>
                        -->
					    	<tr>
                                <th rowspan="3">Id</th>
                                <th rowspan="3">vehicle type</th>
                                <th rowspan="3">vehicle source</th>
                                <th rowspan="3">full load unload time (min)</th>
					    		<th rowspan="3">max<br />Stop</th>
                                <th rowspan="3">maximum load(p)</th>
					    		<th rowspan="3">start<br />Location</th>
					    		<th rowspan="3">end<br />Location</th>
                                <th rowspan="3">maximum distance (km)</th>
					    		<th rowspan="3">max<br />RunningTime</th>
                                <th rowspan="3">speed(km/h)</th>
                                <th rowspan="3">operation</th>
                                <tr>
                                    <th  colspan="4">distance interval 1</th>
                                    <th  colspan="4">distance interval 2</th>
                                    <th  colspan="4">distance interval 3</th>
                                    <th  colspan="4">distance interval 4</th>
                                    <th  colspan="4">distance interval 5</th>
                                    <tr>
                                        <th>0 km</th>
                                        <th>a km</th>
                                        <th>ride</th>
                                        <th>km</th>
                                        <th>a km</th>
                                        <th>b km</th>
                                        <th>ride</th>
                                        <th>km</th>
                                        <th>b km</th>
                                        <th>c km</th>
                                        <th>ride</th>
                                        <th>km</th>
                                        <th>c km</th>
                                        <th>d km</th>
                                        <th>ride</th>
                                        <th>km</th>
                                        <th>d km</th>
                                        <th>e km</th>
                                        <th>ride</th>
                                        <th>km</th>
                                    </tr>
                                </tr>
                            </tr>
					    </thead>
					    <tbody>
					    </tbody>
					</table>
				</div>
	    	</div>
	    </div>
	    <!-- /#page-wrapper -->
	</div>
</div>
<!-- /#wrapper -->



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
                        <label class="col-sm-2 control-label">type</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="type" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">carSource</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="carSource" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">dimensions</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="dimensions" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">maxLoad</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxLoad" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">durationUnloadFull</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationUnloadFull" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">maxStop</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxStop" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">fixed_round</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="fixedRound" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">fixed_round_fee</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="fixedRoundFee" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">start_location</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="startLocation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">end_location</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="endLocation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
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
                    <!--<div class="form-group">
                        <label class="col-sm-3 control-label">skills</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="skills" >
                        </div>
                    </div>-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">max_distance</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxDistance" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">max_running_time</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxRunningTime" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">cost_per_distance</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="costPerDistance" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">cost_per_time</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="costPerTime" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">fixed_cost</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="fixedCost" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">velocity</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="velocity" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
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
                    <input type="hidden" name="id" id="siteId-tran" value="">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">type</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="type" id="cartype" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">carSource</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="carSource" id="carSource" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">dimensions</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="dimensions" id="dimensions" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">maxLoad</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxLoad" id="maxLoad" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">durationUnloadFull</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationUnloadFull" id="durationUnloadFull" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">maxStop</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxStop" id="maxStop" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">fixed_round</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="fixedRound" id="fixedRound" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">fixed_round_fee</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="fixedRoundFee" id="fixedRoundFee" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">start_location</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="startLocation" id="startLocation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">end_location</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="endLocation" id="endLocation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
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
                    <!--<div class="form-group">
                        <label class="col-sm-3 control-label">skills</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="skills" >
                        </div>
                    </div>-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">max_distance</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxDistance" id="maxDistance" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">max_running_time</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxRunningTime" id="maxRunningTime" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">cost_per_distance</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="costPerDistance" id="costPerDistance" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">cost_per_time</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="costPerTime" id="costPerTime" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">fixed_cost</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="fixedCost" id="fixedCost" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">velocity</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="velocity" id="velocity" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
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
                <h4 class="modal-title">Delete Data</h4>
            </div>
            <div class="modal-body">
                <p>Are you sure want to delete this line of data?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                <button type="button" id="modal-trandelBtn" class="btn btn-primary">Yes</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


	<!--  上传 导出 下载模板 -->
	<!--Model input-->
<div class="modal fade bs-example-modal-input" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" data-backdrop="static">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
		<div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	    </div>
	    
        <!--content s-->
        <form  method="post" id="cond-input-form-tran" enctype="multipart/form-data">

        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Vehicles" Settings:</div>
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
    		<div class="col-sm-6 text-right">Export "Vehicles" Settings:</div>
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
