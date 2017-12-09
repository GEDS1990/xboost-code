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
                    <a href="/Research" class="nav_xb" ><span class="icon alt icon-bar-chart"></span>My Research</a>
                </li>
                <shiro:hasRole name="管理员">
                <li class="xb-hover" id="nav-AllScenarios">
                    <a href="/MyScenarios/AllScenarios" class="nav_xb" ><span class="icon alt icon-sitemap"></span>All Scenarios</a>
                </li>
                <li class="xb-hover" id="UserManage">
                    <a href="/account" class="nav_xb" ><span class="icon alt icon-users"></span>All Users</a>
                </li>
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