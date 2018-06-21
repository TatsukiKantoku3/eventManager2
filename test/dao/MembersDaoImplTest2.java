package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;

import domain.Members;

public class MembersDaoImplTest2 extends TestDBAccess{


	//前提条件
	final String NORMAL_MEMBER_ID1="001";
	final String NORMAL_MEMBER_ID2="002";
	final String NORMAL_MEMBER_ID3="003";
	final String NORMAL_MEMBER_ID4="100";
	final String NORMAL_MEMBER_ID5="12121";
	final String NORMAL_MEMBER_ID6="";
	final String NORMAL_MEMBER_NAME1="山本葵";
	final String NORMAL_MEMBER_NAME2="中村悠真";
	final String NORMAL_MEMBER_NAME3="小林蓮";
	final String NORMAL_MEMBER_NAME4="田中太郎";
	final String NORMAL_MEMBER_NAME5="坂本";
	final String NORMAL_MEMBER_KANA1="ヤマモトアオイ";
	final String NORMAL_DEPARTMENT1="人事";
	final String NORMAL_ADDRESS1="東京都新宿区飯田橋54-10-1";
	final String NORMAL_TEL="090-6433-1233";
	final String NORMAL_LOGIN_ID1="aoi";
	final String NORMAL_LOGIN_PASS="pass";
	final String NORMAL_HASH_PASS="$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC";
	final String NORMAL_HIRE_DAY="2018/04/02";
	final String NORMAL_BIRTHDAY="1995/12/10";
	//求める数値
	final int NUMBER0=0;
	final int NUMBER1=1;
	final int NUMBER2=2;
	final int NUMBER3=3;
	final int NUMBER4=4;
	final int NUMBER5=5;



	//insertでの入力値 member
	final String INSERT_DATE="2016/5/2";
	final String INSERT_MEMBER_ID="123a";
	final String INSERT_MEMBER_NAME="高橋幸助";
	final String INSERT_MEMBER_KANA="タカハシコウスケ";
	final String INSERT_MEMBER_ADDRESS="北海道函館市";
	final String INSERT_MEMBER_TEL="090-1122-4921";

	final String INSERT_LOGIN_ID="paser";
	//insert account
	final String INSERT_ACCOUNT_LOGIN_ID="BBCCDD";
	final String INSERT_ACCOUNT_LOGIN_PASS="pass";
	final int INSERT_ACCOUNT_AUTH_ID=2;

	//update member
	final String UP_MEMBER_ID="T2018A";
	final String UP_MEMBER_NAME="斉藤高志";
	final String UP_MEMBER_KANA="サイトウタカシ";
	final String UP_ADDRESS="沖縄県那覇市";
	final String UP_TEL="080-1212-1212";
	final String UP_LOGIN_ID="saitou";
	final String UP_DEPARTMENT="人事";
	final String UP_DATE="2016-05-02";

	final String UP_LOGIN_PASS="pass";

	//異常系
	final String FAULT_MEMBER_ID1="AABBCCDD";
	final String FSULT_LOGIN_ID="SINAGAWA";
	final String FSULT_LOGIN_PASS="TOKYO";
	final int FAULT_NUMBER=10000;

	//	public void setUp() throws Exception {
	//	    // JNDI準備
	//
	//	}

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



//		try {
//			JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
//		} catch (NamingException | IOException e) {
//
//			e.printStackTrace();
//		};
		try (Connection con = ds.getConnection()){
//				"jdbc:mysql://127.0.0.1:3306/eventdb2?useUnicode=true&characterEncoding=utf8&useSSL=true", "root",
//				"rootpass")){
			PreparedStatement stmt;
			String sql;
			//全件削除

			 sql="TRUNCATE members";
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();

			sql="TRUNCATE account";
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();

			//テストデータを登録
			sql=	"INSERT INTO `members` VALUES ('001','山本葵','ヤマモトアオイ','1995-12-10','東京都新宿区飯田橋54-10-1','090-6433-1233','2018-04-02',1,0,'aoi'),"
					+ "('002','中村悠真','ナカムラユウマ','1995-12-11','東京都新宿区飯田橋54-10-1','090-6433-1234','2018-04-02',1,0,'yuma'),"
					+ "('003','小林蓮','コバヤシレン','1995-12-12','東京都新宿区飯田橋54-10-1','090-6433-1235','2018-04-02',1,NULL,'ren'),"
					+ "('100','田中太郎','タナカタロウ','1990-12-12','東京都新宿区飯田橋54-10-1','090-6433-1200','2010-04-02',1,NULL,'taro'),"
					+ "('12121','坂本','サカモト','2016-05-02','都会','090-1122-4921','2016-05-02',1,0,'pass'); ";
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();

			sql="INSERT INTO `account` VALUES ('aoi','$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC',1),('ren','$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC',2),('takaha','$2a$10$CoGBg/6zSKJQKwdvZPmRbOfHrIr.fseKjYJO4TRIrCGoF3J4r3vFe',1),('taro','$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC',1),('test','pass',1),('yuma','$2a$10$Yqwi/LAApvFZ8W4g3OM9AeEBy1gLOathGPuBD7yKqhx1jKmzpIBtC',1);";
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();

		}
	}


	@AfterClass
	public static void DBsetAfter() throws SQLException {
		try (Connection con = ds.getConnection()){
		PreparedStatement stmt;
		String sql;
		//全件削除

		 sql="TRUNCATE members";
		stmt=con.prepareStatement(sql);

		stmt.executeUpdate();
		sql="TRUNCATE account";
		stmt=con.prepareStatement(sql);
		stmt.executeUpdate();
		}
	}
	@Test
	public void testFindAllInt正常1() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();

		List<Members> memall=membersDao.findAll(NUMBER0);
		assertThat(memall.get(NUMBER0).getMember_id(),is(NORMAL_MEMBER_ID1));
		assertThat(memall.get(NUMBER1).getMember_id(),is(NORMAL_MEMBER_ID2));
		assertThat(memall.get(NUMBER2).getMember_id(),is(NORMAL_MEMBER_ID3));
		assertThat(memall.get(NUMBER3).getMember_id(),is(NORMAL_MEMBER_ID4));
		assertThat(memall.get(NUMBER4).getMember_id(),is(NORMAL_MEMBER_ID5));
	}



	@Test
	public void testFindfive正常2() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		List<Members> memall=membersDao.findAll(NUMBER0);

		List<Members> memfiv=membersDao.findfive(memall);

		assertThat(memfiv.get(NUMBER0).getName(),is(NORMAL_MEMBER_NAME1));
		assertThat(memfiv.get(NUMBER1).getName(),is(NORMAL_MEMBER_NAME2));
		assertThat(memfiv.get(NUMBER2).getName(),is(NORMAL_MEMBER_NAME3));
		assertThat(memfiv.get(NUMBER3).getName(),is(NORMAL_MEMBER_NAME4));
		assertThat(memfiv.get(NUMBER4).getName(),is(NORMAL_MEMBER_NAME5));

	}



	@Test
	public void testFindById正常3() throws Exception {

		MembersDao membersDao = DaoFactory.createMembersDao();

		Members findmember=membersDao.findById(NORMAL_MEMBER_ID1);
		Timestamp date = new Timestamp(new SimpleDateFormat("yyyy/MM/dd")
				.parse(NORMAL_HIRE_DAY).getTime());
		Timestamp birth = new Timestamp(new SimpleDateFormat("yyyy/MM/dd")
				.parse(NORMAL_BIRTHDAY).getTime());
		assertThat(findmember.getMember_id(),is(NORMAL_MEMBER_ID1));
		assertThat(findmember.getName(),is(NORMAL_MEMBER_NAME1));
		assertThat(findmember.getKana(),is(NORMAL_MEMBER_KANA1));
		assertThat(findmember.getDepartment(),is(NORMAL_DEPARTMENT1));
		assertThat(findmember.getAddress(),is(NORMAL_ADDRESS1));
		assertThat(findmember.getTel(),is(NORMAL_TEL));
		assertThat(findmember.getBirthday(),is(birth));
		assertThat(findmember.getPosition_type(),is((Integer)NUMBER0));
		assertThat(findmember.getHired(),is(date));
		assertThat(findmember.getLogin_id(),is(NORMAL_LOGIN_ID1));

	}



	@Test
	public void testInsert正常4() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member =new Members();
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
			membersDao.insert(member);

			Members insertMember=membersDao.findById(INSERT_MEMBER_ID);
			String member_id=insertMember.getMember_id();
			String name=insertMember.getName();
			String kana=insertMember.getKana();
			int dep_id=insertMember.getDep_id();
			String address=insertMember.getAddress();
			String tel=insertMember.getTel();
			Date birth=insertMember.getBirthday();
			Integer posi=insertMember.getPosition_type();
			Date hired=insertMember.getHired();
			String login_id=insertMember.getLogin_id();

			assertThat(member_id,is(INSERT_MEMBER_ID));
			assertThat(name,is(INSERT_MEMBER_NAME));
			assertThat(kana,is(INSERT_MEMBER_KANA));
			assertThat(dep_id,is((Integer)NUMBER0));
			assertThat(address,is(INSERT_MEMBER_ADDRESS));
			assertThat(tel,is(INSERT_MEMBER_TEL));
			assertThat(birth,is(date1));
			assertThat(posi,is((Integer)NUMBER0));
			assertThat(hired,is(date1));
			assertThat(login_id,is(INSERT_LOGIN_ID));
			membersDao.delete(member);

		}catch(Exception e) {

			membersDao.delete(member);
		}
	}





	@Test
	public void testInsertacount正常5() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();

		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		int line=membersDao.insertacount(member);

		assertThat(line,equalTo(NUMBER1));

		membersDao.deleteAccount(member);
	}




	@Test
	public void testFindByLoginIdAndLoginPass正常6() throws Exception {

		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member =new Members();

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
		membersDao.insert(member);



		Members checkmem= membersDao.findByLoginIdAndLoginPass(INSERT_LOGIN_ID, "");
		assertThat(checkmem.getMember_id(),is(INSERT_MEMBER_ID));
		assertThat(checkmem.getName(),is(INSERT_MEMBER_NAME));

		membersDao.delete(member);

	}




	@Test
	public void testLogin正常7() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();

		Members member=new Members();

		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(NORMAL_HASH_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		membersDao.insertacount(member);

		Members account= membersDao.login(INSERT_ACCOUNT_LOGIN_ID, INSERT_ACCOUNT_LOGIN_PASS);
		assertThat(account.getLogin_id(),is(INSERT_ACCOUNT_LOGIN_ID));
		assertThat(account.getLogin_pass(),is(NORMAL_HASH_PASS));
		assertThat(account.getAuth_id(),is((Integer)INSERT_ACCOUNT_AUTH_ID));

		membersDao.deleteAccount(member);
	}




	@Test
	public void testUpdate正常8() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();

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
		membersDao.insert(member);


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

		membersDao.update(member);

		Members upMem= membersDao.findById(UP_MEMBER_ID);

		assertThat(upMem.getMember_id(),is(UP_MEMBER_ID));
		assertThat(upMem.getName(),is(UP_MEMBER_NAME));
		assertThat(upMem.getKana(),is(UP_MEMBER_KANA));
		assertThat(upMem.getDepartment(),is(UP_DEPARTMENT));
		assertThat(upMem.getAddress(),is(UP_ADDRESS));
		assertThat(upMem.getTel(),is(UP_TEL));
		assertThat(upMem.getBirthday().toString(),is(UP_DATE));
		assertThat(upMem.getPosition_type(),is((Integer)NUMBER0));
		assertThat(upMem.getLogin_id(),is(UP_LOGIN_ID));

		membersDao.delete(member);

	}




	@Test
	public void testUpdateaccount正常9() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();


		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		membersDao.insertacount(member);

		member.setLogin_id(UP_LOGIN_ID);
		member.setLogin_pass(UP_LOGIN_PASS);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);

		int line=membersDao.updateaccount(member);

		assertThat(line, is(NUMBER1));

		membersDao.deleteAccount(member);
	}




	@Test
	public void testUpdateWhithoutPass正常10() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();

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
		membersDao.insert(member);


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

		membersDao.updateWhithoutPass(member);

		Members upMem= membersDao.findById(UP_MEMBER_ID);

		assertThat(upMem.getMember_id(),is(UP_MEMBER_ID));
		assertThat(upMem.getName(),is(UP_MEMBER_NAME));
		assertThat(upMem.getKana(),is(UP_MEMBER_KANA));
		assertThat(upMem.getDepartment(),is(UP_DEPARTMENT));
		assertThat(upMem.getAddress(),is(UP_ADDRESS));
		assertThat(upMem.getTel(),is(UP_TEL));
		assertThat(upMem.getBirthday().toString(),is(UP_DATE));
		assertThat(upMem.getPosition_type(),is((Integer)NUMBER0));
		assertThat(upMem.getLogin_id(),is(UP_LOGIN_ID));

		membersDao.delete(member);
	}





	@Test
	public void testUpdateAccountWhithoutPass正常11() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();


		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		membersDao.insertacount(member);

		member.setLogin_id(UP_LOGIN_ID);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);

		int line=membersDao.updateAccountWhithoutPass(member);
		assertThat(line, is(NUMBER1));


		membersDao.deleteAccount(member);
	}




	@Test
	public void testDelete正常12() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member =new Members();

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
		membersDao.insert(member);

		int line=membersDao.delete(member);

		assertThat(line, is(NUMBER1));


	}




	@Test
	public void testDeleteAccount正常13() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();

		member.setLogin_id(INSERT_ACCOUNT_LOGIN_ID);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		membersDao.insertacount(member);

		int line=membersDao.deleteAccount(member);

		assertThat(line, is(NUMBER1));
	}


	@Test
	public void testCountAll正常14() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		int result=membersDao.countAll();
		assertThat(result, is(5));
	}

	@Test
	public void testCheckLoginId正常15() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		boolean  result=membersDao.CheckLoginId(NORMAL_LOGIN_ID1);

		assertThat(result,is(false));
	}

	@Test
	public void testFindfive異常1() {
		MembersDao membersDao = DaoFactory.createMembersDao();
		List<Members> memall;
		try {
			memall = membersDao.findAll(FAULT_NUMBER);
			List<Members> memfiv=membersDao.findfive(memall);

			assertThat(memfiv.get(NUMBER0).getName(),is(NORMAL_MEMBER_NAME1));

		} catch (Exception e) {

			assertThat(e.getMessage(),equalTo("Index: 0, Size: 0"));
		}

	}


	@Test
	public void testFindById異常2()  {

		MembersDao membersDao = DaoFactory.createMembersDao();
		try {
			Members findmember=membersDao.findById(FAULT_MEMBER_ID1);
			assertThat(findmember.getName(),is(NORMAL_MEMBER_NAME1));

		} catch (Exception e) {

			assertThat(e.getMessage(),equalTo(null));
		}

	}

	@Test
	public void testInsert異常3() {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member =new Members();


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
			membersDao.insert(member);

			Members insertMember=membersDao.findById(INSERT_MEMBER_ID);


			assertThat(insertMember.getMember_id(),is(INSERT_MEMBER_ID));
			assertThat(insertMember.getName(),is(INSERT_MEMBER_NAME));
			assertThat(insertMember.getKana(),is(INSERT_MEMBER_KANA));
			assertThat(insertMember.getDep_id(),is((Integer)NUMBER0));
			assertThat(insertMember.getAddress(),is(INSERT_MEMBER_ADDRESS));
			assertThat(insertMember.getTel(),is(INSERT_MEMBER_TEL));
			assertThat(insertMember.getBirthday(),is(date1));
			assertThat(insertMember.getPosition_type(),is((Integer)NUMBER0));
			assertThat(insertMember.getHired(),is(date1));
			assertThat(insertMember.getLogin_id(),is(NORMAL_LOGIN_ID1));
			membersDao.delete(member);

		} catch (Exception e) {


			assertThat(e.getMessage(),equalTo("Unparseable date: \"INSERT_DATE\""));

		}

	}

	@Test
	public void testInsertacount異常4() {

		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();

		member.setLogin_id(NORMAL_LOGIN_ID1);
		member.setLogin_pass(INSERT_ACCOUNT_LOGIN_PASS);
		member.setAuth_id(INSERT_ACCOUNT_AUTH_ID);

		try {
			membersDao.insertacount(member);
		} catch (Exception e) {

			assertThat(e.getMessage(), equalTo("Duplicate entry 'aoi' for key 'login_id'"));

		}

	}

	@Test
	public void testFindByLoginIdAndLoginPass異常5() {
		MembersDao membersDao = DaoFactory.createMembersDao();
		try {
			Members checkmem= membersDao.findByLoginIdAndLoginPass(INSERT_ACCOUNT_LOGIN_ID, "");
			assertThat(checkmem.getMember_id(),is(NORMAL_MEMBER_ID1));
		} catch (Exception e) {
			assertThat(e.getMessage(),equalTo(null));

		}

	}

	@Test
	public void testLogin異常6() {
		MembersDao membersDao = DaoFactory.createMembersDao();
		try {
			Members account= membersDao.login(FSULT_LOGIN_ID, FSULT_LOGIN_PASS);
			assertThat(account.getLogin_id(),is(FSULT_LOGIN_ID));
		} catch (Exception e) {
			assertThat(e.getMessage(),equalTo(null));

		}
	}

	@Test
	public void testUpdate異常7() {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();
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

			membersDao.update(member);
		}catch(Exception e) {
			assertThat(e.getMessage(),equalTo(null));

		}

	}

	@Test
	public void testUpdateaccount異常8() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();

		member.setLogin_id(UP_LOGIN_ID);
		member.setLogin_pass(UP_LOGIN_PASS);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);

		int line=membersDao.updateaccount(member);

		assertThat(line, is(NUMBER0));

		membersDao.deleteAccount(member);
	}


	@Test
	public void testUpdateWhithoutPass異常9() {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();

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

			membersDao.updateWhithoutPass(member);

			Members upMem= membersDao.findById(UP_MEMBER_ID);
			assertThat(upMem.getMember_id(),is(UP_MEMBER_ID));
		}catch(Exception e) {

			assertThat(e.getMessage(),equalTo(null));
		}
	}

	@Test
	public void testUpdateAccountWhithoutPass異常10() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();

		member.setLogin_id(UP_LOGIN_ID);
		member.setAuth_id(NUMBER1);
		member.setOldlogin_id(INSERT_ACCOUNT_LOGIN_ID);

		int line=membersDao.updateAccountWhithoutPass(member);
		assertThat(line, is(NUMBER0));


		membersDao.deleteAccount(member);
	}


	@Test
	public void testDelete異常11() throws Exception{
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member =new Members();
		int line=membersDao.delete(member);
		assertThat(line, is(NUMBER0));

	}



	@Test
	public void testDeleteAccount異常12() throws Exception {
		MembersDao membersDao = DaoFactory.createMembersDao();
		Members member=new Members();

		int line=membersDao.deleteAccount(member);

		assertThat(line, is(NUMBER0));
	}




}
