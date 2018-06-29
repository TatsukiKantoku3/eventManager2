package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.TestDBAccess;

//メソッド名順で実行する
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class MemberFileReaderTest extends TestDBAccess {
	static final int valid_data_quantity = 9;
	static final MemberFileReader target = new MemberFileReader("c:\\work_1\\member_20180402.csv",
			valid_data_quantity);
	static final String MEMBER_ID = "105";
	static final String NAME = "山本葵";
	static final String KANA = "ヤマモトアオイ";
	static final String BIRTHDAY = "1995/12/10";
	static final String ADDRESS = "東京都新宿区飯田橋54-10-1";
	static final String TEL = "090-6433-1233";
	static final String ENTER = "4月1日";
	static final String DEP_ID = "2";
	static final String FALSE_MEMBER_ID = "123456789";
	static final String FALSE_MEMBER_ID2 = "あいうえお";
	static final String FALSE_NAME = "あああああいいいいいうううううえええええおおおおお"
			+ "かかかかかきききききくくくくくけけけけけこここここさ";
	static final String FALSE_KANA = "12345";
	static final String FALSE_KANA2 = "アアアアアイイイイイウウウウウエエエエエオオオオオカカカカカキキキキキククククク"
			+ "ケケケケケコココココサササササシシシシシスススススセセセセセソソソソソタタタタタチチチチチツツツツツテテテテテ"
			+ "トトトトトナナナナナ";
	static final String FALSE_BIRTHDAY = "12月10日";
	static final String FALSE_TEL = "0000-1122-3344";
	static final String FALSE_ENTER = "04/01";
	static final String FALSE_DEP_ID = "6";
	static final String FILE1="c:\\work\\member.csv";
	static final String FILE2="c:\\work_1\\member_20180403.csv";
	static final String FILE3="c:\\work_1\\member_20180404.csv";
	static final String FILE4="c:\\work_1\\member_001.csv";
	static final String EMPTY = "";
	static final String NULL = null;

	static DataSource ds;

	/**
	 * クラスを実行する前に一度だけDBと接続をします。
	 *
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InitialContext ctx = null;

		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/eventdb2");
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
	 * 各メソッドを実行する前に対象テーブルを空にします。
	 * @throws Exception
	 */
	@Before
	public void truncate() throws Exception {
		try (Connection con = ds.getConnection()) {
			PreparedStatement stmt;
			String sql;

			//全件削除
			sql = "TRUNCATE members";
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		}
	}

	/**
	 * データ有効性チェック
	 * 正常なデータを入力して戻り値がtrueであることを確認します。
	 */
	@Test
	public void 正常系testEnableLine() {
		String[] test = { EMPTY, MEMBER_ID, NAME, KANA, BIRTHDAY, ADDRESS, TEL, ENTER, DEP_ID };
		assertThat(target.enableLine(test), is(true));

	}

	/**
	 * データ有効性チェック
	 * 正常なデータを入力して戻り値がtrueであることを確認します。
	 */
	@Test
	public void 正常系testEnableLine2() {
		String[] test = { EMPTY, MEMBER_ID, NAME, EMPTY, BIRTHDAY, ADDRESS, TEL, ENTER, DEP_ID };
		assertThat(target.enableLine(test), is(true));

	}

	/**
	 * データの登録
	 *正常なデータの入ったファイルを読み込んでファイル有効性チェック、データ有効性チェック、
	 *DBへの登録を行い、正常系コード100が返ってくることを確認します。
	 *
	 */
	@Test
	public void 正常系testMainString() {
		try {
			String result = target.main();
			String expected = "100";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *メンバーIDに指定桁数異常の値を入力してエラーコードを確認します。
	 */
	@Test
	public void 異常系testEnableLine1() {
		String[] test = { EMPTY, FALSE_MEMBER_ID, NAME, KANA, BIRTHDAY, ADDRESS, TEL, ENTER, DEP_ID };
		target.enableLine(test);
		String expected = "205";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 * 名前に指定桁数を超える文字列を入力してエラーコードを確認します。
	 */
	@Test
	public void 異常系testEnableLine2() {
		String[] test = { EMPTY, MEMBER_ID, FALSE_NAME, KANA, BIRTHDAY, ADDRESS, TEL, ENTER, DEP_ID };
		target.enableLine(test);
		String expected = "205";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 *フリガナにカタカナ以外の文字を入力してエラーコードを確認します。
	 */
	@Test
	public void 異常系testEnableLine3() {
		String[] test = { EMPTY, MEMBER_ID, NAME, FALSE_KANA, BIRTHDAY, ADDRESS, TEL, ENTER, DEP_ID };
		target.enableLine(test);
		String expected = "204";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 * フリガナに指定桁数を超える文字列を入力してエラーコードを確認します。
	 */
	@Test
	public void 異常系testEnableLine4() {
		String[] test = { EMPTY, MEMBER_ID, NAME, FALSE_KANA2, BIRTHDAY, ADDRESS, TEL, ENTER, DEP_ID };
		target.enableLine(test);
		String expected = "205";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 * 誕生日に指定された形式ではない値を入力してエラーコードを確認します。
	 */
	@Test
	public void 異常系testEnableLine5() {
		String[] test = { EMPTY, MEMBER_ID, NAME, KANA, FALSE_BIRTHDAY, ADDRESS, TEL, ENTER, DEP_ID };
		target.enableLine(test);
		String expected = "200";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 * 電話番号に不正な値を入力してエラーコードを確認します。
	 */
	@Test
	public void 異常系testEnableLine6() {
		String[] test = { EMPTY, MEMBER_ID, NAME, KANA, BIRTHDAY, ADDRESS, FALSE_TEL, ENTER, DEP_ID };
		target.enableLine(test);
		String expected = "200";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 * 入社日に指定された形式以外の値を入力してエラーコードを確認する。
	 */
	@Test
	public void 異常系testEnableLine7() {
		String[] test = { EMPTY, MEMBER_ID, NAME, KANA, BIRTHDAY, ADDRESS, TEL, FALSE_ENTER, DEP_ID };
		target.enableLine(test);
		String expected = "200";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 * 所属部署IDに指定範囲を超える値を入力してエラーコードを確認する。
	 */
	@Test
	public void 異常系testEnableLine8() {
		String[] test = { EMPTY, MEMBER_ID, NAME, KANA, BIRTHDAY, ADDRESS, TEL, ENTER, FALSE_DEP_ID };
		target.enableLine(test);
		String expected = "200";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 * データの一部を空白にしてデータ有効性を確認し、エラーコードを確認する。
	 */
	@Test
	public void 異常系testEnableLine9() {
		String[] test = { EMPTY, MEMBER_ID, NAME, KANA, BIRTHDAY, ADDRESS, TEL, ENTER, EMPTY };
		target.enableLine(test);
		String expected = "203";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 *メンバーIDに半角英数字以外の値を入力してエラーコードを確認します。
	 */
	@Test
	public void 異常系testEnableLine10() {
		String[] test = { EMPTY, FALSE_MEMBER_ID2, NAME, KANA, BIRTHDAY, ADDRESS, TEL, ENTER, DEP_ID };
		target.enableLine(test);
		String expected = "205";
		assertThat(MemberFileReader.errorCode, is(expected));
	}

	/**
	 * ファイルの存在しないパスを指定して登録を実行し、エラーコードを確認する。
	 */
	@Test
	public void 異常系testMainString1() {
		try {
			MemberFileReader MembersFileReader = new MemberFileReader(FILE1,
					valid_data_quantity);
			String result = MembersFileReader.main();
			String expected = "214";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 不正なデータが入っているファイルを読み込んで、エラーコードを確認する。
	 */
	@Test
	public void 異常系testMainString2() {
		try {
			MemberFileReader MembersFileReader = new MemberFileReader(FILE2,
					valid_data_quantity);
			String result = MembersFileReader.main();
			String expected = "200";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重複が許可されていない項目に複数の同様なデータを登録しようとして、
	 * エラーコードを確認する。
	 */
	@Test
	public void 異常系testMainString3() {
		try {
			MemberFileReader MembersFileReader = new MemberFileReader(FILE3,
					valid_data_quantity);
			String result = MembersFileReader.main();
			String expected = "300";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ファイル名に含まれる日付を不正な値にして読み込み、エラーコードを確認する。
	 */
	@Test
	public void 異常系testMainString4() {
		try {
			MemberFileReader MembersFileReader = new MemberFileReader(FILE4,
					valid_data_quantity);
			String result = MembersFileReader.main();
			String expected = "201";
			assertThat(result, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
