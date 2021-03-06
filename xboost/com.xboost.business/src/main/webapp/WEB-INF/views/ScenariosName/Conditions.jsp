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
	        		<p>${sessionScope.openScenariosName}(Input)</p>
	        	</div>
	        	<div class="xb-fr">
	        		<button id="btn-input" data-toggle="modal" data-target=".bs-example-modal-input"><span class="icon-download"></span>Import Settings</button>
	        		<button id="btn-export" data-toggle="modal" data-target=".bs-example-modal-export"><span class="icon-upload"></span>Export Settings</button>
	        	</div>
	        </div>
	        <div class="cond-top">
	        	<ul class="cond-top-ul clearfix">
	        		<li class="active"><a class="active" href="/siteInfo">Depots Info</a></li>
	        		<li><a href="/siteDist">Depots Distance</a></li>
	        		<li><a href="/car">Shipping Supply</a></li>
	        		<li><a href="/demandInfo">Shipments</a></li>
	        		<li><a href="/modelArg">Parameters</a></li>
	        	</ul>
	        </div>
	    	<div>
	    		<!--Depots Info-->
			    <div class="active">
			    	<a href="javascript:;" id="addNewUser-info" class="btn btn-success btn-xs pull-right"><i class="fa fa-plus"></i>Add Info</a>
		            <table id="Depots_Info" class="table table-striped table-bordered table-hover">
					    <thead>
					    	<tr>
					    		<th rowspan="2">id</th>
					    		<th rowspan="2">depot ID</th>
					    		<th rowspan="2">longitude</th>
					    		<th rowspan="2">latitude</th>
					    		<th rowspan="2">depot<br />name</th>
					    		<th rowspan="2">depot<br />address</th>
					    		<th rowspan="2">depot<br />area(㎡)</th>
					    		<th rowspan="2">depot<br />type</th>
					    		<th rowspan="2">To Super Depot</th>
					    		<th rowspan="2">Night Operation<br />Y/N</th>
					    		<th colspan="7">per 10 minutes</th>
                                <th rowspan="2">operation</th>
                                <tr>
                                    <th>No. of<br />Truck Limitation</th>
                                    <th>No. of<br />Baidu Limitation</th>
                                    <th>No. of<br />DiDi Limitation</th>
                                    <th>No. of<br />DaDa Limitation</th>
                                    <th>Reserve</th>
                                    <th>Vehicle Weight<br />Limitation</th>
                                    <th>piece<br />capacity(p)</th>
                                </tr>

					    	</tr>
					    </thead>
			            <tbody id="info-tbody">
			            </tbody>
		            </table>
		        </div>
		        
		        
		        
	    	</div>
	    </div>
	    <!-- /#page-wrapper -->
	</div>
</div>
<!-- /#wrapper -->


<!--Add info-->
<div class="modal fade" id="newUserModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Adding dot information</h4>
            </div>
            <form id="newUserForm" class="form-horizontal">
	            <div class="modal-body">
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">depot ID</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteCode" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                    <label class="col-sm-2 control-label">longitude</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteLongitude" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">latitude</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteLatitude" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                    <label class="col-sm-2 control-label">depot name</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteName" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">depot address</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="siteAddress" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">depot area(㎡)</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteArea" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                    <label class="col-sm-2 control-label">depot type</label>
	                    <div class="col-sm-4">
	                        <select type="text" class="form-control" name="siteType" >
	                        	<option value="Super Depot">Super Depot</option>
	                        	<option value="None-SD">None-SD</option>
	                        </select>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">To Super Depot</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="distribCenter" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                    <label class="col-sm-2 control-label">Night Operation Y/N</label>
	                    <div class="col-sm-4">
	                        <select type="text" class="form-control" name="siteNightDelivery" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                        	<option value="Y">Y</option>
	                        	<option value="N">N</option>
	                        </select>
	                    </div>
	                    
	                </div>
	                <div class="form-group">
	                	<label class="col-sm-2 control-label">No. of Truck Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="noOfTruckLimitation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                    <label class="col-sm-2 control-label">No. of Baidu Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="noOfBaiduLimitation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                </div>
	                <div class="form-group">
	                	<label class="col-sm-2 control-label">No. of DiDi Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="noOfDidiLimitation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                    <label class="col-sm-2 control-label">No. of DaDa Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="noOfDadaLimitation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                </div>
	                <div class="form-group">
	                	<label class="col-sm-2 control-label">Reserve</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="reserve" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                    <label class="col-sm-2 control-label">Vehicle Weight Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="largeCarModel" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                </div>
	                <div class="form-group">
	                	<label class="col-sm-2 control-label">piece capacity(p)</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="maxOperateNum" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                </div>
	                
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                <input type="submit" id="saveBtn" class="btn btn-primary" value="Save" />
	            </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<!--Edit info-->
<div class="modal fade" id="editUserModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit dot information</h4>
            </div>
            <form id="editUserForm" class="form-horizontal">
	            <div class="modal-body">
	            	<input type="hidden" name="id" id="siteId" value="">
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">depot ID</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteCode" id="siteCode" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                    <label class="col-sm-2 control-label">longitude</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteLongitude" id="siteLongitude" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">latitude</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteLatitude" id="siteLatitude" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                    <label class="col-sm-2 control-label">depot name</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteName" id="siteName" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">depot address</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" name="siteAddress" id="siteAddress" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">depot area(㎡)</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="siteArea" id="siteArea" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                    <label class="col-sm-2 control-label">depot type</label>
	                    <div class="col-sm-4">
	                        <select type="text" class="form-control" name="siteType" id="siteType" >
	                        	<option value="Super Depot">Super Depot</option>
	                        	<option value="None-SD">None-SD</option>
	                        </select>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">To Super Depot</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="distribCenter" id="distribCenter" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                    </div>
	                    <label class="col-sm-2 control-label">Night Operation Y/N</label>
	                    <div class="col-sm-4">
	                        <select type="text" class="form-control" name="siteNightDelivery" id="siteNightDelivery" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
	                        	<option value="N">N</option>
	                        	<option value="Y">Y</option>
	                        </select>
	                    </div>
	                    
	                </div>
	                <div class="form-group">
	                	<label class="col-sm-2 control-label">No. of Truck Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="noOfTruckLimitation" id="noOfTruckLimitation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                    <label class="col-sm-2 control-label">No. of Baidu Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="noOfBaiduLimitation" id="noOfBaiduLimitation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                </div>
	                <div class="form-group">
	                	<label class="col-sm-2 control-label">No. of DiDi Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="noOfDidiLimitation" id="noOfDidiLimitation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                    <label class="col-sm-2 control-label">No. of DaDa Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="noOfDadaLimitation" id="noOfDadaLimitation" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                </div>
	                <div class="form-group">
	                	<label class="col-sm-2 control-label">Reserve</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="reserve" id="reserve" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                    <label class="col-sm-2 control-label">Vehicle Weight Limitation</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="largeCarModel" id="largeCarModel" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                </div>
	                <div class="form-group">
	                	<label class="col-sm-2 control-label">piece capacity(p)</label>
	                    <div class="col-sm-4">
	                        <input type="text" class="form-control" name="maxOperateNum" id="maxOperateNum" required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
	                    </div>
	                </div>
	                
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                <input type="submit" id="editBtn" class="btn btn-primary" value="Save" />
	            </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<!--Del info-->
<div class="modal fade" id="modal-info">
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
                <button type="button" id="modal-infodelBtn" class="btn btn-primary">Yes</button>
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
        <form  method="post" id="cond-input-form-info" enctype="multipart/form-data">
  
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Depots Info" Settings:</div>
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
    		<div class="col-sm-6 text-right">Export "Depots Info" Settings:</div>
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
<script type="text/javascript" src="/static/js/Xboost/ScenariosName.js" ></script>
<script type="text/javascript">
	$(function  () {
		(function  () {
			$('#nav-Conditions').addClass("active");
		})()
	});
</script>
<script>
	$(function  () {
		var table = $('#Depots_Info').DataTable();
		$(document).on("click","#b",function  () {
			var val = $('#a').val();
			table.search(val).draw(false);
		});
	});
</script>
</body>

</html>
