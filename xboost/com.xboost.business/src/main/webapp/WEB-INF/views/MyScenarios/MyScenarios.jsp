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
	        		<p><shiro:principal property="username"/>′s Scenarios</p>
	        	</div>
	        	<div class="xb-fr">
	        		<button id="btn-input" data-toggle="modal" data-target=".bs-example-modal-input"><span class="icon-download"></span>Import Scenario</button>
	        		<button id="addNewUser-scen" ><span class="icon-file-text-o"></span>Create Scenario</button>
	        	</div>
	        </div>
	        <div style="position: relative;">
		        <div class="cond-top">
		        </div>
		    	<div>
		    		<!--Depots Info-->
				    <div class="table-responsive active">
			            <table id="MyScenarios" class="table table-striped table-bordered table-hover">
				            <thead>
				            	<tr>
				            		<th>ID</th>
				            		<th>useID</th>
					                <th>Scenario Name</th>
					                <th>Category</th>
					                <th>Description</th>
					                <th>mode</th>
					                <th>out</th>
					                <th>Last Opened</th>
					                <th>Current Status</th>
					                <th>Operation</th>
				            	</tr>
				            </thead>
				            <tbody id="scen-tbody">
				            </tbody>
			            </table>
			        </div>
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
        <form  method="post" id="cond-input-form-dist" enctype="multipart/form-data">
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import Failed</div>
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
	        <button type="button" class="btn btn-primary" id="cond-file-upload-dist">Upload</button>
	    </div>
    </div>
  </div>
</div>








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
	                        <input type="text" class="form-control" name="scenariosName"  required oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');" >
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-4 control-label">Category*:</label>
	                    <div class="col-sm-7">
	                    	<input type="hidden" name="scenariosCategory"  />
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


<div class="modal fade" id="modal-del">
    <div class="modal-dialog modal-del">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Delete Scenario</h4>
            </div>
            <div class="modal-body">
                <p>Are you sure want to delete this Scenario?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                <button type="button" id="modal-delBtn" class="btn btn-primary">Yes</button>
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
<script src="/static/js/flex.js"></script>
<script src="/static/js/Xboost/xbMain.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/Xboost/ScenariosName.js" ></script>
<script type="text/javascript">
	$(function  () {
		(function  () {
			$('#after-content').addClass("active");
		})()
	});
</script>
</body>

</html>
