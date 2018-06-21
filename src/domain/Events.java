package domain;

import java.util.Date;

public class Events {

	private int event_id;
	private String title;
	private Date start;
	private Date end;
	private int place_id;
	private String place_name;
	private String dep_name;
	private int dep_id;
	private String detail;
	private String registered_id;
	private String member_id;
	private String member_name;
	private Date created;

	/**
	 * @return registered_id
	 */
	public String getRegistered_id() {
		return registered_id;
	}
	/**
	 * @param registered_id セットする registered_id
	 */
	public void setRegistered_id(String registered_id) {
		this.registered_id = registered_id;
	}
	/**
	 * @return member_id
	 */
	public String getMember_id() {
		return member_id;
	}
	/**
	 * @param member_id セットする member_id
	 */
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	/**
	 * @return event_id
	 */
	public int getEvent_id() {
		return event_id;
	}
	/**
	 * @param event_id セットする event_id
	 */
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title セットする title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return start
	 */
	public Date getStart() {
		return start;
	}
	/**
	 * @param start セットする start
	 */
	public void setStart(Date start) {
		this.start = start;
	}
	/**
	 * @return end
	 */
	public Date getEnd() {
		return end;
	}
	/**
	 * @param end セットする end
	 */
	public void setEnd(Date end) {
		this.end = end;
	}
	/**
	 * @return place_id
	 */
	public int getPlace_id() {
		return place_id;
	}
	/**
	 * @param place_id セットする place_id
	 */
	public void setPlace_id(int place_id) {
		this.place_id = place_id;
	}
	/**
	 * @return dep_name
	 */
	public String getDep_name() {
		return dep_name;
	}
	/**
	 * @param dep_name セットする dep_name
	 */
	public void setDep_name(String dep_name) {
		this.dep_name = dep_name;
	}
	/**
	 * @return dep_id
	 */
	public int getDep_id() {
		return dep_id;
	}
	/**
	 * @param dep_id セットする dep_id
	 */
	public void setDep_id(int dep_id) {
		this.dep_id = dep_id;
	}
	/**
	 * @return detail
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * @param detail セットする detail
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}
	/**
	 * @return member_name
	 */
	public String getMember_name() {
		return member_name;
	}
	/**
	 * @param member_name セットする member_name
	 */
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	/**
	 * @return created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created セットする created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	/**
	 * @return place_name
	 */
	public String getPlace_name() {
		return place_name;
	}
	/**
	 * @param place_name セットする place_name
	 */
	public void setPlace_name(String place_name) {
		this.place_name = place_name;
	}

}