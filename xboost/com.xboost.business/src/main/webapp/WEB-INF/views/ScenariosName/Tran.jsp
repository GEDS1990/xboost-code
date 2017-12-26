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
	        		<li class="active"><a  class="active" href="/car">Vehicles</a></li>
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
					    <!--
                        <tr>
                            <th>fixed<br />Round</th>计算
                            <th>fixed<br />RoundFee</th>计算
                            <th>cost<br />PerDistance</th>计算
                            <th>cost<br />PerTime</th>计算
                            <th>fixed<br />Cost</th>计算
                        </tr>
                        -->
					    	<tr>
                                <th rowspan="2">Id</th>
                                <th rowspan="2">vehicle type</th>
                                <th rowspan="2">vehicle source</th>
                                
					    		<th rowspan="2">max<br />Stop</th>
                                <th rowspan="2">maximum load(p)</th>
					    		
                                <th rowspan="2">maximum distance (km)</th>
					    		<th rowspan="2">max<br />RunningTime</th>
                                <th colspan="3">speed(km/h)</th>
                                <th rowspan="2">unload time (min)</th>
                                <th rowspan="2">start<br />Location</th>
					    		<th rowspan="2">end<br />Location</th>
                                <th colspan="5">distance interval 1</th>
                                <th colspan="5">distance interval 2</th>
                                <th colspan="5">distance interval 3</th>
                                <th colspan="5">distance interval 4</th>
                                <th colspan="5">distance interval 5</th>
                                <th colspan="5">distance interval 6</th>
                                <th rowspan="2">operation</th>
                                <tr>
                                	<th>&lt;10km(km/h)</th>
                                	<th>10-30km(km/h)</th>
                                	<th>&gt;30km(km/h)</th>
                                    <th>0 km</th>
                                    <th>a km</th>
                                    <th>/ride</th>
                                    <th>/km</th>
                                    <th>/min</th>
                                    <th>a km</th>
                                    <th>b km</th>
                                    <th>/ride</th>
                                    <th>/km</th>
                                    <th>/min</th>
                                    <th>b km</th>
                                    <th>c km</th>
                                    <th>/ride</th>
                                    <th>/km</th>
                                    <th>/min</th>
                                    <th>c km</th>
                                    <th>d km</th>
                                    <th>/ride</th>
                                    <th>/km</th>
                                    <th>/min</th>
                                    <th>d km</th>
                                    <th>e km</th>
                                    <th>/ride</th>
                                    <th>/km</th>
                                    <th>/min</th>
                                    <th>e km</th>
                                    <th>f km</th>
                                    <th>/ride</th>
                                    <th>/km</th>
                                    <th>/min</th>
                                </tr>
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



<div class="modal fade" id="newUserModal-tran">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Adding dot information</h4>
            </div>
            <form id="newUserForm-tran" class="form-horizontal">
           		<div class="modal-body">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">vehicle type</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="type" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">vehicle source</label>
                        <div class="col-sm-4">
                            <select  class="form-control" name="carSource" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
                            	<option value="hire">hire</option>
                            	<option value="own">own</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        
                        <label class="col-sm-2 control-label">maxStop</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxStop" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">unload time (min)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationUnloadFull" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                    	<label class="col-sm-2 control-label">maximum load(p)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="dimensions" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">maximum distance (km)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="max_distance" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">max RunningTime</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="max_running_time" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">(&lt;10km)speed(km/h)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="velocity" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">(10km-30km)speed(km/h)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="velocity2" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">(&gt;30km)speed(km/h)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="velocity3" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">start Location</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="startLocation" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">end Location</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="endLocation" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                <!--
                    <div class="form-group">
                        <label class="col-sm-3 control-label">time_window</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="time_window_start" placeholder="start">
                        </div>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="time_window_end" placeholder="end">
                            <input type="hidden" name="tw" id="tw" value="" />
                        </div>
                    </div>
                    -->
                    <!--<div class="form-group">
                        <label class="col-sm-3 control-label">skills</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="skills" >
                        </div>
                    </div>-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">vehicle cost</label>
                        <div class="table-responsive active" style="padding:0 15px;">
							<table  class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>distance interval</th>
										<th>price</th>
									</tr>
								</thead>
								<tbody class="tran-add-tbody">
									<tr>
										<td>
											<span class="tran-add-frspan">flag-fall</span>
											<span><input type="number" name="a1" min="0" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/></span>
											<span>km to</span>
											<span><input type="number" name="a2" min="0" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="costa1" min="0" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/></span>
											<span><label><input type="radio" value="/ride" data-name="costa1" checked="checked"/>/ride</span></label>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="b1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="b2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="" min="0" /></span>
											<span><label><input type="radio" data-name="costb1" value="/ride" />/ride</span></label>
											<span><label><input type="radio" data-name="costb2" value="/km"/>/km</span></label>
											<span>+</span>
											<span><input type="number" class="km-min" name="costb3" min="0" /></span>
											<span>/min</span>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="c1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="c2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="" min="0" /></span>
											<span><label><input type="radio" data-name="costc1" value="/ride" />/ride</span></label>
											<span><label><input type="radio" data-name="costc2" value="/km"/>/km</span></label>
											<span>+</span>
											<span><input type="number" class="km-min" name="costc3" min="0" /></span>
											<span>/min</span>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="d1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="d2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="" min="0" /></span>
											<span><label><input type="radio" data-name="costd1" value="/ride"/>/ride</span></label>
											<span><label><input type="radio" data-name="costd2" value="/km"/>/km</span></label>
											<span>+</span>
											<span><input type="number" class="km-min" name="costd3" min="0" /></span>
											<span>/min</span>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="e1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="e2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="" min="0" /></span>
											<span><label><input type="radio" data-name="coste1" value="/ride"/>/ride</span></label>
											<span><label><input type="radio" data-name="coste2" value="/km"/>/km</span></label>
											<span>+</span>
											<span><input type="number" class="km-min" name="coste3" min="0" /></span>
											<span>/min</span>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="f1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="f2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="" min="0" /></span>
											<span><label><input type="radio" data-name="costf1" value="/ride"/>/ride</span></label>
											<span><label><input type="radio" data-name="costf2" value="/km"/>/km</span></label>
											<span>+</span>
											<span><input type="number" class="km-min" name="costf3" min="0" /></span>
											<span>/min</span>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
                    </div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                <button type="submit" id="saveBtn-tran" class="btn btn-primary">Save</button>
	            </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="editUserModal-tran">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit dot information</h4>
            </div>
            <form id="editUserForm-tran" class="form-horizontal">
            	<input type="hidden" name="id" id="siteId-tran" value="">
           		<div class="modal-body">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">vehicle type</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="type" id="type" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">vehicle source</label>
                        <div class="col-sm-4">
                            <select  class="form-control" name="carSource" id="carSource" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');">
                            	<option value="hire">hire</option>
                            	<option value="own">own</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        
                        <label class="col-sm-2 control-label">maxStop</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="maxStop" id="maxStop" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">unload time (min)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="durationUnloadFull" id="durationUnloadFull" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                    	<label class="col-sm-2 control-label">maximum load(p)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="dimensions" id="dimensions" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">maximum distance (km)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="max_distance" id="max_distance" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">max RunningTime</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="max_running_time" id="max_running_time" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">(&lt;10km)speed(km/h)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="velocity" id="velocity" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">(10km-30km)speed(km/h)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="velocity2" id="velocity2" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">(&gt;30km)speed(km/h)</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="velocity3" id="velocity3" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">start location</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="startLocation" id="start_location" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                        <label class="col-sm-2 control-label">end Location</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="endLocation" id="end_location" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/>
                        </div>
                    </div>
                <!--
                    <div class="form-group">
                        <label class="col-sm-3 control-label">time_window</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="time_window_start" placeholder="start">
                        </div>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="time_window_end" placeholder="end">
                            <input type="hidden" name="tw" id="tw" value="" />
                        </div>
                    </div>
                    -->
                    <!--<div class="form-group">
                        <label class="col-sm-3 control-label">skills</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="skills" >
                        </div>
                    </div>-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">vehicle cost</label>
                        <div class="table-responsive active" style="padding:0 15px;">
							<table  class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>distance interval</th>
										<th>price</th>
									</tr>
								</thead>
								<tbody class="tran-add-tbody">
									<tr>
										<td>
											<span class="tran-add-frspan">flag-fall</span>
											<span><input type="number" name="a1" id="a1" min="0" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/></span>
											<span>km to</span>
											<span><input type="number" name="a2" id="a2" min="0" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="costa1" id="costa1" min="0" required="required" oninvalid="setCustomValidity('Please enter information');" oninput="setCustomValidity('');"/></span>
											<span><label><input type="radio" value="/ride" data-name="costa1" checked="checked"/>/ride</span></label>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="b1" id="b1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="b2" id="b2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="costb1" id="costb" min="0" /></span>
											<span><label><input type="radio" data-name="costb1" value="/ride" />/ride</span></label>
											<span><label><input type="radio" data-name="costb2" value="/km" />/km</span></label>
											<span>+</span>
											<span><input type="number" name="costb3" class="km-min" id="costb3" min="0" /></span>
											<span>/min</span>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="c1" id="c1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="c2" id="c2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="costc1" id="costc" min="0" /></span>
											<span><label><input type="radio" data-name="costc1" value="/ride" />/ride</span></label>
											<span><label><input type="radio" data-name="costc2" value="/km"/>/km</span></label>
											<span>+</span>
											<span><input type="number" name="costc3" class="km-min" id="costc3" min="0" /></span>
											<span>/min</span>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="d1" id="d1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="d2" id="d2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="costd1" id="costd" min="0" /></span>
											<span><label><input type="radio" data-name="costd1" value="/ride"/>/ride</span></label>
											<span><label><input type="radio" data-name="costd2" value="/km"/>/km</span></label>
											<span>+</span>
											<span><input type="number" name="costd3" class="km-min" id="costd3" min="0" /></span>
											<span>/min</span>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="e1" id="e1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="e2" id="e2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="coste1" id="coste" min="0" /></span>
											<span><label><input type="radio" data-name="coste1" value="/ride"/>/ride</span></label>
											<span><label><input type="radio" data-name="coste2" value="/km"/>/km</span></label>
											<span>+</span>
											<span><input type="number" name="coste3" class="km-min" id="coste3" min="0" /></span>
											<span>/min</span>
										</td>
									</tr>
									<tr>
										<td>
											<span class="tran-add-frspan"></span>
											<span><input type="number" name="f1" id="f1" min="0" /></span>
											<span>km to</span>
											<span><input type="number" name="f2" id="f2" min="0" /></span>
											<span>km</span>
										</td>
										<td class="text-left">
											<span><input type="number" name="costf1" id="costf" min="0"  /></span>
											<span><label><input type="radio" data-name="costf1" value="/ride"/>/ride</span></label>
											<span><label><input type="radio" data-name="costf2" value="/km"/>/km</span></label>
											<span>+</span>
											<span><input type="number" class="km-min" name="costf3" id="costf3" min="0" /></span>
											<span>/min</span>
										</td>
								</tbody>
							</table>
						</div>
                    </div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                <button type="submit" id="editBtn-tran" class="btn btn-primary">Save</button>
	            </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->




<div class="modal fade" id="modal-tran">
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
                <button type="button" id="modal-trandelBtn" class="btn btn-primary">Yes</button>
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
        <form  method="post" id="cond-input-form-tran" enctype="multipart/form-data">

        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Vehicles" Settings:</div>
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
  
        </form>
        <!--content e-->

        <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" id="cond-file-upload-tran">Upload</button>
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
    		<div class="col-sm-6 text-right">Export "Vehicles" Settings:</div>
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
