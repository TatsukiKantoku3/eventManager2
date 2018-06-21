package dao;

import java.util.List;

import domain.Account;

public interface AccountDao{
	public String insertAcount(List<Account> accountList) throws Exception;
}
