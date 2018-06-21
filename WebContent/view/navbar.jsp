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
				<li><a href="EventServlet?servletName=eventToday">本日のイベント</a></li>
				<li><a href="EventServlet?servletName=eventList">イベント管理</a></li>
				<c:if test="${auth_id == 1}"><li><a href="Member?servletName=memberList">ユーザ管理</a></li></c:if>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>
					<c:out value="${name}" /> <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="logout">ログアウト</a></li>
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