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
import org.junit.Test;

import com.TestDBAccess;
import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;

public class DepartFileReaderTest extends TestDBAccess {
	private final String FILE_NAME[] = {"C:\\work\\department_20180601.csv",
			"C:\\xxxx.csv", "C:\\work\\lossDataFile.csv", "C:\\work\\noneValidData.csv"};
	private final int COLUMNS = 4;
	private final DepartFileReader DEP_FIL_RED = new DepartFileReader(FILE_NAME[0], COLUMNS);

	static DataSource ds;

	private final static String DEP_NAME[] = {"人事部", "経理部", "総務部", "営業部", "開発部"};
	private final static int DEP_FLOOR[] = {4, 2, 2, 3, 4};
	protected final String FAULT_DATA[] =
		{"123456789012345678901234567890123456789012345678901234567890", "floor", "1145141919", "佐藤"};

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

		//
		try (Connection conn = ds.getConnection() ) {

			try {
				//オートコミットを切る
				conn.setAutoCommit(false);
				String sqlTrn = "TRUNCATE TABLE eventdb2.department;";
				Statement stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlTrn);

				String sqlDep = "INSERT INTO department(department, floor) VALUES"
						+ " ('"+DEP_NAME[0]+"', "+DEP_FLOOR[0]+"),"
						+ " ('"+DEP_NAME[1]+"', "+DEP_FLOOR[1]+"),"
						+ " ('"+DEP_NAME[2]+"', "+DEP_FLOOR[2]+"),"
						+ " ('"+DEP_NAME[3]+"', "+DEP_FLOOR[3]+"),"
						+ " ('"+DEP_NAME[4]+"', "+DEP_FLOOR[4]+");";
				Statement stmtDep = (Statement) conn.createStatement();
				stmtDep.executeUpdate(sqlDep);

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


	//---- ここから正常パターン ----//
	@Test
	public void testMain() throws Exception {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String result = DEP_FIL_RED.main();
		System.out.print(result);

		assertThat(result, is("100"));
	}

	@Test
	public void testEnableLine() {
		String[] dataArray = {"D", DEP_NAME[0], String.valueOf(DEP_FLOOR[0]), "12"};
		boolean dataOver1 = DEP_FIL_RED.enableLine(dataArray);
		assertThat(dataOver1, is(true));
	}

	@Test
	public void testMainStringArray() throws NamingException, IOException {
		// DepartFileReaderをコメントアウト
	}

	@Test
	public void testDepartFileReader() {
		// コンストラクタのみのためスキップ
	}
	//---- ここまで正常パターン ----//


	//---- ここから異常パターン ----//
	// 存在しないファイル
	@Test
	public void 異常系testMainNoneFile() throws Exception {
		DepartFileReader DepFaultFilReader = new DepartFileReader(FILE_NAME[1], COLUMNS);
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String result = DepFaultFilReader.main();
		assertThat(result, is("ファイル読み込みエラー"));
	}

	// csvデータ不整合
	@Test
	public void 異常系testMainFaultFile() throws Exception {
		DepartFileReader DepFaultFilRed = new DepartFileReader(FILE_NAME[3], COLUMNS);
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String result = DepFaultFilRed.main();
		assertThat(result, is("データ有効性エラー"));
	}

	// データ行にnull
	@Test
	public void 異常系testEnableLineNullData() throws NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String[] LostString = {"D", "", String.valueOf(DEP_FLOOR[1]), "45"};
		boolean dataNull = DEP_FIL_RED.enableLine(LostString);
		assertThat(dataNull, is(false));
	}

	// 部署名の文字数(50)超過
	@Test
	public void 異常系testEnableLineOverData1() throws NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String[] overString1 = {"D", FAULT_DATA[0], String.valueOf(DEP_FLOOR[2]), "12"};
		boolean dataOver1 = DEP_FIL_RED.enableLine(overString1);
		assertThat(dataOver1, is(false));
	}

	// フロアの文字規定違反(intのみ)
		@Test
		public void 異常系testEnableLineFaultData2() throws NamingException, IOException {
			JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

			String[] overString3 = {"D", DEP_NAME[1], FAULT_DATA[1], "12"};
			boolean dataOver3 = DEP_FIL_RED.enableLine(overString3);
			assertThat(dataOver3, is(false));
		}

	// 部長IDの文字数(8)超過
	@Test
	public void 異常系testEnableLineOverData3() throws NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String[] overString3 = {"D", DEP_NAME[2], String.valueOf(DEP_FLOOR[3]), FAULT_DATA[2]};
		boolean dataOver3 = DEP_FIL_RED.enableLine(overString3);
		assertThat(dataOver3, is(false));
	}

	// 部長IDの文字規定違反(半角英数字とハイフンのみ)
	@Test
	public void 異常系testEnableLineFaultData3() throws NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String[] faultString3 = {"D", DEP_NAME[3], String.valueOf(DEP_FLOOR[4]), FAULT_DATA[3]};
		boolean dataFault3 = DEP_FIL_RED.enableLine(faultString3);
		assertThat(dataFault3, is(false));
	}
	//---- ここまで異常パターン ----//

}
