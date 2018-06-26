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

						<input type="hidden" name="member_id" placeholder="社員番号" class="form-control" readonly="readonly" value="${member.member_id}" maxlength="8" required>

					<p>
						<input type="hidden" name="name" placeholder="氏名" class="form-control" readonly="readonly" value="${member.name}" maxlength="50" required >
					</p>


					<p>
						<input type="hidden" name="kana" placeholder="フリガナ" class="form-control" readonly="readonly" value="${member.kana}" maxlength="100" required >
					</p>


					<p>
						<input type="hidden" name="address" placeholder="(例)東京都新宿区○○△△-□□" class="form-control" readonly="readonly" value="${member.address}" maxlength="255" required >
					</p>


					<p>
						<input type="hidden" name="tel" placeholder="(例)090-1234-5678" class="form-control" readonly="readonly" value="${member.tel}" maxlength="13" required >
					</p>


					<p>
						<input type="hidden" name="birthday" placeholder="yyyy-mm-dd" class="form-control" readonly="readonly"  value="${member.birthday}" required>
					</p>



					<p>
						<input type="hidden" name="login_id" placeholder="ログインID" class="form-control" readonly="readonly" value="${member.login_id}" maxlength="20" required>
					</p>
					<p class="bold">パスワード</p>
					<c:if test="${!empty errorchar}">
						<div class="alert alert-warning" role="alert">パスワードは半角英数字のみ入力できます</div>
					</c:if>
					<input type="text" name="login_pass" placeholder="パスワード" class="form-control" maxlength="60"  placeholder="パスワード">
		<br><br>
 				<div class="display">
					<p class="bold">所属部署</p>
					<p><select  name="dep_id" class="form-control"  >
							<option value="1">人事部</option>
							<option value="2">経理部</option>
							<option value="3">総務部</option>
							<option value="4">営業部</option>
							<option value="5">開発部</option>
						</select>
					</p>

					<p class="bold">役職</p>
					<p><select name="position_type" class="form-control"  >
							<option value="0">一般社員</option>
							<option value="1">部長</option>
							<option value="2">課長</option>
							<option value="3">係長</option>
						</select>
					</p>

					<p class="bold">管理権限</p>
					<p><select name="auth_id" class="form-control" >
							<option value="2">一般</option>

						</select>
					</p>
				</div>

					<p>
						<a href="Event?servletName=eventToday" type="button" class="btn btn-default">キャンセル</a>

						<input type="hidden" name="oldlogin_id" value="${member.login_id}"/>
						<input type="hidden" name="oldmember_id" value="${member.member_id}"/>
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