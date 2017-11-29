<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Xboost System Login</title>

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
	        		<p>All Users</p>
	        	</div>
	        </div>
	        <div class="cond-top">
	        </div>
	    	<div>
	    		<!--Depots Info-->
			    <div class="table-responsive active">
			    	<a href="javascript:;" id="addNewUser-user" class="btn btn-success btn-xs pull-right"><i class="fa fa-plus"></i>Add New User</a>
		            <table id="userTable" class="table table-striped table-bordered table-hover">
			            <thead>
			            	<tr>
			            		<th>ID</th>
				                <th>Username</th>
				                <th>Permission</th>
				                <th>Last Login</th>
				                <th>Current status</th>
				                <th>operation</th>
			            	</tr>
			            </thead>
			            <tbody id="cond-tbody">
			            	<tr>
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








<div class="modal fade" id="newUserModal-dist">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Adding dot information</h4>
            </div>
            <div class="modal-body">
                <form id="newUserForm-dist" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">pickup depot</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="siteCollect" >
                        </div>
                        <label class="col-sm-2 control-label">delivery depot</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="siteDelivery" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">transportation distance(km)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="carDistance" >
                        </div>
                        <label class="col-sm-2 control-label">night transportation time(min)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationNightDelivery" >
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="saveBtn-dist" class="btn btn-primary">Save</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="editUserModal-dist">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit dot information</h4>
            </div>
            <div class="modal-body">
                <form id="editUserForm-dist" class="form-horizontal">
                    <input type="hidden" name="id" id="siteId-dist" value="">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">pickup depot</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="siteCollect" id="siteCollect">
                        </div>
                        <label class="col-sm-2 control-label">delivery depot</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="siteDelivery" id="siteDelivery">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">transportation distance(km)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="carDistance" id="carDistance">
                        </div>
                        <label class="col-sm-2 control-label">transportation distance(km)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationNightDelivery" id="durationNightDelivery">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="editBtn-dist" class="btn btn-primary">Save</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->



<div class="modal fade" id="modal-dist">
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
                <button type="button" id="modal-distdelBtn" class="btn btn-primary">Yes</button>
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
			$('#UserManage').addClass("active");
		})()
	});
</script>
</body>

</html>