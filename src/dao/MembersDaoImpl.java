package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

import domain.Members;

public class MembersDaoImpl implements MembersDao {
	private DataSource ds;

	public MembersDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * ユーザー一覧
	 * ResultSetをUsersクラスにセットします
	 * 5行分とってくる
	 * @return users
	 */
	@Override
	public List<Members> findAll(int page) throws Exception {
		List<Members> userList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			String sql = "SELECT"
					+ " members.member_id "
					+ "FROM members ORDER BY member_id "
					+ "ASC LIMIT ? , 5;";

			PreparedStatement stms = con.prepareStatement(sql);
			stms.setObject(1, page);
			ResultSet rs = stms.executeQuery();

			while (rs.next()) {
				userList.add(mapToUsers(rs));
			}
		}
		return userList;
	}

	/**
	 * ユーザー一覧
	 * ResultSetをUsersクラスにセットします
	 * 5行分とってきたやつをjoinして表示を整える
	 * @return users
	 */
	@Override
	public List<Members> findfive(List<Members> hoge) throws Exception {
		List<Members> userList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			for (Members fuga : hoge) {
				String sql = "SELECT "
						+ "members.member_id, members.name, department.department "
						+ "FROM members JOIN department "
						+ "ON members.dep_id = department.dep_id "
						+ "WHERE members.member_id = ?;";


				PreparedStatement stms = con.prepareStatement(sql);
				stms.setObject(1, fuga.getMember_id());
				ResultSet rs = stms.executeQuery();

				if (rs.next()) {
					userList.add(mapToUsers2(rs));
				}
			}

		}
		return userList;
	}

	/**
	 * findAllメソッドから呼び出し
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Members mapToUsers(ResultSet rs) throws SQLException {

		domain.Members users = new Members();
		users.setMember_id(rs.getString("member_id"));
		return users;
	}

	/**
	 * findAfiveメソッドから呼び出し
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Members mapToUsers2(ResultSet rs) throws SQLException {

		domain.Members users = new Members();
		users.setMember_id(rs.getString("member_id"));
		users.setName(rs.getString("name"));
		users.setDepartment(rs.getString("department"));

		return users;
	}

	/**
	 * findByIdメソッドから呼び出される
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Members mapToUser(ResultSet rs) throws SQLException {

		domain.Members users = new Members();
		users.setMember_id(rs.getString("member_id"));
		users.setLogin_id((rs.getString("login_id")));
		users.setName(rs.getString("name"));
		users.setKana(rs.getString("kana"));
		users.setDepartment(rs.getString("department"));
		users.setAddress(rs.getString("address"));
		users.setTel(rs.getString("tel"));
		users.setBirthday(rs.getDate("birthday"));
		users.setPosition_type((Integer)rs.getInt("position_type"));
		users.setHired(rs.getDate("hired"));

		return users;
	}





	/**
	 * ユーザー詳細時に処理
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	@Override
	public Members findById(String member_id) throws Exception {
		Members member = null;
		try (Connection con = ds.getConnection()) {
			String sql = "SELECT "
					+ "members.member_id, members.login_id, members.name, members.kana, department.department,"
					+ "members.address,members.tel,members.birthday,members.position_type,members.hired"
					+ " FROM members "
					+ "JOIN department ON members.dep_id = department.dep_id "
					+ "WHERE members.member_id = ?;";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, member_id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				member = mapToUser(rs);
			}
			//member=mapToUsers(rs);
		}

		return member;

	}

	/**
	 * ユーザ情報の入力
	 * @param member
	 */
	//書き換え必須
	@Override
	public void insert(Members member) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "INSERT INTO members(member_id,name,kana,birthday,address,tel,hired,dep_id,position_type,login_id)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?);";

			//accountも編集

			Timestamp Birth = new Timestamp(member.getBirthday().getTime());
			Timestamp  Hire= new Timestamp(member.getHired().getTime());

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, member.getMember_id());
			stmt.setString(2, member.getName());
			stmt.setString(3, member.getKana());
			stmt.setTimestamp(4,Birth);
			stmt.setString(5, member.getAddress());
			stmt.setString(6, member.getTel());
			stmt.setTimestamp(7, Hire);
			stmt.setObject(8, member.getDep_id(),Types.INTEGER);
			stmt.setObject(9, 0);
			stmt.setString(10, member.getLogin_id());
			stmt.executeUpdate();
		}
	}

	@Override
	public int insertacount(Members member) throws Exception {
		int line=0;
		try (Connection con = ds.getConnection()) {
			String sql = "INSERT INTO account(login_id,login_pass,auth_id)VALUES(?,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			//account
			stmt.setString(1, member.getLogin_id());
			stmt.setString(2, member.getLogin_pass());
			stmt.setObject(3, member.getAuth_id(),Types.INTEGER);

			line= stmt.executeUpdate();

		}
		return line;
		}

	@Override
	public String insertMast(List<Members> memberList) throws Exception {
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);//オートコミットを外す
			String sql = "INSERT INTO members(member_id,name,kana,birthday,address,tel,hired,dep_id,position_type,login_id)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?);";

			for (Members member : memberList) {
				Timestamp Birth = new Timestamp(member.getBirthday().getTime());
				Timestamp Hire = new Timestamp(member.getHired().getTime());
				PreparedStatement stmt = con.prepareStatement(sql);
				stmt.setString(1, member.getMember_id());
				stmt.setString(2, member.getName());
				stmt.setString(3, member.getKana());
				stmt.setTimestamp(4, Birth);
				stmt.setString(5, member.getAddress());
				stmt.setString(6, member.getTel());
				stmt.setTimestamp(7, Hire);
				stmt.setObject(8, member.getDep_id(), Types.INTEGER);
				stmt.setObject(9, 0);
				stmt.setString(10, member.getLogin_id());
				stmt.executeUpdate();
			}
			con.commit();
		} catch (Exception e) {
			return "300";
		}
		return "100";
	}



	/**
	 * ログイン時に処理
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	@Override
	public Members findByLoginIdAndLoginPass(String loginId, String loginPass) throws Exception {
		Members member = null;
		try (Connection con = ds.getConnection()) {
			String sql1 = "SELECT  * FROM members WHERE login_id = ?;";
			PreparedStatement stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, loginId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				member = mapToLogin(rs1);
			}
		}
		return member;
	}

	@Override
	public Members login(String loginId, String loginPass)throws Exception {
		Members member = null;
		try (Connection con = ds.getConnection()) {
			String sql2="SELECT*FROM account WHERE login_id=?";
			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setString(1, loginId);
			ResultSet rs2 = stmt2.executeQuery();

			if (rs2.next()) {
				if (BCrypt.checkpw(loginPass, rs2.getString("login_pass"))) {
					member = mapToLoginAccount(rs2);
				}
			}
			return member;
		}
	}

	/**
	 * ログイン時に処理
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Members mapToLogin(ResultSet rs) throws SQLException {

		domain.Members users = new Members();
		users.setMember_id(rs.getString("member_id"));
		users.setName(rs.getString("name"));


		return users;
	}

	private Members mapToLoginAccount(ResultSet rs) throws SQLException {
		domain.Members users = new Members();
		users.setLogin_id(rs.getString("login_id"));
		users.setLogin_pass(rs.getString("login_pass"));
		users.setAuth_id((Integer) rs.getObject("auth_id"));
		return users;

	}

	/**
	 * ユーザ情報更新処理
	 * @param Members
	 */
	@Override
	public void update(Members Members) throws Exception {

		try (Connection con = ds.getConnection()) {
			String sql ="UPDATE members SET member_id=?,name =?,kana=?,dep_id=?,"
					+ "address=?,tel=?,birthday=?,position_type=?,login_id = ? WHERE member_id = ?;";

			Timestamp Birth = new Timestamp(Members.getBirthday().getTime());

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, Members.getMember_id());
			stmt.setString(2, Members.getName());
			stmt.setString(3, Members.getKana());
			stmt.setObject(4, Members.getDep_id(),Types.INTEGER);
			stmt.setString(5, Members.getAddress());
			stmt.setString(6, Members.getTel());
			stmt.setTimestamp(7,Birth);
			stmt.setObject(8, Members.getPosition_type(),Types.INTEGER);
			stmt.setString(9, Members.getLogin_id());
			stmt.setString(10, Members.getOldmember_id());
			stmt.executeUpdate();
		}
	}

	public int updateaccount(Members Members) throws Exception {

			int line=0;
		try (Connection con = ds.getConnection()) {
			String sql= "UPDATE account SET login_id=?,login_pass=?,auth_id=? WHERE login_id=?;";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Members.getLogin_id());
			stmt.setString(2, Members.getLogin_pass());
			stmt.setObject(3, Members.getAuth_id(),Types.INTEGER);
			stmt.setString(4, Members.getOldlogin_id());

			line=stmt.executeUpdate();
		}
		return line;
	}

	/**
	 * パスワードの変更なしの場合のユーザ情報の更新
	 */
	@Override
	public int updateWhithoutPass(Members Members) throws Exception {

		int line=0;
		try (Connection con = ds.getConnection()) {
			String sql = "UPDATE members SET member_id=?,name = ?,kana=?,dep_id=?,"
					+ "address=?,tel=?,birthday=?,position_type=?,login_id = ? WHERE member_id = ?;";

			Timestamp Birth = new Timestamp(Members.getBirthday().getTime());
			//アカウントの扱い
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Members.getMember_id());
			stmt.setString(2, Members.getName());
			stmt.setString(3, Members.getKana());
			stmt.setObject(4, Members.getDep_id(),Types.INTEGER);
			stmt.setString(5, Members.getAddress());
			stmt.setString(6, Members.getTel());
			stmt.setTimestamp(7,Birth);
			stmt.setObject(8, Members.getPosition_type(),Types.INTEGER);
			stmt.setString(9, Members.getLogin_id());
			stmt.setString(10, Members.getOldmember_id());

			line=stmt.executeUpdate();
		}
		return line;
	}

	public int updateAccountWhithoutPass(Members Members) throws Exception{

		int line=0;

		try (Connection con = ds.getConnection()) {
			String sql= "UPDATE account SET login_id=?,auth_id=? WHERE login_id=?;";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Members.getLogin_id());
			stmt.setObject(2, Members.getAuth_id(),Types.INTEGER);
			stmt.setString(3, Members.getOldlogin_id());
			line=stmt.executeUpdate();
	}
		return line;
	}
	/**
	 * ユーザー情報の削除
	 * @return users
	 */
	@Override
	public int delete(Members Members) throws Exception {
		int line=0;
		try (Connection con = ds.getConnection()) {
			String sql ="DELETE "
					+ "FROM members "
					+ "WHERE login_id = ?;";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Members.getLogin_id());
			line=stmt.executeUpdate();
		}
		return line;
	}
	@Override
	public int deleteAccount(Members Members) throws Exception{
		int line=0;

		try (Connection con = ds.getConnection()) {
			String sql ="DELETE "
					+ "FROM account "
					+ "WHERE login_id = ?;";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Members.getLogin_id());
			line=stmt.executeUpdate();
		}
		return line;
	}


	/**
	 * @return count
	 */
	public int countAll() throws Exception {
		List<Members> userList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			String sql ="SELECT "
					+ "members.member_id "
					+ "FROM members;";

			PreparedStatement stms = con.prepareStatement(sql);
			ResultSet rs = stms.executeQuery();

			while (rs.next()) {
				userList.add(mapToUsers(rs));
			}
		}
		double count2 = userList.size();
		int count=(int)count2;
		return count;
	}

	/**
	 * login_idを検索して検索結果が１件以上ならfalseを返す
	 *  return の初期値 true
	 * @return check
	 */
	@Override
	public boolean CheckLoginId(String loginId) throws Exception {
		boolean check = true;
		int count = 0;
		try(Connection con=ds.getConnection()) {
			String sql=	"SELECT "
					+ "COUNT(*) FROM members "
					+ "WHERE members.login_id = ?;";
			PreparedStatement stmt=con.prepareStatement(sql);
			stmt.setString(1, loginId);
			ResultSet rs =stmt.executeQuery();
			while(rs.next()) {
				count = Integer.parseInt(rs.getString("count(*)"));
			}
			if(count > 0) {
				check = false;
			}
			return check;
		}
	}


}