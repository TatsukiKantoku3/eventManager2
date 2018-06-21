package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;

public class AccountFileReaderTest extends TestDBAccess {

	//正常系
	String DATA="D";
	String MEMBERID="105";
	String LOGINID="a-yamamoto";
	String LOGINPASS="ayamamoto";
	String AUTHID="1";

	//異常系
	String FALSE_DATA="H";
	String FALSE_MEMBERID="あいうえおかきくけこ";
	String FALSE_MEMBERID2="111111111111";
	String FALSE_LOGINID="ログインＩＤ番号20180402-t-tanaka";
	String FALSE_LOGINID2="ログインID";
	String FALSE_LOGINPASS="ログインパス";
	String FALSE_LOGINPASS2="loginid_20180402";
	String FALSE_AUTHID="10";
	String FALSE_AUTHID2="壱";

	static DataSource  ds;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InitialContext ctx=null;

		try {
			ctx=new InitialContext();
			ds=(DataSource)ctx.lookup("java:comp/env/jdbc/eventdb2");
		} catch (NamingException e) {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException el) {
					try {
						throw new RuntimeException(el);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
			throw new RuntimeException(e);
		  }

		try (Connection con = ds.getConnection()){

		PreparedStatement stmt;
		String sql;
		//全件削除

		 sql="TRUNCATE account";
		stmt=con.prepareStatement(sql);
		stmt.executeUpdate();
		}
	}


	final int valid_data_quantity = 5;
	AccountFileReader accountFileReader = new AccountFileReader("c:\\work_1\\account_20180601.csv",
			valid_data_quantity);


	@Test
	public void testMainStringArray正常1() throws Exception {

		String result = accountFileReader.main();
		String Result="100";
		assertThat(result,is(Result));

	}

	@Test
	public void testMainStringArray異常1() throws Exception{
		int valid_data_quantity = 5;
		String result=null;

			AccountFileReader accountFileReader2 = new AccountFileReader("c:\\work_1\\sample.csv",
					valid_data_quantity);
			result = accountFileReader2.main();
			String Result="ファイル読み込みエラー";
			assertThat(result,is(Result));


	}

	@Test
	public void testMainStringArray異常2() throws Exception{
		int valid_data_quantity = 1;
		String result=null;

			AccountFileReader accountFileReader2 = new AccountFileReader("c:\\work_1\\account_20180601.csv",
					valid_data_quantity);
			result = accountFileReader2.main();
			String Result="ヘッダ行異常１";
			assertThat(result,is(Result));
	}

	@Test
	public void testMainStringArray異常3() throws Exception{
		//utf8の指定なし
		int valid_data_quantity = 5;
		String result=null;

			AccountFileReader accountFileReader2 = new AccountFileReader("c:\\work_1\\account_fake.csv",
					valid_data_quantity);
			result = accountFileReader2.main();

			String Result="ファイルキャラクターセットエラー";
			assertThat(result,is(Result));


	}

	@Test
	public void testMainStringArray異常4() throws Exception{
		//有効性エラー
		//ログインパスを8桁より少なく
		String result=null;

			AccountFileReader accountFileReader2 = new AccountFileReader("C:\\work_1\\account_fake2.csv",
					valid_data_quantity);
			result = accountFileReader2.main();
			String Result="データ有効性エラー";
			assertThat(result,is(Result));

	}

	@Test
	public void testMainStringArray異常5() throws Exception{
		//データ重複
		String result=null;

		AccountFileReader accountFileReader2 = new AccountFileReader("C:\\work_1\\account_doubledata.csv",
				valid_data_quantity);
		result = accountFileReader2.main();
		String Result="302";
		assertThat(result,is(Result));
	}

	@Test
	public void testMainStringArray異常6() throws Exception {
		//データ行の空白
		String result=null;

		AccountFileReader accountFileReader2 = new AccountFileReader("C:\\work_1\\account_blankdata.csv",
				valid_data_quantity);
		result = accountFileReader2.main();
		String Result="データ有効性エラー";
		assertThat(result,is(Result));
	}

	@Test
	public void testEnableLine正常() {

		String[] columns= {DATA,MEMBERID,LOGINID,LOGINPASS,AUTHID};
		boolean check= accountFileReader.enableLine(columns);
		assertThat(check,is(true));
	}

	@Test
	public void testEnableLine異常1() {

		//メンバーID異常
		String[] a= {FALSE_DATA,FALSE_MEMBERID,LOGINID,LOGINPASS,AUTHID};
		boolean check =accountFileReader.enableLine(a);
		assertThat(check,is(false));
	}

	@Test
	public void testEnableLine異常2() {

		//メンバーID異常
		String[] a= {FALSE_DATA,FALSE_MEMBERID2,LOGINID,LOGINPASS,AUTHID};
		boolean check =accountFileReader.enableLine(a);
		assertThat(check,is(false));
	}

	@Test
	public void testEnableLine異常3() {
		//ログインID異常
		String[] a= {FALSE_DATA,MEMBERID,FALSE_LOGINID,LOGINPASS,AUTHID};
		boolean check =accountFileReader.enableLine(a);
		assertThat(check,is(false));
	}

	@Test
	public void testEnableLine異常4() {
		//ログインID異常
		String[] a= {FALSE_DATA,MEMBERID,FALSE_LOGINID2,LOGINPASS,AUTHID};
		boolean check =accountFileReader.enableLine(a);
		assertThat(check,is(false));
	}

	@Test
	public void testEnableLine異常5() {
		//ログインパス異常
		String[] a= {FALSE_DATA,MEMBERID,LOGINID,FALSE_LOGINPASS,AUTHID};
		boolean check =accountFileReader.enableLine(a);
		assertThat(check,is(false));
	}

	@Test
	public void testEnableLine異常6() {
		//ログインパス異常
		String[] a= {FALSE_DATA,MEMBERID,LOGINID,FALSE_LOGINPASS2,AUTHID};
		boolean check =accountFileReader.enableLine(a);
		assertThat(check,is(false));
	}
	@Test
	public void testEnableLine異常7() {

		//管理ID以上
		String[] a= {FALSE_DATA,MEMBERID,LOGINID,LOGINPASS,FALSE_AUTHID};
		boolean check =accountFileReader.enableLine(a);
		assertThat(check,is(false));

	}


	@Test
	public void testEnableLine異常8() {
		//管理ID以上
		try {
			String[] a= {FALSE_DATA,MEMBERID,LOGINID,LOGINPASS,FALSE_AUTHID2};
			boolean check =accountFileReader.enableLine(a);
			assertThat(check,is(false));
		}catch(Exception e) {

			String Result="For input string: \"壱\"";
			assertThat(e.getMessage(),is(Result));
		}
	}

}
