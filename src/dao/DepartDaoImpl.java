package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import domain.Depart;

public class DepartDaoImpl implements DepartDao {
	private DataSource ds;

	public DepartDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	public String insert(List<Depart> department) throws Exception {

		try (Connection con = ds.getConnection()) {
			//必ず決まっているデータのスタート地点をセット（テーブルによって量、数値が変わります）
			try {
				if (department.isEmpty()) {
					return "300";
				}
				//DB接続
				//オートコミットを切る
				con.setAutoCommit(false);
				//departmentテーブルに部署名とフロア情報を挿入
				//membersテーブルにpositionタイプ（役職情報）を挿入
				for (Depart depart : department) {

					String sql = "insert into department (dep_id,department,floor)  values (null,?,?)";
					PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setObject(1, depart.getDepartment());
					stmt.setObject(2, depart.getFloor());
					stmt.executeUpdate();

					//member_idを検索して結果をResultSetにセットする

					String sql1 = "SELECT COUNT(*) from members where member_id=?";
					PreparedStatement stmt1 = con.prepareStatement(sql1);
					stmt1.setObject(1, depart.getPosition_type());
					ResultSet rs = stmt1.executeQuery();
					int member_count = 0;
					while (rs.next()) {
						member_count = Integer.parseInt(rs.getString("count(*)"));
					}

					//検索結果が0ならロールバック
					if (member_count == 0) {
						con.rollback();
						return "302";
					} else {

						//membersテーブルにpositionタイプ（役職情報）を挿入
						String sql2 = "update Members set position_type =1 where member_id=?;";
						PreparedStatement stmt2 = con.prepareStatement(sql2);
						stmt2.setObject(1, depart.getPosition_type());

						stmt2.executeUpdate();
					}
				}

				//エラーがなければコミットする
				con.commit();
				return "100";
			}
			//挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				con.rollback();
				return "300";

			}

		}
	}

	@Override
	public List<Depart> DepList() throws SQLException {
		List<Depart> DepList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {
			String sql = " SELECT department FROM department ORDER BY dep_id;";
			PreparedStatement stms = con.prepareStatement(sql);
			ResultSet rs = stms.executeQuery();
			while (rs.next()) {
				DepList.add(mapToDepart(rs));
			}
		}

		return DepList;

	}

	private Depart mapToDepart(ResultSet rs) throws SQLException {
		Depart dep = new Depart();
		dep.setDepartment(rs.getString("department"));
		return dep;

	}

}
