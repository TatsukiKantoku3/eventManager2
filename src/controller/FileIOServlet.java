package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fileio.FileController;

/**
 * Servlet implementation class FileIOServlet
 */
@WebServlet("/FileIOServlet")
public class FileIOServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected final String MEMBER_INSERT = "memberInsert";
	protected final String ACCOUNT_INSERT = "accountInsert";
	protected final String PLACE_INSERT = "placeInsert";
	protected final String DEPART_INSERT = "departInsert";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("view/MasterInsert.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String fileName = (String) request.getParameter("fileName");
		String error = fileName + "error";
		String complete = fileName + "complete";

		String result = null;
		FileController fileController=new FileController();
		try {
			switch (fileName) {

			case MEMBER_INSERT:
				result = fileController.member(fileName);

				break;
			case ACCOUNT_INSERT:
				result = fileController.account(fileName);

				break;
			case PLACE_INSERT:
				result = fileController.place(fileName);

				break;
			case DEPART_INSERT:
				result = fileController.depart(fileName);

				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// マスター登録するjavaクラスのコントローラーをインスタンス
		// インスタンス.メソッド名(rb.getString(fileName),rb.getString(fileName + "column"));
		if (result.equals("100")) {

			request.setAttribute(complete, "test");

		} else {

			request.setAttribute(error, "testt2");

		}
		request.setAttribute("errorCode", result);
		request.getRequestDispatcher("view/MasterInsert.jsp").forward(request, response);

	}

}
