package domain;

public class Account {
	private String member_id;
	private String login_id;
	private String login_pass;
	private Integer auth_id;

	// getter,setter
	public String getMemberId() {
		return member_id;
	}
	public void setMemberId(String member_id) {
		this.member_id = member_id;
	}
	public String getLoginId() {
		return login_id;
	}
	public void setLoginId(String login_id) {
		this.login_id = login_id;
	}
	public String getLoginPass() {
		return login_pass;
	}
	public void setLoginPass(String login_pass) {
		this.login_pass = login_pass;
	}
	public Integer getAuthId() {
		return auth_id;
	}
	public void setAuthId(Integer auth_id) {
		this.auth_id = auth_id;
	}

}
