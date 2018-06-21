package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.DataValid;

import dao.AttendDao;
import dao.DaoFactory;
import dao.EventsDao;
import domain.Attend;
import domain.Events;

/**
 * Servlet implementation class EventServlet
 */
@WebServlet("/EventServlet")
public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected final String EVENT_TODAY = "eventToday";
	protected final String EVENT_LIST = "eventList";
	protected final String EVENT_INFO = "eventInfo";
	protected final String EVENT_EDIT = "eventEdit";
	protected final String EVENT_INSERT = "eventInsert";
	protected final String EVENT_DELETE = "eventDelete";


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// どのページ遷移かをパラメータから取得し定数と比較
		//EventServName eventServName = EventServName.valueOf(request.getParameter("servName"));
		String pageName = (String)request.getSession().getAttribute("servletName");
		if(DataValid.isNotNull(pageName)) { //(pageName==null || pageName == "")
			pageName = (String)request.getParameter("servletName");
		}

		switch(pageName){
			default:
				// doGetメソッドでは無効な処理 404ページへ遷移
				break;
			case EVENT_TODAY:
				eventToday(request, response);
				break;

			case EVENT_LIST:
				int page = 0;
				try {
					try {
						page = Integer.parseInt(request.getParameter("page")); //eventlist.jspから送られてきたのを受け取る

						//pageに開始ページを格納する
						if (page == 1) {
							page = 0;
						} else {
							page = 5 * (page - 1);
						}
						request.getSession().setAttribute("event_page", page); //セッションに格納する
					} catch (Exception e) {
						int prevnext = Integer.parseInt(request.getParameter("prevnext")); //eventslistから送られてきたのを受け取る
						int event_page = (Integer) request.getSession().getAttribute("event_page");

						//←→をするための仕組みを準備する
						if (prevnext == 1) {
							//もどる
							event_page = event_page - 5;
						} else {
							//すすむ
							event_page = event_page + 5;
						}
						request.getSession().setAttribute("event_page", event_page); //セッションに格納する
					}
				} catch (Exception e) {
					page = 0; //navbarなどからこのページにきたときの処理
					request.getSession().setAttribute("event_page", page); //セッションに格納する
				}
				String member_id = (String) request.getSession().getAttribute("member_id");
				try {
					//セッションに保存したevent_pageを取得し、変数に格納する
					int event_page = (Integer) request.getSession().getAttribute("event_page");
					EventsDao eventsDao = DaoFactory.createEventsDao();
					List<Events> eventsList = eventsDao.findfive(eventsDao.findAll(event_page), member_id);
					//lastpageを設定する
					int lastpage = (eventsDao.countAll());
					request.getSession().setAttribute("pageExist",lastpage );
					request.setAttribute("eventsList", eventsList);
					request.setAttribute("lastpage", lastpage);
					request.getRequestDispatcher("view/eventlist.jsp").forward(request, response);
				} catch (Exception e) {
					throw new ServletException(e);
				}
				break;

			case EVENT_INFO:
				try {
					int event_id = Integer.parseInt(request.getParameter("event_id"));
					request.getSession().setAttribute("findEventId", event_id);
					} catch(Exception e){

					}
					int findEventId=(int)request.getSession().getAttribute("findEventId");

				try {
					EventsDao eventsDao = DaoFactory.createEventsDao();
					Events event = eventsDao.findById(findEventId);
					request.setAttribute("event",event);
					AttendDao attendDao = DaoFactory.createAttendDao();
					List<Attend> attendList = attendDao.findAttends(findEventId);
					request.setAttribute("attendList",attendList);
		            request.getRequestDispatcher("view/eventinfo.jsp").forward(request, response);
				} catch (Exception e) {
					throw new ServletException(e);
				}
				break;

			case EVENT_EDIT:
				int event_id = Integer.parseInt(request.getParameter("event_id"));
				try {
					EventsDao eventsDao = DaoFactory.createEventsDao();
					Events event = eventsDao.findById(event_id);
					request.setAttribute("event",event);
		            request.getRequestDispatcher("view/eventedit.jsp").forward(request, response);
				} catch (Exception e) {
					throw new ServletException(e);
				}
				break;

			case EVENT_INSERT:
				request.getRequestDispatcher("view/eventinsert.jsp").forward(request, response);
				break;

			case EVENT_DELETE:
				// doGetメソッドでは無効な処理 404ページへ遷移予定
				break;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String pageName = (String) request.getAttribute("servletName");
		if (DataValid.isNotNull(pageName)) {
			pageName = (String) request.getParameter("servletName");
		}
		switch (pageName) {
		default:
			// doPostメソッドでは無効な処理 404ページへ遷移
			break;

		case EVENT_TODAY:
			eventToday(request, response);
			break;

		case EVENT_LIST:
			// doPostメソッドでは無効な処理 404ページへ遷移
			break;

		case EVENT_INFO:
			try {
				int event_id = Integer.parseInt(request.getParameter("event_id"));
				request.getSession().setAttribute("findEventId", event_id);
			} catch (Exception e) {

			}
			int findEventId = (int) request.getSession().getAttribute("findEventId");

			try {
				EventsDao eventsDao = DaoFactory.createEventsDao();
				Events event = eventsDao.findById(findEventId);
				request.setAttribute("event", event);
				AttendDao attendDao = DaoFactory.createAttendDao();
				List<Attend> attendList = attendDao.findAttends(findEventId);
				request.setAttribute("attendList", attendList);
				request.getRequestDispatcher("view/eventinfo.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;

		case EVENT_EDIT:
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int event_id = Integer.parseInt(request.getParameter("event_id"));
			String title = request.getParameter("title");
			Date start = null;
			Date end = null;
			try {
				start = sdf.parse(request.getParameter("start"));
				end = sdf.parse(request.getParameter("end"));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			int place_id = Integer.parseInt(request.getParameter("place_id"));
			int dep_id = Integer.parseInt(request.getParameter("dep_id"));
			String detail = request.getParameter("detail");

			Events event = new Events();
			event.setEvent_id(event_id);
			event.setTitle(title);
			event.setStart(start);
			event.setEnd(end);
			event.setPlace_id(place_id);
			event.setDep_id(dep_id);
			event.setDetail(detail);
			event.setRegistered_id((String) request.getSession().getAttribute("member_id"));

			try {
				EventsDao eventsDao = DaoFactory.createEventsDao();
				eventsDao.update(event);
				request.setAttribute("eventId", event_id);
				request.getRequestDispatcher("view/eventeditDone.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;

		case EVENT_INSERT:
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String title2 = request.getParameter("title");
			Date start2 = null;
			Date end2 = null;
			try {
				start2 = sdf2.parse(request.getParameter("start"));
				end2 = sdf2.parse(request.getParameter("end"));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			int place_id2 = Integer.parseInt(request.getParameter("place_id"));
			int dep_id2 = Integer.parseInt(request.getParameter("dep_id"));
			String detail2 = request.getParameter("detail");

			Events event2 = new Events();
			event2.setTitle(title2);
			event2.setStart(start2);
			event2.setEnd(end2);
			event2.setPlace_id(place_id2);
			event2.setDep_id(dep_id2);
			event2.setDetail(detail2);
			event2.setRegistered_id((String) request.getSession().getAttribute("member_id"));

			try {
				EventsDao eventsDao = DaoFactory.createEventsDao();
				eventsDao.insert(event2);
				request.getRequestDispatcher("view/eventinsertDone.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;

		case EVENT_DELETE:

			int event_id3 = Integer.parseInt(request.getParameter("event_id"));
			try {
				EventsDao eventsDao = DaoFactory.createEventsDao();
				AttendDao attendDao = DaoFactory.createAttendDao();
				Events event3 = new Events();
				event3.setEvent_id(event_id3);
				attendDao.deleteByEventId(event3);
				eventsDao.delete(event3);
				request.getRequestDispatcher("view/eventdelDone.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;
		}
	}

//-- switch文内のメソッドの記述 -----------------------------------------------------------------------------------

	protected void eventToday(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		// 処理内容
		int page = 0;
		try {
			try {
				page = Integer.parseInt(request.getParameter("page")); //jspから送られてきたのを受け取る
				//pageに開始ページを格納する
				if (page == 1) {
					page = 0;
				} else {
					page = 5 * (page - 1);
				}
				request.getSession().setAttribute("todayevent_page", page); //セッションに格納する
			} catch (Exception e) {
				int prevnext = Integer.parseInt(request.getParameter("prevnext")); //eventslistから送られてきたのを受け取る
				//セッションに保存したtodayevent_pageを取得し、変数に格納する
				int todayevent_page = (Integer) request.getSession().getAttribute("todayevent_page");
				//←→をするための仕組みを準備する
				if (prevnext == 1) {
					//もどる
					todayevent_page = todayevent_page - 5;
				} else {
					//すすむ
					todayevent_page = todayevent_page + 5;
				}
				request.getSession().setAttribute("todayevent_page", todayevent_page); //セッションに格納する
			}
		} catch (Exception e) {
			page = 0; //navbarなどからこのページにきたときの処理
			request.getSession().setAttribute("todayevent_page", page); //セッションに格納する
		}
		String member_id = (String)request.getSession().getAttribute("member_id");
		try {
			//セッションに保存したtodayevent_pageを取得し、変数に格納する
			int todayevent_page = (Integer)request.getSession().getAttribute("todayevent_page");
			EventsDao eventsDao = DaoFactory.createEventsDao();
			List<Events> eventsList = eventsDao.findfive(eventsDao.findToday(todayevent_page), member_id);
			//lastpageを設定する
			int lastpage =(eventsDao.countAllToday());
			request.getSession().setAttribute("pageExist",lastpage );
			request.setAttribute("lastpage", lastpage);
			request.setAttribute("eventsList", eventsList);
			request.getRequestDispatcher("view/event_today.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}


}
