package domain;

import java.util.Date;

public class Place {
	private Integer place_id;
	private String place;
	private Integer capa;
	private Integer equ_mic;
	private Integer equ_whitebord;
	private Integer equ_projector;
	private Integer admin_id;
	private Date locking_time;


	public Integer getPlace_id() {
		return place_id;
	}
	public void setPlace_id(Integer place_id) {
		this.place_id = place_id;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public Integer getCapa() {
		return capa;
	}
	public void setCapa(Integer capa) {
		this.capa = capa;
	}
	public Integer getEqu_mic() {
		return equ_mic;
	}
	public void setEqu_mic(Integer equ_mic) {
		this.equ_mic = equ_mic;
	}
	public Integer getEqu_whitebord() {
		return equ_whitebord;
	}
	public void setEqu_whitebord(Integer equ_whitebord) {
		this.equ_whitebord = equ_whitebord;
	}
	public Integer getEqu_projector() {
		return equ_projector;
	}
	public void setEqu_projector(Integer equ_projector) {
		this.equ_projector = equ_projector;
	}
	public Integer getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(Integer admin_id) {
		this.admin_id = admin_id;
	}
	public Date getLocking_time() {
		return locking_time;
	}
	public void setLocking_time(Date locking_time) {
		this.locking_time = locking_time;
	}


}
