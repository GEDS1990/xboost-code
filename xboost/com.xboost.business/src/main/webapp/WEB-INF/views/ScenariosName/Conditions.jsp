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
    <link rel="stylesheet" href="../../../static/css/Xboost/icon.css" />
    <link rel="stylesheet" href="../../../static/css/Xboost/xb_main.css" />
    <link rel="stylesheet" href="../../../static/css/Xboost/ScenariosName.css" />

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
	        		<li class="active"><a class="active" href="#">Depots Info</a></li>
	        		<li><a href="#">Depots Distance</a></li>
	        		<li><a href="#">Transportation</a></li>
	        		<li><a href="#">Demands</a></li>
	        		<li><a href="#">Parameters</a></li>
	        	</ul>
	        </div>
	    	<div>
	    		<!--Depots Info-->
			    <div class="table-responsive active">
			    	<a href="javascript:;" id="addNewUser" class="btn btn-success btn-xs pull-right"><i class="fa fa-plus"></i> 新增</a>
		            <table id="Depots_Info" class="table table-striped table-bordered table-hover">
					    <thead>
					    	<!--<tr>
					    		<th rowspan="3">#</th>
					    		<th rowspan="3">ID</th>
					    		<th rowspan="3">longitude</th>
					    		<th rowspan="3">latitude</th>
					    		<th rowspan="3">name</th>
					    		<th rowspan="3">address</th>
					    		<th rowspan="3">area</th>
					    		<th rowspan="3">type</th>
					    		<th rowspan="3">distrib<br/>.center</th>
					    	</tr>
					    	<tr>
					    		<th colspan="2">10 mins</th>
					    		<th rowspan="3">capacity</th>
					    	</tr>
					    	<tr>
					    		<th>truck quantity limit</th>
					    		<th>truck weight limit</th>
					    	</tr>-->
					    	<tr>
					    		<th>#</th>
					    		<th>ID</th>
					    		<th>longitude</th>
					    		<th>latitude</th>
					    		<th>name</th>
					    		<th>address</th>
					    		<th>area</th>
					    		<th>type</th>
					    		<th>distrib.center</th>
					    		<th>truck quantity limit</th>
					    		<th>truck weight limit</th>
					    		<th>capacity</th>
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
		        
		        <!--Depots Distance-->
		        <div class="table-responsive">
		            <table id="Depots_Distance" class="table table-striped table-bordered table-hover">
			            <thead>
			            	<tr>
				                <th>pickup depot</th>
				                <th>delivery depot</th>
				                <th>transportation distance(km)</th>
				                <th>night transportation time(min)</th>
			            	</tr>
			            </thead>
			            <tbody id="cond-tbody">
			            	<tr>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
			            	</tr>
			            </tbody>
		            </table>
		        </div>
		        
		        <!--Transportation-->
		        <div class="table-responsive">
					<table id="Transportation" class="table table-striped table-bordered table-hover">
					    <thead>
					    	<tr>
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
					    	</tr>
					    </thead>
					    <tbody>
					    	<tr>
					    		<td rowspan="3"></td>
					    		<td rowspan="3"></td>
					    		<td rowspan="3"></td>
					    		<td rowspan="3"></td>
					    		<td rowspan="3"></td>
					    		<td rowspan="3"></td>
					    		<td rowspan="3"></td>
					    	</tr>
					    	<tr>
					    		<td colspan="2"></td>
					    		<td colspan="2"></td>
					    		<td colspan="2"></td>
					    		<td colspan="2"></td>
					    		<td colspan="2"></td>
					    		<td colspan="2"></td>
					    	</tr>
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
					    	</tr>
					    </tbody>
					</table>
				</div>
				
		        <!--demands-->
	            <div class="table-responsive">
		            <table id="Demands" class="table table-striped table-bordered table-hover">
			            <thead>
			            	<tr>
				                <th>date</th>
				                <th>pickup depot</th>
				                <th>delivery depot</th>
				                <th>product type</th>
				                <th>time</th>
				                <th>weight</th>
				                <th>piece</th>
				                <th>effectiveness</th>
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
			            	</tr>
			            </tbody>
		            </table>
		        </div>
		        
		        <!--Parameters-->
		        <div class="table-responsive">
		            <table id="Patameters" class="table table-striped table-bordered table-hover">
			            <thead>
			            	<tr>
				                <th>parameter name</th>
				                <th>data</th>
				                <th>notes</th>
			            	</tr>
			            </thead>
			            <tbody>
			            	<tr>
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
        <form id="cond-input-form">
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import All Data From Single File</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="fileAll" class="cond_file" />
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
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Depots Info"</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="fileiInfo" class="cond_file"/>
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
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Depots Distance"</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="fileDistance" class="cond_file"/>
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
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Transportation"</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="fileTran" class="cond_file"/>
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
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Demands"</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="fileDemands" class="cond_file"/>
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
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Parameters"</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="filePara" class="cond_file"/>
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
	        <button id="cond-file-upload" type="button" class="btn btn-primary">Upload</button>
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
    		<div class="col-sm-6 text-right">Export All Data to Single File</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="ALL_Data">
        			<span class="icon-upload"></span>
        			Export All Data
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Depots Info"</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="Depots_Info">
        			<span class="icon-upload"></span>
        			Export 
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
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
    	</div>
        <!--content e-->
        
        <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	    </div>
    </div>
  </div>
</div>







<div class="modal fade" id="newUserModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">添加用户</h4>
            </div>
            <div class="modal-body">
                <form id="newUserForm" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">ID</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="username" id="site_code">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">longitude</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="longitude">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">latitude</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="latitude">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">name</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">address</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_address">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">area</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_area">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">type</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_type">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">distrib.center</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_nightDelivery">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">truck quantity limit</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="car_num">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">truck weight limit</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="large_carModle">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">capacity</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="max_operate_num">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" id="saveBtn" class="btn btn-primary">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="editUserModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">编辑用户</h4>
            </div>
            <div class="modal-body">
                <form id="editUserForm" class="form-horizontal">
                    <input type="hidden" name="id" id="id">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">ID</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="username" id="site_code">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">longitude</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="longitude">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">latitude</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="latitude">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">name</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">address</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_address">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">area</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_area">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">type</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_type">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">distrib.center</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="site_nightDelivery">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">truck quantity limit</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="car_num">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">truck weight limit</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="large_carModle">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">capacity</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel" id="max_operate_num">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" id="editBtn" class="btn btn-primary">保存</button>
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
<script src="../../../static/js/tableExporter.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/Xboost/navMain.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="../../../static/js/Xboost/ScenariosName.js" ></script>
<script type="text/javascript">
	$(function  () {
		(function  () {
			$('.nav.xb_alt>li:first-child>ul.xb-nav_ul>li:first-child').addClass("active");
		})()
	});
</script>
<script>
    $(function(){
        var dt = $("#Depots_Info").DataTable({
            "processing": true, //loding效果
            "serverSide":true, //服务端处理
            "searchDelay": 1000,//搜索延迟
            "order":[[0,'desc']],//默认排序方式
            "lengthMenu":[5,10,25,50,100],//每页显示数据条数菜单
            "ajax":{
                url:"/account/users.json", //获取数据的URL
                type:"get" //获取数据的方式
            },
            "columns":[  //返回的JSON中的对象和列的对应关系
                {"data":"id","name":"id"},
                {"data":"site_code"},
                {"data":"longitude"},
                {"data":"latitude"},
                {"data":"site_name"},
                {"data":"site_address"},
                {"data":"site_area"},
                {"data":"site_type"},
                {"data":"site_nightDelivery"},
                {"data":"car_num"},
                {"data":"large_carModle"},
                {"data":"max_operate_num"},
                {"data":function(row){
                    return "<a href='javascript:;' class='editLink' data-id='"+row.id+"'>编辑</a> <a href='javascript:;' class='delLink' data-id='"+row.id+"'>删除</a>";
                }}
            ],
            "columnDefs":[ //具体列的定义
                {
                    "targets":[0],
                    "visible":true
                },
                {
                    "targets":[3],
                    "orderable":false
                },
                {
                    "targets":[0,1,2,4,5,6,7,8,9,10,11,12,13],
                    "orderable":false
                }
            ],
            "language":{
                "lengthMenu":"显示 _MENU_ 条记录",
                "search":"搜索:",
                "info": "从 _START_ 到 _END_ 共 _TOTAL_ 条记录",
                "processing":"加载中...",
                "zeroRecords":"暂无数据",
                "infoEmpty": "从 0 到 0 共 0 条记录",
                "infoFiltered":"(从 _MAX_ 条记录中读取)",
                "paginate": {
                    "first":      "首页",
                    "last":       "末页",
                    "next":       "下一页",
                    "previous":   "上一页"
                }
            }
        });

        //添加新用户
        $("#addNewUser").click(function(){
            $("#newUserModal").modal('show');
        });
        $("#saveBtn").click(function(){
            $.post("/account/new",$("#newUserForm").serialize())
                    .done(function(result){
                        if("success" == result) {
                            $("#newUserForm")[0].reset();
                            $("#newUserModal").modal("hide");
                            dt.ajax.reload();
                        }
                    }).fail(function(){
                        alert("添加时出现异常");
                    });

        });

        //删除用户
        $(document).delegate(".delLink","click",function(){
            var id = $(this).attr("data-id");
            if(confirm("确定要删除该数据吗?")) {
                $.post("/account/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                    }
                }).fail(function(){
                    alert("删除出现异常");
                });

            }
        });

        //编辑用户
        $(document).delegate(".editLink","click",function(){
            $("#editUserForm")[0].reset();
            var id = $(this).attr("data-id");
            $.get("/account/user.json",{"id":id}).done(function(result){
                $("#id").val(result.id);
                $("#site_code").val(result.site_code);
                $("#longitude").val(result.longitude);
                $("#latitude").val(result.latitude);
                $("#site_name").val(result.site_name);
                $("#site_address").val(result.site_address);
                $("#site_area").val(result.site_area);
                $("#site_type").val(result.site_type);
                $("#site_nightDelivery").val(result.site_nightDelivery);
                $("#car_num").val(result.car_num);
                $("#large_carModle").val(result.large_carModle);
                $("#max_operate_num").val(result.max_operate_num);

            }).fail(function(){

            });

            $("#editUserModal").modal("show");
        });

        $("#editBtn").click(function(){

            $.post("/account/edit",$("#editUserForm").serialize()).done(function(result){
                if(result == "success") {
                    $("#editUserModal").modal("hide");
                    dt.ajax.reload();
                }
            }).fail(function(){
                alert("修改用户异常");
            });

        });
    });
</script>
</body>

</html>
