package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import domain.Depart;
import domain.Events;
import domain.Members;

public class DepartDaoImpl implements DepartDao {
	private DataSource ds;

	public DepartDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public List<Depart> findAttends(Integer dep_id) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String insert(List<Depart> department) throws Exception {

		try (Connection con = ds.getConnection()) {
			//必ず決まっているデータのスタート地点をセット（テーブルによって量、数値が変わります）
			try {
				//DB接続
				//オートコミットを切る
				con.setAutoCommit(false);

				//departmentテーブルに部署名とフロア情報を挿入
				//membersテーブルにpositionタイプ（役職情報）を挿入
				for (Depart depart:department) {

					String sql = "insert into department (dep_id,department,floor)  values (null,?,?)";
					PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setObject(1, depart.getDepartment());
					stmt.setObject(2, depart.getFloor());
					System.out.println(stmt);
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
						System.out.println(stmt2);
						stmt2.executeUpdate();
					}
				}

				//エラーがなければコミットする
				con.commit();
				return "100";
			}
			//挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				e.printStackTrace();
				con.rollback();
				return "300";

			} finally {
				try {
					if (con != null) {
						con.close();

					}
				} catch (SQLException e) {
					return "301";
				}
			}
		}
	}

	@Override
	public void delete(String member_Id, int event_Id) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void deleteByUserId(Members user) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void deleteByEventId(Events event) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}

}
