package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.Account;

public class AccountDaoImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAccountDaoImpl() {
		fail("まだ実装されていません");
	}

	@Test
	public void testInsertAcount() throws Exception {
		List<Account> accountList=new ArrayList<Account>();
		AccountDao accountDao = DaoFactory.createAccountDao();
		Account account=new Account();
		account.setLoginId("112");
		account.setMemberId("112");
		account.setLoginPass("112");
		account.setAuthId(1);
		accountList.add(account);

		String result=accountDao.insertAcount(accountList);
		assertThat(result, is("300"));
	}

}
