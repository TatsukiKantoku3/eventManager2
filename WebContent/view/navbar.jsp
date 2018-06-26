<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<link href="css/style.css" rel="stylesheet" />
<link href="css/bootstrap.css" rel="stylesheet" />
<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/drop.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet" />

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

<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">
<title>ヘッダー</title>
</head>
<body>
<div class="col-md-10 col-md-offset-1">
	<header>

<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbarEexample">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#" id="impact"> Event Manager </a>
		</div>

		<div class="collapse navbar-collapse" id="navbarEexample">
			<ul class="nav navbar-nav">
				<li <c:if test="${param.page==1}">class="active"</c:if>><a href="EventServlet?servletName=eventToday">本日のイベント</a></li>

				<li <c:if test="${param.page==2}">class="active"</c:if>><a href="EventServlet?servletName=eventList">イベント管理</a></li>

				<c:if test="${auth_id == 1}"><li <c:if test="${param.page==3}">class="active"</c:if>><a href="Member?servletName=memberList">メンバー管理</a></li></c:if>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>
					<c:out value="${name}" /> <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="logout">ログアウト</a></li>
						<c:if test="${auth_id == 1}"><li><a href="FileIOServlet">マスタ登録</a></li></c:if>
						<c:if test="${auth_id == 2}"><li><a href="Member?&servletName=memberEdit">パスワード変更</a></li></c:if>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</nav>
	</header>
	</div>
</body>
</html>