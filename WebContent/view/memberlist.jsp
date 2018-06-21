<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>ユーザ一覧</title>
</head>
<body>
	<%@ include file="navbar.jsp"%>
	<div class="col-md-10 col-md-offset-1">
		<h1>ユーザ一覧</h1>


		<%
		//最終ページ番号をセッションから取得する
		Object lastpage = session.getAttribute("lastpage");

			//現在のページ番号を計算する
			Object member_page = session.getAttribute("member_page");
			int a = (int) member_page;
			int now = (a / 5) + 1;
		%>


		<nav>
			<ul class="pagination">
				<% if(now!=1){ %>
				<li><a href="Member?prevnext=1&servletName=memberList" aria-label="前のページへ">
						<span aria-hidden="true">«</span>
				</a></li>
				<%}

				int i = 1;%>

				<c:forEach var="i" begin="1" end="${lastpage }">

					<li <%

				if (now == i) {%> class="active" <%}%>>
						<a href="Member?servletName=memberList&page=<%= i %>"><%= i %></a>
					</li>
					<%i++; %>
				</c:forEach>

					<%
					if(now!=i-1){ %>
					<li><a href="Member?servletName=memberList&prevnext=2" aria-label="次のページへ">
							<span aria-hidden="true">»</span>
					</a></li>
					<%}
				%>

			</ul>
		</nav>







		<table class="table table-bordered table-hover ">
			<tr>
				<th>ID</th>
				<th>氏名</th>
				<th>所属グループ</th>
				<th>詳細</th>

			</tr>
			<c:forEach items="${membersList}" var="member">
				<tr>
					<td><c:out value="${member.member_id}" /></td>
					<td><c:out value="${member.name}" /></td>
					<td><c:out value="${member.department}" /></td>
					<td>
						<form action="Member" method="post">
							<input type="hidden" name="member_id" value="${member.member_id}">
							<input type="hidden" name="login_id" value="${member.login_id }">
							<input type="hidden" name="servletName" value="memberInfo">
							<input type="submit" class="btn btn-info" value="詳細" />
						</form>
					</td>

				</tr>
			</c:forEach>
		</table>
		<form action="Member" method="get">
			<!--  view/addMember.jsp-->
			<input type="hidden" name="servletName" value="memberInsert">
			<input type="submit" class="btn btn-primary" value="ユーザの登録" />
		</form>
	</div>
</body>
</html>