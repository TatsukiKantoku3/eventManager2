package domain;

import java.util.Date;

public class Members {
	private String member_id;
	private String name;
	private String kana;
	private Integer dep_id;
	private String address;
	private String tel;
	private Date birthday;
	private Integer position_type;
	private Date hired;
	private String login_id;
	private String department;
	private Integer floor;
	private String login_pass;
	private Integer auth_id;
	private String auth_type;
	private String oldlogin_id;
	private String oldmember_id;



	public String getOldmember_id() {
		return oldmember_id;
	}
	public void setOldmember_id(String oldmember_id) {
		this.oldmember_id = oldmember_id;
	}
	public String getOldlogin_id() {
		return oldlogin_id;
	}
	public void setOldlogin_id(String oldlogin_id) {
		this.oldlogin_id = oldlogin_id;
	}
	public String getLogin_pass() {
		return login_pass;
	}
	public void setLogin_pass(String login_pass) {
		this.login_pass = login_pass;
	}
	public Integer getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(Integer auth_id) {
		this.auth_id = auth_id;
	}
	public String getAuth_type() {
		return auth_type;
	}
	public void setAuth_type(String auth_type) {
		this.auth_type = auth_type;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKana() {
		return kana;
	}
	public void setKana(String kana) {
		this.kana = kana;
	}
	public Integer getDep_id() {
		return dep_id;
	}
	public void setDep_id(Integer dep_id) {
		this.dep_id = dep_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Integer getPosition_type() {
		return position_type;
	}
	public void setPosition_type(Integer position_type) {
		this.position_type = position_type;
	}
	public Date getHired() {
		return hired;
	}
	public void setHired(Date hired) {
		this.hired = hired;
	}
	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}



}
