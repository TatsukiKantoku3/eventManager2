<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>ユーザ編集の完了</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
<div class="col-md-10 col-md-offset-1">
<h1>ユーザ編集</h1>
ユーザの編集が完了しました。<br>

<form action="Member" method="get">
<input type="hidden" name="member_id" value="${member.member_id}"/>
<input type="hidden" name="servletName" value="memberInfo"/>
<input type="submit" class="btn btn-link" value="ユーザー詳細に戻る" style="padding:0"/>
</form>

</div>
</body>
</html>