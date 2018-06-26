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

<!-- 外部参照(CDN)によってbootstrapを有効化 -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<!-- cssが効かないため、一時的に直書きで対応 -->
<style>
	#radius {
	  border: 1px solid #aaa;
	  border-collapse: separate;
	  border-spacing: 0;
	  border-radius: 6px;
	}

	.color {
	  border-bottom: 1px solid #aaa;
	  background-color: #ddd;
	}

	/* ヘッダー*/
	#impact{
	  color:#6495ed;
	  font-family: Impact;
	}

	div.login_top{
		margin-top: 30px;
	}

	/* eventinsert */
	.eventinsert { width: 100%; }

	/* 太字	*/
	.bold{font-weight: bold;}

	/* 表示するデータが0のときのページネーションボタン非表示 */
	.invisible{visility:hidden}

	.display {
	    display:none
	}

	#hh2 {text-align:center}

	.drop {
		width: 750px;
		height: 40px;
		margin: 0 auto;
		padding: 0;
		display: flex;
	}
	.drop li {
		position: relative;
		list-style: none;
	}
	.drop li a {
		display: block;
		width: 150px;
		height: 40px;
		text-align: center;
		font-size: 14px;
		line-height: 2.8;
		color: #000000;
		background: #dcdcdc;
		transition: 0.5s;
		text-decoration: none;
		box-sizing: border-box;
	}
	.drop li ul {
		top: 40px;
		left: -40px;
		position: absolute;
	}
	.drop li ul li {
		overflow: hidden;
		height: 0;
		transition: 0.2s;
	}
	.drop li:hover > ul > li {
		overflow: visible;
		height: 40px;
	}
</style>

<meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<div class="container-fluid login_top">
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
						<br> <input type="tel" placeholder="ログインID" name="login_id"
						id="loginId" class="form-control" maxlength="20" required style="ime-mode:disabled"> <br> <br>
						<input type="password" placeholder="パスワード" name="login_pass"
							id="loginPass" class="form-control" maxlength="60" required> <br> <br>
						<input type="submit" value="ログイン" class="btn btn-primary form-control" />
					</form></td>
				</tr>
			</table>
		</div>
	</div>
</div>
</body>
</html>