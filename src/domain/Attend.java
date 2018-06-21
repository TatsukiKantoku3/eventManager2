package domain;

public class Attend {
	private int attends_id;
	private String member_id;
	private int event_id;
	private String member_name;

	public int getAttends_id() {
		return attends_id;
	}

	public void setAttends_id(int attends_id) {
		this.attends_id = attends_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

}
