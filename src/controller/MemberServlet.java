package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import com.DataValid;

import dao.AttendDao;
import dao.DaoFactory;
import dao.DepartDao;
import dao.MembersDao;
import domain.Depart;
import domain.Members;



@WebServlet("/Member")
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String editId;
	private String loginId;

	protected final String MEMBER_LIST = "memberList";
	protected final String MEMBER_INFO = "memberInfo";
	protected final String MEMBER_INSERT = "memberInsert";
	protected final String MEMBER_EDIT = "memberEdit";
	protected final String MEMBER_DELETE = "memberDelete";


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String servlet_id = request.getParameter("servletName");
		switch (servlet_id) {
		case MEMBER_LIST:
			int page = 0;
			try {
				try {
					page = Integer.parseInt(request.getParameter("page")); //memberlistから送られてきたのを受け取る

					//pageに開始ページを格納する
					if (page == 1) {
						page = 0;
					} else {
						page = 5 * (page - 1);
					}
					request.getSession().setAttribute("member_page", page); //セッションに格納する
				} catch (Exception e) {
					int prevnext = Integer.parseInt(request.getParameter("prevnext")); //memberlistから送られてきたのを受け取る
					//セッションに保存したmember_pageを取得し、変数に格納する
					int member_page = (Integer) request.getSession().getAttribute("member_page");

					//←→をするための仕組みを準備する
					if (prevnext == 1) {
						//もどる
						member_page = member_page - 5;
					} else {
						//すすむ
						member_page = member_page + 5;
					}
					request.getSession().setAttribute("member_page", member_page); //セッションに格納する

				}
			} catch (Exception e) {
				page = 0; //navbarなどからこのページにきたときの処理
				request.getSession().setAttribute("member_page", page); //セッションに格納する
			}
			try {
				//セッションに保存したmember_pageを取得し、変数に格納する
				int member_page = (Integer) request.getSession().getAttribute("member_page");

				MembersDao membersDao = DaoFactory.createMembersDao();
				List<Members> memberList = membersDao.findfive(membersDao.findAll(member_page));

				//lastpageを設定する
				int a = (membersDao.countAll());
				int lastpage =  (int) Math.ceil((double)a / 5);

				request.setAttribute("membersList", memberList);
				request.setAttribute("lastpage", lastpage);

				request.getRequestDispatcher("view/memberlist.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;
		case MEMBER_INFO:
			try {
				String memberId = request.getParameter("member_id");
				request.getSession().setAttribute("editingId", memberId);
			} catch (Exception e) {

			}
			String editingId = (String) request.getSession().getAttribute("editingId");

			try {
				MembersDao MembersDao = DaoFactory.createMembersDao();
				Members member = MembersDao.findById(editingId);
				//account分の作成
				request.setAttribute("member", member);
				request.getRequestDispatcher("view/memberinfo.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;

		case MEMBER_INSERT:
			try {
				DepartDao departDao =DaoFactory.createDepartDao();
				List<Depart> DepList=departDao.DepList();
				request.getSession().setAttribute("DepList", DepList);
				request.getRequestDispatcher("view/memberinsert.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;
		case MEMBER_EDIT:
			MembersDao MembersDao = DaoFactory.createMembersDao();
			DepartDao departDao =DaoFactory.createDepartDao();
			int auth_id=(Integer)request.getSession().getAttribute("auth_id");
			if(auth_id==2) {
				String member_id=(String)request.getSession().getAttribute("member_id");
				String login_id=(String)request.getSession().getAttribute("login_id");
				try {
					Members member = MembersDao.findById(member_id);
					member.setLogin_id(login_id);
					this.loginId = member.getLogin_id();
					List<Depart> DepList=departDao.DepList();
					request.getSession().setAttribute("DepList", DepList);
					request.setAttribute("member", member);
					request.getRequestDispatcher("view/onlyPassEdit.jsp").forward(request, response);
				} catch (Exception e) {
					throw new ServletException(e);
				}
			}else {
			this.editId = (String) request.getSession().getAttribute("editingId");
			String login_id = request.getParameter("login_id");

			try {
				Members member = MembersDao.findById(this.editId);
				member.setLogin_id(login_id);
				this.loginId = member.getLogin_id();

				request.setAttribute("member", member);
				request.getRequestDispatcher("view/memberedit.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			}
			break;
		case MEMBER_DELETE:
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String servlet_id = request.getParameter("servletName");


		switch (servlet_id) {
		case MEMBER_LIST:

			break;
		case MEMBER_INFO:
			try {
				String memberId = request.getParameter("member_id");

				request.getSession().setAttribute("editingId", memberId);
			} catch (Exception e) {
				throw new ServletException(e);

			}
			String editingId = (String) request.getSession().getAttribute("editingId");

			try {
				MembersDao MembersDao = DaoFactory.createMembersDao();
				Members member = MembersDao.findById(editingId);
				request.setAttribute("member", member);
				request.getRequestDispatcher("view/memberinfo.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}

			break;
		case MEMBER_INSERT:


//			boolean Check=true;
//
//			//DataValidcheck
//			if(request.getParameter("kana")!=null) {
//			if(!DataValid.isKana(request.getParameter("kana"))){
//				request.setAttribute("error_kana", true);
//				Check=false;
//			}
//			}
//			if(!DataValid.isTelFormat(request.getParameter("tel"))) {
//				request.setAttribute("error_tel", true);
//				Check=false;
//
//			}
//			//日付のM Dを1つにしている
//			if(!DataValid.isDateFormat(request.getParameter("birthday"), "yyyy-M-d")) {
//				request.setAttribute("error_birthday", true);
//				Check=false;
//
//			}
//
//			if(!DataValid.isDateFormat(request.getParameter("hired"), "yyyy-M-d")) {
//				request.setAttribute("error_hired", true);
//				Check=false;
//
//			}
//
//			if(!DataValid.isAlphanum(request.getParameter("login_id"))||
//					DataValid.limitChar(request.getParameter("login_id"), 8)) {
//				request.setAttribute("error_login_id", true);
//				Check=false;
//							}
//
//			if(!DataValid.inNotNum(request.getParameter("login_pass"))) {
//				request.setAttribute("error_login_pass", true);
//
//				Check=false;
//
//			}

//			if(Check==false) {
//
//				request.getRequestDispatcher("view/memberinsertDone.jsp").forward(request, response);
//			}

			String member_id=request.getParameter("member_id");
			String name = request.getParameter("name");
			String kana=request.getParameter("kana");
			String address=request.getParameter("address");
			String tel =request.getParameter("tel");

			//birthdayの定義
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date birthday=null;
			Date hired=null;

			try {
				birthday = sdf.parse(request.getParameter("birthday"));
				hired = sdf.parse(request.getParameter("hired"));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			String login_id = request.getParameter("login_id");
			String login_pass = request.getParameter("login_pass");
			int dep_id = Integer.parseInt(request.getParameter("dep_id"));
			//int position_type=Integer.parseInt(request.getParameter("position_type"));
			int auth_id= Integer.parseInt(request.getParameter("auth_id"));
			//String birth_str = request.getParameter("birthday");
			//String hired_str = request.getParameter("hired");


			// データの追加
			Members memberI = new Members();
			memberI.setMember_id(member_id);
			memberI.setName(name);
			memberI.setKana(kana);
			memberI.setAddress(address);
			memberI.setTel(tel);
			memberI.setBirthday(birthday);
			memberI.setHired(hired);
			memberI.setLogin_id(login_id);
			memberI.setLogin_pass(login_pass);
			memberI.setDep_id(dep_id);
			memberI.setAuth_id(auth_id);

// DataValidを使って値をチェックする　クラスメソッドを追加してもよい
			// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
			List<String> errorList = memberDataValid(memberI, "memberInsert");
		//	String error_messages = errorList.get(0);
		//	if( error_messages.equals(null)  ){
			if(errorList == null) {

				// パスワードのハッシュ化
				String hashedPass = BCrypt.hashpw(login_pass, BCrypt.gensalt());
				memberI.setLogin_pass(hashedPass);

				try {
					MembersDao MembersDao = DaoFactory.createMembersDao();
					// login_idが使われているかチェックする
					if (MembersDao.CheckLoginId(login_id)) {
						MembersDao.insert(memberI);
						MembersDao.insertacount(memberI);
						request.getRequestDispatcher("view/memberinsertDone.jsp").forward(request, response);
					} else {
						// login_idが他のユーザーのlogin_idとかぶった場合の処理内容
						request.setAttribute("error_used_loginid", true);
						request.setAttribute("member", memberI);
						request.getRequestDispatcher("view/memberedit.jsp").forward(request, response);
					}
				} catch (Exception e) {
					throw new ServletException(e);
				}
			} else {
				for(String error_message:errorList) {
					request.setAttribute(error_message, true);
				}
				//request.setAttribute(error_message, true);
				request.setAttribute("member", memberI);
				request.getRequestDispatcher("view/memberinsert.jsp").forward(request, response);
			}
			break;

// ----------------------------------------------------------------------------------------------------------

		case MEMBER_EDIT:
			int auth=(int)request.getSession().getAttribute("auth_id");
			if(auth==2) {
				String edit_login_id = request.getParameter("login_id");
				String edit_login_pass = request.getParameter("login_pass");
				if(DataValid.isAlphanum(edit_login_pass)) {
					String hashedPass = BCrypt.hashpw(edit_login_pass, BCrypt.gensalt());
					Members member = new Members();
					member.setLogin_id(edit_login_id);
					member.setLogin_pass(hashedPass);
					String edit_check_login_pass = request.getParameter("check_login_pass");
					if(!edit_login_pass.equals(edit_check_login_pass)) {
						request.setAttribute("errorchar", true);
						request.getRequestDispatcher("view/onlyPassEdit.jsp").forward(request, response);
					}
					MembersDao MembersDao = DaoFactory.createMembersDao();
					try {
						MembersDao.CheckLoginPass(member);
						request.getRequestDispatcher("view/membereditDone.jsp").forward(request, response);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else {
			String edit_member_id=request.getParameter("member_id");
			String edit_name = request.getParameter("name");
			String edit_kana=request.getParameter("kana");
			String edit_address=request.getParameter("address");
			String edit_tel=request.getParameter("tel");
			SimpleDateFormat edit_sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date edit_birthday=null;
			//Date edit_hired=null;

			try {
				edit_birthday = edit_sdf.parse(request.getParameter("birthday"));
				//edit_hired = edit_sdf.parse(request.getParameter("hired"));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			String edit_login_id = request.getParameter("login_id");
			//System.out.println(edit_login_id);
			String edit_login_pass = request.getParameter("login_pass");
			int edit_dep_id = Integer.parseInt(request.getParameter("dep_id"));
			int edit_position_type=Integer.parseInt(request.getParameter("position_type"));
			int edit_auth_id= Integer.parseInt(request.getParameter("auth_id"));
			//int group = Integer.parseInt(request.getParameter("group_id"));

			String oldlogin_id=request.getParameter("oldlogin_id");
			String oldmember_id=request.getParameter("oldmember_id");

			// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
			if(DataValid.isAlphanum(edit_login_id) &&DataValid.isAlphanum(edit_login_pass)) {
				String hashedPass = BCrypt.hashpw(edit_login_pass, BCrypt.gensalt());
				// メンバーからユーザーIdを取得しインスタンスにセット
				Members member = new Members();

				member.setMember_id(edit_member_id);
				member.setName(edit_name);
				member.setKana(edit_kana);
				member.setAddress(edit_address);
				member.setTel(edit_tel);
				member.setBirthday(edit_birthday);
				member.setLogin_id(edit_login_id);
				member.setLogin_pass(hashedPass);
				member.setDep_id(edit_dep_id);
				member.setPosition_type(edit_position_type);
				member.setAuth_id(edit_auth_id);
				member.setOldlogin_id(oldlogin_id);
				member.setOldmember_id(oldmember_id);



				try {
					MembersDao MembersDao = DaoFactory.createMembersDao();
					// login_idが使われているかチェックする
					if (MembersDao.CheckLoginId(edit_login_id) || this.loginId.equals(edit_login_id)) {
						MembersDao.update(member);
						MembersDao.updateaccount(member);

						request.setAttribute("memberId", editId);
						request.setAttribute("member", member);
						request.getRequestDispatcher("view/membereditDone.jsp").forward(request, response);
					} else {
						// login_idが他のユーザーのlogin_idとかぶった場合の処理内容
						request.setAttribute("member", member);
						request.setAttribute("error", true);
						request.getRequestDispatcher("view/memberedit.jsp").forward(request, response);
					}
				} catch (Exception e) {
					throw new ServletException(e);
				}
			} else if(DataValid.isAlphanum(edit_login_id) && !DataValid.isNotNull(edit_login_pass)) {// パスワード無しの場合の更新処理
				Members member = new Members();

				member.setMember_id(edit_member_id);
				member.setName(edit_name);
				member.setKana(edit_kana);
				member.setAddress(edit_address);
				member.setTel(edit_tel);
				member.setBirthday(edit_birthday);

				member.setLogin_id(edit_login_id);
				member.setDep_id(edit_dep_id);
				member.setPosition_type(edit_position_type);
				member.setAuth_id(edit_auth_id);
				member.setOldlogin_id(oldlogin_id);
				member.setOldmember_id(oldmember_id);


				try {
					MembersDao MembersDao = DaoFactory.createMembersDao();
					// login_idが使われているかチェックする
					if (MembersDao.CheckLoginId(edit_login_id) || this.loginId.equals(edit_login_id)) {
						MembersDao.updateWhithoutPass(member);
						MembersDao.updateAccountWhithoutPass(member);
						request.setAttribute("member", member);
						request.setAttribute("memberId", editId);
						request.getRequestDispatcher("view/membereditDone.jsp").forward(request, response);
					} else {
						// login_idが他のユーザーのlogin_idとかぶった場合の処理内容
						request.setAttribute("error", true);
						request.getRequestDispatcher("view/memberedit.jsp").forward(request, response);
					}
				} catch (Exception e) {
					throw new ServletException(e);
				}
			} else {

				request.setAttribute("errorchar", true);
				request.getRequestDispatcher("view/memberedit.jsp").forward(request, response);
			}
			}
			break;


		case MEMBER_DELETE:
			String memberId = request.getParameter("member_id");
			String dlete_login_id=request.getParameter("login_id");
			try {
				MembersDao MembersDao = DaoFactory.createMembersDao();
				AttendDao attendDao = DaoFactory.createAttendDao();
				Members member = new Members();
				member.setMember_id(memberId);
				member.setLogin_id(dlete_login_id);
				attendDao.deleteByMemberId(member);
				MembersDao.delete(member);
				MembersDao.deleteAccount(member);

			} catch (Exception e) {
				throw new ServletException(e);
			}
			request.getRequestDispatcher("view/memberdelDone.jsp").forward(request, response);
			break;
		}

	}

	// メンバー登録、編集時のデータチェックメソッド、戻り値をList<String>のエラー名に変える予定
	private List<String> memberDataValid(Members member, String servname) {
		List<String> validList = new ArrayList<String>();
		if(member.getKana()!=null) {
			if(!DataValid.isKana(member.getKana())){
				validList.add("error_kana");
			}
		}
		if(!DataValid.isTelFormat(member.getTel())) {
			validList.add("error_tel");
		}
		//日付のM Dを1つにしている //そもそもData型の時点でチェックできてるのでこのチェックは必要無い可能性あり
//		if(!DataValid.isDateFormat(member.getBirthday(), "yyyy-M-d")) {
//			return "error_birthday";
//		}
		//このチェックは必要無い可能性あり
//		if(servname.equals("memberInsert")) {
//			if(!DataValid.isDateFormat(member.getHired(), "yyyy-M-d")) {
//				return "error_hired";
//			}
//		}
		if(!DataValid.isAlphanum(member.getLogin_id())||
				DataValid.limitChar(member.getLogin_id(), 8)) {
			validList.add("error_login_id");
		}
		if(!DataValid.inNotNum(member.getLogin_pass())) {
			validList.add("error_login_pass");
		}
		return validList;
	}

}
