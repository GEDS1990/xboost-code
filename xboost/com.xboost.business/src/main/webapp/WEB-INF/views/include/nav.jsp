<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Navigation -->
<nav class="navbar navbar-default navbar-static-top xb_nav" role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="home.html"><i class="fa fa-coffee"></i>XBoost</a>
    </div>
    <!-- /.navbar-header -->

    <ul class="nav navbar-top-links navbar-right">
        <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
                <li><a href="#"><i class="fa fa-user fa-fw alt"></i>User：<shiro:principal property="username"/></a>
                </li>
                <li><a href="javascript:;" id="editLink" ><i class="fa fa-gear fa-fw"></i>Settings</a>
                </li>
                <li class="divider"></li>
                <li><a href="/logout"><i class="fa fa-sign-out fa-fw"></i>LoginOut</a>
                </li>
            </ul>
            <!-- /.dropdown-user -->
        </li>
        <!-- /.dropdown -->
    </ul>
    <!-- /.navbar-top-links 顶部导航栏结束-->
    <div class="navbar-default sidebar xb_side" role="navigation">
        <div class="sidebar-nav navbar-collapse">
            <ul class="nav xb_alt" id="side-menu">
                <li>
                    <div class="nav_xb"><span class="glyphicon glyphicon-triangle-right"></span>Scenario</div>
                    <ul>
                    	<li><a href="/Scenario/Create">Create</a></li>
                    	<li><a href="/Scenario/Open">Open</a></li>
                    	<li><a href="/Scenario/Save">Save</a></li>
                    </ul>
                </li>
                <li>
                    <div class="nav_xb"><span class="glyphicon glyphicon-triangle-right"></span>Input Data Management</div>
                    <ul>
                    	<li><a href="/InputData/View_edit">View & Edit</a></li>
                    	<li><a href="/InputData/Import">Import</a></li>
                    	<li><a href="/InputData/Export">Export</a></li>
                    </ul>
                </li>
                <li>
                    <div class="nav_xb"><span class="glyphicon glyphicon-triangle-right"></span>Run</div>
                    <ul>
                    	<li><a href="/Run/Execute">Execute</a></li>
                    	<li><a href="/Run/Progress">Progress</a></li>
                    </ul>
                </li>
                <li>
                    <div class="nav_xb"><span class="glyphicon glyphicon-triangle-right"></span>Output Data</div>
                    <ul>
                    	<li><a href="/OutputData/View">View</a></li>
                    	<li><a href="/OutputData/Export">Export</a></li>
                    </ul>
                </li>
                <li>
                    <div class="nav_xb"><span class="glyphicon glyphicon-triangle-right"></span>Research</div>
                    <ul>
                    	<li><a href="/Research/Overview">Overview</a></li>
                    </ul>
                </li>
                <shiro:hasRole name="管理员">
                <li>
                    <div class="nav_xb"><span class="glyphicon glyphicon-triangle-right"></span>System</div>
                    <ul>
                    	<li><a href="/System/Users">Users</a></li>
                    	<li><a href="/System/Scenarios">Scenarios</a></li>
                    </ul>
                </li>
                </shiro:hasRole>
                <%--TBD--%>
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
                <%--TBD--%>
            </ul>
        </div>
        <!-- /.sidebar-collapse -->
    </div>
</nav>

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
                        <label>User name</label>
                        <input type="text" readonly="true" class="form-control" value="<shiro:principal property="username"/>" name="username" id="username">
                    </div>
                    <div class="form-group">
                        <label>Original password</label>
                        <input type="text" class="form-control" value="" name="pwd" id="pwd">
                    </div>
                    <div class="form-group">
                        <label>New password</label>
                        <input type="text" class="form-control" value="" name="pwd1" id="pwd1">
                    </div>
                    <div class="form-group">
                        <label>New password again</label>
                        <input type="text" class="form-control" value="" name="pwd2" id="pwd2">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" id="updatePwdBtn" class="btn btn-primary">Save</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!-- jQuery -->
<script src="/static/js/jquery.min.js"></script>
<script src="../../static/js/nav/nav.js" type="text/javascript" charset="utf-8"></script>
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