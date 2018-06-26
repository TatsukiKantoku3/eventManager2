package dao;

import java.sql.SQLException;
import java.util.List;

import domain.Depart;

public interface DepartDao{

	String insert(List<Depart> department) throws Exception;

	List<Depart> DepList() throws SQLException;



}