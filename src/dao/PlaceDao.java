package dao;
import java.util.List;

import domain.Place;

public interface PlaceDao {


	String insert(List<Place> place)throws Exception;

}
