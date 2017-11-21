<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的客户</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <link rel="stylesheet" href="/static/js/weui/lib/weui.min.css">
    <link rel="stylesheet" href="/static/js/weui/css/jquery-weui.min.css">
</head>
<body>

<div class="weui_search_bar" id="search_bar">
    <form class="weui_search_outer">
        <div class="weui_search_inner">
            <i class="weui_icon_search"></i>
            <input type="search" class="weui_search_input" id="search_input" placeholder="搜索" required/>
            <a href="javascript:" class="weui_icon_clear" id="search_clear"></a>
        </div>
        <label for="search_input" class="weui_search_text" id="search_text">
            <i class="weui_icon_search"></i>
            <span>搜索</span>
        </label>
    </form>
    <a href="javascript:" class="weui_search_cancel" id="search_cancel">取消</a>
</div>

<div class="weui_cells weui_cells_access">
    <c:forEach items="${customerList}" var="cust">
        <a class="weui_cell" href="/wx/customer/${cust.id}">
            <div class="weui_cell_bd weui_cell_primary">
                <p>${cust.custname}</p>
            </div>
            <div class="weui_cell_ft"></div>
        </a>
    </c:forEach>
</div>


<script src="/static/js/weui/lib/jquery-2.1.4.js"></script>
<script src="/static/js/weui/js/jquery-weui.min.js"></script>
</body>
</html>