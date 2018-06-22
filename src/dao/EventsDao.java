package dao;

import java.util.List;

import domain.Events;

public interface EventsDao {

	Events findById(Integer event_id) throws Exception;
	void insert(Events events) throws Exception;
	void update(Events events) throws Exception;
	void delete(Events events) throws Exception;
	List<Events> findAll(int event_page) throws Exception;
	List<Events> findToday(int event_page) throws Exception;
	List<Events> findfive(List<Events> eventsList, String member_id) throws Exception;
	int countAll() throws Exception;
	int countAllToday() throws Exception;

}
