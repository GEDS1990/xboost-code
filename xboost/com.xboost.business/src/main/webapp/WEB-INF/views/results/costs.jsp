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
	        		<button id="btn-export" data-toggle="modal" data-target=".bs-example-modal-export"><span class="icon-upload"></span>Export Results</button>
	        	</div>
	        </div>
	        <div class="cond-top">
	        	<ul class="cond-top-ul clearfix">
	        		<li><a href="/depots">Depots</a></li>
	        		<li><a href="/route">Route</a></li>
	        		<li><a href="/vehicles">Vehicles</a></li>
	        		<li class="active"><a class="active" href="/costs">Costs</a></li>
	        		<li><a href="/efficiency">Efficiency</a></li>
	        		<li><a href="/distribution">Distribution</a></li>
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
		    			<p style="color: #000;">Costs of <span id="model-type"></span></p>
	    			</div>
	    			<div class="xb-fr">
	    				<!--<button><span class="icon-upload"></span>Export this Sheet</button>-->
	    			</div>
	    		</header>
	    		<div class="cost-plan-choose">
	    			<span class="cost-choose">Choose Plan </span>
	    			<select id="costs-choose">
	    				<option value="a">Plan A</option>
	    				<option value="b">Plan B</option>
	    			</select>
	    			<button class="cost-btn">submit</button>
	    			<span> 选中的Plan的数据会被用于其它页面的展示</span>
	    		</div>
	    		<div id="costs" class="result-cost clearfix">
	    			<div class="result-cost-item itemstyle" >
	    				<form id="cost-form-a">
	    					<input type="hidden" name="plan" value="A" />
	    					<h1>Plan A</h1>
		    				<div class="cost-item text-left">
		    					<div class="item-box">
		    						<h1>Sorting Efficiency</h1>
		    						<ul class="item-content">
		    							<li>Sorting Efficiency in Depot: <input type="number" name="sitePeopleWork"  min="0" v-model="sitePeople"/></li>
		    							<li>Sorting Efficiency in Second Layer Hub: <input type="number" name="distribPeopleWork" min="0" v-model="collectPeople"/></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>Staffing Arrangement</h1>
		    						<!--serial-->
		    						<ul class="item-content" v-show="serialSeen">
		    							<li>No. of Dummy Hub: <span>{{depotcount}}</span></li>
		    							<input type="hidden" name="siteCount" v-bind:value="depotcount" />
		    							<li>No. of Staff per Dummy Hub: <input type="number"  min="0" v-model="depotPeoplecount" /></li>
		    							<input type="hidden" name="peopleNumPerSite"  v-bind:value="depotPeoplecount"/>
		    							<li>Total Staff: <span>{{depotAllPeople}}</span></li>
										<input type="hidden"  v-bind:value="depotAllPeople" />
		    							<li>Full-time Staff: <input type="number" name="fullTimeStaff" min="0" v-model="full_staff"/></li>
		    							<li>Part-time Staff: <input type="number" name="partTimeStaff" min="0" v-model="part_staff"/></li>
		    						</ul>
		    						<!--relay-->
		    						<ul class="item-content" v-show="relaySeen" v-for="site in sitelist">
		    							<li>Dummy Hub: <span>{{site.siteCode}}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Votes: <span>{{site.totalVol}}</span></li>
		    							<template v-if="site.siteType=='Non Dummy Hub'">
		    								<li><span>{{site.totalVol}}</span> ÷ <span>{{sitePeople}}(Sorting Efficiency in Depot)</span> ≈ <span>{{site.perMan}}</span>
		    							</template>
		    							<template v-else>
		    								<li><span>{{site.totalVol}}</span> ÷ <span>{{collectPeople}}(Sorting Efficiency in Second Layer Hub)</span> ≈ <span>{{site.perMan}}</span>
		    							</template>
		    							<li>Total Staff of Dummy Hub <span>{{site.siteCode}}</span>: <span>{{site.perMan}}</span></li>
		    							<li>Full-time Staff: <input type="number" data-name="fullTimeStaff" min="0" v-bind:data-total="site.perMan" v-model="site.full"  v-on:input="input_full(site)"/></li>
		    							<li>Part-time Staff: <input type="number" data-name="partTimeStaff" min="0" v-bind:data-total="site.perMan" v-model="site.part"  v-on:input="input_part(site)"/></li>
		    						</ul>
		    						<!--mixed-->
		    						<!--<ul class="item-content" v-show="mixedSeen">
		    							<li>No. of Dummy Hub: <span>{{depotcount}}</span></li>
		    							<input type="hidden" name="siteCount" v-bind:value="depotcount" />
		    							<li>No. of Staff per Dummy Hub: <input type="number"  min="0" v-model="depotPeoplecount" /></li>
		    							<input type="hidden" name="peopleNumPerSite"  v-bind:value="depotPeoplecount"/>
		    							<li>Total Staff: <span>{{depotAllPeople}}</span></li>
		    							<li>Full-time Staff: <input type="number" name="fullTimeStaff" min="0" v-model="full_staff"/></li>
		    							<li>Part-time Staff: <input type="number" name="partTimeStaff" min="0" v-model="part_staff"/></li>
		    						</ul>-->
		    						
		    						
		    					</div>
		    					<div class="item-box">
		    						<h1>Salary</h1>
		    						<ul class="item-content">
		    							<li>Full-time salary: <input type="number" name="fullTimeSalary" min="0" v-model="full_salaty" /> <span>RMB/Month</span></li>
		    							<li>Full-time working days: <input type="number" name="fullTimeWorkDay"  min="0" max="30" v-model="full_days"/> <span>Days/Month</span></li>
		    							<li>Part-time wage: <input type="number" min="0" name="partTimeSalary" max="24" v-model="part_wage"/> <span>RMB/Hour</span></li>
		    							<li>Part-time working hours: <input type="number" name="partTimeWorkDay" min="0" v-model="part_work"/> <span>Hours/Day</span></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>Total Cost</h1>
		    						<ul class="item-content">
		    							<li>Total pieces:{{piece}}</li>
		    							<li>Total Staffing Cost: <span>{{day_p_cost}}</span></li>
		    							<li>Staffing Cost per piece: <span>{{day_allp_cost}}</span> <span> (per piece)</span></li>
		    							<li>Transportation Cost per piece: <span>{{line_cost}}</span> <span> (per piece)</span></li>
										<input type="hidden" name="piece" v-bind:value="piece"/>
		    							<input type="hidden" name="sum2" v-bind:value="day_p_cost"/>
		    							<input type="hidden" name="totalDailyLaborCost" v-bind:value="day_allp_cost"/>
		    							<input type="hidden" name="branchTransportCost" v-bind:value="line_cost"/>
		    							<input type="hidden" name="totalCost" v-bind:value="allcost"/>
		    						</ul>
		    						<p>Total Cost per piece: {{allcost}} <span> (per piece)</span></p>
		    						<p>{{a}}</p>
		    					</div>
		    				</div>
	    				</form>
	    				
	    				
	    			</div>
	    			<div class="result-cost-item" >
	    				<form id="cost-form-b" >
	    					<input type="hidden" name="plan" value="A" />
	    					<h1>Plan B</h1>
		    				<div class="cost-item text-left">
		    					<div class="item-box">
		    						<h1>Sorting Efficiency</h1>
		    						<ul class="item-content">
		    							<li>Sorting Efficiency in Depot: <input type="number" name="sitePeopleWork"  min="0" v-model="sitePeople"/></li>
		    							<li>Sorting Efficiency in Second Layer Hub: <input type="number" name="distribPeopleWork" min="0" v-model="collectPeople"/></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>Staffing Arrangement</h1>
		    						<!--serial-->
		    						<ul class="item-content" v-show="serialSeen">
		    							<li>No. of Dummy Hub: <span>{{depotcount}}</span></li>
		    							<input type="hidden" name="siteCount" v-bind:value="depotcount" />
		    							<li>No. of Staff per Dummy Hub: <input type="number"  min="0" v-model="depotPeoplecount" /></li>
		    							<input type="hidden" name="peopleNumPerSite"  v-bind:value="depotPeoplecount"/>
		    							<li>Total Staff: <span>{{depotAllPeople}}</span></li>
										<input type="hidden"  v-bind:value="depotAllPeople" />
		    							<li>Full-time Staff: <input type="number" name="fullTimeStaff" min="0" v-model="full_staff"/></li>
		    							<li>Part-time Staff: <input type="number" name="partTimeStaff" min="0" v-model="part_staff"/></li>
		    						</ul>
		    						<!--relay-->
		    						<ul class="item-content" v-show="relaySeen" v-for="site in sitelist">
		    							<li>Dummy Hub: <span>{{site.siteCode}}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Votes: <span>{{site.totalVol}}</span></li>
		    							<template v-if="site.siteType=='Non Dummy Hub'">
		    								<li><span>{{site.totalVol}}</span> ÷ <span>{{sitePeople}}(Sorting Efficiency in Depot)</span> ≈ <span>{{site.perMan}}</span>
		    							</template>
		    							<template v-else>
		    								<li><span>{{site.totalVol}}</span> ÷ <span>{{collectPeople}}(Sorting Efficiency in Second Layer Hub)</span> ≈ <span>{{site.perMan}}</span>
		    							</template>
		    							<li>Total Staff of Dummy Hub <span>{{site.siteCode}}</span>: <span>{{site.perMan}}</span></li>
		    							<li>Full-time Staff: <input type="number" data-name="fullTimeStaff" min="0" v-bind:data-total="site.perMan" v-model="site.full"  v-on:input="input_full(site)"/></li>
		    							<li>Part-time Staff: <input type="number" data-name="partTimeStaff" min="0" v-bind:data-total="site.perMan" v-model="site.part"  v-on:input="input_part(site)"/></li>
		    						</ul>
		    						<!--mixed-->
		    						<!--<ul class="item-content" v-show="mixedSeen">
		    							<li>No. of Dummy Hub: <span>{{depotcount}}</span></li>
		    							<input type="hidden" name="siteCount" v-bind:value="depotcount" />
		    							<li>No. of Staff per Dummy Hub: <input type="number"  min="0" v-model="depotPeoplecount" /></li>
		    							<input type="hidden" name="peopleNumPerSite"  v-bind:value="depotPeoplecount"/>
		    							<li>Total Staff: <span>{{depotAllPeople}}</span></li>
		    							<li>Full-time Staff: <input type="number" name="fullTimeStaff" min="0" v-model="full_staff"/></li>
		    							<li>Part-time Staff: <input type="number" name="partTimeStaff" min="0" v-model="part_staff"/></li>
		    						</ul>-->
		    						
		    						
		    					</div>
		    					<div class="item-box">
		    						<h1>Salary</h1>
		    						<ul class="item-content">
		    							<li>Full-time salary: <input type="number" name="fullTimeSalary" min="0" v-model="full_salaty" /> <span>RMB/Month</span></li>
		    							<li>Full-time working days: <input type="number" name="fullTimeWorkDay"  min="0" max="30" v-model="full_days"/> <span>Days/Month</span></li>
		    							<li>Part-time wage: <input type="number" min="0" name="partTimeSalary" max="24" v-model="part_wage"/> <span>RMB/Hour</span></li>
		    							<li>Part-time working hours: <input type="number" name="partTimeWorkDay" min="0" v-model="part_work"/> <span>Hours/Day</span></li>
		    						</ul>
		    					</div>
		    					<div class="item-box">
		    						<h1>Total Cost</h1>
		    						<ul class="item-content">
		    							<li>Total pieces:{{piece}}</li>
		    							<li>Total Staffing Cost: <span>{{day_p_cost}}</span></li>
		    							<li>Staffing Cost per piece: <span>{{day_allp_cost}}</span> <span> (per piece)</span></li>
		    							<li>Transportation Cost per piece: <span>{{line_cost}}</span> <span> (per piece)</span></li>
		    							<input type="hidden" name="piece" v-bind:value="piece"/>
		    							<input type="hidden" name="sum2" v-bind:value="day_p_cost"/>
		    							<input type="hidden" name="totalDailyLaborCost" v-bind:value="day_allp_cost"/>
		    							<input type="hidden" name="branchTransportCost" v-bind:value="line_cost"/>
		    							<input type="hidden" name="totalCost" v-bind:value="allcost"/>
		    						</ul>
		    						<p>Total Cost per piece: {{allcost}} <span> (per piece)</span></p>
		    						<p>{{a}}</p>
		    					</div>
		    				</div>
	    				</form>
	    			</div>
	    		</div>
	    		
	    		<%--<div class="cost-plan-choose alt">
	    			<span class="cost-choose">Choose Plan </span>
	    			<select id="costss-choose">
	    				<option value="a">Plan A</option>
	    				<option value="b">Plan B</option>
	    			</select>
	    			<button class="costs-btn">submit</button>
	    			<span> 选中的Plan的数据会被用于其它页面的展示</span>
	    		</div>--%>
	    		
	    	
	    	
	    	
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
    		<div class="col-sm-6 text-right">Export "Costs"</div>
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
