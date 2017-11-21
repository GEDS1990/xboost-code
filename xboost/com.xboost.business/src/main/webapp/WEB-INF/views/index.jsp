<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Xboost-系统登录</title>

    <!-- Bootstrap Core CSS -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/static/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/static/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/static/js/metisMenu/metisMenu.min.css">
    <link rel="stylesheet" href="../../static/css/xb_main.css" />
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body class="xb_index_bg">

<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-coffee"></i>Xboost System login</h3>
                </div>
                <div class="panel-body">
                    <c:if test="${not empty message}">
                        <div class="alert ${message.state}">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            ${message.message}
                        </div>
                    </c:if>
                    <form id="loginForm" method="post">
                        <fieldset>
                            <div class="form-group">
                                <input class="form-control" placeholder="User name" name="tel" type="text" id="tel" autofocus>
                            </div>
                            <div class="form-group">
                                <input class="form-control" placeholder="Password" name="password" type="password" id="password" value="">
                            </div>
                            <div class="checkbox">
                                <label>
                                    <input name="remember" type="checkbox" value="Remember Me">Remember user name
                                </label>
                            </div>
                            <!-- Change this to a button or input when using this as a form -->
                            <button id="loginBtn" type="button" class="btn btn-lg btn-success btn-block">Login in</button>
                            <%--<button id="registerBtn" type="button" class="btn btn-lg btn-register btn-block">注册账号</button>--%>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- /.navbar-static-side -->
<div class="modal fade" id="newUserModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">注册</h4>
            </div>
            <div class="modal-body">
                <form id="newUserForm" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="username">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">联系电话</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="tel">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="password" value="123123">
                            <span class="help-block">默认密码为：123123</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">微信号</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="weixinid">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">角色</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                               <%-- <label>
                                    <input type="checkbox" name="role" value="1"> 管理员
                                </label>
                                <label>
                                    <input type="checkbox" name="role" value="2"> 经理
                                </label>--%>
                                <label>
                                    <input type="checkbox" name="role" value="3"> 普通员工
                                </label>
                            </div>
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
<!-- jQuery -->
<script src="/static/js/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/metisMenu/metisMenu.min.js"></script>
<!-- Custom Theme JavaScript -->
<script src="/static/js/sb-admin-2.js"></script>

<script>
    $(function(){

        $("#loginBtn").click(function(){
            if(!$("#tel").val()) {
                $("#tel").focus();
                return;
            }
            if(!$("#password").val()) {
                $("#password").focus();
                return;
            }
            $("#loginForm").submit();
        });

        //新用户注册
        $("#registerBtn").click(function(){
            $("#newUserModal").modal('show');
        });
        $("#saveBtn").click(function(){
            debugger;
            $.post("/account/new",$("#newUserForm").serialize())
                .done(function(result){
                    if("success" == result) {
                        debugger;
                        $("#newUserForm")[0].reset();
                        $("#newUserModal").modal("hide");
                        dt.ajax.reload();
                    }
                }).fail(function(){
                alert("添加时出现异常");
            });

        });
    });
</script>


</body>

</html>
