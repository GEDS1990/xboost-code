<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Navigation -->
    <!-- /.navbar-top-links 顶部导航栏结束-->
    <div class="navbar-default sidebar xb_side" role="navigation">
    	<input type="hidden" id="scenName" value="${sessionScope.openScenariosName}" data-id="${sessionScope.openScenariosId}" />
        <div class="sidebar-nav navbar-collapse">
            <ul class="nav xb_alt" id="side-menu">
                <li id="scen-info">
                	<div class="nav_xban"><span class="icon icon-3d"></span>Xboots System</div>
                </li>
            	<li class="xb-hover" id="after-content">
            		<a href="/MyScenarios" id="nav-MyScenarios">
            			<span class="icon-item alt icon-files"></span>My Scenarios
                	</a>
            	</li>
                <li class="xb-hover" id="nav-Research">
                    <a class="nav_xb" ><span class="icon alt icon-bar-chart"></span>Research</a>
                </li>
                <li class="xb-hover" id="nav-AllScebario">
                    <a class="nav_xb" ><span class="icon alt icon-sitemap"></span>All Scebario</a>
                </li>
                <li class="xb-hover" id="UserManage">
                    <a href="/account" class="nav_xb" ><span class="icon alt icon-users"></span>User Manage</a>
                </li>
                <shiro:hasRole name="管理员">
                <li class="xb-hover" id="System">
                    <a href="/druid" class="nav_xb" ><span class="icon alt icon-desktop"></span>System</a>
                </li>
                </shiro:hasRole>
                
            </ul>
        </div>
        <!-- /.sidebar-collapse -->
        
        <!--LOGIN-->
	    <ul class="nav navbar-top-linkS">
		    <li>
		        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
		            <i class="fa icon-gear fa-fw"></i>
		            UserName
		        </a>
		        <ul class="dropdown-menu dropdown-user">
		            <li>
		            	<a href="#"><i class="fa fa-user fa-fw alt"></i>User：<shiro:principal property="username"/></a>
		            </li>
		            <li class="divider"></li>
		            <li>
		            	<a href="javascript:;" id="editLink" ><i class="fa fa-gear fa-fw"></i>Settings</a>
		            </li>
		        </ul>
		        <!-- /.dropdown-user -->
		    </li>
		    <li>
            	<a class="xb-a" href="/logout"><i class="fa fa-fw icon-power-off"></i>LoginOut</a>
            </li>
		    <!-- /.dropdown -->
		</ul>
    </div>
    <!--修改密码-->
	<div class="modal fade" id="editPwdModal">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                <h4 class="modal-title">User modify password</h4>
	            </div>
	            <div class="modal-body">
	                <form action="" id="editPwdForm">
	                    <input type="hidden" value="<shiro:principal property="id"/>" name="id" id="id">
	                    <div class="form-group">
	                        <label>UserName</label>
	                        <input type="text" readonly="true" class="form-control" value="<shiro:principal property="username"/>" name="username" id="username">
	                    </div>
	                    <%--<div class="form-group">
	                        <label>原始密码</label>
	                        <input type="text" class="form-control" value="" name="pwd" id="pwd">
	                    </div>--%>
	                    <div class="form-group">
	                        <label>New password</label>
	                        <input type="text" class="form-control" value="" name="pwd1" id="pwd1">
	                    </div>
	                    <div class="form-group">
	                        <label>Reenter the new password again</label>
	                        <input type="text" class="form-control" value="" name="pwd2" id="pwd2">
	                    </div>
	                </form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                <button type="button" id="updatePwdBtn" class="btn btn-primary">Save</button>
	            </div>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	
	<!-- 公共部分  上传 导出 下载模板 -->
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
        		<div class="col-sm-4 text-right">Import All Settings from A Single File:</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="fileAll" class="cond_file" />
	        		<div class="cond-file-box clearfix">
	        			<div class="cond-file-btn">Choose File</div>
	        			<p>No file chosen</p>
	        		</div>
	        	</div>

	        	<div class="col-sm-4">
	        		<div>
	        			<a id="template-all" href="javascript:void(0);">
		        			<span class="icon-upload"></span>
		        			Download Template
	        			</a>
	        		</div>
	        	</div>
        	</div>
  
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
	        			<a id="template-info" href="javascript:void(0);" >
	        				<span class="icon-upload"></span>
	        				Download Template
	        			</a>
	        		</div>
	        	</div>
        	</div>
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Depots Distance" Settings:</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="file" class="cond_file"/>
	        		<div class="cond-file-box clearfix">
	        			<div class="cond-file-btn">Choose File</div>
	        			<p>No file chosen</p>
	        		</div>
	        	</div>
	        	<div class="col-sm-4">
	        		<div>
	        			<a id="template-dist" href="javascript:void(0);">
		        			<span class="icon-upload"></span>
		        			Download Template
	        			</a>
	        		</div>
	        	</div>
        	</div>

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
	        			<a id="template-veh" href="javascript:void(0);">
		        			<span class="icon-upload"></span>
		        			Download Template
	        			</a>
	        		</div>
	        	</div>
        	</div>
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Demands" Settings:</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="file" class="cond_file"/>
	        		<div class="cond-file-box clearfix">
	        			<div class="cond-file-btn">Choose File</div>
	        			<p>No file chosen</p>
	        		</div>
	        	</div>
	        	<div class="col-sm-4">
	        		<div>
	        			<a id="template-dem" href="javascript:void(0);">
		        			<span class="icon-upload"></span>
		        			Download Template
	        			</a>
	        		</div>
	        	</div>
        	</div>
        	<div class="model-input-content clearfix">
        		<div class="col-sm-4 text-right">Import "Parameters" Settings:</div>
	        	<div class="col-sm-4">
	        		<input type="file" name="file" class="cond_file"/>
	        		<div class="cond-file-box clearfix">
	        			<div class="cond-file-btn">Choose File</div>
	        			<p>No file chosen</p>
	        		</div>
	        	</div>
	        	<div class="col-sm-4">
	        		<div>
	        			<a id="template-pata" href="javascript:void(0);">
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
    		<div class="col-sm-6 text-right">Export All Settings to A Single File:</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="ALL_Data">
        			<span class="icon-upload"></span>
        			Export 
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Depots Info" Settings:</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="Depots_Info">
        			<span class="icon-upload"></span>
        			Export
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Depots Distance" Settings:</div>
        	<div class="col-sm-4">
        		<div class="export-btn"  data-xls="dist">
        			<span class="icon-upload"></span>
        			Export 
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Vehicles" Settings:</div>
        	<div class="col-sm-4">
        		<div class="export-btn" data-xls="Transportation">
        			<span class="icon-upload"></span>
        			Export 
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Demands" Settings:</div>
        	<div class="col-sm-4" data-xls="Demands">
        		<div class="export-btn">
        			<span class="icon-upload"></span>
        			Export 
        		</div>
        	</div>
    	</div>
    	<div class="model-input-content clearfix">
    		<div class="col-sm-6 text-right">Export "Patameters" Settings:</div>
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




<!-- jQuery -->
<script src="/static/js/jquery.min.js"></script>
<script src="/static/js/flex.js"></script>
<script src="/static/js/vue.min.js"></script>
<script src="/static/js/Xboost/nav.js"></script>
<script>
    $(function () {
    //修改密码页面
    $("#editLink").click(function(){
        $("#editPwdForm")[0].reset();
        $("#editPwdModal").modal("show");
    });
        //保存新密码
        $("#updatePwdBtn").click(function(){
             var id = document.getElementById("id").value;
             var pwd1 = document.getElementById("pwd1").value;
             var pwd2 = document.getElementById("pwd2").value;
             if(pwd1=pwd2){
             $.post("/account/setPwd",{"id":id,"pwd":pwd2}).done(function(result){
                 //清空session
                 $("#editPwdForm")[0].reset();
                 $("#editPwdModal").modal('hide');
                 window.location.href="/home";
                 alert("修改密码成功，下次登录请使用新密码！");
             }).fail(function(){
                 alert("修改密码异常！请联系管理员！");
             });
             }else{
                alert("新密码不一致！");
             }
        });

    });
    
   
</script>