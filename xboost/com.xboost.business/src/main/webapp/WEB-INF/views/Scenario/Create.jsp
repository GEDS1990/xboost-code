<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Xboost-物流规划管理系统</title>
    <!-- Bootstrap Core CSS -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="/static/js/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/static/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/static/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/static/js/morris/morris.css">
    <link rel="stylesheet" href="../../../static/css/xb_main.css" />

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
		    <div id="page-wrapper">
		        <h1 class="xb_sce_h1">Scene information</h1>
		        <div class="clearfix">
			        <div class="xb_sce_left">
				        <div class="input-group">
						  <input type="text" class="form-control" placeholder="Scene name" aria-describedby="basic-addon2">
						  <span class="input-group-addon " id="basic-addon2"><span class="xb_search_icon glyphicon glyphicon-search"></span></span>
						</div>
					</div>
					<div class="xb_sce_right">
						<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#Modal_table">Create</button>
						<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">Import</button>
					</div>
				</div>
				<div class="xb_create_record">
					<div class="panel panel-default">
					  <div class="panel-heading">Historical record</div>
					  <!-- Table -->
					  	<table class="table">
					        <thead>
					          <tr>
					            <th>Number</th>
					            <th>Scene Name</th>
					            <th>Scene describe</th>
					            <th>Scene model</th>
					            <th>Scene output</th>
					            <th>Operation</th>
					          </tr>
					        </thead>
					        <tbody>
					          <tr>
					            <th scope="row">1</th>
					            <td>Mark</td>
					            <td>Otto</td>
					            <td>#Fff</td>
					            <td>@mdo</td>
					            <td>
					            	<ul class="xb_tb_ul clearfix">
					            		<li><a href="#">Detail</a></li>
					            		<li><a href="#">Modify</a></li>
					            		<li><a href="#">Del</a></li>
					            		<li><a class="export_xls" href="#">Export</a></li>
					            	</ul>
					            </td>
					          </tr>
					          <tr>
					            <th scope="row">2</th>
					            <td>Jacob</td>
					            <td>Thornton</td>
					            <td>@fat</td>
					            <td>@mdo</td>
					            <td>
					            	<ul class="xb_tb_ul clearfix">
					            		<li><a href="#">Detail</a></li>
					            		<li><a href="#">Modify</a></li>
					            		<li><a href="#">Del</a></li>
					            		<li><a class="export_xls" href="#">Export</a></li>
					            	</ul>
					            </td>
					          </tr>
					          <tr>
					            <th scope="col">3</th>
					            <td>sdfdsf</td>
					            <td>tddird</td>
					            <td>@faffft</td>
					            <td>@mrrrrdo</td>
					            <td>
					            	<ul class="xb_tb_ul clearfix">
					            		<li><a href="#">Detail</a></li>
					            		<li><a href="#">Modify</a></li>
					            		<li><a href="#">Del</a></li>
					            		<li><a class="export_xls" href="#">Export</a></li>
					            	</ul>
					            </td>
					          </tr>
					          <tr>
					            <th scope="col">4</th>
					            <td>Larr111y</td>
					            <td>the gfgBird</td>
					            <td>@twitdfter</td>
					            <td>@mdssssso</td>
					            <td>
					            	<ul class="xb_tb_ul clearfix">
					            		<li><a href="#">Detail</a></li>
					            		<li><a href="#">Modify</a></li>
					            		<li><a href="#">Del</a></li>
					            		<li><a class="export_xls" href="#">Export</a></li>
					            	</ul>
					            </td>
					          </tr>
					        </tbody>
				    	</table>
					</div>
				</div>
		    </div>
	    </div>
	    <!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	
	<!--model upload-->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Upload files</h4>
	      </div>
	      <div class="modal-body">
	      	<input type="text" id="xb_file" disabled="false" value=""/><button id="xb_btn_up" type="button" class="btn btn-default dropdown-toggle" >Choice</button>
	        <input type="file" id="file"  />
	        <button id="xb_btn_upload" type="button" class="btn btn-default dropdown-toggle"  aria-haspopup="true" aria-expanded="false">Upload</button>
	        <div id="fileName"></div>
		    <div id="fileSize"></div>
		    <div id="fileType"></div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary">Confim</button>
	      </div>
	    </div>
	  </div>
	</div>
	<!--model tables-->
	<div class="modal fade" id="Modal_table" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Choice type</h4>
	      </div>
	      <div class="modal-body">
	      	<div class="mod_tab">
	      		<table class="table">
			        <thead>
			          <tr>
			            <th>Scene Name</th>
			            <th>Scene describe</th>
			            <th>Scene model</th>
			            <th>Scene output</th>
			          </tr>
			        </thead>
			        <tbody>
			          <tr>
			            <td></td>
			            <td></td>
			            <td></td>
			            <td></td>
			          </tr>
			        </tbody>
			    </table>
			    <div class="mod_type">
			    	<label>
						<input type="radio" value="xls"/>xls
					</label>
			    </div>
	      	</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary">Confim</button>
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
	<script src="../../../static/js/tableExporter.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$(function() {
			var $this = $(".xb_alt>li:first-child")
			$this.find("ul").css("display","block");
			$this.find("span").attr("class","glyphicon glyphicon-triangle-bottom");
			$this.find("li").eq(0).addClass("active");
		});
	</script>

</body>

</html>
