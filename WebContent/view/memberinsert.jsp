<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>メンバー登録</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
	<!-- Main -->
	<section>
		<div class="row">
			<div class="container col-md-10 col-md-offset-1">
				<h1>メンバー登録</h1>
				<!-- 入力画面-->

					<div class="form-group">
				<form action="Member" method="post">

					<p class="bold">社員ID（必須）</p>
					<p>
						<input type="text" name="member_id" placeholder="社員ID" class="form-control" value="${member.member_id}" maxlength="8" required>
					</p>

					<p class="bold">氏名（必須）</p>
					<p>
						<input type="text" name="name" placeholder="氏名" class="form-control" value="${member.name}" maxlength="50" required>
					</p>

					<p class="bold">フリガナ(全角カタカナ)</p>
					<c:if test="${!empty error_kana}">
						<div class="alert alert-warning" role="alert">カタカナ以外は入力できません</div>
					</c:if>
					<p>
						<input type="text" name="kana" placeholder="フリガナ" class="form-control" value="${member.kana}" maxlength="100" >
					</p>

					<p class="bold">住所（必須）</p>
					<p>
						<input type="text" name="address" placeholder="(例)東京都新宿区○○△△-□□" class="form-control" value="${member.address}" maxlength="255" required>
					</p>

					<p class="bold">電話番号（必須）</p>
					<c:if test="${!empty error_tel}">
						<div class="alert alert-warning" role="alert">不正な電話番号です</div>
					</c:if>
					<p>
						<input type="text" name="tel" placeholder="(例)090-1234-5678" class="form-control" value="${member.tel}" maxlength="13" required>
					</p>

					<p class="bold">誕生日（必須）</p>
					<c:if test="${!empty error_birthday}">
						<div class="alert alert-warning" role="alert">不正な日付です</div>
					</c:if>
					<p>
						<input type="date" name="birthday" placeholder="1990-12-12" class="form-control" value="${member.birthday}" required>
					</p>

					<p class="bold">入社日（必須）</p>
					<c:if test="${!empty error_hired}">
						<div class="alert alert-warning" role="alert">不正な日付です</div>
					</c:if>
					<p>
						<input type="date" name="hired" placeholder="2018-4-1" class="form-control" value="${member.hired}" required>
					</p>

					<p class="bold">ログインID（必須）</p>
					<c:if test="${!empty error_login_id}">
						<div class="alert alert-warning" role="alert">ログインIDにはハイフン(-)と半角英数字のみ8文字以上20文字以下で指定されています</div>
					</c:if>
					<c:if test="${!empty error_used_loginid}">
						<div class="alert alert-warning" role="alert">ログインIDが既に使用されています</div>
					</c:if>
					<p>
						<input type="text" name="login_id" placeholder="ログインID" class="form-control" value="${member.login_id}" maxlength="20" required>
					</p>

					<p class="bold">パスワード（必須)</p>
					<c:if test="${!empty error_login_pass}">
						<div class="alert alert-warning" role="alert">パスワードには半角英数字のみ使用可能です</div>
					</c:if>
					<input type="text" name="login_pass" placeholder="パスワード" class="form-control"  maxlength="60" required>

					<p class="bold">所属部署（必須）</p>
					<p><select name="dep_id" class="form-control">
							<c:forEach items="${DepList}" var="DepList" varStatus="status">
								<option value="${status.count}">
								<c:out	value="${DepList.department}" /></option>
							</c:forEach>
						</select>
					</p>
					<p class="bold">権限（必須）</p>
					<p><select name="auth_id" class="form-control">
							<option value="1">管理者</option>
							<option value="2" selected>一般</option>
							</select>
					</p>

					<p>
						<a href="Member?servletName=memberList" class="btn btn-default">キャンセル</a>
						<input type="hidden" name="servletName" value="memberInsert">
						<input type="submit" class="btn btn-primary" value="登録" />
					</p>
				</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>