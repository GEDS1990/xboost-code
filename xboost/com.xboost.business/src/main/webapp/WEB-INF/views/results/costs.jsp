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
    <link rel="stylesheet" href="/static/css/bootstrap.min.css" />

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
	        		<p>${sessionScope.openScenariosName}(Results)</p>
	        	</div>
	        	<div class="xb-fr">
	        		<button id="btn-export" data-toggle="modal" data-target=".bs-example-modal-export"><span class="icon-upload"></span>Expot Results</button>
	        	</div>
	        </div>
	        <div class="cond-top">
	        	<ul class="cond-top-ul clearfix">
	        		<li><a href="/depots">Depots</a></li>
	        		<li><a href="/route">Route</a></li>
	        		<li><a href="/vehicles">Vehicles</a></li>
	        		<li class="active"><a class="active" href="/costs">Costs</a></li>
	        		<li><a href="#">Efficiency</a></li>
	        		<li><a href="#">Distribution</a></li>
	        	</ul>
	        	<!--<ul class="cond-top-ul clearfix">
	        		<li class="active"><a class="active" href="/route">Solution Route</a></li>
	        		<li><a href="#">Solution Activity</a></li>
	        		<li><a href="#">Solution Stats</a></li>
	        		<li><a href="#">Solution Arrinfos</a></li>
	        		<li><a href="#">Solution Jobinfos</a></li>
	        	</ul>-->
	        </div>
	        
	        
	        
	    	<div id="route-wrap">
	    		<header class="clearfix result-header">
	    			<div class="xb-fl">
		    			<p style="color: #000;">Costs of <span>串点模型</span></p>
	    			</div>
	    			<div class="xb-fr">
	    				<button><span class="icon-upload"></span>Export this Sheet</button>
	    			</div>
	    		</header>
	    		<div class="cost-plan-choose">
	    			<span class="cost-choose">Choose Plan </span>
	    			<select>
	    				<option>Plan A</option>
	    				<option>Plan B</option>
	    			</select>
	    			<span> 选中的Plan的数据会被用于其它页面的展示</span>
	    		</div>
	    		<div class="result-cost clearfix">
	    			<div class="result-cost-item itemstyle" >
	    				<form id="cost-form-a" v-show="cseen">
	    					<h1>Plan A</h1>
		    				<div class="cost-item text-left">
		    					<div class="item-box">
		    						<h1>人效</h1>
		    						<ul class="item-content">
		    							<li>网点集散点人效 (p): <input type="number"  min="0" v-model="sitePeople"/></li>
		    							<li>集配站集散点人效 (p): <input type="number"  min="0" v-model="collectPeople"/></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>人员配备</h1>
		    						<ul class="item-content">
		    							<li>支线depot&distrib.center数量: <span>{{depotcount}}</span></li>
		    							<li>每个支线depot/distrib.center的人数: <input type="number"  min="0" v-model="depotPeoplecount" /></li>
		    							<li>支线depot&distrib.center总人数: <span>{{depotAllPeople}}</span></li>
		    							<li>Full-time Staff: <input type="number"  min="0" v-model="full_staff"/></li>
		    							<li>Part-time Staff: <input type="number"  min="0" v-model="part_staff"/></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>工资设定</h1>
		    						<ul class="item-content">
		    							<li>Full-time salary: <input type="number" min="0" v-model="full_salaty" /> <span>/month</span></li>
		    							<li>Full-time working days: <input type="number"  min="0" max="30" v-model="full_days"/> <span>/month</span></li>
		    							<li>Part-time wage: <input type="number" min="0" max="24" v-model="part_wage"/> <span>/hour</span></li>
		    							<li>Part-time working hours: <input type="number" min="0" v-model="part_work"/> <span>/month</span></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>成本</h1>
		    						<ul class="item-content">
		    							<li>支线depot&distrib.center单日人工成本: <span>{{day_p_cost}}</span></li>
		    							<li>单日总体人工成本: <span>{{day_allp_cost}}</span> <span> (per piece)</span></li>
		    							<li>支线运输成本: <span>{{line_cost}}</span> <span> (per piece)</span></li>
		    						</ul>
		    						<p>总成本: {{allcost}} <span> (per piece)</span></p>
		    					</div>
		    				</div>
	    				</form>
	    				
	    			</div>
	    			<div class="result-cost-item" >
	    				<form id="cost-form-b" v-show="cseen">
	    					<h1>Plan B</h1>
		    				<div class="cost-item text-left">
		    					<div class="item-box">
		    						<h1>人效</h1>
		    						<ul class="item-content">
		    							<li>网点集散点人效 (p): <input type="number"  min="0" v-model="sitePeople"/></li>
		    							<li>集配站集散点人效 (p): <input type="number"  min="0" v-model="collectPeople"/></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>人员配备</h1>
		    						<ul class="item-content">
		    							<li>支线depot&distrib.center数量: <span id="site-count">{{depotcount}}</span></li>
		    							<li>每个支线depot/distrib.center的人数: <input type="number"  min="0" v-model="depotPeoplecount" /></li>
		    							<li>支线depot&distrib.center总人数: <span>{{depotAllPeople}}</span></li>
		    							<li>Full-time Staff: <input type="number"  min="0" v-model="full_staff"/></li>
		    							<li>Part-time Staff: <input type="number"  min="0" v-model="part_staff"/></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>工资设定</h1>
		    						<ul class="item-content">
		    							<li>Full-time salary: <input type="number" min="0" v-model="full_salaty" /> <span>/month</span></li>
		    							<li>Full-time working days: <input type="number"  min="0" max="30" v-model="full_days"/> <span>/month</span></li>
		    							<li>Part-time wage: <input type="number" min="0" max="24" v-model="part_wage"/> <span>/hour</span></li>
		    							<li>Part-time working hours: <input type="number" min="0" v-model="part_work"/> <span>/month</span></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>成本</h1>
		    						<ul class="item-content">
		    							<li>支线depot&distrib.center单日人工成本: <span>{{day_p_cost}}</span></li>
		    							<li>单日总体人工成本: <span>{{day_allp_cost}}</span> <span> (per piece)</span></li>
		    							<li>支线运输成本: <span>{{line_cost}}</span> <span> (per piece)</span></li>
		    						</ul>
		    						<p>总成本: {{allcost}} <span> (per piece)</span></p>
		    					</div>
		    				</div>
	    				</form>
	    			</div>
	    		</div>
	    		
	    		<div class="cost-plan-choose alt">
	    			<span class="cost-choose">Choose Plan </span>
	    			<select>
	    				<option>Plan A</option>
	    				<option>Plan B</option>
	    			</select>
	    			<span> 选中的Plan的数据会被用于其它页面的展示</span>
	    		</div>
	    		
	    	
	    	
	    	
	    		<!--Solution Route-->
			    
	    	</div>
	    </div>
	    <!-- /#page-wrapper -->
	</div>
</div>
<!-- /#wrapper -->



<!--Model export-->
<div class="modal fade bs-example-modal-export" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" data-backdrop="static">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
		<div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	    </div>
	    
        <!--content s-->
        
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Depots Info"</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="Depots_Info">
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
<script src="/static/js/datatables/media/js/jquery.dataTables.js"></script>
<script src="/static/js/datatables/media/js/dataTables.bootstrap.min.js"></script>
<script src="/static/js/tableExporter.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/flex.js"></script>
<script type="text/javascript" src="/static/js/vue.min.js" ></script>
<script type="text/javascript" src="/static/js/Xboost/costs.js" ></script>
<script type="text/javascript">
	$(function  () {
		(function  () {
			$('#nav-Results').addClass("active");
		})()
	});
</script>
</body>

</html>
