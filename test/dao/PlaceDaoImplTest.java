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

import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;

import domain.Place;

public class PlaceDaoImplTest extends TestDBAccess  {

	private static DataSource testds;
	static final Date DATE = new Date();
	static final int ADMIN_ID=9;
	static final int CAPA=90;
	static final int EQU_MIC=0;
	static final int EQU_PROJECTOR=1;
	static final int EQU_WHITEBORD=0;
	static final String PLACE="会議室100";


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

		try (Connection conn=testds.getConnection()) {

			//登録したテストデータの存在を確認
			String sql = "select * from place where place = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setObject(1, place.getPlace());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				assertThat(rs.getObject("place").toString(), is(PLACE));
			}

			//テストデータを削除
			String sql2 = "DELETE FROM eventdb2.place WHERE place='会議室100';";
			PreparedStatement stmt2 = conn.prepareStatement(sql2);
			stmt2.executeUpdate();
		}
	}

	@Test
	public void 異常系testInsert() throws Exception {

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
}
