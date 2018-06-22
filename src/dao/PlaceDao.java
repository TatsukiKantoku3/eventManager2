package dao;
import java.sql.SQLException;
import java.util.List;

import domain.Events;
import domain.Place;

public interface PlaceDao {


	String insert(List<Place> place)throws Exception;
	List<Events> placeList() throws SQLException;

}
