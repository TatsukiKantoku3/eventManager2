package dao;

import java.util.List;

import domain.Members;


public interface MembersDao{
	Members findById(String id) throws Exception;
	void insert(Members member)throws Exception;
	String insertMast(List<Members> memberList)throws Exception;
	void update(Members member)throws Exception;
	int delete(Members member)throws Exception;
	Members findByLoginIdAndLoginPass(String loginId,String loginPass) throws Exception;
	List<Members> findAll(int page) throws Exception;
	List<Members> findfive(List<Members> userList) throws Exception;
	int countAll() throws Exception;
	boolean CheckLoginId(String loginId) throws Exception;
	int updateWhithoutPass(Members Members) throws Exception;

	public Members login(String loginId, String loginPass)throws Exception;
	public int insertacount(Members member) throws Exception;
	public int updateaccount(Members Members) throws Exception;
	public int updateAccountWhithoutPass(Members Members) throws Exception;
	int deleteAccount(Members Members) throws Exception;


}

