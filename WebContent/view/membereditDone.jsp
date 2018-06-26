<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>メンバー編集の完了</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
<div class="col-md-10 col-md-offset-1">
<h1>メンバー編集</h1>
メンバーの編集が完了しました。<br>
<c:if test="${auth_id == 1}">
<form action="Member" method="get">
<input type="hidden" name="member_id" value="${member.member_id}"/>
<input type="hidden" name="servletName" value="memberInfo"/>
<input type="submit" class="btn btn-link" value="メンバー詳細に戻る" style="padding:0"/>
</form>
</c:if>
<c:if test="${auth_id == 2}">
<a href="Event?servletName=eventToday" type="button" class="btn btn-default">戻る</a>
</c:if>

</div>
</body>
</html>