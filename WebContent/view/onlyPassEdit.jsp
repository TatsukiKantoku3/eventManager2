<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>パスワード編集</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
	<!-- Main -->
	<section>
		<div class="row">
			<div class="container col-md-10 col-md-offset-1">
				<h1>パスワード編集</h1>
				<!-- 入力画面-->

					<p>社員ID:<c:out value="${member.member_id}"></c:out><p>
					<p>氏名:<c:out value="${member.name}"></c:out></p>
					<div class="form-group">
				<form action="Member" method="post">
					<p class="bold">パスワード</p>

					<input type="password" name="login_pass" placeholder="パスワード" class="form-control" maxlength="60" placeholder="パスワード" required>
					<p class="bold">パスワード(確認用)</p>
					<c:if test="${!empty errorchar}">
						<div class="alert alert-warning" role="alert">パスワードが異なっています</div>
					</c:if>
					<input type="password" name="check_login_pass" placeholder="パスワード" class="form-control" maxlength="60" placeholder="パスワード" required>
		<br><br>

					<p>
						<a href="EventServlet?servletName=eventToday" type="button" class="btn btn-default">キャンセル</a>
						<input type="hidden" name="login_id" value="${member.login_id}" >
						<input type="hidden" name="servletName" value="memberEdit"/>
						<input type="submit" class="btn btn-primary" value="登録" />
					</p>
				</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>