package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import domain.Attend;
import domain.Events;
import domain.Members;

public class AttendDaoImpl implements MembersDao, AttendDao {
	private DataSource ds;

	public AttendDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * テーブルattendに新しい参加者を追加するメソッド
	 * @param int userId, int eventId
	 */
	@Override
	public void insert(String member_id, int event_Id) throws SQLException {
		try (Connection con = ds.getConnection()) {
			String sql = "INSERT INTO attends "
					+ " (attends_id, member_id,event_id) "
					+ "values(null,?,?);";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, member_id);
			stmt.setObject(2, event_Id);
			stmt.executeUpdate();
		}
	}

	/**
	 * テーブルattendから参加者を削除するメソッド
	 * @param int userId, int eventId
	 */
	@Override
	public int delete(String member_id, int event_Id) throws SQLException {
		int line = 0;
		try (Connection con = ds.getConnection()) {
			String sql = "delete from attends  where  member_id=? AND event_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, member_id);
			stmt.setObject(2, event_Id);
			line = stmt.executeUpdate();
		}
		return line;
	}

	/**
	 *@param Integer id
	 *@return List<Attend> attendList
	 */
	@Override
	public List<Attend> findAttends(Integer event_id) throws SQLException {
		List<Attend> attendList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {
			String sql = "SELECT "
					+ " members.name, attends.member_id "
					+ " from attends join members "
					+ " on attends.member_id = members.member_id"
					+ " where attends.event_id = ? ";
			PreparedStatement stms = con.prepareStatement(sql);
			stms.setLong(1, event_id);
			ResultSet rs = stms.executeQuery();
			while (rs.next()) {
				attendList.add(mapToAttends(rs));
			}
		}
		return attendList;
	}

	/**
	 *
	 * @param rs
	 * @throws SQLException
	 */
	private Attend mapToAttends(ResultSet rs) throws SQLException {
		Attend users = new Attend();
		users.setMember_name(rs.getString("name"));
		try {
			users.setMember_id( rs.getString("member_id"));
		} catch (NullPointerException e) {
		}
		return users;
	}



	/**
	 * userDeleteServletから削除したﾕｰｻﾞｰが参加していたイベント情報を削除します
	 */
	@Override
	public int deleteByMemberId(Members member_id) throws Exception {
		int line=0;
		try (Connection con = ds.getConnection()) {
			String sql = "delete from attends  where member_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, member_id.getMember_id());
			line = stmt.executeUpdate();
		}
		return line;
	}

	/**
	 * userDeleteServletから削除したイベントのアテンド情報を削除します
	 */
	@Override
	public int deleteByEventId(Events event) throws Exception {
		int line=0;
		try (Connection con = ds.getConnection()) {
			String sql = "delete from attends  where event_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, event.getEvent_id());
			line = stmt.executeUpdate();
		}
		return line;
	}


	@Override
	public Members findById(String id) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


	@Override
	public void insert(Members member) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public void update(Members member) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public Members findByLoginIdAndLoginPass(String loginId, String loginPass) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


	@Override
	public List<Members> findAll(int page) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


	@Override
	public List<Members> findfive(List<Members> userList) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


	@Override
	public int countAll() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}


	@Override
	public boolean CheckLoginId(String loginId) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}



	@Override
	public Members login(String loginId, String loginPass) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


	@Override
	public int delete(Members member) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}


	@Override
	public int updateWhithoutPass(Members Members) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}


	@Override
	public int insertacount(Members member) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}


	@Override
	public int updateaccount(Members Members) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}


	@Override
	public int updateAccountWhithoutPass(Members Members) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}


	@Override
	public int deleteAccount(Members Members) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public String insertMast(List<Members> memberList) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}



}