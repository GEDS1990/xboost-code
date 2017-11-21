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
                    <h4 class="page-header">整体模型参数管理</h4>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            整体模型参数信息
                            <a href="javascript:;" id="addNewUser" class="btn btn-success btn-xs pull-right"><i class="fa fa-plus"></i> 新增</a>
                        </div>
                        <div class="panel-body">
                            <table class="table" id="ModelArgTable">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>收件串连耗时(分钟)</th>
                                    <th>上车装货时间(分钟)</th>
                                    <th>始发地分拣耗时(分钟)</th>
                                    <th>支线高速接力耗时(分钟)</th>
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
        var dt = $("#ModelArgTable").DataTable({
            "processing": true, //loding效果
            "serverSide":true, //服务端处理
            "searchDelay": 1000,//搜索延迟
            "order":[[0,'desc']],//默认排序方式
            "lengthMenu":[5,10,25,50,100],//每页显示数据条数菜单
            "ajax":{
                url:"/modelArg/modelArg.json", //获取数据的URL
                type:"get" //获取数据的方式
            },
            "columns":[  //返回的JSON中的对象和列的对应关系
                {"data":"id","name":"id"},
                {"data":"durationCollect","name":"duration_collect"},
                {"data":"durationLoad","name":"duration_load"},
                {"data":"durationSiteStartSort","name":"duration_siteStart_sort"},
                {"data":"durationRelay","name":"duration_relay"},
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
