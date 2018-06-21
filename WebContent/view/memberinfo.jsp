<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>ユーザ詳細</title>
</head>
<body>
	<%@ include file="navbar.jsp"%>
	<div class="col-md-10 col-md-offset-1">
		<h1>ユーザ詳細</h1>

		<table class="table">



			<tr>
				<th class="col-xs-2">ID</th>
				<td class="col-xs-10"><c:out value="${member.member_id}" /></td>
			</tr>
			<tr>
				<th>氏名</th>
				<td><c:out value="${member.name}" /></td>
			</tr>
			<tr>
				<th>フリガナ</th>
				<td><c:out value="${member.kana}" /></td>
			</tr>
			<tr>
				<th>住所</th>
				<td><c:out value="${member.address}" /></td>
			</tr>
			<tr>
				<th>電話番号</th>
				<td><c:out value="${member.tel}" /></td>
			</tr>
			<tr>
				<th>誕生日</th>
				<td><c:out value="${member.birthday}" /></td>
			</tr>
			<tr>
				<th>入社日</th>
				<td><c:out value="${member.hired}" /></td>
			</tr>
			<tr>
				<th>所属グループ</th>
				<td><c:out value="${member.department}" /></td>
			</tr>
			<tr>
				<th>役職</th>
				<td>
				<c:if test="${member.position_type==1}">
							部長
				</c:if>
				<c:if test="${member.position_type==2}">
							課長
				</c:if>
				<c:if test="${member.position_type==3}">
							係長
				</c:if>
				<c:if test="${member.position_type==0}">
							一般社員
				</c:if>
				</td>
			</tr>

		</table>
		<div class="btn-toolbar">
  <div class="btn-group">
			<a href="Member?servletName=memberList"><button type="button" class="btn btn-primary" >一覧に戻る</button></a>

		<form action="Member" method="get">
			<input type="hidden" name="memberId" value="${member.member_id}">
			<input type="hidden" name="login_id" value="${member.login_id}">
			<input type="hidden" name="servletName" value="memberEdit">
			<input type="submit" class="btn btn-default" value="編集" />
		</form>
		<section>
		<button type="button" class="btn btn-danger"
			data-toggle="modal" data-target="#deleteAlert">削除
		</button>
		</section>


		<!-- モーダル・ダイアログ -->
		<section>
		<div class="modal fade" id="deleteAlert" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span>×</span>
						</button>
						<h5 class="modal-title">本当に削除してよろしいですか？</h5></div>
					<div class="modal-footer" align="right">

						<form action="Member" method="post" >
							<input type="hidden" name="member_id" value="${member.member_id}">
							<input type="hidden" name="login_id" value="${member.login_id}">
							<input type="hidden" name="servletName" value="memberDelete">
							<input type="submit" class="btn btn-primary pull-right"  value="OK" />
						</form>
						<button type="button" class="btn btn-default pull-right" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>
		</section>
		</div>
		</div>
	</div>
</body>
</html>