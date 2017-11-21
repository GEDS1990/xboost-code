<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${customer.custname}</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <link rel="stylesheet" href="/static/js/weui/lib/weui.min.css">
    <link rel="stylesheet" href="/static/js/weui/css/jquery-weui.min.css">
</head>
<body>

姓名：${customer.custname} <br>
联系电话: ${customer.tel}


<script src="/static/js/weui/lib/jquery-2.1.4.js"></script>
<script src="/static/js/weui/js/jquery-weui.min.js"></script>
</body>
</html>