package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;

import domain.Place;

public class PlaceDaoImplTest extends TestDBAccess {

	private static DataSource testds;
	static final Date DATE = new Date();
	static final int ADMIN_ID = 100;
	static final int CAPA = 90;
	static final int EQU_MIC = 0;
	static final int EQU_PROJECTOR = 1;
	static final int EQU_WHITEBORD = 0;
	static final String PLACE = "会議室100";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InitialContext ctx = null;

		try {
			ctx = new InitialContext();
			testds = (DataSource) ctx.lookup("java:comp/env/jdbc/eventdb2");
		} catch (Exception e) {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception el) {
					throw new RuntimeException(el);
				}
			}
			throw new RuntimeException(e);
		}

		//memberテーブルを一度空にして、テスト用のマスタ情報をインサート
		try (Connection conn = testds.getConnection()) {
			String sql2 = "TRUNCATE members;";
			PreparedStatement stmt2 = conn.prepareStatement(sql2);
			stmt2.executeUpdate();

			String sql=	"INSERT INTO `members` VALUES ('001','山本葵','ヤマモトアオイ','1995-12-10','東京都新宿区飯田橋54-10-1','090-6433-1233','2018-04-02',1,0,'aoi'),"
					+ "('002','中村悠真','ナカムラユウマ','1995-12-11','東京都新宿区飯田橋54-10-1','090-6433-1234','2018-04-02',1,0,'yuma'),"
					+ "('003','小林蓮','コバヤシレン','1995-12-12','東京都新宿区飯田橋54-10-1','090-6433-1235','2018-04-02',1,NULL,'ren'),"
					+ "('100','田中太郎','タナカタロウ','1990-12-12','東京都新宿区飯田橋54-10-1','090-6433-1200','2010-04-02',1,NULL,'taro'),"
					+ "('12121','坂本','サカモト','2016-05-02','都会','090-1122-4921','2016-05-02',1,0,'pass'); ";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.executeUpdate();
		}
	}

	@Test
	public void 正常系testInsert() throws Exception {

		//インサート用のテストデータをセットしリストに格納
		Place place = new Place();
		place.setAdmin_id(ADMIN_ID);
		place.setCapa(CAPA);
		place.setEqu_mic(EQU_MIC);
		place.setEqu_projector(EQU_PROJECTOR);
		place.setEqu_whitebord(EQU_WHITEBORD);
		place.setLocking_time(DATE);
		place.setPlace(PLACE);

		List<Place> testList = new ArrayList<>();
		testList.add(place);

		//リストにセットしたテストデータをデータベースに登録

		PlaceDao placeDao = DaoFactory.createPlaceDao();
		placeDao.insert(testList);

		//確認のためにDBに接続

		try (Connection conn = testds.getConnection()) {

			//登録したテストデータの存在を確認
			String sql = "select * from place where place = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setObject(1, place.getPlace());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				assertThat(rs.getObject("place").toString(), is(PLACE));
			}

		}
	}

	@Test
	public void 異常系testInsert1() throws Exception {

		//インサート用のサンプルデータをセットしリストに格納
		//not nullに設定されているデータ「capa」に何もセットしない
		Place place = new Place();
		place.setAdmin_id(ADMIN_ID);
		//place.setCapa(90);
		place.setEqu_mic(EQU_MIC);
		place.setEqu_projector(EQU_PROJECTOR);
		place.setEqu_whitebord(EQU_WHITEBORD);
		place.setLocking_time(DATE);
		place.setPlace(PLACE);

		List<Place> testList = new ArrayList<>();
		testList.add(place);

		//上で作成した不完全なリストをインサートする
		//エラーコード300が返ってくる

		PlaceDao placeDao = DaoFactory.createPlaceDao();
		assertThat(placeDao.insert(testList), is("300"));
	}

	@Test
	public void 異常系testInsert2() throws Exception {

		//インサート用のサンプルデータをセットしリストに格納
		Place place = new Place();
		place.setAdmin_id(ADMIN_ID);
		place.setCapa(90);
		place.setEqu_mic(EQU_MIC);
		place.setEqu_projector(EQU_PROJECTOR);
		place.setEqu_whitebord(EQU_WHITEBORD);
		place.setLocking_time(DATE);
		place.setPlace(PLACE);

		List<Place> testList = new ArrayList<>();
		testList.add(place);

		PlaceDao placeDao = DaoFactory.createPlaceDao();

		//正常なデータを二回インサートしてエラーを確認する
					placeDao.insert(testList);
		assertThat(placeDao.insert(testList), is("300"));
	}

	@Test
	public void 異常系testInsert3() throws Exception {
		//マスタ情報を削除
		try (Connection conn = testds.getConnection()) {
			String sql2 = "TRUNCATE members;";
			PreparedStatement stmt2 = conn.prepareStatement(sql2);
			stmt2.executeUpdate();
		}

		//インサート用のサンプルデータをセットしリストに格納
		Place place = new Place();
		place.setAdmin_id(ADMIN_ID);
		place.setCapa(90);
		place.setEqu_mic(EQU_MIC);
		place.setEqu_projector(EQU_PROJECTOR);
		place.setEqu_whitebord(EQU_WHITEBORD);
		place.setLocking_time(DATE);
		place.setPlace(PLACE);

		List<Place> testList = new ArrayList<>();
		testList.add(place);

		PlaceDao placeDao = DaoFactory.createPlaceDao();

		//マスタ参照不可のエラーコードを確認する
		assertThat(placeDao.insert(testList), is("302"));
	}


	@Before
	public void truncate() throws Exception {
		try (Connection conn = testds.getConnection()) {
			String sql2 = "TRUNCATE place;";
			PreparedStatement stmt2 = conn.prepareStatement(sql2);
			stmt2.executeUpdate();
		}
	}
}
