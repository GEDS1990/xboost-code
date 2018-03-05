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
	        	<div class="xb-fr">
	        		<button id="btn-input" data-toggle="modal" data-target=".bs-example-modal-input"><span class="icon-download"></span>Import Settings</button>
	        		<button id="btn-export" data-toggle="modal" data-target=".bs-example-modal-export"><span class="icon-upload"></span>Export Settings</button>
	        	</div>
	        </div>
	        <div class="cond-top">
	        	<ul class="cond-top-ul clearfix">
	        		<li><a href="/siteInfo">Depots Info</a></li>
	        		<li><a href="/siteDist">Depots Distance</a></li>
	        		<li><a href="/car">Shipping Supply</a></li>
	        		<li class="active"><a  class="active" href="/demandInfo">Shipments</a></li>
	        		<li><a href="/modelArg">Parameters</a></li>
	        	</ul>
	        </div>
	    	<div>
		        <!--demands-->
	            <div class="table-responsive active">
	            	<a href="javascript:;" id="addNewUser-dem" class="btn btn-success btn-xs pull-right"><i class="fa fa-plus"></i>Add Info</a>
		            <table id="Demands" class="table table-striped table-bordered table-hover">
			            <thead>
			            	<tr>
			            		<th>ID</th>
				                <th>Origination Depot</th>
				                <th>Start Time</th>
				                <th>Destination Depot</th>
				                <th>End Time</th>
				                <th>Piece(p)</th>
				                <th>Weight(kg)</th>
				                <th>Product Type</th>
				                <th>Operation</th>
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








<div class="modal fade" id="newUserModal-dem">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Adding dot information</h4>
            </div>
            <form id="newUserForm-dem" class="form-horizontal">
            	<div class="modal-body">
                    <div class="form-group">
                        <!--<label class="col-sm-2 control-label">date</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="date" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>-->
                        <label class="col-sm-2 control-label">Origination Depot</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="siteCodeCollect" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">Start Time</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationStart" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">Destiantion Depot</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="siteCodeDelivery" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">Effective End Time</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationEnd" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        
                        <label class="col-sm-2 control-label">Piece(p)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="votes" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">Weight(kg)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="weight" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">Product Type</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="productType" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                <input type="submit" id="saveBtn-dem" class="btn btn-primary" value="Save" />
	            </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="editUserModal-dem">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit dot information</h4>
            </div>
            <form id="editUserForm-dem" class="form-horizontal">
              	<div class="modal-body">
              		<input type="hidden" name="id" id="siteId-dem" value="">
                    <div class="form-group">
                        <!--<label class="col-sm-2 control-label">date</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="date" id="date" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>-->
                        <label class="col-sm-2 control-label">Origination Depot</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="siteCodeCollect" id="siteCodeCollect" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">Start Time</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationStart" id="durationStart" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">Destiantion Depot</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="siteCodeDelivery" id="siteCodeDelivery" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">Effective End Time</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationEnd" id="durationEnd" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        
                        <label class="col-sm-2 control-label">Piece(p)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="votes" id="votes" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">Weight(kg)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="weight" id="weight" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">Product Type</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="productType" id="productType" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                <input type="submit" id="editBtn-dem" class="btn btn-primary" value="Save" />
	            </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="modal-dem">
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
                <button type="button" id="modal-demdelBtn" class="btn btn-primary">Yes</button>
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
        <form  method="post" id="cond-input-form-dem" enctype="multipart/form-data">
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Shipments" Settings:</div>
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
	        <button type="button" class="btn btn-primary" id="cond-file-upload-dem">Upload</button>
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
    		<div class="col-sm-6 text-right">Export "Shipments" Settings:</div>
        	<div class="col-sm-4" data-xls="Demands">
        		<div class="export-btn" data-xls="demands">
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