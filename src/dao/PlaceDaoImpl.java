package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import domain.Place;

public class PlaceDaoImpl implements PlaceDao {
	private DataSource ds;

	public PlaceDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	public String insert(List<Place> placeList) throws Exception {
		try (Connection con = ds.getConnection()) {

			try {
				con.setAutoCommit(false);

				for(Place place:placeList) {
				String sql1 = "SELECT member_id from members where member_id=?;";
				PreparedStatement stmt1 = con.prepareStatement(sql1);
				stmt1.setString(1, place.getAdmin_id().toString());
				ResultSet rs = stmt1.executeQuery();
				int admin_count = -1;
				while (rs.next()) {
					admin_count = Integer.parseInt(rs.getString("count(*)"));
				}

				String sql2 = "SELECT COUNT(*) from place where place=?";
				PreparedStatement stmt2 = con.prepareStatement(sql2);
				stmt2.setString(1, place.getPlace());
				ResultSet rs2 = stmt2.executeQuery();
				int place_count = 0;
				while (rs2.next()) {
					place_count = Integer.parseInt(rs2.getString("count(*)"));
				}

				if (admin_count == 0) {
					con.rollback();
					return "302";

				} else if (place_count != 0) {

					con.rollback();
					return "302";

				} else {

					String sql = "INSERT INTO place"
							+ "(place,capa,equ_mic,equ_whitebord,equ_projector, admin_id,locking_time) "
							+ "VALUES(?,?,?,?,?,?,?);";
					Timestamp LockTime = new Timestamp(place.getLocking_time().getTime());
					PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setString(1, place.getPlace());
					stmt.setObject(2, place.getCapa());
					stmt.setObject(3, place.getEqu_mic());
					stmt.setObject(4, place.getEqu_whitebord());
					stmt.setObject(5, place.getEqu_projector());
					stmt.setObject(6, place.getAdmin_id());
					stmt.setTimestamp(7, LockTime);
					stmt.executeUpdate();
				}

				}
				con.commit();
				return "100";

			} catch (Exception e) {

				System.out.println("error1");
				e.printStackTrace();
				con.rollback();
				return "300";
			} finally {
				try {
					if (con != null) {
						con.close();
					}
				} catch (SQLException e) {
					System.out.println("error2");
				}
			}
		}

	}

}
