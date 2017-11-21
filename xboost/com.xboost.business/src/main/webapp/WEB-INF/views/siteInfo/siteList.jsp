<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>CRM-客户关系管理系统</title>

    <!-- Bootstrap Core CSS -->
    <link href="http://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="/static/js/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/static/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/static/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <link rel="stylesheet" href="/static/js/datatables/media/css/dataTables.bootstrap.min.css">

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
    <div id="page-wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="col-lg-12">
                    <h4 class="page-header">网点信息管理</h4>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            网点信息
                            <a href="javascript:;" id="addNewUser" class="btn btn-success btn-xs pull-right"><i class="fa fa-plus"></i> 新增</a>
                        </div>
                        <div class="panel-body">
                            <table class="table" id="SiteInfoTable">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>网点编码</th>
                                    <th>网点名称</th>
                                    <th>网点类型</th>
                                    <th>网点地址</th>
                                    <th width="100">#</th>
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
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>


                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.container-fluid -->
    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->


<div class="modal fade" id="newUserModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">新增网点</h4>
            </div>
            <div class="modal-body">
                <form id="newUserForm" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">网点编码</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteCode">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">网点名称</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">网点地址</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteAddress">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">网点类型</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" name="siteType" value="1"> 网点
                                </label>
                                <label>
                                    <input type="checkbox" name="siteType" value="2"> 集散点
                                </label>
                                <label>
                                    <input type="checkbox" name="siteType" value="3"> 集配站
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


<div class="modal fade" id="editSiteInfoModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">编辑网点信息</h4>
            </div>
            <div class="modal-body">
                <form id="editSiteInfoForm" class="form-horizontal">
                    <input type="hidden" name="id" id="id">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">网点编码</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteCode" id="siteCode">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">网点名称</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteName" id="siteName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">网点地址</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="siteAddress" id="siteAddress">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">网点类型</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" name="siteType" value="1" class="siteType"> 网点
                                </label>
                                <label>
                                    <input type="checkbox" name="siteType" value="2" class="siteType"> 集散点
                                </label>
                                <label>
                                    <input type="checkbox" name="siteType" value="3" class="siteType"> 集配站
                                </label>
                            </div>
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
<script src="/static/js/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="/static/js/datatables/media/js/dataTables.bootstrap.min.js"></script>
<script>
    $(function(){
debugger;
        var dt = $("#SiteInfoTable").DataTable({
            "processing": true, //loding效果
            "serverSide":true, //服务端处理
            "searchDelay": 1000,//搜索延迟
            "order":[[0,'desc']],//默认排序方式
            "lengthMenu":[5,10,25,50,100],//每页显示数据条数菜单
            "ajax":{
                url:"/siteInfo/siteInfo.json", //获取数据的URL
                type:"get" //获取数据的方式
            },
            "columns":[  //返回的JSON中的对象和列的对应关系
                {"data":"id","name":"id"},
////              {"data":"siteCode","name":"site_code"},
//              {"data":"siteName","name":"site_name"},
//              {"data":"siteType","name":"site_type"},
//              {"data":"siteAddress","name":"site_address"},
                {"data":function(row){
                    return "<a href='javascript:;' class='editLink' data-id='"+row.id+"'>编辑</a> <a href='javascript:;' class='delLink' data-id='"+row.id+"'>删除</a>";
                }}
            ],
            "columnDefs":[ //具体列的定义
                {
                    "targets":[0],
                    "visible":false
                },
                {
                    "targets":[1,2,4,5],
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

        //新增网点
        $("#addNewUser").click(function(){
            $("#newUserModal").modal('show');
        });
        $("#saveBtn").click(function(){
            $.post("siteInfo/add",$("#newUserForm").serialize())
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

        //删除网点
        $(document).delegate(".delLink","click",function(){
            var id = $(this).attr("data-id");
            if(confirm("确定要删除该网点信息吗?")) {
                $.post("/siteInfo/del",{"id":id}).done(function(result){
                    if("success" == result) {
                        dt.ajax.reload();
                    }
                }).fail(function(){
                    alert("删除网点信息出现异常");
                });

            }
        });

        //编辑用户
        $(document).delegate(".editLink","click",function(){
            $("#editSiteInfoForm")[0].reset();
            var id = $(this).attr("data-id");
            $.get("/siteInfo/site.json",{"id":id}).done(function(result){
                $("#id").val(result.id);
                $("#siteCode").val(result.site_code);
                $("#siteName").val(result.site_name);
                $("#siteAddress").val(result.site_address);
                $("#siteType").val(result.site_type);

            }).fail(function(){

            });

            $("#editSiteInfoModal").modal("show");
        });

        $("#editBtn").click(function(){

            $.post("/siteInfo/edit",$("#editSiteInfoForm").serialize()).done(function(result){
                if(result == "success") {
                    $("#editSiteInfoModal").modal("hide");
                    dt.ajax.reload();
                }
            }).fail(function(){
                alert("修改网点信息异常");
            });

        });
    });
</script>

</body>

</html>
