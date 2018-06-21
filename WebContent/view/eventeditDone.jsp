<%@ page pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<title>イベント情報編集の完了</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
<div class="container col-md-10 col-md-offset-1">
<h1>イベント編集</h1>
イベントの編集が完了しました。<br>
<a href="EventServlet?servletName=eventInfo&event_id=${event_id}">イベント詳細に戻る</a>
</div>
</body>
</html>