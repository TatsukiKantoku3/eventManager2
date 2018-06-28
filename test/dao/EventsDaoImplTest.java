package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;

import domain.Events;

public class EventsDaoImplTest extends TestDBAccess {

	// DataSource
	private static DataSource ds;
	// 各メソッドで必要となる変数
	private static EventsDao target;
	private static List<Events> evList;
	private static Events events;

	// 登録用イベントid
	private final int EVE_UPD_NUM = 7;
	private final int EVE_NUM = 8;
	// 今日の日付
	private static Date nowDate = new Date();
	static SimpleDateFormat nowDatFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static String NOWDATE = nowDatFmt.format(nowDate).toString();

	// テスト用データ(イベント：作成日時)
	private final static String CREATED = "2018-06-11 10:00:00";
	// テスト用データ配列(イベント)
	private final static String EVENTS[][] = {
			{"1", "人事部ミーティング", NOWDATE, 				"2018-06-11 10:00:00", "1", "1", "人事部でのミーティングです", 		"001"},
			{"2", "経理部ミーティング", "2018-06-12 09:30:00", 	"2018-06-12 10:30:00", "2", "2", "経理部でのミーティングです", 		"002"},
			{"3", "総務部ミーティング", "2018-06-13 10:00:00", 	"2018-06-13 11:30:00", "3", "3", "総務部でのミーティングです", 		"003"},
			{"4", "営業部ミーティング", "2018-06-14 10:30:00", 	"2018-06-14 12:00:00", "1", "4", "営業部でのミーティングです", 		"002"},
			{"5", "開発部ミーティング", "2018-06-15 11:00:00", 	"2018-06-15 12:15:00", "3", "5", "開発部でのミーティングです", 		"002"},
			{"6", "Xプロジェクト会議", 	"2018-06-18 13:00:00", 	"2018-06-18 14:30:00", "2", "3", "Xプロジェクトに関する会議です", 	"001"},
			{"7", "新商品開発会議", 	"2018-06-19 14:00:00", 	"2018-06-19 16:00:00", "3", "1", "新商品の開発会議です", 			"003"}};
	//テスト用データ配列(メンバ)
	private final static String MEMBERS[][] = {
			{ "001", "山本葵", "ヤマモトアオイ", "1995-12-10", "東京都新宿区飯田橋54-10-1", "090-6433-1111", "2018-04-02", "2" },
			{ "002", "中村悠真", "ナカムラユウマ", "1995-12-11", "東京都新宿区飯田橋54-10-2", "090-6433-1122", "2018-04-02", "3" },
			{ "003", "小林蓮", "コバヤシレン", "1995-12-12", "東京都新宿区飯田橋54-10-3", "090-6433-1133", "2018-04-02", "4" } };
	//テスト用データ配列(プレイス)
	private final static String PLACE[][] = { { "1", "第一会議室", "10", "1", "1", "1", "001", "17:00:00" },
			{ "2", "第二会議室", "20", "0", "1", "0", "001", "19:00:00" },
			{ "3", "第三会議室", "30", "1", "1", "0", "001", "20:00:00" } };
	//テスト用データ配列(アテンド)
	private final static String ATTEND[][] = { { "1", "001", "1" }, { "2", "002", "2" }, { "3", "003", "1" } };
	//テスト用データ配列(部署)
	private final static String DEPART[][] = { { "1", "人事部", "4", "001" }, { "2", "経理部", "2", "002" },
			{ "3", "総務部", "2", "001" }, { "4", "営業部", "3", "003" }, { "5", "開発部", "4", "003" } };


	/**
	 * DBとのコネクション
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		target = DaoFactory.createEventsDao();
		InitialContext ctx = null;

		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/servereventdb2");
		} catch (NamingException e) {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException el) {
					throw new RuntimeException(el);
				}
			}
			throw new RuntimeException(e);
		}
	}


	@Before
	public void setUpBefore() throws Exception{
		try (Connection conn = ds.getConnection()) {
			try {
				//オートコミットを切る
				conn.setAutoCommit(false);
				String sqlTrn = "set foreign_key_checks = 0";
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.events";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.place";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.attends";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.department";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.members";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);

				String sqlEve = "INSERT INTO events VALUES"
						+ "('" + EVENTS[0][0] + "', '" + EVENTS[0][1] + "', '" + EVENTS[0][2] + "', '" + EVENTS[0][3] + "', '"
						+ EVENTS[0][4] + "', '" + EVENTS[0][5] + "', '" + EVENTS[0][6] + "', '" + EVENTS[0][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[1][0] + "', '" + EVENTS[1][1] + "', '" + EVENTS[1][2] + "', '" + EVENTS[1][3] + "', '"
						+ EVENTS[1][4] + "', '" + EVENTS[1][5] + "', '" + EVENTS[1][6] + "', '" + EVENTS[1][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[2][0] + "', '" + EVENTS[2][1] + "', '" + EVENTS[2][2] + "', '" + EVENTS[2][3] + "', '"
						+ EVENTS[2][4] + "', '" + EVENTS[2][5] + "', '" + EVENTS[2][6] + "', '" + EVENTS[2][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[3][0] + "', '" + EVENTS[3][1] + "', '" + EVENTS[3][2] + "', '" + EVENTS[3][3] + "', '"
						+ EVENTS[3][4] + "', '" + EVENTS[3][5] + "', '" + EVENTS[3][6] + "', '" + EVENTS[3][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[4][0] + "', '" + EVENTS[4][1] + "', '" + EVENTS[4][2] + "', '" + EVENTS[4][3] + "', '"
						+ EVENTS[4][4] + "', '" + EVENTS[4][5] + "', '" + EVENTS[4][6] + "', '" + EVENTS[4][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[5][0] + "', '" + EVENTS[5][1] + "', '" + EVENTS[5][2] + "', '" + EVENTS[5][3] + "', '"
						+ EVENTS[5][4] + "', '" + EVENTS[5][5] + "', '" + EVENTS[5][6] + "', '" + EVENTS[5][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[6][0] + "', '" + EVENTS[6][1] + "', '" + EVENTS[6][2] + "', '" + EVENTS[6][3] + "', '"
						+ EVENTS[6][4] + "', '" + EVENTS[6][5] + "', '" + EVENTS[6][6] + "', '" + EVENTS[6][7] + "', '"
						+ CREATED + "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlEve);

				String sqlMem = "INSERT INTO members (member_id, name, kana, birthday, address, tel, hired, dep_id) VALUES"
						+ "('" + MEMBERS[0][0] + "', '" + MEMBERS[0][1] + "', '" + MEMBERS[0][2] + "', '"
						+ MEMBERS[0][3] + "', '" + MEMBERS[0][4] + "', '" + MEMBERS[0][5] + "', '" + MEMBERS[0][6]
						+ "', '" + MEMBERS[0][7] + "'),"
						+ "('" + MEMBERS[1][0] + "', '" + MEMBERS[1][1] + "', '" + MEMBERS[1][2] + "', '"
						+ MEMBERS[1][3] + "', '" + MEMBERS[1][4] + "', '" + MEMBERS[1][5] + "', '" + MEMBERS[1][6]
						+ "', '" + MEMBERS[1][7] + "'),"
						+ "('" + MEMBERS[2][0] + "', '" + MEMBERS[2][1] + "', '" + MEMBERS[2][2] + "', '"
						+ MEMBERS[2][3] + "', '" + MEMBERS[2][4] + "', '" + MEMBERS[2][5] + "', '" + MEMBERS[2][6]
						+ "', '" + MEMBERS[2][7] + "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlMem);

				String sqlPlc = "INSERT INTO place VALUES"
						+ "('" + PLACE[0][0] + "', '" + PLACE[0][1] + "', '" + PLACE[0][2] + "', '" + PLACE[0][3]
						+ "', '" + PLACE[0][4] + "', '" + PLACE[0][5] + "', '" + PLACE[0][6] + "', '" + PLACE[0][7]
						+ "'),"
						+ "('" + PLACE[1][0] + "', '" + PLACE[1][1] + "', '" + PLACE[1][2] + "', '" + PLACE[1][3]
						+ "', '" + PLACE[1][4] + "', '" + PLACE[1][5] + "', '" + PLACE[1][6] + "', '" + PLACE[1][7]
						+ "'),"
						+ "('" + PLACE[2][0] + "', '" + PLACE[2][1] + "', '" + PLACE[2][2] + "', '" + PLACE[2][3]
						+ "', '" + PLACE[2][4] + "', '" + PLACE[2][5] + "', '" + PLACE[2][6] + "', '" + PLACE[2][7]
						+ "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlPlc);

				String sqlAtn = "INSERT INTO attends VALUES"
						+ "('" + ATTEND[0][0] + "', '" + ATTEND[0][1] + "', '" + ATTEND[0][2] + "'),"
						+ "('" + ATTEND[1][0] + "', '" + ATTEND[1][1] + "', '" + ATTEND[1][2] + "'),"
						+ "('" + ATTEND[2][0] + "', '" + ATTEND[2][1] + "', '" + ATTEND[2][2] + "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlAtn);

				String sqlDep = "INSERT INTO department VALUES"
						+ "('" + DEPART[0][0] + "', '" + DEPART[0][1] + "', '" + DEPART[0][2] + "'),"
						+ "('" + DEPART[1][0] + "', '" + DEPART[1][1] + "', '" + DEPART[1][2] + "'),"
						+ "('" + DEPART[2][0] + "', '" + DEPART[2][1] + "', '" + DEPART[2][2] + "'),"
						+ "('" + DEPART[3][0] + "', '" + DEPART[3][1] + "', '" + DEPART[3][2] + "'),"
						+ "('" + DEPART[4][0] + "', '" + DEPART[4][1] + "', '" + DEPART[4][2] + "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlDep);

				// 外部キー制約の再制限
				sqlTrn = "set foreign_key_checks = 1";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);

				//エラーがなければコミットする
				conn.commit();

			}
			//挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				System.out.println("error1");
				e.printStackTrace();
				conn.rollback();

			} finally {
				try {
					if (conn != null) {
						conn.close();
						//System.out.println("切断しました");
					}
				} catch (SQLException e) {
					System.out.println("error2");
				}
			}
		}
	}


	@AfterClass
	public static void AfterClass() throws Exception {

		try (Connection conn = ds.getConnection()) {
			try {
// 下記コメントは、テスト後にDBを初期化する場合にのみ外して下さい
					//オートコミットを切る
//					conn.setAutoCommit(false);
//					String sqlTrn = "set foreign_key_checks = 0";
//					Statement stmt = conn.createStatement();
//					stmt.executeUpdate(sqlTrn);
//					sqlTrn = "TRUNCATE TABLE eventdb2.events;";
//					stmt = conn.createStatement();
//					stmt.executeUpdate(sqlTrn);
//					sqlTrn = "TRUNCATE TABLE eventdb2.place";
//					stmt = conn.createStatement();
//					stmt.executeUpdate(sqlTrn);
//					sqlTrn = "TRUNCATE TABLE eventdb2.attends;";
//					stmt = conn.createStatement();
//					stmt.executeUpdate(sqlTrn);
//					sqlTrn = "TRUNCATE TABLE eventdb2.department;";
//					stmt = conn.createStatement();
//					stmt.executeUpdate(sqlTrn);
//					sqlTrn = "TRUNCATE TABLE eventdb2.members;";
//					stmt = conn.createStatement();
//					stmt.executeUpdate(sqlTrn);
//
//					// 外部キー制約の再制限
//					sqlTrn = "set foreign_key_checks = 1";
//					stmt = conn.createStatement();
//					stmt.executeUpdate(sqlTrn);
//					//エラーがなければコミットする
//					conn.commit();
			}
			//挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				System.out.println("error3");
				e.printStackTrace();
				conn.rollback();
			} finally {
				try {
					if (conn != null) {
						conn.close();
						//System.out.println("切断しました");
					}
				} catch (SQLException e) {
					System.out.println("error4");
				}
			}
		}

	}

	/**
	 * DBのEventsテーブルにあるevent_idをNUM番目から上限5件まで取得する
	 * NUM=1なので、event_id[2～6]を取得する
	 * @throws Exception
	 */
	@Test
	public void 正常系1testFindAll() throws Exception {
		final int NUM = 1;
		target = DaoFactory.createEventsDao();
		evList = target.findAll(NUM);

		assertThat(evList.get(0).getEvent_id(), is(2));
		assertThat(evList.size(), is(5));
	}

	/**
	 * NUM=5なので、event_id[6,7]を取得する
	 * @throws Exception
	 */
	@Test
	public void 正常系2testFindAll() throws Exception {
		final int NUM = 5;
		target = DaoFactory.createEventsDao();
		evList = target.findAll(NUM);

		assertThat(evList.get(0).getEvent_id(), is(6));
		assertThat(evList.size(), is(2));
	}

	/**
	 * イベントidを～にし、エラー
	 * @throws Exception
	 */
	/*
	@Test
	public void 異常系testFindAll() throws Exception {
		final int NUM = 0;
		target = DaoFactory.createEventsDao();
		evList = target.findAll(NUM);

		assertThat(evList.size(), is(5));
	}
	*/


	/**
	 * イベントデータを5件まで取得し、FindAllで取得したメンバidを参照し、各情報をリストに格納する
	 * @throws Exception
	 */
	@Test
	public void 正常系1testFindfive() throws Exception {
		//Insert Attend (null,member_id=001,Event_id=1)
		target = DaoFactory.createEventsDao();
		events = new Events();
		events.setEvent_id(1);

		List<Events> eventsList = new ArrayList<Events>();
		eventsList.add(events);

		List<Events> fiveEvents = target.findfive(eventsList, "001");//loginしているmember_id

		assertThat(fiveEvents.get(0).getMember_name(), is("山本葵"));
		assertThat(fiveEvents.get(0).getTitle(), is(EVENTS[0][1]));
		assertThat(fiveEvents.get(0).getDetail(), is(EVENTS[0][6]));
		assertThat(fiveEvents.get(0).getDep_name(), is(DEPART[0][1]));
		assertThat(fiveEvents.get(0).getStart().toString(), is(EVENTS[0][2] + ".0"));
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void 正常系2testFindfive() throws Exception {
		target = DaoFactory.createEventsDao();
		events = new Events();
		events.setEvent_id(2);

		List<Events> eventsList = new ArrayList<Events>();
		eventsList.add(events);

		List<Events> fiveEvents = target.findfive(eventsList, "002");//loginしているmember_id

		assertThat(fiveEvents.get(0).getMember_name(), is("中村悠真"));
		assertThat(fiveEvents.get(0).getTitle(), is(EVENTS[1][1]));
		assertThat(fiveEvents.get(0).getDetail(), is(EVENTS[1][6]));
		assertThat(fiveEvents.get(0).getDep_name(), is(DEPART[1][1]));
		assertThat(fiveEvents.get(0).getStart().toString(), is(EVENTS[1][2] + ".0"));
	}

	/**
	 * イベントデータを5件まで取得し、FindAllで取得したメンバidを参照し、各情報をリストに格納する
	 * @throws Exception
	 */
	/*
	@Test
	public void 異常系testFindfive() throws Exception {
		//Insert Attend (null,member_id=001,Event_id=1)
		target = DaoFactory.createEventsDao();
		events = new Events();
		events.setEvent_id(1);

		List<Events> eventsList = new ArrayList<Events>();
		eventsList.add(events);

		List<Events> fiveEvents = target.findfive(eventsList, "002");//loginしているmember_id

		assertThat(fiveEvents.get(0).getMember_name(), is("山本葵"));
		assertThat(fiveEvents.get(0).getTitle(), is(EVENTS[0][1]));
		assertThat(fiveEvents.get(0).getDetail(), is(EVENTS[0][6]));
		assertThat(fiveEvents.get(0).getDep_name(), is(DEPART[0][1]));
		assertThat(fiveEvents.get(0).getStart().toString(), is(EVENTS[0][2] + ".0"));
	}
	*/


	/**
	 * 開始日時が今日の日付のイベントidを5件まで取得し、リストに格納する
	 * @throws Exception
	 */
	@Test
	public void 正常系1estFindToday() throws Exception {
		target = DaoFactory.createEventsDao();
		List<Events> eventList = target.findToday(0);

		// DBのeventsテーブルでstartが今日のイベントid
		final int ID = 1;

		assertThat(eventList.get(0).getEvent_id(), is(ID));
	}

	/**
	 * 開始日時が今日の日付のイベントid[3,4]を取得し、リストに格納する
	 * @throws Exception
	 */
	@Test
	public void 正常系2testFindToday() throws Exception {
		// DBのeventsテーブルでstartが今日のイベントid
		final int ID_1 = 1;
		final int ID_3 = 3;
		final int ID_4 = 4;

		target = DaoFactory.createEventsDao();
		events = new Events();
		events.setEvent_id(ID_1);
		events.setTitle(EVENTS[0][1]);
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(EVENTS[0][3] + ".0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(EVENTS[0][3] + ".0"));
		events.setPlace_id(new Integer(EVENTS[0][4]).intValue());
		events.setDep_id(new Integer(EVENTS[0][5]).intValue());
		events.setDetail(EVENTS[0][6]);
		target.update(events);

		events.setEvent_id(ID_3);
		events.setTitle(EVENTS[2][1]);
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(NOWDATE + ".0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(EVENTS[2][3] + ".0"));
		events.setPlace_id(new Integer(EVENTS[2][4]).intValue());
		events.setDep_id(new Integer(EVENTS[2][5]).intValue());
		events.setDetail(EVENTS[2][6]);
		target.update(events);

		events.setEvent_id(ID_4);
		events.setTitle(EVENTS[3][1]);
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(NOWDATE + ".0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(EVENTS[3][3] + ".0"));
		events.setPlace_id(new Integer(EVENTS[3][4]).intValue());
		events.setDep_id(new Integer(EVENTS[3][5]).intValue());
		events.setDetail(EVENTS[3][6]);
		target.update(events);

		List<Events> eventList = target.findToday(0);

		assertThat(eventList.get(0).getEvent_id(), is(ID_3));
		assertThat(eventList.get(1).getEvent_id(), is(ID_4));
	}

	/**
	 * 開始日時が今日の日付のイベントidを5件まで取得し、リストに格納する
	 * @throws Exception
	 */
	/*
	@Test
	public void 異常系testFindToday() throws Exception {
		target = DaoFactory.createEventsDao();
		List<Events> eventList = target.findToday(0);

		// DBのeventsテーブルでstartが今日のイベントid
		final int ID_1 = 3;
		final int ID_2 = 4;

		assertThat(eventList.get(0).getEvent_id(), is(ID_1));
		assertThat(eventList.get(1).getEvent_id(), is(ID_2));
	}
	*/


	/**
	 * 指定されたイベントidの各情報を、Eventsにセットする
	 * @throws Exception
	 */
	@Test
	public void 正常系1testFindById() throws Exception {
		final Integer ID = 1;

		target = DaoFactory.createEventsDao();
		 events = target.findById(ID + 1);

		assertThat(events.getTitle(), is(EVENTS[ID][1]));
		assertThat(events.getStart().toString(), is(EVENTS[ID][2] + ".0"));
		assertThat(events.getEnd().toString(), is(EVENTS[ID][3] + ".0"));
		assertThat(events.getPlace_name(), is(PLACE[ID][1]));
		assertThat(events.getDep_id(), is(2));
		assertThat(events.getDetail(), is(EVENTS[ID][6]));
		assertThat(events.getMember_name(), is(MEMBERS[ID][1]));
	}

	/**
	 * 指定されたイベントidの各情報を、Eventsにセットする
	 * @throws Exception
	 */
	/*
	@Test
	public void 正常系2testFindById() throws Exception {
		final Integer ID = 1;

		target = DaoFactory.createEventsDao();
		 events = target.findById(ID + 1);

		assertThat(events.getTitle(), is(EVENTS[ID][1]));
		assertThat(events.getStart().toString(), is(EVENTS[ID][2] + ".0"));
		assertThat(events.getEnd().toString(), is(EVENTS[ID][3] + ".0"));
		assertThat(events.getPlace_name(), is(PLACE[ID][1]));
		assertThat(events.getDep_id(), is(2));
		assertThat(events.getDetail(), is(EVENTS[ID][6]));
		assertThat(events.getMember_name(), is(MEMBERS[ID][1]));
	}
	*/

	/**
	 * 指定されたイベントidの各情報を、Eventsにセットする
	 * @throws Exception
	 */
	/*
	@Test
	public void 異常系testFindById() throws Exception {
		final Integer ID = 1;

		target = DaoFactory.createEventsDao();
		 events = target.findById(ID + 1);

		assertThat(events.getTitle(), is(EVENTS[ID][1]));
		assertThat(events.getStart().toString(), is(EVENTS[ID][2] + ".0"));
		assertThat(events.getEnd().toString(), is(EVENTS[ID][3] + ".0"));
		assertThat(events.getPlace_name(), is(PLACE[ID][1]));
		assertThat(events.getDep_id(), is(2));
		assertThat(events.getDetail(), is(EVENTS[ID][6]));
		assertThat(events.getMember_name(), is(MEMBERS[ID][1]));
	}
	*/


	/**
	 * Eventsにデータをセットし、そのデータをInsertする。(テスト後、Insertデータを削除する)
	 * @throws Exception
	 */
	@Test
	public void 正常系1testInsert() throws Exception {
		target = DaoFactory.createEventsDao();
		events = new Events();
		events.setEvent_id(EVE_NUM); //autoIncrement
		events.setTitle("研修会");
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-20 16:00:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-21 12:00:00.0"));
		events.setPlace_id(1);
		events.setDep_id(2);
		events.setDetail("経理の基礎を学びます");
		events.setRegistered_id("001");
		target.insert(events);

		Events insEvents = target.findById(EVE_NUM);
		assertThat(insEvents.getTitle(), is("研修会"));
		assertThat(insEvents.getStart().toString(), is("2018-06-20 16:00:00.0"));
		assertThat(insEvents.getEnd().toString(), is("2018-06-21 12:00:00.0"));
		assertThat(insEvents.getPlace_name(), is("第一会議室"));
		assertThat(insEvents.getDep_id(), is(2));
		assertThat(insEvents.getDetail(), is("経理の基礎を学びます"));
		assertThat(insEvents.getMember_name(), is("山本葵"));

		// Insertデータの削除
		events.setEvent_id(EVE_NUM);
		target.delete(events);
	}

	/**
	 * Eventsにデータをセットし、そのデータをInsertする。(テスト後、Insertデータを削除する)
	 * @throws Exception
	 */
	/*
	@Test
	public void 異常系testInsert() throws Exception {
		target = DaoFactory.createEventsDao();
		 events = new Events();
		events.setEvent_id(EVE_NUM); //autoIncrement
		events.setTitle("研修会");
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-20 16:00:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-21 12:00:00.0"));
		events.setPlace_id(10);
		events.setDep_id(2);
		events.setDetail("経理の基礎を学びます");
		events.setRegistered_id("001");
		target.insert(events);

		Events insEvents = target.findById(EVE_NUM);
		assertThat(insEvents.getTitle(), is("研修会"));
		assertThat(insEvents.getStart().toString(), is("2018-06-20 16:00:00.0"));
		assertThat(insEvents.getEnd().toString(), is("2018-06-21 12:00:00.0"));
		assertThat(insEvents.getPlace_name(), is("第一会議室"));
		assertThat(insEvents.getDep_id(), is(2));
		assertThat(insEvents.getDetail(), is("経理の基礎を学びます"));
		assertThat(insEvents.getMember_name(), is("山本葵"));

		// Insertデータの削除
		events.setEvent_id(EVE_NUM);
		target.delete(events);
	}
	*/


	/**
	 * Eventsにデータをセットし、そのデータをDBeventsテーブルのevent_idに対してupdateする。
	 * @throws Exception
	 */
	@Test
	public void 正常系1testUpdate() throws Exception {
		target = DaoFactory.createEventsDao();
		events = new Events();
		events.setEvent_id(EVE_UPD_NUM);
		events.setTitle("講習会");
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-10 19:30:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-10 21:15:00.0"));
		events.setPlace_id(2);
		events.setDep_id(1);
		events.setDetail("座学");
		target.update(events);

		Events updEvents = target.findById(EVE_UPD_NUM);
		assertThat(updEvents.getTitle(), is("講習会"));
		assertThat(updEvents.getStart().toString(), is("2018-06-10 19:30:00.0"));
		assertThat(updEvents.getEnd().toString(), is("2018-06-10 21:15:00.0"));
		assertThat(updEvents.getPlace_name(), is("第二会議室"));
		assertThat(updEvents.getDep_id(), is(1));
		assertThat(updEvents.getDetail(), is("座学"));
		assertThat(updEvents.getMember_name(), is("小林蓮"));
	}

	/**
	 * Eventsにデータをセットし、そのデータをDBeventsテーブルのevent_idに対してupdateする。
	 * @throws Exception
	 */
	/*
	@Test
	public void 異常系testUpdate() throws Exception {
		target = DaoFactory.createEventsDao();
		 events = new Events();
		events.setEvent_id(EVE_UPD_NUM);
		events.setTitle("講習会");
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-10 19:30:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-10 21:15:00.0"));
		events.setPlace_id(2);
		events.setDep_id(1);
		events.setDetail("座学");
		target.update(events);

		Events updEvents = target.findById(EVE_UPD_NUM);
		assertThat(updEvents.getTitle(), is("講習会"));
		assertThat(updEvents.getStart().toString(), is("2018-06-10 19:30:00.0"));
		assertThat(updEvents.getEnd().toString(), is("2018-06-10 21:15:00.0"));
		assertThat(updEvents.getPlace_name(), is("第二会議室"));
		assertThat(updEvents.getDep_id(), is(1));
		assertThat(updEvents.getDetail(), is("座学"));
		assertThat(updEvents.getMember_name(), is("小林蓮"));
	}
	*/


	/**
	 * 事前にイベントをInsertし、そのデータを削除する
	 * @throws Exception
	 */
	@Test
	public void 正常系1testDelete() throws Exception {
		target = DaoFactory.createEventsDao();
		events = new Events();
		events.setEvent_id(EVE_NUM); //autoIncrement
		events.setTitle("研修会");
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-20 16:00:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-21 12:00:00.0"));
		events.setPlace_id(1);
		events.setDep_id(2);
		events.setDetail("経理");
		events.setRegistered_id("001");
		target.insert(events);

		events.setEvent_id(EVE_NUM);
		target.delete(events);
		int event_count = 1;
		try (Connection con = (Connection) ds.getConnection()) {
			String sql = "SELECT COUNT(*) from events where event_id=" + EVE_NUM;
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				event_count = Integer.parseInt(rs.getString("count(*)"));
			}
		}
		assertThat(event_count, is(0));
	}

	/**
	 * 事前にイベントをInsertし、そのデータを削除する
	 * @throws Exception
	 */
	/*
	@Test
	public void 異常系testDelete() throws Exception {
		target = DaoFactory.createEventsDao();
		events = new Events();
		events.setEvent_id(EVE_NUM); //autoIncrement
		events.setTitle("研修会");
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-20 16:00:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-21 12:00:00.0"));
		events.setPlace_id(1);
		events.setDep_id(2);
		events.setDetail("経理");
		events.setRegistered_id("001");
		target.insert(events);

		events.setEvent_id(EVE_NUM);
		target.delete(events);
		int event_count = 1;
		try (Connection con = (Connection) ds.getConnection()) {
			String sql = "SELECT COUNT(*) from events where event_id=" + EVE_NUM;
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				event_count = Integer.parseInt(rs.getString("count(*)"));
			}
		}
		assertThat(event_count, is(0));
	}
	*/


	/**
	 * DBeventsテーブルにあるデータ件数を元にページネーションのページ数を算出する
	 * @throws Exception
	 */
	@Test
	public void 正常系1testCountAll() throws Exception {
		target = DaoFactory.createEventsDao();
		int expected = 2;

		assertThat(target.countAll(), is(expected));
	}

	/**
	 * 7件のイベントをインサート後に3件削除し、ページ数が1となるか
	 * @throws Exception
	 */
	@Test
	public void 正常系2testCountAll() throws Exception {
		target = DaoFactory.createEventsDao();
		events = new Events();

		int id = 1;
		events.setEvent_id(id);
		target.delete(events);

		id = 2;
		events.setEvent_id(id);
		target.delete(events);

		id = 3;
		events.setEvent_id(id);
		target.delete(events);


		int expected = 1;
		assertThat(target.countAll(), is(expected));
	}

	/**
	 * イベントのカウントでエラーを起こす
	 * @throws Exception
	 */
	/*
	@Test
	public void 異常系testCountAll() throws Exception {
		target = DaoFactory.createEventsDao();
		int expected = 2;

		assertThat(target.countAll(), is(expected));
	}
	*/


	/**
	 * DBeventsテーブルにあるstartが今日の日付のデータ件数をカウントする
	 * @throws Exception
	 */
	@Test
	public void 正常系1testCountAllToday() throws Exception {
		target = DaoFactory.createEventsDao();
		int val = target.countAllToday();
		int expected = 1; // 期待値:今日のイベント件数

		assertThat(val, is(expected));
	}

	/**
	 * DBeventsテーブルにあるstartが今日の日付のデータ件数をカウントする
	 * @throws Exception
	 */
	@Test
	public void 正常系2testCountAllToday() throws Exception {
		target = DaoFactory.createEventsDao();
		events = new Events();

		for(int i=1; i<=6; i++) {
			events.setEvent_id(i);
			events.setTitle(EVENTS[i-1][1]);
			events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(NOWDATE + ".0"));
			events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(EVENTS[i-1][3] + ".0"));
			events.setPlace_id(new Integer(EVENTS[i-1][4]).intValue());
			events.setDep_id(new Integer(EVENTS[i-1][5]).intValue());
			events.setDetail(EVENTS[i-1][6]);
			target.update(events);
		}

		int val = target.countAllToday();

		int expected = 2;
		assertThat(val, is(expected));
	}

	/**
	 * DBeventsテーブルにあるstartが今日の日付のデータ件数をカウントする
	 * @throws Exception
	 */
	/*
	@Test
	public void 異常系testCountAllToday() throws Exception {
		target = DaoFactory.createEventsDao();
		int val = target.countAllToday();
		int expected = 1; // 期待値:今日のイベント件数

		assertThat(val, is(expected));
	}
	*/


	// insert,deleteテスト用DBデータ初期化メソッド
	public static void DataIns() throws SQLException {

		try (Connection conn = ds.getConnection()) {
			try {
				//オートコミットを切る
				conn.setAutoCommit(false);
				String sqlTrn = "set foreign_key_checks = 0";
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.events";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.place";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.attends";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.department";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);
				sqlTrn = "TRUNCATE TABLE eventdb2.members";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);

				String sqlEve = "INSERT INTO events VALUES"
						+ "('" + EVENTS[0][0] + "', '" + EVENTS[0][1] + "', '" + EVENTS[0][2] + "', '" + EVENTS[0][3] + "', '"
						+ EVENTS[0][4] + "', '" + EVENTS[0][5] + "', '" + EVENTS[0][6] + "', '" + EVENTS[0][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[1][0] + "', '" + EVENTS[1][1] + "', '" + EVENTS[1][2] + "', '" + EVENTS[1][3] + "', '"
						+ EVENTS[1][4] + "', '" + EVENTS[1][5] + "', '" + EVENTS[1][6] + "', '" + EVENTS[1][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[2][0] + "', '" + EVENTS[2][1] + "', '" + EVENTS[2][2] + "', '" + EVENTS[2][3] + "', '"
						+ EVENTS[2][4] + "', '" + EVENTS[2][5] + "', '" + EVENTS[2][6] + "', '" + EVENTS[2][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[3][0] + "', '" + EVENTS[3][1] + "', '" + EVENTS[3][2] + "', '" + EVENTS[3][3] + "', '"
						+ EVENTS[3][4] + "', '" + EVENTS[3][5] + "', '" + EVENTS[3][6] + "', '" + EVENTS[3][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[4][0] + "', '" + EVENTS[4][1] + "', '" + EVENTS[4][2] + "', '" + EVENTS[4][3] + "', '"
						+ EVENTS[4][4] + "', '" + EVENTS[4][5] + "', '" + EVENTS[4][6] + "', '" + EVENTS[4][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[5][0] + "', '" + EVENTS[5][1] + "', '" + EVENTS[5][2] + "', '" + EVENTS[5][3] + "', '"
						+ EVENTS[5][4] + "', '" + EVENTS[5][5] + "', '" + EVENTS[5][6] + "', '" + EVENTS[5][7] + "', '"
						+ CREATED + "'),"
						+ "('" + EVENTS[6][0] + "', '" + EVENTS[6][1] + "', '" + EVENTS[6][2] + "', '" + EVENTS[6][3] + "', '"
						+ EVENTS[6][4] + "', '" + EVENTS[6][5] + "', '" + EVENTS[6][6] + "', '" + EVENTS[6][7] + "', '"
						+ CREATED + "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlEve);

				String sqlMem = "INSERT INTO members (member_id, name, kana, birthday, address, tel, hired, dep_id) VALUES"
						+ "('" + MEMBERS[0][0] + "', '" + MEMBERS[0][1] + "', '" + MEMBERS[0][2] + "', '"
						+ MEMBERS[0][3] + "', '" + MEMBERS[0][4] + "', '" + MEMBERS[0][5] + "', '" + MEMBERS[0][6]
						+ "', '" + MEMBERS[0][7] + "'),"
						+ "('" + MEMBERS[1][0] + "', '" + MEMBERS[1][1] + "', '" + MEMBERS[1][2] + "', '"
						+ MEMBERS[1][3] + "', '" + MEMBERS[1][4] + "', '" + MEMBERS[1][5] + "', '" + MEMBERS[1][6]
						+ "', '" + MEMBERS[1][7] + "'),"
						+ "('" + MEMBERS[2][0] + "', '" + MEMBERS[2][1] + "', '" + MEMBERS[2][2] + "', '"
						+ MEMBERS[2][3] + "', '" + MEMBERS[2][4] + "', '" + MEMBERS[2][5] + "', '" + MEMBERS[2][6]
						+ "', '" + MEMBERS[2][7] + "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlMem);

				String sqlPlc = "INSERT INTO place VALUES"
						+ "('" + PLACE[0][0] + "', '" + PLACE[0][1] + "', '" + PLACE[0][2] + "', '" + PLACE[0][3]
						+ "', '" + PLACE[0][4] + "', '" + PLACE[0][5] + "', '" + PLACE[0][6] + "', '" + PLACE[0][7]
						+ "'),"
						+ "('" + PLACE[1][0] + "', '" + PLACE[1][1] + "', '" + PLACE[1][2] + "', '" + PLACE[1][3]
						+ "', '" + PLACE[1][4] + "', '" + PLACE[1][5] + "', '" + PLACE[1][6] + "', '" + PLACE[1][7]
						+ "'),"
						+ "('" + PLACE[2][0] + "', '" + PLACE[2][1] + "', '" + PLACE[2][2] + "', '" + PLACE[2][3]
						+ "', '" + PLACE[2][4] + "', '" + PLACE[2][5] + "', '" + PLACE[2][6] + "', '" + PLACE[2][7]
						+ "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlPlc);

				String sqlAtn = "INSERT INTO attends VALUES"
						+ "('" + ATTEND[0][0] + "', '" + ATTEND[0][1] + "', '" + ATTEND[0][2] + "'),"
						+ "('" + ATTEND[1][0] + "', '" + ATTEND[1][1] + "', '" + ATTEND[1][2] + "'),"
						+ "('" + ATTEND[2][0] + "', '" + ATTEND[2][1] + "', '" + ATTEND[2][2] + "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlAtn);

				String sqlDep = "INSERT INTO department VALUES"
						+ "('" + DEPART[0][0] + "', '" + DEPART[0][1] + "', '" + DEPART[0][2] + "'),"
						+ "('" + DEPART[1][0] + "', '" + DEPART[1][1] + "', '" + DEPART[1][2] + "'),"
						+ "('" + DEPART[2][0] + "', '" + DEPART[2][1] + "', '" + DEPART[2][2] + "'),"
						+ "('" + DEPART[3][0] + "', '" + DEPART[3][1] + "', '" + DEPART[3][2] + "'),"
						+ "('" + DEPART[4][0] + "', '" + DEPART[4][1] + "', '" + DEPART[4][2] + "')";
				stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlDep);

				// 外部キー制約の再制限
				sqlTrn = "set foreign_key_checks = 1";
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlTrn);

				//エラーがなければコミットする
				conn.commit();

			}
			//挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				System.out.println("error1");
				e.printStackTrace();
				conn.rollback();

			} finally {
				try {
					if (conn != null) {
						conn.close();
						//System.out.println("切断しました");
					}
				} catch (SQLException e) {
					System.out.println("error2");
				}
			}
		}
	}

}