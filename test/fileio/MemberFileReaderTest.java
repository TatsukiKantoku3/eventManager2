package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;

public class MemberFileReaderTest extends TestDBAccess {
	static final int valid_data_quantity = 9;
	static final MemberFileReader MembersFileReader = new MemberFileReader("c:\\work_1\\member_20180402.csv",
			valid_data_quantity);
	static final String MEMBER_ID="105";
	static final String NAME="山本葵";
	static final String KANA="ヤマモトアオイ";
	static final String BIRTHDAY="1995/12/10";
	static final String ADDRESS="東京都新宿区飯田橋54-10-1";
	static final String TEL="090-6433-1233";
	static final String ENTER="4月1日";
	static final String DEP_ID="2";
	static final String FALSE_MEMBER_ID="123456789";
	static final String FALSE_NAME="あああああいいいいいうううううえええええおおおおお"
			+ "かかかかかきききききくくくくくけけけけけこここここさ";
	static final String FALSE_KANA="12345";
	static final String FALSE_KANA2="アアアアアイイイイイウウウウウエエエエエオオオオオカカカカカキキキキキククククク"
			+ "ケケケケケコココココサササササ";
	static final String FALSE_BIRTHDAY="12月10日";
	static final String FALSE_TEL="0000-1122-3344";
	static final String FALSE_ENTER="04/01";
	static final String FALSE_DEP_ID="6";
	static final String EMPTY="";

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
					throw new RuntimeException(el);
				}
			}
			throw new RuntimeException(e);
		  }

		try (Connection con = ds.getConnection()){
			PreparedStatement stmt;
			String sql;
			//全件削除

			 sql="TRUNCATE members";
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();

		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
//		MembersDao membersDao=DaoFactory.createMembersDao();
//		Members members = new Members();
//		members.setMember_id(MEMBER_ID);
//		membersDao.delete(members);
	}



	@Test
	public void 正常系testEnableLine() {
		String[] test= {EMPTY,MEMBER_ID,NAME,KANA,BIRTHDAY,ADDRESS,TEL,ENTER,DEP_ID};
		assertThat(MembersFileReader.enableLine(test), is(true));

	}

	@Test
	public void 正常系testMainStringArray() {
		try {
			String result = MembersFileReader.main();
			String expected="100";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Test
	public void 異常系testEnableLine1() {
		String[] test= {EMPTY,FALSE_MEMBER_ID,NAME,KANA,BIRTHDAY,ADDRESS,TEL,ENTER,DEP_ID};
		MembersFileReader.enableLine(test);
		String expected="205";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	@Test
	public void 異常系testEnableLine2() {
		String[] test= {EMPTY,MEMBER_ID,FALSE_NAME,KANA,BIRTHDAY,ADDRESS,TEL,ENTER,DEP_ID};
		MembersFileReader.enableLine(test);
		String expected="205";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	@Test
	public void 異常系testEnableLine3() {
		String[] test= {EMPTY,MEMBER_ID,NAME,FALSE_KANA,BIRTHDAY,ADDRESS,TEL,ENTER,DEP_ID};
		MembersFileReader.enableLine(test);
		String expected="204";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	@Test
	public void 異常系testEnableLine4() {
		String[] test= {EMPTY,MEMBER_ID,NAME,FALSE_KANA2,BIRTHDAY,ADDRESS,TEL,ENTER,DEP_ID};
		MembersFileReader.enableLine(test);
		String expected="205";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	@Test
	public void 異常系testEnableLine5() {
		String[] test= {EMPTY,MEMBER_ID,NAME,KANA,FALSE_BIRTHDAY,ADDRESS,TEL,ENTER,DEP_ID};
		MembersFileReader.enableLine(test);
		String expected="200";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	@Test
	public void 異常系testEnableLine6() {
		String[] test= {EMPTY,MEMBER_ID,NAME,KANA,BIRTHDAY,ADDRESS,FALSE_TEL,ENTER,DEP_ID};
		MembersFileReader.enableLine(test);
		String expected="200";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	@Test
	public void 異常系testEnableLine7() {
		String[] test= {EMPTY,MEMBER_ID,NAME,KANA,BIRTHDAY,ADDRESS,TEL,FALSE_ENTER,DEP_ID};
		MembersFileReader.enableLine(test);
		String expected="200";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	@Test
	public void 異常系testEnableLine8() {
		String[] test= {EMPTY,MEMBER_ID,NAME,KANA,BIRTHDAY,ADDRESS,TEL,ENTER,FALSE_DEP_ID};
		MembersFileReader.enableLine(test);
		String expected="200";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	@Test
	public void 異常系testEnableLine9() {
		String[] test= {EMPTY,MEMBER_ID,NAME,KANA,BIRTHDAY,ADDRESS,TEL,ENTER,EMPTY};
		MembersFileReader.enableLine(test);
		String expected="203";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	@Test
	public void 異常系testMainStringArray1() {
		try {
			MemberFileReader MembersFileReader = new MemberFileReader("c:\\work\\member.csv",
					valid_data_quantity);
			String result = MembersFileReader.main();
			String expected="ファイル読み込みエラー";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void 異常系testMainStringArray2() {
		try {
			MemberFileReader MembersFileReader = new MemberFileReader("c:\\work_1\\member_20180403.csv",
					valid_data_quantity);
			String result = MembersFileReader.main();
			String expected="データ有効性エラー";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 異常系testMainStringArray3() {
		try {
			MemberFileReader MembersFileReader = new MemberFileReader("c:\\work_1\\member_20180404.csv",
					valid_data_quantity);
			String result = MembersFileReader.main();
			String expected="DB接続エラー";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 異常系testMainStringArray4() {
		try {
			MemberFileReader MembersFileReader = new MemberFileReader("c:\\work_1\\member_001.csv",
					valid_data_quantity);
			String result = MembersFileReader.main();
			String expected="201";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
