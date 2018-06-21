package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	private final int EVENUM = 8;
	// DataSource
	private static DataSource ds;

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
		final int ID = 5;

		assertThat(eventList.get(0).getEvent_id(), is(ID));
	}


	/**
	 * 指定されたイベントidの各情報を、Eventsにセットする
	 * @throws Exception
	 */
	@Test
	public void testFindById() throws Exception {
		final Integer ID = 1;

		EventsDao eventsDao = DaoFactory.createEventsDao();
		Events events = eventsDao.findById(ID);

		assertThat(events.getTitle(), is("人事部ミーティング"));
		assertThat(events.getStart().toString(), is("2018-06-14 10:00:00.0"));
		assertThat(events.getEnd().toString(), is("2018-06-14 11:30:00.0"));
		assertThat(events.getPlace_name(), is("第一会議室"));
		assertThat(events.getDep_id(), is(1));
		assertThat(events.getDetail(), is("人事部でのミーティングです"));
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
		events.setEvent_id(EVENUM+1);	//autoIncrement
		events.setTitle("研修会");
        events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-20 16:00:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-21 12:00:00.0"));
		events.setPlace_id(1);
		events.setDep_id(2);
		events.setDetail("経理の基礎を学びます");
		events.setRegistered_id("001");
		eventsDao.insert(events);

		Events insEvents = eventsDao.findById(EVENUM+1);
		assertThat(insEvents.getTitle(), is("研修会"));
		assertThat(insEvents.getStart().toString(), is("2018-06-20 16:00:00.0"));
		assertThat(insEvents.getEnd().toString(), is("2018-06-21 12:00:00.0"));
		assertThat(insEvents.getPlace_name(), is("第一会議室"));
		assertThat(insEvents.getDep_id(), is(2));
		assertThat(insEvents.getDetail(), is("経理の基礎を学びます"));
		assertThat(insEvents.getMember_name(), is("山本葵"));

		// Insertデータの削除
		events.setEvent_id(EVENUM+1);
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
		events.setEvent_id(EVENUM-1);
		events.setTitle("講習会");
		events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-10 19:30:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-10 21:15:00.0"));
		events.setPlace_id(2);
		events.setDep_id(1);
		events.setDetail("座学");
		eventsDao.update(events);

		Events updEvents = eventsDao.findById(EVENUM-1);
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
		events.setEvent_id(EVENUM);	//autoIncrement
		events.setTitle("研修会");
        events.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-20 16:00:00.0"));
		events.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-06-21 12:00:00.0"));
		events.setPlace_id(1);
		events.setDep_id(2);
		events.setDetail("経理の基礎を学びます");
		events.setRegistered_id("001");
		eventsDao.insert(events);

		events.setEvent_id(EVENUM);
		eventsDao.delete(events);
		int event_count = 1;
		try (Connection con = (Connection) ds.getConnection()) {
			String sql = "SELECT COUNT(*) from events where event_id="+ EVENUM;
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				event_count = Integer.parseInt(rs.getString("count(*)"));
			}
		}
		assertThat(event_count, is(0));
	}


	/**
	 * DBeventsテーブルにあるデータの件数をカウントする
	 * @throws Exception
	 */
	@Test
	public void testCountAll() throws Exception {
		EventsDao eventsDao = DaoFactory.createEventsDao();
		double val = eventsDao.countAll();
		// 期待値:イベント数[7件]
		double expected = 7.0;

		assertThat(val, is(expected));
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
