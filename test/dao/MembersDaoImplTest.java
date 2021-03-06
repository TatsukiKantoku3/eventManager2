package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import com.TestDBAccess;

import domain.Members;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MembersDaoImplTest extends TestDBAccess {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	//前提条件
	final String NORMAL_MEMBER_ID1 = "001";
	final String NORMAL_MEMBER_ID2 = "002";
	final String NORMAL_MEMBER_ID3 = "003";
	final String NORMAL_MEMBER_ID4 = "100";
	final String NORMAL_MEMBER_ID5 = "12121";
	final String NORMAL_MEMBER_ID6 = "";
	final String NORMAL_MEMBER_NAME1 = "山本葵";
	final String NORMAL_MEMBER_NAME2 = "中村悠真";
	final String NORMAL_MEMBER_NAME3 = "小林蓮";
	final String NORMAL_MEMBER_NAME4 = "田中太郎";
	final String NORMAL_MEMBER_NAME5 = "坂本";
	final String NORMAL_MEMBER_KANA1 = "ヤマモトアオイ";
	final String NORMAL_DEPARTMENT1 = "人事";
	final String NORMAL_ADDRESS1 = "東京都新宿区飯田橋54-10-1";
	final String NORMAL_TEL = "090-6433-1233";
	final String NORMAL_LOGIN_ID1 = "aoi";
	final String NORMAL_LOGIN_PASS = "pass";
	final String NORMAL_HASH_PASS = "$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC";
	final String NORMAL_HIRE_DAY = "2018/04/02";
	final String NORMAL_BIRTHDAY = "1995/12/10";
	//求める数値
	final int NUMBER0 = 0;
	final int NUMBER1 = 1;
	final int NUMBER2 = 2;
	final int NUMBER3 = 3;
	final int NUMBER4 = 4;
	final int NUMBER5 = 5;

	//insertでの入力値 member
	final String INSERT_DATE = "2016/5/2";
	final String INSERT_MEMBER_ID = "123a";
	final String INSERT_MEMBER_NAME = "高橋幸助";
	final String INSERT_MEMBER_KANA = "タカハシコウスケ";
	final String INSERT_MEMBER_ADDRESS = "北海道函館市";
	final String INSERT_MEMBER_TEL = "090-1122-4921";

	final String INSERT_LOGIN_ID = "paser";
	//insert account
	final String INSERT_ACCOUNT_LOGIN_ID = "BBCCDD";
	final String INSERT_ACCOUNT_LOGIN_PASS = "pass";
	final int INSERT_ACCOUNT_AUTH_ID = 2;

	//update member
	final String UP_MEMBER_ID = "T2018A";
	final String UP_MEMBER_NAME = "斉藤高志";
	final String UP_MEMBER_KANA = "サイトウタカシ";
	final String UP_ADDRESS = "沖縄県那覇市";
	final String UP_TEL = "080-1212-1212";
	final String UP_LOGIN_ID = "saitou";
	final String UP_DEPARTMENT = "人事";
	final String UP_DATE = "2016-05-02";

	final String UP_LOGIN_PASS = "pass";

	//異常系
	final String FAULT_MEMBER_ID1 = "AABBCCDD";
	final String FSULT_LOGIN_ID = "SINAGAWA";
	final String FSULT_LOGIN_PASS = "TOKYO";
	final int FAULT_NUMBER = 10000;

	//	public void setUp() throws Exception {
	//	    // JNDI準備
	//
	//	}

	static DataSource ds;
	static Members member;
	static MembersDao target = DaoFactory.createMembersDao();

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

		try (Connection con = ds.getConnection()) {
			PreparedStatement stmt;
			String sql;
			//全件削除

			sql = "TRUNCATE members";
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();

			sql = "TRUNCATE account";
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();

			sql = "TRUNCATE department";
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();

			//テストデータを登録
			sql = "INSERT INTO `members` VALUES ('001','山本葵','ヤマモトアオイ','1995-12-10','東京都新宿区飯田橋54-10-1','090-6433-1233','2018-04-02',1,0,'aoi'),"
					+ "('002','中村悠真','ナカムラユウマ','1995-12-11','東京都新宿区飯田橋54-10-1','090-6433-1234','2018-04-02',1,0,'yuma'),"
					+ "('003','小林蓮','コバヤシレン','1995-12-12','東京都新宿区飯田橋54-10-1','090-6433-1235','2018-04-02',1,NULL,'ren'),"
					+ "('100','田中太郎','タナカタロウ','1990-12-12','東京都新宿区飯田橋54-10-1','090-6433-1200','2010-04-02',1,NULL,'taro'),"
					+ "('12121','坂本','サカモト','2016-05-02','都会','090-1122-4921','2016-05-02',1,0,'pass'); ";
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();

			sql = "INSERT INTO `account` VALUES ('aoi','$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC',1),('ren','$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC',2),('takaha','$2a$10$CoGBg/6zSKJQKwdvZPmRbOfHrIr.fseKjYJO4TRIrCGoF3J4r3vFe',1),('taro','$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC',1),('test','pass',1),('yuma','$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC',1);";
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();

			sql = "INSERT INTO `department` VALUES (1,'人事',4),(2,'経理',2),(3,'総務',2),(4,'営業',3),(5,'開発',4);";
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();

		}
	}

	@Before
	public void setUp() throws Exception {
		member = new Members();
		//リフレクションでDataSourceをnullに設定したままであれば生成しなおします
		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);
		if ((DataSource) fld.get(target) == null) {
			target = DaoFactory.createMembersDao();
		}
	}

	@AfterClass
	public static void DBsetAfter() throws SQLException {
		try (Connection con = ds.getConnection()) {
			PreparedStatement stmt;
			String sql;
			//全件削除

			sql = "TRUNCATE members";
			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			sql = "TRUNCATE account";
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		}
	}

	@Test
	public void testFindAllInt正常() throws Exception {

		List<Members> memall = target.findAll(NUMBER0);
		assertThat(memall.get(NUMBER0).getMember_id(), is(NORMAL_MEMBER_ID1));
		assertThat(memall.get(NUMBER1).getMember_id(), is(NORMAL_MEMBER_ID2));
		assertThat(memall.get(NUMBER2).getMember_id(), is(NORMAL_MEMBER_ID3));
		assertThat(memall.get(NUMBER3).getMember_id(), is(NORMAL_MEMBER_ID4));
		assertThat(memall.get(NUMBER4).getMember_id(), is(NORMAL_MEMBER_ID5));
	}

	@Test
	public void testFindAllInt異常DB() throws Exception {
		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);
		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.findAll(1);
	}

	@Test
	public void testFindfive正常() throws Exception {

		List<Members> memall = target.findAll(NUMBER0);

		List<Members> memfiv = target.findfive(memall);

		assertThat(memfiv.get(NUMBER0).getName(), is(NORMAL_MEMBER_NAME1));
		assertThat(memfiv.get(NUMBER1).getName(), is(NORMAL_MEMBER_NAME2));
		assertThat(memfiv.get(NUMBER2).getName(), is(NORMAL_MEMBER_NAME3));
		assertThat(memfiv.get(NUMBER3).getName(), is(NORMAL_MEMBER_NAME4));
		assertThat(memfiv.get(NUMBER4).getName(), is(NORMAL_MEMBER_NAME5));

	}

	@Test
	public void testFindfive異常1() throws Exception {

		List<Members> memall;

		memall = target.findAll(FAULT_NUMBER);
		List<Members> memfiv = target.findfive(memall);

		assertThat(memfiv.isEmpty(), is(true));

	}

	@Test
	public void testFindfive異常2() throws Exception {
		List<Members> memall = new ArrayList<>();
		member.setMember_id("member_id");
		memall.add(0, member);
		List<Members> memfiv = target.findfive(memall);
		assertThat(memfiv.isEmpty(), is(true));

	}

	@Test
	public void testFindfive異常DB() throws Exception {
		List<Members> memberList = new ArrayList<>();
		member = new Members();
		member.setMember_id("member_id");
		memberList.add(0, member);

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.findfive(memberList);
	}

	@Test
	public void testFindById正常() throws Exception {

		Members findmember = target.findById(NORMAL_MEMBER_ID1);
		Timestamp date = new Timestamp(new SimpleDateFormat("yyyy/MM/dd")
				.parse(NORMAL_HIRE_DAY).getTime());
		Timestamp birth = new Timestamp(new SimpleDateFormat("yyyy/MM/dd")
				.parse(NORMAL_BIRTHDAY).getTime());
		assertThat(findmember.getMember_id(), is(NORMAL_MEMBER_ID1));
		assertThat(findmember.getName(), is(NORMAL_MEMBER_NAME1));
		assertThat(findmember.getKana(), is(NORMAL_MEMBER_KANA1));
		assertThat(findmember.getDepartment(), is(NORMAL_DEPARTMENT1));
		assertThat(findmember.getAddress(), is(NORMAL_ADDRESS1));
		assertThat(findmember.getTel(), is(NORMAL_TEL));
		assertThat(findmember.getBirthday(), is(birth));
		assertThat(findmember.getPosition_type(), is((Integer) NUMBER0));
		assertThat(findmember.getHired(), is(date));
		assertThat(findmember.getLogin_id(), is(NORMAL_LOGIN_ID1));

	}

	@Test
	public void testFindById異常() {

		try {
			Members findmember = target.findById(FAULT_MEMBER_ID1);
			assertThat(findmember.getName(), is(NORMAL_MEMBER_NAME1));

		} catch (Exception e) {

			assertThat(e.getMessage(), equalTo(null));
		}

	}

	@Test
	public void testFindById異常DB() throws Exception {

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.findById("aoi");
	}

	@Test
	public void testInsert正常() throws Exception {

		member = new Members();
		try {
			DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date1 = dateTimeFormat.parse(INSERT_DATE);
			member.setMember_id(INSERT_MEMBER_ID);
			member.setName(INSERT_MEMBER_NAME);
			member.setKana(INSERT_MEMBER_KANA);
			member.setDep_id(NUMBER1);
			member.setAddress(INSERT_MEMBER_ADDRESS);
			member.setTel(INSERT_MEMBER_TEL);
			member.setBirthday(date1);
			member.setPosition_type(NUMBER0);
			member.setHired(date1);
			member.setLogin_id(INSERT_LOGIN_ID);
			target.insert(member);

			Members insertMember = target.findById(INSERT_MEMBER_ID);
			String member_id = insertMember.getMember_id();
			String name = insertMember.getName();
			String kana = insertMember.getKana();
			int dep_id = insertMember.getDep_id();
			String address = insertMember.getAddress();
			String tel = insertMember.getTel();
			Date birth = insertMember.getBirthday();
			Integer posi = insertMember.getPosition_type();
			Date hired = insertMember.getHired();
			String login_id = insertMember.getLogin_id();

			assertThat(member_id, is(INSERT_MEMBER_ID));
			assertThat(name, is(INSERT_MEMBER_NAME));
			assertThat(kana, is(INSERT_MEMBER_KANA));
			assertThat(dep_id, is((Integer) NUMBER0));
			assertThat(address, is(INSERT_MEMBER_ADDRESS));
			assertThat(tel, is(INSERT_MEMBER_TEL));
			assertThat(birth, is(date1));
			assertThat(posi, is((Integer) NUMBER0));
			assertThat(hired, is(date1));
			assertThat(login_id, is(INSERT_LOGIN_ID));
			target.delete(member);

		} catch (Exception e) {

			target.delete(member);
		}
	}

	@Test
	public void testInsert異常() {

		member = new Members();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1;
		try {
			date1 = dateTimeFormat.parse("INSERT_DATE");
			member.setMember_id(INSERT_MEMBER_ID);
			member.setName(INSERT_MEMBER_NAME);
			member.setKana(INSERT_MEMBER_KANA);
			member.setDep_id(NUMBER1);
			member.setAddress(INSERT_MEMBER_ADDRESS);
			member.setTel(INSERT_MEMBER_TEL);
			member.setBirthday(date1);
			member.setPosition_type(NUMBER0);
			member.setHired(date1);
			member.setLogin_id(NORMAL_LOGIN_ID1);
			target.insert(member);

			Members insertMember = target.findById(INSERT_MEMBER_ID);

			assertThat(insertMember.getMember_id(), is(INSERT_MEMBER_ID));
			assertThat(insertMember.getName(), is(INSERT_MEMBER_NAME));
			assertThat(insertMember.getKana(), is(INSERT_MEMBER_KANA));
			assertThat(insertMember.getDep_id(), is((Integer) NUMBER0));
			assertThat(insertMember.getAddress(), is(INSERT_MEMBER_ADDRESS));
			assertThat(insertMember.getTel(), is(INSERT_MEMBER_TEL));
			assertThat(insertMember.getBirthday(), is(date1));
			assertThat(insertMember.getPosition_type(), is((Integer) NUMBER0));
			assertThat(insertMember.getHired(), is(date1));
			assertThat(insertMember.getLogin_id(), is(NORMAL_LOGIN_ID1));
			target.delete(member);

		} catch (Exception e) {

			assertThat(e.getMessage(), equalTo("Unparseable date: \"INSERT_DATE\""));

		}

	}

	@Test
	public void testInsert異常DB() throws Exception {
		member = new Members();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(INSERT_MEMBER_ID);
		member.setName(INSERT_MEMBER_NAME);
		member.setKana(INSERT_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(INSERT_MEMBER_ADDRESS);
		member.setTel(INSERT_MEMBER_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setHired(date1);
		member.setLogin_id(INSERT_LOGIN_ID);

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.insert(member);
	}

	@Test
	public void testInsertacount正常() throws Exception {

		member = new Members();

		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		int line = target.insertacount(member);

		assertThat(line, equalTo(NUMBER1));

		target.deleteAccount(member);
	}

	@Test
	public void testInsertacount異常() {

		member = new Members();

		member.setLogin_id(NORMAL_LOGIN_ID1);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		try {
			target.insertacount(member);
		} catch (Exception e) {

			assertThat(e.getMessage(), equalTo("Duplicate entry 'aoi' for key 'login_id'"));

		}

	}

	@Test
	public void testInsertacount異常DB() throws Exception {
		member = new Members();

		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);

		target.insertacount(member);
	}

	@Test
	public void testFindByLoginIdAndLoginPass正常() throws Exception {

		member = new Members();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(INSERT_MEMBER_ID);
		member.setName(INSERT_MEMBER_NAME);
		member.setKana(INSERT_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(INSERT_MEMBER_ADDRESS);
		member.setTel(INSERT_MEMBER_TEL);
		member.setBirthday(date1);
		//member.setPosition_type(NUMBER0);
		member.setHired(date1);
		member.setLogin_id(INSERT_LOGIN_ID);
		target.insert(member);

		Members checkmem = target.findByLoginIdAndLoginPass(INSERT_LOGIN_ID, "");
		assertThat(checkmem.getMember_id(), is(INSERT_MEMBER_ID));
		assertThat(checkmem.getName(), is(INSERT_MEMBER_NAME));

		target.delete(member);

	}

	@Test
	public void testFindByLoginIdAndLoginPass異常() throws Exception {

		Members checkmem = target.findByLoginIdAndLoginPass(INSERT_ACCOUNT_LOGIN_ID, "");
		assertThat(checkmem, is(nullValue()));

	}

	@Test
	public void testFindByLoginIdAndLoginPass異常DB() throws Exception {
		member = new Members();

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.findByLoginIdAndLoginPass("INSERT_LOGIN_ID", "");
	}

	@Test
	public void testLogin正常() throws Exception {

		member = new Members();

		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(NORMAL_HASH_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		target.insertacount(member);

		Members account = target.login(INSERT_ACCOUNT_LOGIN_ID, INSERT_ACCOUNT_LOGIN_PASS);
		assertThat(account.getLogin_id(), is(INSERT_ACCOUNT_LOGIN_ID));
		assertThat(account.getLogin_pass(), is(NORMAL_HASH_PASS));
		assertThat(account.getAuth_id(), is((Integer) INSERT_ACCOUNT_AUTH_ID));

		target.deleteAccount(member);
	}

	@Test
	public void testLogin異常1() {

		try {
			Members account = target.login(FSULT_LOGIN_ID, FSULT_LOGIN_PASS);
			assertThat(account.getLogin_id(), is(FSULT_LOGIN_ID));
		} catch (Exception e) {
			assertThat(e.getMessage(), equalTo(null));

		}
	}

	@Test
	public void testLogin異常2() throws Exception {
		member = new Members();
		member.setLogin_id(INSERT_LOGIN_ID);
		member.setLogin_pass(NORMAL_HASH_PASS);
		member.setAuth_id(1);

		target.insertacount(member);

		Members result = target.login(INSERT_LOGIN_ID, FSULT_LOGIN_PASS);
		assertThat(result, is(nullValue()));
		target.deleteAccount(member);

	}

	@Test
	public void testLogin異常DB() throws Exception {
		member = new Members();

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.login(INSERT_ACCOUNT_LOGIN_ID, INSERT_ACCOUNT_LOGIN_PASS);
	}

	@Test
	public void testUpdate正常() throws Exception {

		member = new Members();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(INSERT_MEMBER_ID);
		member.setName(INSERT_MEMBER_NAME);
		member.setKana(INSERT_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(INSERT_MEMBER_ADDRESS);
		member.setTel(INSERT_MEMBER_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setHired(date1);
		member.setLogin_id(INSERT_LOGIN_ID);
		target.insert(member);

		member.setMember_id(UP_MEMBER_ID);
		member.setName(UP_MEMBER_NAME);
		member.setKana(UP_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(UP_ADDRESS);
		member.setTel(UP_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setLogin_id(UP_LOGIN_ID);
		member.setOldmember_id(INSERT_MEMBER_ID);

		target.update(member);

		Members upMem = target.findById(UP_MEMBER_ID);

		assertThat(upMem.getMember_id(), is(UP_MEMBER_ID));
		assertThat(upMem.getName(), is(UP_MEMBER_NAME));
		assertThat(upMem.getKana(), is(UP_MEMBER_KANA));
		assertThat(upMem.getDepartment(), is(UP_DEPARTMENT));
		assertThat(upMem.getAddress(), is(UP_ADDRESS));
		assertThat(upMem.getTel(), is(UP_TEL));
		assertThat(upMem.getBirthday().toString(), is(UP_DATE));
		assertThat(upMem.getPosition_type(), is((Integer) NUMBER0));
		assertThat(upMem.getLogin_id(), is(UP_LOGIN_ID));

		target.delete(member);

	}

	@Test
	public void testUpdate異常() {

		member = new Members();
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date date1 = dateTimeFormat.parse(INSERT_DATE);
			member.setMember_id(UP_MEMBER_ID);
			member.setName(UP_MEMBER_NAME);
			member.setKana(UP_MEMBER_KANA);
			member.setDep_id(NUMBER1);
			member.setAddress(UP_ADDRESS);
			member.setTel(UP_TEL);
			member.setBirthday(date1);
			member.setPosition_type(NUMBER0);
			member.setLogin_id(UP_LOGIN_ID);
			member.setOldmember_id(INSERT_MEMBER_ID);

			target.update(member);
		} catch (Exception e) {
			assertThat(e.getMessage(), equalTo(null));

		}

	}

	@Test
	public void testUpdate異常DB() throws Exception {
		member = new Members();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(UP_MEMBER_ID);
		member.setName(UP_MEMBER_NAME);
		member.setKana(UP_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(UP_ADDRESS);
		member.setTel(UP_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setLogin_id(UP_LOGIN_ID);
		member.setOldmember_id(INSERT_MEMBER_ID);

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.update(member);
	}

	@Test
	public void testUpdateaccount正常() throws Exception {

		member = new Members();

		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		target.insertacount(member);

		member.setLogin_id(UP_LOGIN_ID);
		member.setLogin_pass(UP_LOGIN_PASS);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);

		int line = target.updateaccount(member);

		assertThat(line, is(NUMBER1));

		target.deleteAccount(member);
	}

	@Test
	public void testUpdateaccount異常() throws Exception {

		member = new Members();

		member.setLogin_id(UP_LOGIN_ID);
		member.setLogin_pass(UP_LOGIN_PASS);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);

		int line = target.updateaccount(member);

		assertThat(line, is(NUMBER0));

		target.deleteAccount(member);
	}

	@Test
	public void testUpdateaccount異常DB() throws Exception {
		member = new Members();
		member.setLogin_id(UP_LOGIN_ID);
		member.setLogin_pass(UP_LOGIN_PASS);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.updateaccount(member);
	}

	@Test
	public void testUpdateWhithoutPass正常10() throws Exception {

		member = new Members();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(INSERT_MEMBER_ID);
		member.setName(INSERT_MEMBER_NAME);
		member.setKana(INSERT_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(INSERT_MEMBER_ADDRESS);
		member.setTel(INSERT_MEMBER_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setHired(date1);
		member.setLogin_id(INSERT_LOGIN_ID);
		target.insert(member);

		member.setMember_id(UP_MEMBER_ID);
		member.setName(UP_MEMBER_NAME);
		member.setKana(UP_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(UP_ADDRESS);
		member.setTel(UP_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setLogin_id(UP_LOGIN_ID);
		member.setOldmember_id(INSERT_MEMBER_ID);

		target.updateWhithoutPass(member);

		Members upMem = target.findById(UP_MEMBER_ID);

		assertThat(upMem.getMember_id(), is(UP_MEMBER_ID));
		assertThat(upMem.getName(), is(UP_MEMBER_NAME));
		assertThat(upMem.getKana(), is(UP_MEMBER_KANA));
		assertThat(upMem.getDepartment(), is(UP_DEPARTMENT));
		assertThat(upMem.getAddress(), is(UP_ADDRESS));
		assertThat(upMem.getTel(), is(UP_TEL));
		assertThat(upMem.getBirthday().toString(), is(UP_DATE));
		assertThat(upMem.getPosition_type(), is((Integer) NUMBER0));
		assertThat(upMem.getLogin_id(), is(UP_LOGIN_ID));

		target.delete(member);
	}

	@Test
	public void testUpdateWhithoutPass異常9() {

		member = new Members();

		try {
			DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date1 = dateTimeFormat.parse(INSERT_DATE);
			member.setMember_id(UP_MEMBER_ID);
			member.setName(UP_MEMBER_NAME);
			member.setKana(UP_MEMBER_KANA);
			member.setDep_id(NUMBER1);
			member.setAddress(UP_ADDRESS);
			member.setTel(UP_TEL);
			member.setBirthday(date1);
			member.setPosition_type(NUMBER0);
			member.setLogin_id(UP_LOGIN_ID);
			member.setOldmember_id(INSERT_MEMBER_ID);

			target.updateWhithoutPass(member);

			Members upMem = target.findById(UP_MEMBER_ID);
			assertThat(upMem.getMember_id(), is(UP_MEMBER_ID));
		} catch (Exception e) {

			assertThat(e.getMessage(), equalTo(null));
		}
	}

	@Test
	public void testUpdateWhithoutPass異常DB() throws Exception {
		member = new Members();
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(UP_MEMBER_ID);
		member.setName(UP_MEMBER_NAME);
		member.setKana(UP_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(UP_ADDRESS);
		member.setTel(UP_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setLogin_id(UP_LOGIN_ID);
		member.setOldmember_id(INSERT_MEMBER_ID);
		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.updateWhithoutPass(member);

	}

	@Test
	public void testUpdateAccountWhithoutPass正常() throws Exception {

		member = new Members();

		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		target.insertacount(member);

		member.setLogin_id(UP_LOGIN_ID);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);

		int line = target.updateAccountWhithoutPass(member);
		assertThat(line, is(NUMBER1));

		target.deleteAccount(member);
	}

	@Test
	public void testUpdateAccountWhithoutPass異常() throws Exception {

		member = new Members();

		member.setLogin_id(UP_LOGIN_ID);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);

		int line = target.updateAccountWhithoutPass(member);
		assertThat(line, is(NUMBER0));

		target.deleteAccount(member);
	}

	@Test
	public void testUpdateAccountWhithoutPass異常DB() throws Exception {
		member = new Members();
		member.setLogin_id(UP_LOGIN_ID);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);
		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.updateAccountWhithoutPass(member);
	}

	@Test
	public void testDelete正常() throws Exception {

		member = new Members();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(INSERT_MEMBER_ID);
		member.setName(INSERT_MEMBER_NAME);
		member.setKana(INSERT_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(INSERT_MEMBER_ADDRESS);
		member.setTel(INSERT_MEMBER_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setHired(date1);
		member.setLogin_id(INSERT_LOGIN_ID);
		target.insert(member);

		int line = target.delete(member);

		assertThat(line, is(NUMBER1));

	}

	@Test
	public void testDelete異常() throws Exception {

		member = new Members();
		int line = target.delete(member);
		assertThat(line, is(NUMBER0));

	}

	@Test
	public void testDelete異常DB() throws Exception {
		member = new Members();
		member.setLogin_id("login_id");

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.delete(member);
	}

	@Test
	public void testDeleteAccount正常13() throws Exception {

		member = new Members();

		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		target.insertacount(member);

		int line = target.deleteAccount(member);

		assertThat(line, is(NUMBER1));
	}

	@Test
	public void testDeleteAccount異常12() throws Exception {

		member = new Members();

		int line = target.deleteAccount(member);

		assertThat(line, is(NUMBER0));
	}

	@Test
	public void testDeleteAccount異常DB() throws Exception {
		member = new Members();

		member.setLogin_id("login_id");
		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.deleteAccount(member);
	}

	@Test
	public void testCountAll正常() throws Exception {

		int result = target.countAll();
		assertThat(result, is(5));
	}

	@Test
	public void testCountAll異常DB() throws Exception {
		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);
		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.countAll();
	}

	@Test
	public void testCheckLoginId正常1() throws Exception {

		boolean result = target.CheckLoginId(NORMAL_LOGIN_ID1);

		assertThat(result, is(false));
	}

	@Test
	public void testCheckLoginId正常2() throws Exception {

		boolean result = target.CheckLoginId("login_id");
		assertThat(result, is(true));

	}

	@Test
	public void testCheckLoginId異常DB() throws Exception {

		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);
		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.CheckLoginId("login_id");

	}

	@Test
	public void testinsertMast正常() throws Exception {

		member = new Members();
		List<Members> MemList = new ArrayList<>();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(INSERT_MEMBER_ID);
		member.setName(INSERT_MEMBER_NAME);
		member.setKana(INSERT_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(INSERT_MEMBER_ADDRESS);
		member.setTel(INSERT_MEMBER_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setHired(date1);
		member.setLogin_id(INSERT_LOGIN_ID);
		MemList.add(0, member);

		String result = target.insertMast(MemList);
		assertThat(result, is("100"));

		target.delete(member);
	}

	@Test
	public void testinsertMast異常() throws Exception {

		member = new Members();
		List<Members> MemList = new ArrayList<>();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(INSERT_MEMBER_ID);
		member.setName(INSERT_MEMBER_NAME);
		member.setKana(INSERT_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(INSERT_MEMBER_ADDRESS);
		member.setTel(INSERT_MEMBER_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setHired(date1);
		member.setLogin_id(INSERT_LOGIN_ID);
		MemList.add(0, member);
		MemList.add(1, member);
		String result = target.insertMast(MemList);
		assertThat(result, is("300"));

		target.delete(member);
	}

	@Test
	public void testinsertMast異常DB() throws Exception {
		member = new Members();
		List<Members> MemList = new ArrayList<>();

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = dateTimeFormat.parse(INSERT_DATE);
		member.setMember_id(INSERT_MEMBER_ID);
		member.setName(INSERT_MEMBER_NAME);
		member.setKana(INSERT_MEMBER_KANA);
		member.setDep_id(NUMBER1);
		member.setAddress(INSERT_MEMBER_ADDRESS);
		member.setTel(INSERT_MEMBER_TEL);
		member.setBirthday(date1);
		member.setPosition_type(NUMBER0);
		member.setHired(date1);
		member.setLogin_id(INSERT_LOGIN_ID);
		MemList.add(0, member);
		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);

		target.insertMast(MemList);
	}

	@Test
	public void testCheckLoginPass正常() throws Exception {
		member = new Members();

		member.setLogin_pass(UP_LOGIN_PASS);
		member.setLogin_id(INSERT_LOGIN_ID);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);
		target.insertacount(member);

		member = new Members();
		member.setLogin_id(INSERT_LOGIN_ID);
		member.setLogin_pass(UP_LOGIN_PASS);
		String result = target.CheckLoginPass(member);
		assertThat(result, is("100"));

		target.deleteAccount(member);

	}

	@Test
	public void testCheckLoginPass異常() throws Exception {
		member = new Members();
		member.setLogin_id(FSULT_LOGIN_ID);
		member.setLogin_pass(UP_LOGIN_PASS);
		String result = target.CheckLoginPass(member);
		assertThat(result, is("300"));

		target.deleteAccount(member);

	}

	@Test
	public void testCheckLoginPass異常DB() throws Exception {
		member = new Members();

		member.setLogin_id(INSERT_LOGIN_ID);
		member.setLogin_pass(UP_LOGIN_PASS);
		Field fld = target.getClass().getDeclaredField("ds");
		fld.setAccessible(true);

		fld.set(target, null);
		thrown.expect(NullPointerException.class);
		target.CheckLoginPass(member);
	}

}
