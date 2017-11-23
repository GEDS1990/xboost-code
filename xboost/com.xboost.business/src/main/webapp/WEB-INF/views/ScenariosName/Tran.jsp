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
	        		<li class="active"><a  class="active" href="/transport">Transportation</a></li>
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
					    	<!--<tr>
					    		<td rowspan="3">truck<br/>weight limit</td>
					    		<td rowspan="3">truck<br/>quantity</td>
					    		<td rowspan="3">truck<br/>type</td>
					    		<td rowspan="3">speed<br/>(km/h)</td>
					    		<td rowspan="3">maximum<br/>distance (km)</td>
					    		<td rowspan="3">maximum<br/>load</td>
					    		<td rowspan="3">full load<br/>unload time (min)</td>
					    	</tr>
					    	<tr>
					    		<td colspan="6">truck cost (ride or km)</td>
					    		<td colspan="6">single piece cost</td>
					    	</tr>
					    	<tr>
					    		<td colspan="2">flag-fall (0,a] km</td>
					    		<td colspan="2">(a,b] km</td>
					    		<td colspan="2">(b,c] km</td>
					    		<td colspan="2">flag-fall (0,a] km</td>
					    		<td colspan="2">(a,b] km</td>
					    		<td colspan="2">(b,c] km</td>
					    	</tr>-->
					    	<!--<tr>
					    		<th rowspan="3">Id</th>
					    		<th rowspan="3">truck<br/>weight limit</th>
					    		<th rowspan="3">truck<br/>quantity</th>
					    		<th rowspan="3">truck<br/>type</th>
					    		<th rowspan="3">speed<br/>(km/h)</th>
					    		<th rowspan="3">maximum<br/>distance (km)</th>
					    		<th rowspan="3">maximum<br/>load</th>
					    		<th rowspan="3">full load<br/>unload time (min)</th>
					    		<th colspan="3">truck cost (ride or km)</th>
					    		<th colspan="3">single piece cost</th>
					    		<th rowspan="3">operation</th>
						    	<tr>
						    		<th rowspan="2">flag-fall (0,a] km</th>
						    		<th rowspan="2">(a,b] km</th>
						    		<th rowspan="2">(b,c] km</th>
						    	</tr>
						    	<tr>
						    		<th rowspan="2">flag-fall (0,a] km</th>
						    		<th rowspan="2">(a,b] km</th>
						    		<th rowspan="2">(b,c] km</th>
						    	</tr>
					    	</tr>-->
					    	<tr>
					    		<th>Id</th>
					    		<th>truck<br/>weight limit</th>
					    		<th>truck<br/>quantity</th>
					    		<th>truck<br/>type</th>
					    		<th>speed<br/>(km/h)</th>
					    		<th>maximum<br/>distance (km)</th>
					    		<th>maximum<br/>load</th>
					    		<th>full load<br/>unload time (min)</th>
					    		<th>flag-fall (0,a] km</th>
					    		<th>(a,b] km</th>
					    		<th>(b,c] km</th>
					    		<th>flag-fall (0,a] km</th>
					    		<th>(a,b] km</th>
					    		<th>(b,c] km</th>
					    		<th>operation</th>
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
	        <button type="button" class="btn btn-primary" id="cond-file-upload">Upload</button>
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
                        <label class="col-sm-2 control-label">siteCode</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteCode" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">longitude</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteLongitude" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">latitude</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteLatitude" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">name</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteName" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">address</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteAddress" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">area</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteArea" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">type</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteType" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">distrib.center</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteNightDelivery" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">truck quantity limit</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="carNum" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">truck weight limit</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="largeCarModel" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">capacity</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="maxOperateNum" >
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


<div class="modal fade" id="editUserModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit dot information</h4>
            </div>
            <div class="modal-body">
                <form id="editUserForm" class="form-horizontal">
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
                <button type="button" id="editBtn" class="btn btn-primary">Save</button>
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
