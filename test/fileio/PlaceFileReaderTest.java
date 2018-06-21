package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

public class PlaceFileReaderTest extends TestDBAccess {
	private final static String NORMAL_MEMBER_ID1 = "523";
	private final static String LEGAL_FILE_NAME = "C:\\work_1\\place_20180601.csv";
	private final static String BAD_FILE_NAME = "C:\\work_1\\place_20180601_0.csv";//ファイルなし
	private final static String BAD_FILE_NAME_CHAR = "C:\\work_1\\place_20180601_2.csv";//ファイルキャラクターエラー
	private final static String BAD_FILE_NAME_HEAD = "C:\\work_1\\place_20180601_4.csv";//ヘッダー異常ファイル
	private final static String BAD_FILE_NAME_DATA = "C:\\work_1\\place_20180601_5.csv";//データ行に空のデータ
	private final int COLUMNS = 8;
	protected static final String SUCCESS = "100"; //正常結果値100
	private static final String DATA_ROW = "D";//ファイルのデータ行
	private static final String SEPARATE = ",";//区切り文字 カンマ
	private static final String ERROR_FILE_READ = "ファイル読み込みエラー";
	private static final String ERROR_FILE_CHAR = "ファイルキャラクターセットエラー";
	private static final String ERROR_FILE_HEAD = "ヘッダ行異常2";
	private static final String ERROR_FILE_DATA = "データ有効性エラー";

	private static final String[][] BAD_DATA_LIST = {
			{ "D", "123456789012345678901", "10", "1", "1", "1", "523", "17:00" },
			{ "D", "第一会議室", "10", "1", "1", "0", "123456789", "17:00" },
			{ "D", "第一会議室", "10", "1", "0", "1", "523", "30:70" }, //timeFormat_error
			{ "D", "第一会議室", "10", "3", "1", "1", "523", "17:00" },
			{ "D", "第一会議室", "10", "0", "3", "1", "523", "17:00" },
			{ "D", "第一会議室", "10", "0", "-1", "0", "523", "17:00" },
			{ "D", "第一会議室", "10", "1", "0", "3", "523", "17:00" },
			{ "D", "第一会議室", "10", "", "", "", "", "" }};

	private static  DataSource testds;
	private static List<String[]> dataList;

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

		//Placeテーブルの削除  memberからmember_idが"523"の削除してからインサート
		try (Connection conn = testds.getConnection() ) {

			try {
				//SQL処理
				conn.setAutoCommit(false);//オートコミットを外す
				String sqlDel = "DELETE FROM place;";
				Statement stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlDel);

				String sqlDel2 = "DELETE FROM members where member_id = '" +NORMAL_MEMBER_ID1+ "';";
				Statement stmt2 = (Statement)conn.createStatement();
				stmt2.executeUpdate(sqlDel2);

				String sqlMem = "INSERT INTO `members` VALUES ('" + NORMAL_MEMBER_ID1 + "','田中小次郎"
						+ "','タナカコジロウ','1990-12-12','東京都新宿区飯田橋54-10-1','090-6433-1200','2010-04-02',1,NULL,'kojiro');";
				Statement stmt3 = (Statement) conn.createStatement();
				stmt3.executeUpdate(sqlMem);

				//エラーがなければコミットする
				conn.commit();
			}catch (Exception e) {
				System.out.println("error11");
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

		Path path=Paths.get(LEGAL_FILE_NAME);

		dataList = new ArrayList<>();;

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {

				String[] columns = line.split(SEPARATE); // 区切り文字","で分割する

				//D行をコレクションに格納
				if (DATA_ROW.equals(columns[0])) {
					dataList.add(columns);
				}
			} //whileの閉じ --終端

			reader.close();
		}catch(MalformedInputException e) {
			e.printStackTrace();
			dataList.clear();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ファイル読み込みエラー");
			dataList.clear();

		}

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//Placeテーブルの削除
		try (Connection conn = testds.getConnection() ) {
			try {
				//SQL処理
				conn.setAutoCommit(false);//オートコミットを外す
				String sqlDel = "DELETE FROM place;";
				Statement stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlDel);

				String sqlDel2 = "DELETE FROM members where member_id = '" +NORMAL_MEMBER_ID1+ "';";
				Statement stmt2 = (Statement)conn.createStatement();
				stmt2.executeUpdate(sqlDel2);

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

	// ------------------正常値テスト ここから --------------------------------------------------------

	// mainメソッド、正常テスト、インスタンスしてコンストラクタにファイルパスを渡す
	@Test
	public void testMain() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(LEGAL_FILE_NAME, COLUMNS);
			String result = PlaceFileReader.main();
			assertThat(result, is(SUCCESS));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 各ファイルごとに調整したデータチェックのテスト
	@Test
	public void testEnableLine() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(LEGAL_FILE_NAME, COLUMNS);
			//int i = 1;
			for(String[] columns:dataList) {
				assertThat(PlaceFileReader.enableLine(columns), is(true));
				//i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ファイル有効性チェックを行い
	// 有効性チェックが正常であれば
	// D行を格納したList<String[]>を返却します
	@Test
	public void testEnableFile() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(LEGAL_FILE_NAME, COLUMNS);
			List<String[]> fileRead = new ArrayList<String[]>();
			try {
				fileRead = PlaceFileReader.enableFile();//ファイル有効性チェック
			} catch (NoSuchFileException e) {
				e.printStackTrace();
			}

			int i=0;
			for(String[] cols:dataList) {
				int j = 0;
				String[] fileCols = fileRead.get(i);
				for(String value:cols) {
					//System.out.print(fileCols[j] + ":" + value + " " );
					assertThat(fileCols[j], is(value));
					j++;
				}
				System.out.println();
				i++;
			}
		} catch (NoSuchFileException e) {
			e.printStackTrace();
		}
	}

	// String result 初期値nullを返却 期待値null
	@Test
	public void testGetResult() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(LEGAL_FILE_NAME, COLUMNS);
			String result =  PlaceFileReader.getResult();
			assertThat(result, is(not(notNullValue())));
		} catch (NoSuchFileException e) {
			e.printStackTrace();
		}
	}

	// データタイプ(S,H,D,E)のみが入っているか検査します
	// それ以外であればfalseを返却します
	// MatchTypes(String d)
	@Test
	public void testMatchType() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(LEGAL_FILE_NAME, COLUMNS);
			for(String[] cols:dataList) {
				assertThat(PlaceFileReader.matchType(cols[0]), is(true));
			}
		} catch (NoSuchFileException e) {
			e.printStackTrace();
		}
	}

	// ----------------------- 以下 異常値テスト -------------------------------------------

	// mainメソッド、異常系テストIOException
	@Test
	public void 異常系testMain1() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(BAD_FILE_NAME, COLUMNS);
			String result = PlaceFileReader.main();
			assertThat(result, is(ERROR_FILE_READ));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// mainメソッド、異常系テスト ファイルキャラクターセットエラー
	@Test
	public void 異常系testMain2() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(BAD_FILE_NAME_CHAR, COLUMNS);
			String result = PlaceFileReader.main();
			assertThat(result, is(ERROR_FILE_CHAR));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// mainメソッド、異常系テスト データエラー
	@Test
	public void 異常系testMain3() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(BAD_FILE_NAME_DATA, COLUMNS);
			String result = PlaceFileReader.main();
			assertThat(result, is(ERROR_FILE_DATA));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ファイル有効性チェックを行い
	// 有効性チェックが正常であれば
	// D行を格納したList<String[]>を返却します
	@Test
	public void 異常系testEnableFile() throws Exception {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(BAD_FILE_NAME_HEAD, COLUMNS);
			//List<String[]> fileRead = new ArrayList<String[]>();
			String result = PlaceFileReader.main();//ファイル有効性チェック
			assertThat(result, is(ERROR_FILE_HEAD));
		} catch (NoSuchFileException e) {
			//e.printStackTrace();
		}
	}

	// 各ファイルごとに調整したデータチェックのテスト 空白
	@Test
	public void 異常系testEnableLine() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(LEGAL_FILE_NAME, COLUMNS);
			for(String[] columns:BAD_DATA_LIST) {
				assertThat( PlaceFileReader.enableLine(columns), is(false));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
