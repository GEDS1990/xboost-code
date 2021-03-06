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
	        	<div class="xb-fr" style="display: none;">
	        		<button id="btn-input" data-toggle="modal" data-target=".bs-example-modal-input"><span class="icon-download"></span>Import Settings</button>
	        		<button id="btn-export" data-toggle="modal" data-target=".bs-example-modal-export"><span class="icon-upload"></span>Export Settings</button>
	        	</div>
	        </div>
	        <div class="cond-top">
	        	<ul class="cond-top-ul clearfix">
	        		<li><a href="/siteInfo">Depots Info</a></li>
	        		<li><a href="/siteDist">Depots Distance</a></li>
	        		<li><a href="/car">Shipping Supply</a></li>
	        		<li><a href="/demandInfo">Shipments</a></li>
	        		<li class="active"><a  class="active" href="/modelArg">Parameters</a></li>
	        	</ul>
	        </div>
	        
	        <!--Parameters-->
	    	<div id="Patameters-section">
		        <!--<div class="table-responsive active">
		        	<div class="xb-fl" style="margin-bottom: 10px;">
		    			<div class="select-depot">
		    				<span class="glyphicon glyphicon-align-left"></span>
		    				<select id="pata-model-type" class="route-select">
		    					<option value="1">串点模型</option>
		    					<option value="2">接力模型</option>
		    					<option value="3">综合模型</option>
		    				</select>
		    			</div>
	    			</div>
		        	<a href="javascript:;" id="addNewUser-pata" class="btn btn-success btn-xs pull-right"><i class="fa fa-plus"></i>Add Info</a>
		            <table id="Patameters" class="table table-striped table-bordered table-hover">
			            <thead>
			            	<tr>
			            		<th>Id</th>
				                <th>parameter name</th>
				                <th>data</th>
				                <th>notes</th>
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
			            	</tr>
			            </tbody>
		            </table>
		        </div> -->
		        
		        <div style="background-color: #fff;">
		        	<header class="result-header">
		        		<p style="color: #000;">Parameters</p>
		        	</header>
		        	<section>
		        		<form id="form-para-setting" class="form-horizontal para-setting">
			            	<div class="modal-body">
			            		<div class="form-group">
			                        <label class="col-sm-3 control-label">Simulation Method: </label>
			                        <div class="col-sm-3">
			                            <select name="modelType" id="modelType" class="form-control">
						    				<option value="1">Serial model</option>
						    				<option value="2">Relay model</option>
						    			</select>
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-sm-3 control-label">Super Depot Waiting Time(min): </label>
			                        <div class="col-sm-3">
			                            <input type="number" class="form-control" name="durationTransfer" id="durationTransfer" />
			                        </div>
			                        <div class="col-sm-6">
			                            <p class="form-control">Please enter Waiting Time</p>
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-sm-3 control-label">Manual Efficiency in Depot(p): </label>
			                        <div class="col-sm-3">
			                            <input type="number" class="form-control" name="sitePeopleWork" id="sitePeopleWork" />
			                        </div>
			                        <div class="col-sm-6">
			                            <p class="form-control">Please enter Manual Efficiency</p>
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-sm-3 control-label">Manual Efficiency in District hub(p): </label>
			                        <div class="col-sm-3">
			                            <input type="number" class="form-control" name="distriPeopleWork" id="distriPeopleWork" />
			                        </div>
			                        <div class="col-sm-6">
			                            <p class="form-control">Please enter Manual Efficiency</p>
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-sm-3 control-label">Delivery Window(min): </label>
			                        <div class="col-sm-3">
			                            <input type="text" disabled="disabled"  class="form-control"  id="durationRelay1"  />
			                            <input type="hidden" name="durationRelay" id="durationRelay"  />
			                        </div>
			                        <div class="col-sm-6">
			                            <p class="form-control">Please enter Delivery Window</p>
			                        </div>
			                    </div>
			                
			            	</div>
				            <div class="modal-footer" style="text-align: center;">
				            	<button type="button" id="js-save-para" class="btn btn-primary">Save</button>
				            </div>
			            </form>
			            <input hidden="hidden" id="add-edit" value="0" />
		        	</section>
		        </div>
		      
	    	</div>
	    	
	    </div>
	    <!-- /#page-wrapper -->
	</div>
</div>
<!-- /#wrapper -->








<div class="modal fade" id="newUserModal-pata">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Adding dot information</h4>
            </div>
            <div class="modal-body">
                <form id="newUserForm-pata" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">parameter name</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="parameterName" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">data</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="data" >
                        </div>
                        <label class="col-sm-2 control-label">notes</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="note" >
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="saveBtn-pata" class="btn btn-primary">Save</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="editUserModal-pata">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit dot information</h4>
            </div>
            <div class="modal-body">
                <form id="editUserForm-pata" class="form-horizontal">
                    <input type="hidden" name="id" id="siteId-pata" value="">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">parameter name</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="parameterName" id="parameterName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">data</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="data" id="data">
                        </div>
                        <label class="col-sm-2 control-label">notes</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="note" id="note">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="editBtn-pata" class="btn btn-primary">Save</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->




<div class="modal fade" id="modal-pata">
    <div class="modal-dialog modal-del">
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
                <button type="button" id="modal-patadelBtn" class="btn btn-primary">Yes</button>
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
        <form  method="post" id="cond-input-form-pata" enctype="multipart/form-data">
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Parameters" Settings:</div>
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
	        <button type="button" class="btn btn-primary" id="cond-file-upload-pata">Upload</button>
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
    		<div class="col-sm-6 text-right">Export "Patameters" Settings:</div>
        	<div class="col-sm-4">
        		<div class="export-btn" data-xls="Patameters">
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
