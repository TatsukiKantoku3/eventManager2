package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;
import com.mysql.jdbc.Statement;

import domain.Attend;
import domain.Events;
import domain.Members;

public class AttendDaoImplTest extends TestDBAccess {
	private final static String NORMAL_MEMBER_ID1 = "500";
	final static String NORMAL_MEMBER_NAME1 = "田中次郎";
	final static String NORMAL_MEMBER_ID2 = "501";
	final static String NORMAL_MEMBER_NAME2 = "山本楓";
	final String NORMAL_NEW_MEMBER_ID1 = "600";
	final String NORMAL_NEW_MEMBER_ID2 = "601";
	static final int NORMAL_EVENT_ID1 = 100;
	final int NORMAL_NEW_EVENT_ID1 = 200;
	final int NORMAL_NEW_EVENT_ID2 = 201;
	final String FAULT_MEMBER_ID = "100000001";
	final String NULL_MEMBER_ID = null;
	final int FAULT_EVENT_ID = 1000;
	final int TOTAL_ATTEND_INDEX = 0;//findAttendsで返却されるListのindex番号
	final int COUNT_ONE = 1;
	final int COUNT_TWO = 2;
	final int COUNT_ZERO = 0;
	final List<Attend> NULL_LIST= new ArrayList<>();
	private static DataSource testds;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InitialContext ctx = null;

		try {
			ctx = new InitialContext();
			testds = (DataSource)ctx.lookup("java:comp/env/jdbc/eventdb2");
		}catch(Exception e) {
			if(ctx != null) {
				try {
					ctx.close();
				}catch(Exception el) {
					throw new RuntimeException(el);
				}
			}
			throw new RuntimeException(e);
		}

		// member2件、event1件の登録、attend1件の登録
		try (Connection conn = testds.getConnection() ) {

			try {
				//オートコミットを切る
				conn.setAutoCommit(false);
				String sqlDel = "DELETE FROM attends  where  member_id='" + NORMAL_MEMBER_ID1 + "' AND event_id="
						+ NORMAL_EVENT_ID1 + ";";
				Statement stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlDel);

				String sqlDel1 = "DELETE FROM members  where  member_id='" + NORMAL_MEMBER_ID1 + "';";
				Statement stmt1 = (Statement) conn.createStatement();
				stmt1.executeUpdate(sqlDel1);

				String sqlDel2 = "DELETE FROM members  where  member_id='" + NORMAL_MEMBER_ID2 + "';";
				Statement stmt2 = (Statement) conn.createStatement();
				stmt2.executeUpdate(sqlDel2);

				String sqlDel3 = "DELETE FROM events  where  event_id=" + NORMAL_EVENT_ID1 + ";";
				Statement stmt3 = (Statement) conn.createStatement();
				stmt3.executeUpdate(sqlDel3);

				String sqlMem = "INSERT INTO `members` VALUES ('" + NORMAL_MEMBER_ID1 + "','" + NORMAL_MEMBER_NAME1
						+ "','タナカジロウ','1990-12-12','東京都新宿区飯田橋54-10-1','090-6433-1200','2010-04-02',1,NULL,'jiro'),"
						+ "('" + NORMAL_MEMBER_ID2 + "','" + NORMAL_MEMBER_NAME2
						+ "','ヤマモトカエデ','1995-12-10','東京都新宿区飯田橋54-10-1','090-6433-1233','2018-04-02',3,NULL,'kaede');";
				Statement stmt4 = (Statement) conn.createStatement();
				stmt4.executeUpdate(sqlMem);

				String sqlEve = "INSERT INTO `events` VALUES (" + NORMAL_EVENT_ID1
						+ ",'研修会','2018-06-04 15:29:34','2018-06-04 15:29:34',1,2,"
						+ "'経理の基礎を学びます','aoi','2018-06-04 15:29:34');";
				Statement stmt5 = (Statement) conn.createStatement();
				stmt5.executeUpdate(sqlEve);

				String sqlAtt = "INSERT INTO attends VALUES (null,'" + NORMAL_MEMBER_ID1 + "','" + NORMAL_EVENT_ID1
						+ "');";
				Statement stmt6 = (Statement) conn.createStatement();
				stmt6.executeUpdate(sqlAtt);

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
	public static void tearDownAfterClass() throws Exception {
		// member2件、event1件の登録、attend1件の削除
		try (Connection conn = testds.getConnection() ) {
			try {
				//オートコミットを切る
				conn.setAutoCommit(false);
				String sqlDel = "DELETE FROM attends  where  member_id='" + NORMAL_MEMBER_ID1 + "' AND event_id="
						+ NORMAL_EVENT_ID1 + ";";
				Statement stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlDel);

				String sqlDel1 = "DELETE FROM members  where  member_id='" + NORMAL_MEMBER_ID1 + "';";
				Statement stmt1 = (Statement) conn.createStatement();
				stmt1.executeUpdate(sqlDel1);

				String sqlDel2 = "DELETE FROM members  where  member_id='" + NORMAL_MEMBER_ID2 + "';";
				Statement stmt2 = (Statement) conn.createStatement();
				stmt2.executeUpdate(sqlDel2);

				String sqlDel3 = "DELETE FROM events  where  event_id=" + NORMAL_EVENT_ID1 + ";";
				Statement stmt3 = (Statement) conn.createStatement();
				stmt3.executeUpdate(sqlDel3);

				//エラーがなければコミットする
				conn.commit();

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

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testAttendDaoImpl() {
	}

	/**
	 * attend情報をインサートし、findAttendメソッドで
	 * 情報が追加されているかどうか調べる
	 * @throws Exception
	 */
	@Test
	public void testInsertStringInt() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		attendDao.insert(NORMAL_MEMBER_ID2,NORMAL_NEW_EVENT_ID1);//member_id=105, event_id=10
		List<Attend> totalAttend = attendDao.findAttends(NORMAL_NEW_EVENT_ID1);//event_id=10
		String member_id = (totalAttend.get(TOTAL_ATTEND_INDEX).getMember_id());//index=0
		String member_name = (totalAttend.get(TOTAL_ATTEND_INDEX).getMember_name());

		assertThat(member_name, is(NORMAL_MEMBER_NAME2));//equalTo()
		assertThat(member_id, is(NORMAL_MEMBER_ID2));//equalTo()
		// test後にdeleteメソッドで情報を削除する
		attendDao.delete(NORMAL_MEMBER_ID2,NORMAL_NEW_EVENT_ID1);
	}

	/**
	 * insertしたデータをdeleteし、deleteの戻り値の更新行数 line_num=0 になれば良い
	 * @throws Exception
	 */
	@Test
	public void testDeleteStringInt() throws Exception {

		AttendDao attendDao = DaoFactory.createAttendDao();
		attendDao.insert(NORMAL_MEMBER_ID2, NORMAL_NEW_EVENT_ID2);//member_id=105, event_id=11
		int line_num = attendDao.delete(NORMAL_MEMBER_ID2, NORMAL_NEW_EVENT_ID2);

		assertThat(line_num, is(COUNT_ONE));
	}

	@Test
	public void testFindAttends() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		List<Attend> totalAttend=attendDao.findAttends(NORMAL_EVENT_ID1);//event_id=1
		// member_idとmember_nameが格納されて返却されるので、それを比較する
		String member_name = (totalAttend.get(TOTAL_ATTEND_INDEX)).getMember_name();
		String member_id = (totalAttend.get(TOTAL_ATTEND_INDEX)).getMember_id();

		assertThat(member_name, is(NORMAL_MEMBER_NAME1));//equalTo()
		assertThat(member_id, is(NORMAL_MEMBER_ID1));
	}

	/**
	 * memberを削除した際に呼び出されるメソッド
	 * 削除したmemberのattend情報をすべて削除する
	 * insertしたデータをdeleteし、データを検索して count=0 になれば良い
	 * @throws Exception
	 */
	@Test
	public void testDeleteByMemberId() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		attendDao.insert(NORMAL_NEW_MEMBER_ID1, NORMAL_NEW_EVENT_ID1);//member_id=110, event_id=10
		attendDao.insert(NORMAL_NEW_MEMBER_ID1, NORMAL_NEW_EVENT_ID2);//member_id=110, event_id=20
		Members member = new Members();
		member.setMember_id(NORMAL_NEW_MEMBER_ID1);
		int line_num = attendDao.deleteByMemberId(member);

		assertThat(line_num, is(COUNT_TWO));
	}

	/**
	 * eventを削除した際に呼び出されるメソッド
	 * 削除したeventのattend情報をすべて削除する
	 * insertしたデータをdeleteし、データを検索して count=0 になれば良い
	 */
	@Test
	public void testDeleteByEventId() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		attendDao.insert(NORMAL_NEW_MEMBER_ID1, NORMAL_NEW_EVENT_ID1);//member_id=110, event_id=10
		attendDao.insert(NORMAL_NEW_MEMBER_ID2, NORMAL_NEW_EVENT_ID1);//member_id=111, event_id=10
		Events event = new Events();
		event.setEvent_id(NORMAL_NEW_EVENT_ID1);
		int line_num = attendDao.deleteByEventId(event);

		assertThat(line_num, is(COUNT_TWO));
	}


	// --------- 以下から異常系テスト -------------------------------


	/**
	 * attend情報をインサートできないことをエラーメッセージを比較して確認する
	 *
	 * @throws Exception
	 */
	@Test
	public void testInsertStringInt異常() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		try {
			attendDao.insert(FAULT_MEMBER_ID,NORMAL_EVENT_ID1);//member_id=100000001, event_id=1
			fail();
		} catch(Exception e) {
			assertThat(e.getMessage(), equalTo("Data truncation: Data too long for column 'member_id' at row 1"));
		}
	}

	/**
	 * attend情報をインサートできないことをエラーメッセージを比較して確認する nullを引数で渡す
	 *
	 * @throws Exception
	 */
	@Test
	public void testInsertStringInt異常2() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		try {
			attendDao.insert(NULL_MEMBER_ID,NORMAL_EVENT_ID1);//member_id=100000001, event_id=1
			fail();
		} catch(Exception e) {
			assertThat(e.getMessage(), equalTo("Column 'member_id' cannot be null"));
		}
	}

	/**
	 * 存在しないデータをdeleteし、deleteできないことをエラーメッセージを比較して確認する
	 * @throws Exception
	 */
	@Test
	public void testDeleteStringInt異常() throws Exception {

		AttendDao attendDao = DaoFactory.createAttendDao();
		int line_num = attendDao.delete(FAULT_MEMBER_ID, FAULT_EVENT_ID);

		assertThat(line_num, equalTo(COUNT_ZERO));

	}

	@Test
	public void testFindAttends異常() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();

		try {
			List<Attend> totalAttend=attendDao.findAttends(FAULT_EVENT_ID);
			assertThat(totalAttend,equalTo(NULL_LIST));
		}catch(Exception e){
			fail();
		}
	}

	/**
	 * memberを削除した際に呼び出されるメソッド
	 * 偽の社員IDデータをセットして戻り値の更新行数 line_num=0を確認
	 * @throws Exception
	 */
	@Test
	public void testDeleteByMemberId異常() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		Members member = new Members();
		member.setMember_id(FAULT_MEMBER_ID);
		int line_num = attendDao.deleteByMemberId(member);

		assertThat(line_num, is(COUNT_ZERO));
	}

	/**
	 * eventを削除した際に呼び出されるメソッド
	 * 偽のイベントIDデータセットして戻り値の更新行数 line_num=0を確認する
	 */
	@Test
	public void testDeleteByEventId異常() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		Events event = new Events();
		event.setEvent_id(FAULT_EVENT_ID);
		int line_num = attendDao.deleteByEventId(event);

		assertThat(line_num, is(COUNT_ZERO));
	}

}
