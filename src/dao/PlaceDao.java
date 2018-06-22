package dao;
import java.sql.SQLException;
import java.util.List;

import domain.Place;

public interface PlaceDao {


	String insert(List<Place> place)throws Exception;
	List<Place> placeList() throws SQLException;

}
