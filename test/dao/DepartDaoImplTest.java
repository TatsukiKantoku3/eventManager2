package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;

import domain.Depart;

public class DepartDaoImplTest extends TestDBAccess{

	static DataSource  ds;
	//正常系
	final String CORRECT="100";
	final String DEPARTMENT="技術部";
	final String DEPARTMENT2="企画部";
	final int FLOOR=2;
	final int FLOOR2=2;
	final static int POSITIONTYPE=200;
	final static int POSITIONTYPE2=201;

	//異常系
	final String ERROR="300";
	final String ERROR2="302";
	final String FAIL_DEPARTMENT="技術部";
	final int FAIL_FLOOR=12;
	final int FAIL_POSITIONTYPE=792310;

	static DepartDao target=DaoFactory.createDepartDao();
	static Depart dep;
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


			sql="TRUNCATE department";
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();

			//テストデータの登録
			sql=	"INSERT INTO `members` VALUES ('200','山本葵','ヤマモトアオイ','1995-12-10','東京都新宿区飯田橋54-10-1','090-6433-1233','2018-04-02',1,0,'maxmurai'),"
					+ "('201','中村悠真','ナカムラユウマ','1995-12-11','東京都新宿区飯田橋54-10-1','090-6433-1234','2018-04-02',1,0,'hikakinn')";
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();
		}

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//最後に最初に格納したメンバーの削除
		try (Connection con = ds.getConnection()){
			PreparedStatement stmt;
			String sql;

			sql="DELETE FROM `eventdb2`.`members` WHERE `member_id`="+POSITIONTYPE;
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();

			sql="DELETE FROM `eventdb2`.`members` WHERE `member_id`="+POSITIONTYPE2;
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();
		}

	}
	@After
	public void tearDown() throws Exception {
		//departmentテーブルの初期化を毎回行う
		try (Connection con = ds.getConnection()){
			PreparedStatement stmt;
			String sql;


			sql="TRUNCATE department";
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();
		}
	}

	@Test
	public void testInsert正常() throws Exception {

		dep=new Depart();
		dep.setDepartment(DEPARTMENT);
		dep.setFloor(FLOOR);
		dep.setPosition_type(POSITIONTYPE);
		Depart dep2=new Depart();
		dep2.setDepartment(DEPARTMENT2);
		dep2.setFloor(FLOOR2);
		dep2.setPosition_type(POSITIONTYPE2);
		List<Depart> DepList=new ArrayList<>();
		DepList.add(0, dep);

		DepList.add(1, dep2);
		String result=target.insert(DepList);

		//テーブルを整える
		assertThat(result,is(CORRECT));


	}

	@Test
	public void testInsert異常1() throws Exception {
		//UQであるdepartmentを二回格納

	dep=new Depart();
		dep.setDepartment(DEPARTMENT);
		dep.setFloor(FLOOR);
		dep.setPosition_type(POSITIONTYPE);
		List<Depart> DepList=new ArrayList<>();
		DepList.add(0, dep);
		target.insert(DepList);


		dep.setDepartment(FAIL_DEPARTMENT);
		dep.setFloor(FAIL_FLOOR);
		dep.setPosition_type(FAIL_POSITIONTYPE);
		DepList.add(0, dep);

		String result=target.insert(DepList);

		assertThat(result,is(ERROR));
	}

	@Test
	public void testInsert異常2() throws Exception {
		//存在しないmember_idをposition_typeに格納

		dep=new Depart();
		List<Depart> DepList=new ArrayList<>();
		dep.setDepartment(DEPARTMENT2);
		dep.setFloor(FAIL_FLOOR);
		dep.setPosition_type(FAIL_POSITIONTYPE);
		DepList.add(0, dep);

		String result=target.insert(DepList);
		assertThat(result,is(ERROR2));
	}

	@Test
	public void testDepList正常() throws Throwable {

		dep=new Depart();
		dep.setDepartment(DEPARTMENT);
		dep.setFloor(FLOOR);
		dep.setPosition_type(POSITIONTYPE);
		List<Depart> DepList=new ArrayList<>();
		DepList.add(0, dep);
		target.insert(DepList);

		List<Depart> dep=target.DepList();
		assertThat(dep.get(0).getDepartment(),is(DEPARTMENT));
	}
}
