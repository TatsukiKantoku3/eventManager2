<%@ page pageEncoding="UTF-8"%>
<%@ page import="domain.Events"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html>
<html lang="ja">
<head>
<title>イベント詳細</title>
<%
	session.setAttribute("disp", 0);
%>
<c:forEach items="${attendList}" var="attend">
	<c:if test="${attend.member_id==member_id}">
		<%
			session.setAttribute("disp", 1);
		%>
	</c:if>
</c:forEach>

</head>
<body>
	<%@ include file="navbar.jsp"%>
	<div class="col-md-10 col-md-offset-1">
		<h1>イベント詳細</h1>

		<table class="table">
			<tr>
				<th class="col-xs-2">タイトル</th>
				<td class="col-xs-10">
				<c:out value="${event.title}" />
				<%
					int i = (int) session.getAttribute("disp");
					if (i == 1) {
 				%>
 				<span class="label label-danger">参加</span>
 				<%} %>
				</td>
			</tr>
			<tr>
				<th>開始日時</th>
				<td><fmt:formatDate value="${event.start}" pattern="y年M月d日(E) HH時mm分" /></td>
			</tr>
			<tr>
				<th>終了日時</th>
				<td><fmt:formatDate value="${event.end}" pattern="y年M月d日(E) HH時mm分" /></td>
			</tr>
			<tr>
				<th>場所</th>
				<td><c:out value="${event.place_name}" /></td>
			</tr>
			<tr>
				<th>対象グループ</th>
				<td><c:out value="${event.dep_name}" /></td>
			</tr>
			<tr>
				<th>詳細</th>
				<td><c:out value="${event.detail}" /></td>
			</tr>
			<tr>
				<th>登録者</th>
				<td>
					<c:if test="${event.member_name==null}">無効なユーザ</c:if>
					<c:out value="${event.member_name}" />
				</td>
			</tr>
			<tr>
				<th>参加者</th>
				<td><c:forEach items="${attendList}" var="attend" varStatus="status">
						<c:out value="${attend.member_name}" />
						<c:if test="${!status.last}">,
						</c:if>
					</c:forEach></td>
			</tr>
		</table>
		<div class="btn-toolbar">
			<div class="btn-group">
				<a href="EventServlet?servletName=eventList"><button type="button"
						class="btn btn-primary">一覧に戻る</button></a>


				<%
					if (i == 1) {
				%>

				<form action="attend" method="post">
					<input type="hidden" name="event_id" value="${event.event_id}">
					<input type="hidden" name="switchId" value="1">
					<input type="submit" class="btn btn-warning" value="参加を取り消す" />
				</form>

				<%
					} else {
				%>
				<form action="attend" method="post">
					<input type="hidden" name="event_id" value="${event.event_id}">
					<input type="hidden" name="switchId" value="0">
					<input type="submit" class="btn btn-info" value="参加する" />
				</form>
				<%
					}
				%>

				<%
				int auth_id=(int)session.getAttribute("auth_id");
				%>
				<%if(auth_id==1){ %>
				<form action="EventServlet?servletName=eventEdit" method="get">
					<input type="hidden" name="event_id" value="${event.event_id}">
					<input type="hidden" name="servletName" value="eventEdit" />
					<input type="submit" class="btn btn-default" value="編集" />
				</form>
				<%}else{ %>
				<c:if test="${event.member_name==member_name }">
				<form action="EventServlet?servletName=eventEdit" method="get">
					<input type="hidden" name="event_id" value="${event.event_id}">
					<input type="hidden" name="servletName" value="eventEdit" />
					<input type="submit" class="btn btn-default" value="編集" />
				</form>
				</c:if>
				<%} %>
				<%
				auth_id=(int)session.getAttribute("auth_id");
				%>
				<%if(auth_id==1){ %>
				<section>
					<!-- ボタンをクリックしたらモーダル・ダイアログが表示されます -->
					<button type="button" class="btn btn-danger"
						data-toggle="modal" data-target="#deleteAlert">削除
					</button>
				</section>
				<%}else{ %>
				<c:if test="${event.member_name==member_name }">
				<section>
					<!-- ボタンをクリックしたらモーダル・ダイアログが表示されます -->
					<button type="button" class="btn btn-danger"
						data-toggle="modal" data-target="#deleteAlert">削除
					</button>
				</section>
				</c:if>
				<%} %>
			</div>
		</div>
	</div>


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
					<form action="EventServlet?servletName=eventDelete" method="post">
						<input type="hidden" name="event_id" value="${event.event_id}">
						<input type="submit" class="btn btn-primary pull-right" value="OK" />
					</form>
					<button type="button" class="btn btn-default pull-right" data-dismiss="modal">Cancel</button>
				</div>
			</div>
		</div>
	</div>
	</section>

</body>
</html>