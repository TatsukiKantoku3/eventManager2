package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AttendDao;
import dao.DaoFactory;

/**
 * Servlet implementation class AttendServlet
 */
@WebServlet("/attend")
public class AttendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AttendServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 参加する
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String userId = (String)request.getSession().getAttribute("member_id");
			int eventId = Integer.parseInt(request.getParameter("event_id"));
			int switchId = Integer.parseInt(request.getParameter("switchId"));

			if(switchId == 0) {
			AttendDao AttendDao = DaoFactory.createAttendDao();
			AttendDao.insert(userId,eventId);
			} else if(switchId == 1) {
				AttendDao AttendDao = DaoFactory.createAttendDao();
				AttendDao.delete(userId,eventId);
			}
			request.setAttribute("servletName", "eventInfo");
			request.getRequestDispatcher("EventServlet").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
