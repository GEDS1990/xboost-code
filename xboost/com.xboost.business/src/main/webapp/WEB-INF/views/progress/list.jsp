<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="/static/js/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/static/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/static/css/font-awesome.min.css" rel="stylesheet" type="text/css">

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
   
                    <h4 class="page-header">
                        <i class="fa fa-fax"></i> 跟进列表
                    </h4>

                    <c:if test="${not empty message}">
                        <div class="alert ${message.state}">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                ${message.message}
                        </div>
                    </c:if>


                        <div class="panel-heading">
                            <i class="fa fa-search"></i> 搜索
                        </div>

                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-list"></i> 跟进列表
                            
                            111111111111111111111111111111111
                            <button id="SelectSiteInfoBtn"> 查询网点信息</button>
                            <button id="AddSiteInfoBtn"> 新增网点信息</button>
                            <button id="EditSiteInfoBtn">编辑网点信息</button>
                            <button id="DelSiteInfoBtn"> 删除网点信息</button>
							<br>
							111111111111111111111111111111111
							<button id="selectModelArgBtn">查询整体模型参数信息</button>
							<button id="AddModelArgBtn"> 新增整体模型参数信息</button>
							<button id="EditModelArgBtn">编辑整体模型参数信息</button>
							<button id="DelModelArgBtn">删除整体模型参数信息</button>
							<br>
							111111111111111111111111111111111
							<button id="selectDemandBtn">查询需求信息</button>
							<button id="AddDemandBtn">新增需求信息</button>
							<button id="EditDemandBtn"> 编辑需求信息</button>
							<button id="DelDemandBtn"> 删除需求信息</button>
                        </div>
                     
                            <c:forEach items="${page.items}" var="pro">
                            <c:choose>
                            <c:when test="${pro.progress == '成交'}">
                            <div class="panel panel-success">
                                </c:when>
                                <c:when test="${pro.progress == '暂时搁置'}">
                                <div class="panel panel-danger">
                                    </c:when>
                                    <c:otherwise>
                                    <div class="panel panel-default">
                                        </c:otherwise>
                                        </c:choose>

                                        <div class="panel-heading">
                                            <i class="fa fa-calendar"></i> ${pro.monthAndDay} - <span class="text-muted">${pro.user.username}</span> - ${pro.customer.custname}
                                            <c:choose>
                                                <c:when test="${pro.progress == '初访'}">
                                                    <span class="label label-default pull-right">${pro.progress}</span>
                                                </c:when>
                                                <c:when test="${pro.progress == '意向'}">
                                                    <span class="label label-info pull-right">${pro.progress}</span>
                                                </c:when>
                                                <c:when test="${pro.progress == '报价'}">
                                                    <span class="label label-primary pull-right">${pro.progress}</span>
                                                </c:when>
                                                <c:when test="${pro.progress == '成交'}">
                                                    <span class="label label-success pull-right">${pro.progress}</span>
                                                </c:when>
                                                <c:when test="${pro.progress == '暂时搁置'}">
                                                    <span class="label label-danger pull-right">${pro.progress}</span>
                                                </c:when>
                                            </c:choose>

                                        </div>
                                        <div class="panel-body">
                                                ${pro.mark}
                                        </div>
                                        <c:choose>
                                            <c:when test="${sessionScope.curr_user.id == pro.user.id}">
                                                <c:if test="${not empty pro.progressFileList}">
                                                    <div class="panel-footer">
                                                        <c:forEach items="${pro.progressFileList}" var="file">
                                                            <a href="${file.path}?attname=${file.filename}">${file.filename}</a>
                                                        </c:forEach>
                                                    </div>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <shiro:hasRole name="经理">
                                                    <c:if test="${not empty pro.progressFileList}">
                                                        <div class="panel-footer">
                                                            <c:forEach items="${pro.progressFileList}" var="file">
                                                                <a href="${file.path}?attname=${file.filename}">${file.filename}</a>
                                                            </c:forEach>
                                                        </div>
                                                    </c:if>
                                                </shiro:hasRole>
                                            </c:otherwise>
                                        </c:choose>


                                    </div>
                                    </c:forEach>
                        </div>
                    </div>

                    <ul id="pagination" class="pagination-sm pull-right"></ul>
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

<div id="wrapper">

    <%@ include file="../include/nav.jsp"%>

    <!-- Page Content -->
    <div id="page-wrapper">
        <div class="modal-body">
                        <form action="/siteInfo/addByExcel" method="post" id="addSiteInfoByExcel" enctype="multipart/form-data">

                            <div class="form-group">
                                <label>内容</label>
                                <textarea name="mark" class="form-control" rows="3"></textarea>
                            </div>
                            <div class="form-group" id="fileControls" style="">
                                <label>相关文件 <button type="button" class="btn btn-default btn-xs" id="addFileControl"><i class="fa fa-plus"></i></button></label>
                                <input type="file" name="file" class="form-control">
                            </div>
                        </form>
                        <div class="modal-footer">
                            <button type="button" class=" btn btn-primary" id="saveBtn">保存</button>
                        </div>
                    </div>
        <!-- /.container-fluid -->
    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

</div><!-- /.modal -->




<!-- jQuery -->
<script src="/static/js/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="/static/js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="/static/js/metisMenu/metisMenu.min.js"></script>

<!-- Custom Theme JavaScript -->
<script src="/static/js/sb-admin-2.js"></script>
<script src="/static/js/jquery.twbsPagination.min.js"></script>
<script>



		// 网点信息调试数据
		
        $("#AddSiteInfoBtn").click(function(){
            var json = {"siteCode":"KSCD002","siteLongitude":"259.223343","siteLatitude":"34.098586","siteName":"昆山城东","siteAddress":"前进路234号","siteNightDelivery":"1","siteArea":"3442","siteType":"集散点","carNum":"23","largeCarModel":"145","maxOperateNum":"4000"};

            $.post("/siteInfo/add",json).done(function(result){
                alert("success");
                                           }).fail(function(){
                alert("fail");
            });
        });

        $("#EditSiteInfoBtn").click(function(){
            var json = {"id":"1","siteCode":"KSC3301edit","siteLongitude":"258.223343","siteLatitude":"34.098586","siteName":"昆山城东修改后","siteAddress":"前进路234号修改后","siteNightDelivery":"1","siteArea":"3442","siteType":"集散点","carNum":"23","largeCarModel":"145","maxOperateNum":"4000"};

            $.post("/siteInfo/edit",json).done(function(result){
                alert("success");
                                           }).fail(function(){
                alert("fail");
            });
        });

        $("#DelSiteInfoBtn").click(function(){
                var json = {"id":2};
                $.post("/siteInfo/del",json).done(function(result){
                      alert("success");
                      }).fail(function(){
                       alert("fail");
                 });
          });

        $("#SelectSiteInfoBtn").click(function(){
            var json = {"siteCode":"KSCD001","siteLongitude":"258.223343","siteLatitude":"34.098586","siteName":"昆山城东","siteAddress":"前进路234号","siteNightDelivery":"1","siteArea":"3442","siteType":"集散点","carNum":"23","largeCarModel":"145","maxOperateNum":"4000"};

            $.post("/siteInfo/add",json).done(function(result){
                alert("success");
                                           }).fail(function(){
                alert("fail");
            });
        });
		
		//整体模型参数信息调试
		
        $("#AddModelArgBtn").click(function(){
            var json = {"durationCollect":"34","durationLoad":"100","durationSiteStartSort":"340"};

            $.post("/modelArg/add",json).done(function(result){
                alert("success");
                                           }).fail(function(){
                alert("fail");
            });
        });
		
		$("#EditModelArgBtn").click(function(){
            var json = {"id":"2","durationCollect":"2000","durationLoad":"2000","durationSiteStartSort":"2000"};

            $.post("/modelArg/edit",json).done(function(result){
                alert("success");
                                           }).fail(function(){
                alert("fail");
            });
        });

        $("#DelModelArgBtn").click(function(){
                var json = {"id":3};
                $.post("/modelArg/del",json).done(function(result){
                      alert("success");
                      }).fail(function(){
                       alert("fail");
                 });
          });
		  
		  //需求信息调试

		 $("#AddDemandBtn").click(function(){
            var json = {"date":"2017-11-12","siteCodeCollect":"KSCD001","siteCodeDelivery":"SHPD001","productType":"type","weight":"2"};

            $.post("/demandInfo/add",json).done(function(result){
                alert("success");
                                           }).fail(function(){
                alert("fail");
            });
        });
		
		$("#EditDemandBtn").click(function(){
            var json = {"id":1,"date":"2017-11-10","siteCodeCollect":"KSCD002","siteCodeDelivery":"SHPD002","productType":"type3","weight":"5"};

            $.post("/demandInfo/edit",json).done(function(result){
                alert("success");
                                           }).fail(function(){
                alert("fail");
            });
        });

        $("#DelDemandBtn").click(function(){
                var json = {"id":1};
                $.post("/demandInfo/del",json).done(function(result){
                      alert("success");
                      }).fail(function(){
                       alert("fail");
                 });
          });
		  

         $("#saveBtn").click(function(){
                $("#addSiteInfoByExcel").submit();
            });

</script>

</body>

</html>
