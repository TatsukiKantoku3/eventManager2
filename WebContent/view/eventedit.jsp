<%@ page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<title>イベント編集</title>
<script src="js/jquery-3.3.1.min.js"></script>

</head>
<body>
<%@ include file="navbar.jsp"%>
	<!-- Main -->
	<section>
		<div class="row">
			<div class="container col-md-10 col-md-offset-1">
				<h1>イベント編集</h1>
				<!-- 入力画面-->
				<div class="form-group">
				<form action="EventServlet" method="post">
					<p class="bold">タイトル（必須）</p>
					<p>
						<input type="text" name="title" class="form-control" value="${event.title}" maxlength="50" required />
					</p>

					<p class="bold">開始日時（必須）</p>
					<p>
						<input type="text" name="start" placeholder="0000-00-00 00:00:00" class="form-control" value="${event.start}" required>
					</p>

					<p class="bold">終了日時</p>
					<input type="text" name="end" placeholder="0000-00-00 00:00:00" class="form-control"value="${event.end}">


					<p class="bold">場所</p>
					<p><select name="place_id" class="form-control" id="place_id">
							<option value="1">第一会議室</option>
							<option value="2">第二会議室</option>
							<option value="3">第三会議室</option>
						</select>
					</p>

					<p class="bold">対象グループ</p>
					<p><select name="dep_id" class="form-control" id="dep_id">
							<option value="6">全員</option>
							<option value="1">人事部</option>
							<option value="2">経理部</option>
							<option value="3">総務部</option>
							<option value="4">営業部</option>
							<option value="5">開発部</option>
						</select>
					</p>
					<p class="bold">詳細</p>
					<p>
						<textarea name="detail" rows="4" cols="40" class="form-control" >${event.detail}</textarea>
					</p>
					<p>
						<a href="EventServlet?servletName=eventInfo&event_id=${event.event_id}" type="button" class="btn btn-default">キャンセル</a>
						<input type="hidden" name="event_id" value="${event.event_id}">
						<input type="hidden" name="servletName" value="eventEdit" />
						<input type="submit" class="btn btn-primary" value="登録" />
					</p>
				</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>