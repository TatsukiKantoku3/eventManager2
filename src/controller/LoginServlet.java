package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.DataValid;

import dao.DaoFactory;
import dao.MembersDao;
import domain.Members;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("view/login.jsp").forward(request,  response);
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String login_id = request.getParameter("login_id");
		String login_pass = request.getParameter("login_pass");

		// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
		if(DataValid.isAlphanum(login_id) && DataValid.isAlphanum(login_pass)) {
			try {
				MembersDao UsersDao = DaoFactory.createMembersDao();
				Members member2=UsersDao.login(login_id,  login_pass);
				Members member = UsersDao.findByLoginIdAndLoginPass(login_id,  login_pass);
				String pageName = "eventToday";
				if(member2 != null) {
					// セッションにユーザー情報"id,loginId,name,type_id"を登録
					request.getSession().setAttribute("member_id", member.getMember_id());
					request.getSession().setAttribute("login_id", member.getLogin_id());
					request.getSession().setAttribute("name", member.getName());
					request.getSession().setAttribute("auth_id", member2.getAuth_id());
					request.setAttribute("servletName", pageName);
					request.getRequestDispatcher("EventServlet").forward(request,  response);
				} else {
					request.setAttribute("error", true);
					request.getRequestDispatcher("view/login.jsp").forward(request,  response);
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else {
		// if文、文字列が半角英数字、ハイフン、アンダースコア以外の場合は以下の処理
		request.setAttribute("errorchar", true);
		request.getRequestDispatcher("view/login.jsp").forward(request,  response);
		}
	}
}
