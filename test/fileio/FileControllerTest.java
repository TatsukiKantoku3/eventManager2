package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.TestDBAccess;
import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileControllerTest extends TestDBAccess {

	static DataSource ds;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InitialContext ctx = null;

		try {
			ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/eventdb2");
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

		try (Connection conn = ds.getConnection() ) {
			try {
				//オートコミットを切る
				conn.setAutoCommit(false);
				String sqlTrn = "USE eventdb2;"
						+ " TRUNCATE TABLE eventdb2.account;"
						+ " TRUNCATE TABLE eventdb2.attends;"
						+ " TRUNCATE TABLE eventdb2.department;"
//						+ " TRUNCATE TABLE eventdb2.events;"
//						+ " TRUNCATE TABLE eventdb2.members;"
						+ " TRUNCATE TABLE eventdb2.place";
				Statement stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlTrn);

				//エラーがなければコミットする
				conn.commit();
			}catch (Exception e) { // ロールバック&エラー表示
				System.out.println("error1");
				e.printStackTrace();
				conn.rollback();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					System.out.println("error2");
				}
			}
		}
	}

	//-------------------- 単体テスト完了まで放置 --------------------//

	/*
	 * 引数:fileName, 戻り値:code
	 * 正常:戻り値"100"
	 * 異常想定：ファイル有効性チェックなどでエラーを起こす
	 */
	@Test
	public void test1Member() throws NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String result = FileController.member("memberInsert");
		assertThat(result, is("100"));
	}


	@Test
	public void testAccount() throws NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String result = FileController.account("accountInsert");
		assertThat(result, is("100"));
	}


	@Test
	public void testPlace() throws NumberFormatException, NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String result = FileController.place("placeInsert");
		assertThat(result, is("100"));
	}


	@Test
	public void testDepart() throws NumberFormatException, NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String result = FileController.depart("departInsert");
		assertThat(result, is("100"));
	}


	@Test
	public void testOutput() {
		// 一先ずスルー
	}

}
