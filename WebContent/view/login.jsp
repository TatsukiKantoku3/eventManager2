<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ja">
<head>

<title>ログイン</title>
<meta charset="UTF-8" />
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<!-- ▼ Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet"/>
    <link href="css/style.css" rel="stylesheet"/>
    <link href="css/sticky-footer.css" rel="stylesheet"/>


<meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<div class="row">
	<div class="col-md-10 col-md-offset-1">
		<table class="table" id="radius">
			<tr>
				<td class="color" id="impact"><h2>Event Manager</h2></td></tr>
					<tr>
					<td>
					<c:if test="${!empty error}">
						<div class="alert alert-warning" role="alert">ログインIDかパスワードが正しくありません</div>
					</c:if>
					<c:if test="${!empty errorchar}">
						<div class="alert alert-warning" role="alert">ログインID,パスワードは半角英数字のみ使用できます</div>
					</c:if>
					<form action="login" method="post">
						<br> <input type="text" placeholder="ログインID" name="login_id"
							id="loginId" class="form-control" required> <br> <br>
						<input type="password" placeholder="パスワード" name="login_pass"
							id="loginPass" class="form-control" required> <br> <br>
						<input type="submit" value="ログイン" class="btn btn-primary form-control" />
					</form></td>
			</tr>
		</table>
	</div>
	</div>
</body>
</html>