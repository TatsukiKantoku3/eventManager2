package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;


public class FileControllerTest extends TestDBAccess {
	FileController target=new FileController();
	protected final String MEMBER_INSERT = "memberInsert";
	protected final String ACCOUNT_INSERT = "accountInsert";
	protected final String PLACE_INSERT = "placeInsert";
	protected final String DEPART_INSERT = "departInsert";
	protected final String MEMBER_FAULT = "member_fault";
	protected final String ACCOUNT_FAULT = "account_fault";
	protected final String PLACE_FAULT = "place_fault";
	protected final String DEPART_FAULT = "depart_fault";

	private static final String EXPECTED = "100"; // expected
//	private static final String FAULT_EXPECTED300 = "300";
//	private static final String FAULT_EXPECTED302 = "302";
	private static final String FAULT_EXPECTED_FILE = "ファイル読み込みエラー";

	private static  DataSource testds;


//	static {
//		// JNDI準備
//		try {
//			JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
//		} catch (NamingException | IOException e) {
//			e.printStackTrace();
//		}
//	}

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

		// 各テーブルTruncate処理
		try (Connection conn = testds.getConnection() ) {

			try {
				//SQL処理
				conn.setAutoCommit(false);//オートコミットを外す

				//Members,Account,Place,Departmentテーブルをトランケート
				String sqlTrunc = "set foreign_key_checks = 0";
				Statement stmt1 = (Statement) conn.createStatement();
				stmt1.executeUpdate(sqlTrunc);

				sqlTrunc = "TRUNCATE TABLE eventdb2.members";
				stmt1.executeUpdate(sqlTrunc);

				sqlTrunc ="set foreign_key_checks = 1";
				stmt1.executeUpdate(sqlTrunc);


				String sqlTrunc2 = "TRUNCATE TABLE eventdb2.account; ";
				Statement stmt2 = (Statement) conn.createStatement();
				stmt2.executeUpdate(sqlTrunc2);

				String sqlTrunPlac = "TRUNCATE TABLE eventdb2.place; ";

				Statement stmt3 = (Statement) conn.createStatement();
				stmt3.executeUpdate(sqlTrunPlac);

				String sqlTrunDep = "TRUNCATE TABLE eventdb2.department; ";
				Statement stmt4 = (Statement) conn.createStatement();
				stmt4.executeUpdate(sqlTrunDep);

				//エラーがなければコミットする
				conn.commit();
			}catch (Exception e) {
				System.out.println("error-truncate");
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
		// 各テーブルTruncate処理
		try (Connection conn = testds.getConnection()) {

			try {
				//SQL処理
				conn.setAutoCommit(false);//オートコミットを外す

				//Members,Account,Place,Departmentテーブルをトランケート
				String sqlTrunc = "set foreign_key_checks = 0";
				Statement stmt1 = (Statement) conn.createStatement();
				stmt1.executeUpdate(sqlTrunc);

				sqlTrunc = "TRUNCATE TABLE eventdb2.members";
				stmt1.executeUpdate(sqlTrunc);

				sqlTrunc ="set foreign_key_checks = 1";
				stmt1.executeUpdate(sqlTrunc);

				String sqlTrunc2 = "TRUNCATE TABLE eventdb2.account; ";
				Statement stmt2 = (Statement) conn.createStatement();
				stmt2.executeUpdate(sqlTrunc2);

				String sqlTrunPlac = "TRUNCATE TABLE eventdb2.place; ";

				Statement stmt3 = (Statement) conn.createStatement();
				stmt3.executeUpdate(sqlTrunPlac);

				String sqlTrunDep = "TRUNCATE TABLE eventdb2.department; ";
				Statement stmt4 = (Statement) conn.createStatement();
				stmt4.executeUpdate(sqlTrunDep);

				//エラーがなければコミットする
				conn.commit();
			} catch (Exception e) {
				System.out.println("error-truncate");
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

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	// -----------正常系テスト------------------------------------------------------------------------------

	@Test
	public void 正常1testMember() {
		String result;
		try {
			result = target.member(MEMBER_INSERT);
			assertThat(result, is(EXPECTED));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 正常2testAccount() {
		String result;
		try {
			result = target.account(ACCOUNT_INSERT);
			assertThat(result, is(EXPECTED));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 正常3testPlace(){
		try {
			String result = target.place(PLACE_INSERT);
			assertThat(result, is(EXPECTED));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 正常4testDepart() {
		try {
			String result = target.depart(DEPART_INSERT);
			assertThat(result, is(EXPECTED));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


// -----------異常系テスト------------------------------------------------------------------------------

	@Test
	public void 異常系1testMember() {
		String result = null;
		try {
			result =  target.member(MEMBER_FAULT);
			assertThat(result, is(FAULT_EXPECTED_FILE));
		}catch(Exception e) {
			e.printStackTrace();

		}
	}

	@Test
	public void 異常系2testAccount() {
		String result = null;
		try {
			result =  target.account(ACCOUNT_FAULT);
			assertThat(result, is(FAULT_EXPECTED_FILE));
		}catch(Exception e) {
			e.printStackTrace();

		}
	}

	@Test
	public void 異常系3testPlace() {
		String result = null;
		try {
			result =  target.place(PLACE_FAULT);
			assertThat(result, is(FAULT_EXPECTED_FILE));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 異常系4testDepart() {
		String result = null;
		try {
			result =  target.depart(DEPART_FAULT);
			assertThat(result, is(FAULT_EXPECTED_FILE));
		}catch(Exception e) {
			e.printStackTrace();

		}
	}


}
