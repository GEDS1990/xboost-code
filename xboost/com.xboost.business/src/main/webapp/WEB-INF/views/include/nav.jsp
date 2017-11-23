<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Navigation -->
    <!-- /.navbar-top-links 顶部导航栏结束-->
    <div class="navbar-default sidebar xb_side" role="navigation">
        <div class="sidebar-nav navbar-collapse">
            <ul class="nav xb_alt" id="side-menu">
                <li>
                	<div class="nav_xban"><span class="icon icon-3d"></span>Xboots System</div>
                	<div class="xb-hover">
                		<div class="nav_xb" id="nav-MyScenarios">
                			<span class="icon-item alt icon-files"></span>
                			<a href="/MyScenarios">My Scenarios</a>
                		</div>
                	</div>
                    <div class="xb-hover">
                    	<div class="nav_xb" id="xb-nav-xb">
	                    	<span id="xb_nav_span" class="glyphicon glyphicon-triangle-bottom"></span>
	                    	<span class="icon alt1 alt icon-file-text-o"></span>
	                    	<a href="/ScenariosName">ScenariosName1</a>
	                    </div>
                    </div>
                    <ul class="xb-nav_ul">
                    	<li id="nav-Conditions"><a href="/siteInfo"><span class="icon-item alt icon-document-add"></span>Conditions</a></li>
                    	<li id="nav-Simualt"><a href="/Simualt"><span class="icon-item alt icon-play"></span>Simualt</a></li>
                    	<li id="nav-Results"><a href="#"><span class="icon-item alt icon-document-checked"></span>Results</a></li>
                    </ul>
                </li>
                <li class="xb-hover">
                    <div class="nav_xb" id="nav-Research"><span class="icon alt icon-bar-chart"></span>Research</div>
                </li>
                <li class="xb-hover">
                    <div class="nav_xb" id="nav-AllScebario"><span class="icon alt icon-sitemap"></span>All Scebario</div>
                </li>
                <li class="xb-hover">
                    <div class="nav_xb" id="UserManage"><span class="icon alt icon-users"></span>User Manage</div>
                </li>
                <shiro:hasRole name="管理员">
                <li class="xb-hover">
                    <div class="nav_xb" id="System"><span class="icon alt icon-desktop"></span>System</div>
                </li>
                </shiro:hasRole>
                
			    
                <%--TBD
                <li>
                    <a href="/home"><i class="fa fa-dashboard fa-fw"></i> 首页</a>
                </li>
                <li>
                    <a href="/customer"><i class="fa fa-users fa-fw"></i> 客户</a>
                    <!-- /.nav-second-level -->
                </li>
                <li>
                    <a href="/progress"><i class="fa fa-table fa-fw"></i> 跟进</a>
                </li>
                <li>
                    <a href="/task"><i class="fa fa-edit fa-fw"></i> 待办</a>
                </li>
                <li>
                     <a href="/siteInfo"><i class="fa fa-edit fa-fw"></i>网点信息</a>
                </li>
                <li>
                    <a href="/modelArg"><i class="fa fa-edit fa-fw"></i> 整体模型参数</a>
                </li>
                <li>
                     <a href="/demandInfo"><i class="fa fa-edit fa-fw"></i>需求信息</a>
                </li>
                --%>
                <li>
                    <a href="/excelInput"><i class="fa fa-dashboard fa-fw"></i> excelInput</a>
                </li>
                <li>
                    <a href="/account"><i class="fa fa-table fa-fw"></i> 管理员</a>
                </li>

                <%--TBD--%>
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
	                <h4 class="modal-title">用户修改密码</h4>
	            </div>
	            <div class="modal-body">
	                <form action="" id="editPwdForm">
	                    <input type="hidden" value="<shiro:principal property="id"/>" name="id" id="id">
	                    <div class="form-group">
	                        <label>用户姓名</label>
	                        <input type="text" readonly="true" class="form-control" value="<shiro:principal property="username"/>" name="username" id="username">
	                    </div>
	                    <%--<div class="form-group">
	                        <label>原始密码</label>
	                        <input type="text" class="form-control" value="" name="pwd" id="pwd">
	                    </div>--%>
	                    <div class="form-group">
	                        <label>新密码</label>
	                        <input type="text" class="form-control" value="" name="pwd1" id="pwd1">
	                    </div>
	                    <div class="form-group">
	                        <label>再次输入新密码</label>
	                        <input type="text" class="form-control" value="" name="pwd2" id="pwd2">
	                    </div>
	                </form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                <button type="button" id="updatePwdBtn" class="btn btn-primary">保存</button>
	            </div>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->



<!-- jQuery -->
<script src="/static/js/jquery.min.js"></script>
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