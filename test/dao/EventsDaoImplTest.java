package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;

import domain.Events;

public class EventsDaoImplTest extends TestDBAccess{

	// 登録用イベントid
	private final int EVE_NUM = 8;
	// DataSource
	private static DataSource ds;
	// 今日の日付
	private static Date nowDate = new Date();
	static SimpleDateFormat nowDatFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static String NOWDATE = nowDatFmt.format(nowDate).toString();
	// テスト用データ配列
	private final static String EVENT_ID[] = {"1", "2", "3", "4", "5", "6", "7"};
	private final static String TITLE[] = {"人事部ミーティング", "経理部ミーティング",
			"総務部ミーティング", "営業部ミーティング", "開発部ミーティング",
			"Xプロジェクト会議", "新商品開発会議"};
	private final static String START[] = {NOWDATE, "2018-6-12 9:30:00",
			"2018-6-13 10:00:00", "2018-6-14 10:30:00", "2018-6-15 11:00:00",
			"2018-6-18 13:00:00", "2018-6-19 14:00:00"};
	private final static String END[] = {"2018-6-11 10:00:00", "2018-6-12 10:30:00",
			"2018-6-13 11:30:00", "2018-6-14 12:00:00", "2018-6-15 12:15:00",
			"2018-6-18 14:30:00", "2018-6-19 16:00:00"};
	private final static String PLACE_ID[] = {"1", "2", "3", "1", "3", "2", "3"};
	private final static String DEP_ID[] = {"1", "2", "3", "4", "5", "3", "1"};
	private final static String DETAIL[] = {"1", "2", "3", "4", "5", "6", "7"};
	private final static String REGISTERD_ID[] = {"001", "002", "003", "002", "002", "001", "003"};
	private final static String CREATED[] = {"2018-6-11 10:00:00", "2018-6-11 10:00:00",
			"2018-6-11 10:00:00", "2018-6-11 10:00:00", "2018-6-11 10:00:00",
			"2018-6-11 10:00:00", "2018-6-11 10:00:00"};
	//テスト用データ配列(メンバ)
	private final static String MEMBERS[][] =
		{{"001", "山本葵", "ヤマモトアオイ", "1995-12-10", "東京都新宿区飯田橋54-10-1", "090-6433-1111", "2018-04-02", "2"},
				{"002", "中村悠真", "ナカムラユウマ", "1995-12-11", "東京都新宿区飯田橋54-10-2", "090-6433-1122", "2018-04-02", "3"},
				{"003", "小林蓮", "コバヤシレン", "1995-12-12", "東京都新宿区飯田橋54-10-3", "090-6433-1133", "2018-04-02", "4"},};
	//テスト用データ配列(プレイス)
	private final static String PLACE[][] = {{"1", "第一会議室", "10", "1", "1", "1", "001", "17:00:00"},
			{"2", "第二会議室", "20", "0", "1", "0", "001", "19:00:00"},
			{"3", "第三会議室", "30", "1", "1", "0", "001", "20:00:00"}};
	//テスト用データ配列(アテンド)
	private final static String ATTEND[][] = {{"1", "001", "1"}, {"2", "001", "2"}, {"3", "002", "1"}};
	//テスト用データ配列(部署)
	private final static String DEPART[][] = {{"1", "人事部", "4", "001"}, {"2", "経理部", "2", "002"},
			{"3", "総務部", "2", "001"}, {"4", "営業部", "3", "003"}, {"5", "開発部", "4", "003"}};


	/**
	 * DBとのコネクション
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InitialContext ctx = null;

		try {
			ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/eventdb2");

			try (Connection conn = ds.getConnection() ) {

				try {
					//オートコミットを切る
					conn.setAutoCommit(false);
					String sqlTrn = "TRUNCATE TABLE eventdb2.events;"
							+ "TRUNCATE TABLE eventdb2.place;"
							+ "TRUNCATE TABLE eventdb2.attends;"
							+ "TRUNCATE TABLE eventdb2.department;"
							+ "TRUNCATE TABLE eventdb2.members;";
					Statement stmt = (Statement) conn.createStatement();
					stmt.executeUpdate(sqlTrn);

					String sqlEve = "INSERT INTO events VALUES"
							+ "('"+EVENT_ID[0]+"', '"+TITLE[0]+"', '"+START[0]+"', '"+END[0]+"', '"+PLACE_ID[0]+"', '"+DEP_ID[0]+"', '"+DETAIL[0]+"', '"+REGISTERD_ID[0]+"', '"+CREATED[0]+"'),"
							+ "('"+EVENT_ID[1]+"', '"+TITLE[1]+"', '"+START[1]+"', '"+END[1]+"', '"+PLACE_ID[1]+"', '"+DEP_ID[1]+"', '"+DETAIL[1]+"', '"+REGISTERD_ID[1]+"', '"+CREATED[1]+"'),"
							+ "('"+EVENT_ID[2]+"', '"+TITLE[2]+"', '"+START[2]+"', '"+END[2]+"', '"+PLACE_ID[2]+"', '"+DEP_ID[2]+"', '"+DETAIL[2]+"', '"+REGISTERD_ID[2]+"', '"+CREATED[2]+"'),"
							+ "('"+EVENT_ID[3]+"', '"+TITLE[3]+"', '"+START[3]+"', '"+END[3]+"', '"+PLACE_ID[3]+"', '"+DEP_ID[3]+"', '"+DETAIL[3]+"', '"+REGISTERD_ID[3]+"', '"+CREATED[3]+"'),"
							+ "('"+EVENT_ID[4]+"', '"+TITLE[4]+"', '"+START[4]+"', '"+END[4]+"', '"+PLACE_ID[4]+"', '"+DEP_ID[4]+"', '"+DETAIL[4]+"', '"+REGISTERD_ID[4]+"', '"+CREATED[4]+"'),"
							+ "('"+EVENT_ID[5]+"', '"+TITLE[5]+"', '"+START[5]+"', '"+END[5]+"', '"+PLACE_ID[5]+"', '"+DEP_ID[5]+"', '"+DETAIL[5]+"', '"+REGISTERD_ID[5]+"', '"+CREATED[5]+"'),"
							+ "('"+EVENT_ID[6]+"', '"+TITLE[6]+"', '"+START[6]+"', '"+END[6]+"', '"+PLACE_ID[6]+"', '"+DEP_ID[6]+"', '"+DETAIL[6]+"', '"+REGISTERD_ID[6]+"', '"+CREATED[6]+"');";
					stmt = (Statement) conn.createStatement();
					stmt.executeUpdate(sqlEve);

					String sqlPlc = "INSERT INTO place VALUES"
							+ "('"+PLACE[0][0]+"', '"+PLACE[0][1]+"', '"+PLACE[0][2]+"', '"+PLACE[0][3]+"', '"+PLACE[0][4]+"', '"+PLACE[0][5]+"', '"+PLACE[0][6]+"', '"+PLACE[0][7]+"'),"
							+ "('"+PLACE[1][0]+"', '"+PLACE[1][1]+"', '"+PLACE[1][2]+"', '"+PLACE[1][3]+"', '"+PLACE[1][4]+"', '"+PLACE[1][5]+"', '"+PLACE[1][6]+"', '"+PLACE[1][7]+"'),"
							+ "('"+PLACE[2][0]+"', '"+PLACE[2][1]+"', '"+PLACE[2][2]+"', '"+PLACE[2][3]+"', '"+PLACE[2][4]+"', '"+PLACE[2][5]+"', '"+PLACE[2][6]+"', '"+PLACE[2][7]+"'),"
							+ "('"+PLACE[3][0]+"', '"+PLACE[3][1]+"', '"+PLACE[3][2]+"', '"+PLACE[3][3]+"', '"+PLACE[3][4]+"', '"+PLACE[3][5]+"', '"+PLACE[3][6]+"', '"+PLACE[3][7]+"'),"
							+ "('"+PLACE[4][0]+"', '"+PLACE[4][1]+"', '"+PLACE[4][2]+"', '"+PLACE[4][3]+"', '"+PLACE[4][4]+"', '"+PLACE[4][5]+"', '"+PLACE[4][6]+"', '"+PLACE[4][7]+"'),"
							+ "('"+PLACE[5][0]+"', '"+PLACE[5][1]+"', '"+PLACE[5][2]+"', '"+PLACE[5][3]+"', '"+PLACE[5][4]+"', '"+PLACE[5][5]+"', '"+PLACE[5][6]+"', '"+PLACE[5][7]+"'),"
							+ "('"+PLACE[6][0]+"', '"+PLACE[6][1]+"', '"+PLACE[6][2]+"', '"+PLACE[6][3]+"', '"+PLACE[6][4]+"', '"+PLACE[6][5]+"', '"+PLACE[6][6]+"', '"+PLACE[6][7]+"');";
					stmt = (Statement) conn.createStatement();
					stmt.executeUpdate(sqlPlc);

					String sqlAtn = "INSERT INTO attends VALUES"
							+ "('"+ATTEND[0][0]+"', '"+ATTEND[0][1]+"', '"+ATTEND[0][2]+"'),"
							+ "('"+ATTEND[1][0]+"', '"+ATTEND[1][1]+"', '"+ATTEND[1][2]+"'),"
							+ "('"+ATTEND[2][0]+"', '"+ATTEND[2][1]+"', '"+ATTEND[2][2]+"');";
					stmt = (Statement) conn.createStatement();
					stmt.executeUpdate(sqlAtn);

					String sqlDep = "INSERT INTO department VALUES"
							+ "('"+DEPART[0][0]+"', '"+DEPART[0][1]+"', '"+DEPART[0][2]+"', '"+DEPART[0][3]+"', '"+DEPART[0][4]+"'),"
							+ "('"+DEPART[1][0]+"', '"+DEPART[1][1]+"', '"+DEPART[1][2]+"', '"+DEPART[1][3]+"', '"+DEPART[1][4]+"'),"
							+ "('"+DEPART[2][0]+"', '"+DEPART[2][1]+"', '"+DEPART[2][2]+"', '"+DEPART[2][3]+"', '"+DEPART[2][4]+"'),"
							+ "('"+DEPART[3][0]+"', '"+DEPART[3][1]+"', '"+DEPART[3][2]+"', '"+DEPART[3][3]+"', '"+DEPART[3][4]+"'),"
							+ "('"+DEPART[4][0]+"', '"+DEPART[4][1]+"', '"+DEPART[4][2]+"', '"+DEPART[4][3]+"', '"+DEPART[4][4]+"');";
					stmt = (Statement) conn.createStatement();
					stmt.executeUpdate(sqlDep);

					String sqlMem = "INSERT INTO members VALUES"
							+ "('"+MEMBERS[0][0]+"', '"+MEMBERS[0][1]+"', '"+MEMBERS[0][2]+"', '"+MEMBERS[0][3]+"', '"+MEMBERS[0][4]+"', '"+MEMBERS[0][5]+"', '"+MEMBERS[0][6]+"', '"+MEMBERS[0][7]+"'),"
							+ "('"+MEMBERS[1][0]+"', '"+MEMBERS[1][1]+"', '"+MEMBERS[1][2]+"', '"+MEMBERS[1][3]+"', '"+MEMBERS[1][4]+"', '"+MEMBERS[1][5]+"', '"+MEMBERS[1][6]+"', '"+MEMBERS[1][7]+"'),"
							+ "('"+MEMBERS[2][0]+"', '"+MEMBERS[2][1]+"', '"+MEMBERS[2][2]+"', '"+MEMBERS[2][3]+"', '"+MEMBERS[2][4]+"', '"+MEMBERS[2][5]+"', '"+MEMBERS[2][6]+"', '"+MEMBERS[2][7]+"'),"
							+ "('"+MEMBERS[3][0]+"', '"+MEMBERS[3][1]+"', '"+MEMBERS[3][2]+"', '"+MEMBERS[3][3]+"', '"+MEMBERS[3][4]+"', '"+MEMBERS[3][5]+"', '"+MEMBERS[3][6]+"', '"+MEMBERS[3][7]+"'),"
							+ "('"+MEMBERS[4][0]+"', '"+MEMBERS[4][1]+"', '"+MEMBERS[4][2]+"', '"+MEMBERS[4][3]+"', '"+MEMBERS[4][4]+"', '"+MEMBERS[4][5]+"', '"+MEMBERS[4][6]+"', '"+MEMBERS[4][7]+"');";
					stmt = (Statement) conn.createStatement();
					stmt.executeUpdate(sqlMem);

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


	/**
	 * DBのイベントテーブルを削除し、csvのデータをINSERTする
	 */
/*
	@BeforeClass
	public void testEventsDaoImpl() {
		try (Connection con = ds.getConnection()) {
			String sql = "TRUNCATE TABLE eventdb2.events;";
			Statement stmt = (Statement) con.createStatement();
			stmt.executeUpdate(sql);

			// イベントテーブルデータ
			File file = new File("testEvents.csv");
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "SJIS");
			BufferedReader br = new BufferedReader(isr);

			List<Events> eventList = new ArrayList<>();
			String line;

			// ファイル有効性、データ有効性チェックは省略
			while ((line = br.readLine()) != null) {
				byte[] b = line.getBytes();
				line = new String(b, "UTF-8");
				String[] lineArray = line.split(","); // 区切り文字","で分割する
				if (lineArray[0].equals("D")) {
					Events eveData = new Events();
					eveData.setEvent_id(new Integer(Integer.parseInt(lineArray[1])));
					eveData.setTitle(lineArray[2]);
					eveData.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lineArray[3]));
					eveData.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lineArray[4]));
					eveData.setPlace_id(new Integer(Integer.parseInt(lineArray[5])));
					eveData.setDep_id(new Integer(Integer.parseInt(lineArray[6])));
					eveData.setDetail(lineArray[7]);
					eveData.setRegistered_id(lineArray[8]);
					eveData.setCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lineArray[9]));
					eventList.add(eveData);

					// Eventリストデータをinsert
					try {
						EventsDao event = DaoFactory.createEventsDao();
						event.insert(eveData);
					} catch (SQLException e) {
						System.out.println("INSERTエラー");
						throw new IOException(e);
					}

				}
			}
			br.close();

		}catch (Exception e) {
			System.out.println("DB/ファイルエラー");
			e.printStackTrace();
		}
	}
*/


	/**
	 * イベントidを5件まで取得し、リストに格納する
	 * @throws Exception
	 */
	@Test
	public void testFindAll() throws Exception {
		// numは0～6の範囲で指定。NUM=0～2：[5]件、NUM=3～6：[7-NUM]件
		final int NUM = 1;
		EventsDao eventsDao = DaoFactory.createEventsDao();
		List<Events> evList = eventsDao.findAll(NUM);

		assertThat(evList.size(), is(5));
	}

	/**
	 * イベントデータを5件まで取得し、FindAllで取得したイベントidを参照し、各情報をリストに格納する
	 * @throws Exception
	 */
	@Test
	public void testFindfive() throws Exception {
		EventsDao eventsDao = DaoFactory.createEventsDao();

		List<Events> events = eventsDao.findAll(0);
		List<Events> fiveEvents = eventsDao.findfive(events, "001");

		assertThat(fiveEvents.get(0).getMember_name(), is("山本葵"));
		assertThat(fiveEvents.get(1).getTitle(), is("経理部ミーティング"));
		assertThat(fiveEvents.get(2).getDetail(), is("総務部でのミーティングです"));
		assertThat(fiveEvents.get(3).getDep_name(), is("営業部"));
		assertThat(fiveEvents.get(4).getStart().toString(), is("2018-06-20 13:00:00.0"));
	}


	/**
	 * 開始日時が今日の日付のイベントidを5件まで取得し、リストに格納する
	 * @throws Exception
	 */
	@Test
	public void testFindToday() throws Exception {
		EventsDao eventsDao = DaoFactory.createEventsDao();
		List<Events> eventList = eventsDao.findToday(0);

		// DBのeventsテーブルでstartが今日のイベントid
		final int ID = 1;

		assertThat(eventList.get(0).getEvent_id(), is(ID));
	}


	/**
	 * 指定されたイベントidの各情報を、Eventsにセットする
	 * @throws Exception
	 */
	@Test
	public void testFindById() throws Exception {
		final Integer ID = 0;

		EventsDao eventsDao = DaoFactory.createEventsDao();
		Events events = eventsDao.findById(ID);

		assertThat(events.getTitle(), is(TITLE[ID]));
		assertThat(events.getStart().toString(), is(START[ID]));
		assertThat(events.getEnd().toString(), is(END[ID]));
		assertThat(events.getPlace_name(), is("第一会議室"));
		assertThat(events.getDep_id(), is(DEP_ID[ID]));
		assertThat(events.getDetail(), is(DETAIL[ID]));
		assertThat(events.getMember_name(), is("山本葵"));
	}


	/**
	 * Eventsにデータをセットし、そのデータをInsertする。(テスト後、Insertデータを削除する)
	 * @throws Exception
	 */
	@Test
	public void testInsert() throws Exception {
		EventsDao eventsDao = DaoFactory.createEventsDao();
		Events events = new Events();
		events.setEvent_id(EVE_NUM);	//autoIncrement
		events.setTitle("研修会");
        events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-20 16:00:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-21 12:00:00.0"));
		events.setPlace_id(1);
		events.setDep_id(2);
		events.setDetail("経理の基礎を学びます");
		events.setRegistered_id("001");
		eventsDao.insert(events);

		Events insEvents = eventsDao.findById(EVE_NUM);
		assertThat(insEvents.getTitle(), is("研修会"));
		assertThat(insEvents.getStart().toString(), is("2018-06-20 16:00:00.0"));
		assertThat(insEvents.getEnd().toString(), is("2018-06-21 12:00:00.0"));
		assertThat(insEvents.getPlace_name(), is("第一会議室"));
		assertThat(insEvents.getDep_id(), is(2));
		assertThat(insEvents.getDetail(), is("経理の基礎を学びます"));
		assertThat(insEvents.getMember_name(), is("山本葵"));

		// Insertデータの削除
		events.setEvent_id(EVE_NUM);
		eventsDao.delete(events);
	}


	/**
	 * Eventsにデータをセットし、そのデータをDBeventsテーブルのevent_idに対してupdateする。
	 * @throws Exception
	 */
	@Test
	public void testUpdate() throws Exception {
		EventsDao eventsDao = DaoFactory.createEventsDao();
		Events events = new Events();
		events.setEvent_id(3);
		events.setTitle("講習会");
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-10 19:30:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-10 21:15:00.0"));
		events.setPlace_id(2);
		events.setDep_id(1);
		events.setDetail("座学");
		eventsDao.update(events);

		Events updEvents = eventsDao.findById(3);
		assertThat(updEvents.getTitle(), is("講習会"));
		assertThat(updEvents.getStart().toString(), is("2018-06-10 19:30:00.0"));
		assertThat(updEvents.getEnd().toString(), is("2018-06-10 21:15:00.0"));
		assertThat(updEvents.getPlace_name(), is("第二会議室"));
		assertThat(updEvents.getDep_id(), is(1));
		assertThat(updEvents.getDetail(), is("座学"));
		assertThat(updEvents.getMember_name(), is("中村悠真"));
	}


	/**
	 * 事前にイベントをInsertし、そのデータを削除する
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		EventsDao eventsDao = DaoFactory.createEventsDao();
		Events events = new Events();
		events.setEvent_id(EVE_NUM);	//autoIncrement
		events.setTitle("研修会");
        events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-20 16:00:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-21 12:00:00.0"));
		events.setPlace_id(1);
		events.setDep_id(2);
		events.setDetail("経理");
		events.setRegistered_id("001");
		eventsDao.insert(events);

		events.setEvent_id(EVE_NUM);
		eventsDao.delete(events);
		int event_count = 1;
		try (Connection con = (Connection) ds.getConnection()) {
			String sql = "SELECT COUNT(*) from events where event_id="+ EVE_NUM;
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				event_count = Integer.parseInt(rs.getString("count(*)"));
			}
		}
		assertThat(event_count, is(0));
	}


	/**
	 * DBeventsテーブルにあるデータ件数を元にページネーションのページ数を算出する
	 * @throws Exception
	 */
	@Test
	public void testCountAll() throws Exception {
		EventsDao eventsDao = DaoFactory.createEventsDao();
		// 期待値:ページ数[2p] (イベント数[7件]/5[端数切上])
		int expected = 2;

		assertThat(eventsDao.countAll(), is(expected));
	}


	/**
	 * DBeventsテーブルにあるstartが今日の日付のデータ件数をカウントする
	 * @throws Exception
	 */
	@Test
	public void testCountAllToday() throws Exception {
		EventsDao eventsDao = DaoFactory.createEventsDao();
		double val = eventsDao.countAllToday();
		double expected = 1.0;	// 期待値:今日のイベント件数

		assertThat(val, is(expected));
	}

}
//	@AfterClass
//	public static void AfterClass() throws Exception {
//		InitialContext ctx = null;
//
//		try {
//			ctx = new InitialContext();
//			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/eventdb2");
//
//			try (Connection conn = ds.getConnection() ) {
//				try {
//					//オートコミットを切る
//					conn.setAutoCommit(false);
//					String sqlTrn = "TRUNCATE TABLE eventdb2.events;";
//					Statement stmt = (Statement) conn.createStatement();
//					stmt.executeUpdate(sqlTrn);
//
//					//エラーがなければコミットする
//					conn.commit();
//				}
//				//挿入時にエラーが発生したらロールバックしてエラー文を表示
//				catch (Exception e) {
//					System.out.println("error1");
//					e.printStackTrace();
//					conn.rollback();
//				} finally {
//					try {
//						if (conn != null) {
//							conn.close();
//							//System.out.println("切断しました");
//						}
//					} catch (SQLException e) {
//						System.out.println("error2");
//					}
//				}
//			}
//		} catch (NamingException e) {
//			if (ctx != null) {
//				try {
//					ctx.close();
//				} catch (NamingException el) {
//					throw new RuntimeException(el);
//				}
//			}
//			throw new RuntimeException(e);
//		}
//
//	}
//
//}
