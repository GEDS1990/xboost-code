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

    <script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
    <style type="text/css">
        #connect-container {
            float: left;
            width: 400px
        }

        #connect-container div {
            padding: 5px;
        }

        #console-container {
            float: left;
            margin-left: 15px;
            width: 400px;
        }

        #console {
            border: 1px solid #CCCCCC;
            border-right-color: #999999;
            border-bottom-color: #999999;
            height: 170px;
            overflow-y: scroll;
            padding: 5px;
            width: 100%;
        }

        #console p {
            padding: 0;
            margin: 0;
        }
    </style>
</head>

<body>
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websockets
    rely on Javascript being enabled. Please enable
    Javascript and reload this page!</h2></noscript>

<div id="wrapper">

    <%@ include file="../include/nav.jsp"%>

    <!-- Page Content -->
    <div id="page-wrapper">
        <div class="modal-body">
                        <form action="/sitedist/addByExcel" method="post" id="addSiteDistByExcel" enctype="multipart/form-data">

                            <div class="form-group" id="fileControls" style="">
                                <label>批量导入网点距离信息：<button type="button" class="btn btn-default btn-xs" id="addFileControl"><i class="fa fa-plus"></i></button></label>
                                <input type="file" name="file" class="form-control">
                            </div>
                        </form>
                        <div class="modal-footer">
                            <button type="button" class=" btn btn-primary" id="saveBtn">提交表格</button>
                        </div>
                    </div>
                        <div class="modal-footer">
                            <button type="button" class=" btn btn-primary" id="MrnBtn">跑算法</button>
                            <div id="console-container">
                                <div id="console"></div>
                            </div>
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
<script src="/static/js/jquery.twbsPagination.min.js"></script>
<script>



        $("#newDistBtn").click(function(){
            var json = {"id":1,"siteCollect":"KSCD001","siteDelivery":"KSCX001","carDistance":"50","durationNightDelivery":"50"};
            $.post("/sitedist/new",json).done(function(result){
                alert("success");
                                           }).fail(function(){
                alert("fail");
            });
        });

        $("#AddSiteInfoBtn").click(function(){
            var json = {"siteCode":"KSCD001","siteLongitude":"258.223343","siteLatitude":"34.098586","siteName":"昆山城东","siteAddress":"前进路234号","siteNightDelivery":"1","siteArea":"3442","siteType":"集散点","carNum":"23","largeCarModel":"145","maxOperateNum":"4000"};

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

         $("#saveBtn").click(function(){
                var form = new FormData(document.getElementById("addSiteDistByExcel"));
                            $.ajax({
                                url:"${pageContext.request.contextPath}/sitedist/addByExcel",
                                type:"post",
                                data:form,
                                processData:false,
                                contentType:false,
                                success:function(data){
                                   // window.clearInterval(timer);
                                    alert("批量导入信息完成！");
                                },
                                error:function(e){
                                    alert("错误！！");
                                    window.clearInterval(timer);
                                }
                            });
                            //此处为上传文件的进度条get();

            });

        $("#MrnBtn").click(function(){
            document.getElementById('console').innerHTML="";
            ws = new SockJS("http://182.254.216.232:8080/webSocketServer/sockjs");
            ws.onopen = function () {
                log('Info: connection opened.');
            };

            ws.onmessage = function (event) {
                log(event.data);
            };
            ws.onclose = function (event) {

            };

            var json = {"senairoid":1}
            $.post("/cascade/newInput",json).done(function(result){
                alert("success");
                                           }).fail(function(){
                alert("fail");
            });
        });
        function log(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.appendChild(document.createTextNode(message));
            console.appendChild(p);
            console.scrollTop = console.scrollHeight;
        }



</script>

</body>

</html>
