<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>File Insert Page</title>

    <script src="js/bootstrap.min.js"></script>
<%
	request.setCharacterEncoding("Shift_JIS");
	String memberInsertcomplete = (String)request.getParameter("memberInsertcomplete");
	session.setAttribute("memInscompl",memberInsertcomplete);
	if(memberInsertcomplete==null || memberInsertcomplete == "")
		memberInsertcomplete = (String)session.getAttribute("memInscompl");

	String accInscompl = (String)request.getParameter("accountInsertcomplete");
	session.setAttribute("accInscompl",accInscompl);
	String plaInscompl = (String)request.getParameter("placeInsertcomplete");
	session.setAttribute("plaInscompl",plaInscompl);
	String depInscompl = (String)request.getParameter("departInsertcomplete");
	session.setAttribute("depInscompl",depInscompl);
%>
<!-- ▼ Bootstrap -->
<!--     <link href="css/bootstrap.min.css" rel="stylesheet"/>
    <link href="css/style.css" rel="stylesheet"/>
    <link href="css/sticky-footer.css" rel="stylesheet"/> -->
</head>
<body>
<div class="container">
	<%@ include file="navbar.jsp"%>
	<div class="col-md-8 col-md-offset-2">


	<div class="row">
	<div class="col-md-6 col-md-offset-3">
		<h2 id="hh2">マスタ登録用ページです</h2>
	</div></div>

	<div class="row">

	<form action="FileIOServlet" method="post">
		<input type="hidden" name="fileName"  value="memberInsert">
		<input type="submit" class="btn btn-primary center-block btn-block form-control" value="メンバー登録" />
		<c:if test="${!empty memberInsertcomplete}">
			<div class="alert alert-warning" role="alert">登録が完了しました</div>
		</c:if>
		<c:if test="${!empty memberInserterror}">
			<div class="alert alert-warning" role="alert">処理ができません</div>
		</c:if>
	</form>
	</div>
	<hr>

	<div class="row">

	<form action="FileIOServlet" method="post">
		<input type="hidden" name="fileName" value="accountInsert">
		<input type="submit" class="btn btn-primary center-block btn-block form-control" value="アカウント登録" />
		<c:if test="${!empty accountInsertcomplete}">
			<div class="alert alert-warning" role="alert">登録が完了しました</div>
		</c:if>
		<c:if test="${!empty accountInserterror}">
			<div class="alert alert-warning" role="alert">処理ができません</div>
		</c:if>
	</form>
	</div>
	<hr>

	<div class="row">

	<form action="FileIOServlet" method="post">
		<input type="hidden" name="fileName" value="placeInsert">
		<input type="submit" class="btn btn-primary center-block btn-block form-control" value="会議室登録" />
		<c:if test="${!empty placeInsertcomplete}">
			<div class="alert alert-warning" role="alert">登録が完了しました</div>
		</c:if>
		<c:if test="${!empty placeInserterror}">
			<div class="alert alert-warning" role="alert">処理ができません</div>
		</c:if>
	</form>
	</div>
	<hr>

	<div class="row">

	<form action="FileIOServlet" method="post">
		<input type="hidden" name="fileName" value="departInsert">
		<input type="submit" class="btn btn-primary center-block btn-block form-control" value="部署登録" />
		<c:if test="${!empty departInsertcomplete}">
			<div class="alert alert-warning" role="alert">登録が完了しました</div>
		</c:if>
		<c:if test="${!empty departInserterror}">
			<div class="alert alert-warning" role="alert">処理ができません</div>
		</c:if>
	</form>
	</div>
	</div>


</div>


</body>
</html>