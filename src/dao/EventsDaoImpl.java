package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import domain.Events;

public class EventsDaoImpl implements EventsDao {
	private DataSource ds;

	public EventsDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * @see dao.EventsDao#findAll()
	 */
	@Override
	public List<Events> findAll(int event_page) throws Exception {
		List<Events> eventsList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			String sql = "SELECT "
					+ " EVENTS.event_id"
					+ " FROM EVENTS"
					+ " LIMIT ?, 5";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, event_page);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				eventsList.add(mapToEvents(rs));
			}
		}
		return eventsList;
	}

	/**
	 * ユーザー一覧
	 * ResultSetをUsersクラスにセットします
	 * 5行分とってきたやつをjoinして表示を整える
	 * @return users
	 */
	@Override
	public List<Events> findfive(List<Events> events,String member_id) throws Exception {
		List<Events> eventsList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {
			for (Events event : events) {
				String sql = "SELECT"
						+ "		EVENTS.event_id,"
						+ "		EVENTS.title,"
						+ "		EVENTS.start,"
						+ "		EVENTS.end,"
						+ "		EVENTS.place_id,"
						+ "		PLACE.place,"
						+ "		atn.member_id,"
						+ "		DEPARTMENT.department,"
						+ "		EVENTS.dep_id,"
						+ "		EVENTS.detail,"
						+ "		MEMBERS.name AS MEMBERS_name,"
						+ "		EVENTS.created"
						+ " FROM"
						+ " 	EVENTS join"
						+ "		   PLACE"
						+ "     ON EVENTS.place_id=Place.place_id LEFT JOIN"
						+ " 		(SELECT * FROM ATTENDS WHERE member_id= ?) AS atn"
						+ " 	ON EVENTS.event_id = atn.event_id JOIN"
						+ " 		DEPARTMENT"
						+ " 	ON EVENTS.dep_id = DEPARTMENT.dep_id  LEFT JOIN"
						+ " 		MEMBERS"
						+ " 	ON EVENTS.registered_id = MEMBERS.member_id"
						+ " WHERE"
						+ " 	EVENTS.event_id =?";

				PreparedStatement stms = con.prepareStatement(sql);
				stms.setObject(1, member_id);
				stms.setObject(2, event.getEvent_id());
				ResultSet rs = stms.executeQuery();
				if (rs.next()) {
					eventsList.add(mapToEvents2(rs));
				}
			}
		}
		return eventsList;
	}

	@Override
	public List<Events> findToday(int event_page) throws Exception {
		List<Events> eventsList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		String today = String.format("%04d", year) + "-" + String.format("%02d", month) + "-"
				+ String.format("%02d", day);

		try (Connection con = ds.getConnection()) {
			String sql = "SELECT "
					+ " 	EVENTS.event_id"
					+ " FROM"
					+ " 	EVENTS"
					+ " WHERE"
					+ " 	SUBSTRING( `start`, 1, 10 ) = ?"
					+ " LIMIT ?, 5";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, today);
			stmt.setInt(2, event_page);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				eventsList.add(mapToEvents(rs));
			}
		}
		return eventsList;
	}

/**
 * 今日のイベント用のpagenation
 */

	/**
	 * findAllメソッドから呼び出し
	 */
	private Events mapToEvents(ResultSet rs) throws SQLException {
		Events events = new Events();
		events.setEvent_id((Integer) rs.getObject("event_id"));
		return events;
	}

	/**
	 * findfiveメソッドから呼び出し
	 */
	private Events mapToEvents2(ResultSet rs) throws SQLException {
	Events events = new Events();
	events.setEvent_id((Integer) rs.getObject("event_id"));
	events.setTitle(rs.getString("title"));
	events.setStart(rs.getTimestamp("start"));
	events.setEnd(rs.getTimestamp("end"));
	events.setPlace_id((Integer) rs.getObject("place_id"));
	events.setPlace_name(rs.getString("place"));
	events.setDep_name(rs.getString("department"));
	events.setDep_id((Integer) rs.getObject("dep_id"));
	events.setDetail(rs.getString("detail"));
	events.setMember_name(rs.getString("MEMBERS_name"));
	events.setCreated(rs.getTimestamp("created"));
	try {
		events.setMember_id(rs.getString("member_id"));
	} catch (NullPointerException e) {

	}
	return events;
}


	private Events mapToEventInfo(ResultSet rs) throws SQLException {
		Events events = new Events();
		events.setEvent_id((Integer) rs.getObject("event_id"));
		events.setTitle(rs.getString("title"));
		events.setStart(rs.getTimestamp("start"));
		events.setEnd(rs.getTimestamp("end"));
		events.setPlace_name(rs.getString("place"));
		events.setDep_name(rs.getString("department"));
		events.setDep_id((Integer) rs.getObject("dep_id"));
		events.setDetail(rs.getString("detail"));
		events.setMember_name(rs.getString("registered"));
		events.setCreated(rs.getTimestamp("created"));
		return events;
	}

	@Override
	public Events findById(Integer event_id) throws Exception {
		Events events = new Events();

		try (Connection con = ds.getConnection()) {
			String sql = "SELECT"
					+ "		EVENTS.event_id,"
					+ " 	EVENTS.title,"
					+ " 	EVENTS.start,"
					+ " 	EVENTS.end,"
					+ " 	PLACE.place,"
					+ " 	DEPARTMENT.department AS department,"
					+ " 	EVENTS.dep_id,"
					+ " 	EVENTS.detail,"
					+ " 	MEMBERS.name AS registered,"
					+ " 	EVENTS.created"
					+ " FROM"
					+ " 	EVENTS"
					+ " 		JOIN"
					+ " 	PLACE ON EVENTS.place_id = PLACE.place_id"
					+ " 		JOIN"
					+ " 	DEPARTMENT ON EVENTS.dep_id = DEPARTMENT.dep_id"
					+ " 		JOIN"
					+ "		MEMBERS ON EVENTS.registered_id = MEMBERS.member_id"
					+ " WHERE"
					+ "		EVENTS.event_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, event_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				events = mapToEventInfo(rs);
			}
		}
		return events;
	}

	@Override
	public void insert(Events events) throws Exception {
		Timestamp castStart = new Timestamp(events.getStart().getTime());
		Timestamp castEnd = new Timestamp(events.getEnd().getTime());
		try (Connection con = ds.getConnection()) {
			String sql = "INSERT INTO events"
					+ " 	(event_id, title, start, end, place_id, dep_id, detail, registered_id, created)"
					+ " VALUES(null,?,?,?,?,?,?,?,NOW())";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, events.getTitle());
			stmt.setTimestamp(2, castStart);
			stmt.setTimestamp(3, castEnd);
			stmt.setInt(4, events.getPlace_id());
			stmt.setInt(5, events.getDep_id());
			stmt.setString(6, events.getDetail());
			stmt.setString(7, events.getRegistered_id());
			stmt.executeUpdate();
		}
	}


	@Override
	public void update(Events events) throws Exception {
		Timestamp castStart = new Timestamp(events.getStart().getTime());
		Timestamp castEnd = new Timestamp(events.getEnd().getTime());
		try (Connection con = ds.getConnection()) {
			String sql = "UPDATE"
					+ " 	EVENTS"
					+ " SET"
					+ "     title = ?,"
					+ "     start = ?,"
					+ "     end = ?,"
					+ "     place_id = ?,"
					+ "     dep_id = ?,"
					+ "     detail = ?"
					+ " WHERE"
					+ "     event_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, events.getTitle());
			stmt.setTimestamp(2, castStart);
			stmt.setTimestamp(3, castEnd);
			stmt.setInt(4, events.getPlace_id());
			stmt.setInt(5, events.getDep_id());
			stmt.setString(6, events.getDetail());
			stmt.setInt(7, events.getEvent_id());
			stmt.executeUpdate();
		}
	}

	@Override
	public void delete(Events events) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "DELETE FROM"
					+ "	 	EVENTS"
					+ " WHERE"
					+ " 	event_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, events.getEvent_id(), Types.INTEGER);
			stmt.executeUpdate();
		}
	}

	/*
	 * pagenationで使用する
	 */
	public int countAll() throws Exception {
		List<Events> eventsList = new ArrayList<>();
		try (Connection con = ds.getConnection()) {
			String sql = "SELECT "
					+ " EVENTS.event_id "
					+ " FROM EVENTS";
			PreparedStatement stms = con.prepareStatement(sql);
			ResultSet rs = stms.executeQuery();
			while (rs.next()) {
				eventsList.add(mapToEvents(rs));
			}
		}
		double count = eventsList.size();
		int lastpage = (int) Math.ceil(count/5);

		return lastpage;
	}

	/*
	 * Todayのpagenationで使用する
	 */
	public int countAllToday() throws Exception {
		List<Events> eventsList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		String today = String.format("%04d", year) + "-" + String.format("%02d", month) + "-"
				+ String.format("%02d", day);
		try (Connection con = ds.getConnection()) {
			String sql = "SELECT "
					+ " EVENTS.event_id "
					+ " FROM EVENTS"
					+ " WHERE SUBSTRING( `start`, 1, 10 ) = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, today);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				eventsList.add(mapToEvents(rs));
			}
		}
		double count = eventsList.size();
		int lastpage = (int) Math.ceil(count/5);

		return lastpage;
	}

}
