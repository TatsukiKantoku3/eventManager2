package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import com.TestDBAccess;

import domain.Account;

//メソッド名順で実行する
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class AccountDaoImplTest extends TestDBAccess {

	 //Exceptionがthrowされた場合の検証
	 @Rule
	 public ExpectedException thrown = ExpectedException.none();

	 private static String member_id;
	 private static AccountDao target;
	 private static List<Account> accountList;
	 private static  Account account;


	 /**
	 *
	 *クラスロード時にメンバーテーブルへ存在確認し
	 *メンバーがいなければ新規登録したメンバーIDでアカウントを登録します
	 *
	 * */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		target = DaoFactory.createAccountDao();

		InitialContext ctx = null;
		DataSource testds=null;

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

		// アカウントテーブルを全削除し、アカウント情報を書き換えるメンバー情報を設定
		try (Connection conn = testds.getConnection() ) {
				PreparedStatement stmt;
				String sql;

				//アカウントテーブル全件削除
				sql="TRUNCATE account";
				stmt=conn.prepareStatement(sql);
				stmt.executeUpdate();

				//メンバーテーブルのメンバーIDを取得する
				sql="select member_id from members";
				stmt=conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				//メンバーがいれば一件目をテスト対象にします
				if (rs.next()) {
					member_id=rs.getString("member_id");
				//メンバーが一件もなければテスト用に登録します
				}else {
					member_id="001";
					sql = "INSERT INTO members(member_id,name,kana,birthday,"
							+ "address,tel,hired,dep_id,position_type)"
							+ "VALUES(?,'test','テスト','2018-11-10','東京都','090-0000-0000','2010-04-02','1','2')";
					PreparedStatement stmtAcc = conn.prepareStatement(sql);
					stmtAcc.setString(1,member_id);
					stmtAcc.executeUpdate();
				}
		}
	}


	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	/**
	 *
	 *メソッド実行前にAccountとListをインスタンスします
	 * */
	@Before
	public void setUp() throws Exception {
		accountList=new ArrayList<Account>();
		account=new Account();
		//リフレクションでDataSourceをnullに設定したままであれば生成しなおします
		Field fld = target.getClass().getDeclaredField("ds");
	    fld.setAccessible(true);
		if((DataSource)fld.get(target)==null) {
			target = DaoFactory.createAccountDao();
		}
	}

	@After
	public void tearDown() throws Exception {


	}

	/**
	 *
	 *ターゲットに渡す正常データのセットです
	 *
	 * */
	private Account getNormalAccount() {
		account.setLoginId("test");
		account.setLoginPass("testpass");
		account.setAuthId(1);
		account.setMemberId(member_id);
		return account;
	}

	/**
	 *
	 *正常データをパラメタに設定して正常に登録します
	 *
	 * */
	@Test
	public void testInsertAcount_1_正常系() throws Exception {
		accountList.add(getNormalAccount());
		String result=target.insertAcount(accountList);
		assertThat(result, is("100"));
	}
	/**
	 *
	 *存在しないメンバーIDで登録しようとすることでエラーコードが返却されます
	 *
	 * */
	@Test
	public void testInsertAcount_2_異常系() throws Exception {

		account.setLoginId("112");
		account.setMemberId("112");
		account.setLoginPass("112");
		account.setAuthId(1);
		accountList.add(account);

		String result=target.insertAcount(accountList);
		assertThat(result, is("300"));
	}

	/**
	 *
	 * login_idを20桁以上にしてSQLSQLExceptionを発生させることで
	 * エラーコードが返却されます
	 * */
	@Test
	public void testInsertAcount_3_SQLException() throws Exception {
        account.setMemberId(member_id);
		account.setLoginId("1234567890123456789012"); //loginidが20桁以上
		accountList.add(account);
		String result=target.insertAcount(accountList);
		assertThat(result, is("300"));

	}



	/**
	 *
	 * DataSourceに間違えたリソースを参照してNullPointerExceptionを発生させます
	 * このメソッドはターゲットのDataSourceがnullになりますが
	 * setUp()でnull判定後にDataSourceを生成し、
	 * 後に実行されるメソッドには影響がないようにしています
	 *
	 * @throws Exception
	 *
	 * */
	@Test
	public void testInsertAcount_4_NullPointerException() throws Exception {
		//privateフィールドDataSource dsを操作可能にします
	    Field fld = target.getClass().getDeclaredField("ds");
	    fld.setAccessible(true);
	    InitialContext ctx=new InitialContext();
	    //DataSource dsにをセットします
		fld.set(target,(DataSource)ctx.lookup("java:comp/env/jdbc/eventdb"));

		//正常値を引数で渡します
		accountList.add(getNormalAccount());
		// 検証するExceptionの内容を設定
		thrown.expect(NullPointerException.class);

		target.insertAcount(accountList);


	}
	/**
	 *
	 * DataSourceをnullに設定してNullPointerExceptionを発生させます
	 * このメソッドはターゲットのDataSourceがnullになりますが
	 * setUp()でnull判定後にDataSourceを生成し、
	 * 後に実行されるメソッドには影響がないようにしています
	 *
	 * @throws Exception
	 *
	 * */
	@Test
	public void testInsertAcount_5_NullPointerException() throws Exception {
		//privateフィールドDataSource dsを操作可能にします
	    Field fld = target.getClass().getDeclaredField("ds");
	    fld.setAccessible(true);

	    //DataSource dsにnullをセットします
		fld.set(target,null);

		//正常値を引数で渡します
		accountList.add(getNormalAccount());
		// 検証するExceptionの内容を設定
		thrown.expect(NullPointerException.class);

		target.insertAcount(accountList);


	}


}
