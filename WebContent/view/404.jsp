<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<title>ページがありません</title>
</head>
<body>
<c:import url="../view/header.jsp" >
        <c:param name="page" value="4" />
    </c:import>
	<div class="container nav-space">
<div class="row">
	<div class="col-xs-6 col-xs-offset-3">

		<h1>404 Page Not Found</h1>
		<p>リクエストされたページは存在しません</p>
	</div>
</div>
</div>
</body>
</html>