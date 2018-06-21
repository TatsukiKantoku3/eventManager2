package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

import domain.Account;

public class AccountDaoImpl implements AccountDao {

	private DataSource ds;

	public AccountDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * アカウントデータ(memberId,loginId,loginPass,authId)をDBのテーブルaccount,membersにインサートする
	 * @param Account
	 */
	@Override
	public String insertAcount(List<Account> accountList) throws Exception {
		try (Connection con = ds.getConnection()) {
			try {
				//オートコミットを切る
				con.setAutoCommit(false);
				for(Account account:accountList) {

					// membersテーブルのlogin_idにidをセット
					String sqlMem = "UPDATE members SET login_id = ? WHERE member_id = ?;";
					PreparedStatement stmtMem = con.prepareStatement(sqlMem);
					stmtMem.setString(1, account.getLoginId());
					stmtMem.setString(2, account.getMemberId());
					int udCnt=stmtMem.executeUpdate();

					if(udCnt!=0) {
						// accountテーブルにINSERTする
						String sqlAcc = "INSERT INTO account(login_id,login_pass, auth_id) VALUES(?,?,?);";
						PreparedStatement stmtAcc = con.prepareStatement(sqlAcc);
						stmtAcc.setString(1, account.getLoginId());
						String hashPass = BCrypt.hashpw(account.getLoginPass(), BCrypt.gensalt());//パスワードハッシュ化
						stmtAcc.setString(2, hashPass);
						stmtAcc.setObject(3, account.getAuthId());
						stmtAcc.executeUpdate();
					}else {
						con.rollback();
						return "300";
					}
				}
				con.commit();
			} //挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
				return "300";

			}
		}
		return "100";
	}


}