package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;

public class FileControllerTest extends TestDBAccess {
	protected final String MEMBER_INSERT = "memberInsert";
	protected final String ACCOUNT_INSERT = "accountInsert";
	protected final String PLACE_INSERT = "placeInsert";
	protected final String DEPART_INSERT = "departInsert";

	private static final String EXPECTED = "100"; // expected


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
	public void 正常testMember() {
		String result =  FileController.member(MEMBER_INSERT);
		assertThat(result, is(EXPECTED));
	}

	@Test
	public void 正常testAccount() {
		String result =  FileController.account(ACCOUNT_INSERT);
		assertThat(result, is(EXPECTED));
	}

	@Test
	public void 正常testPlace(){
		String result = FileController.place(PLACE_INSERT);
		assertThat(result, is(EXPECTED));
	}

	@Test
	public void 正常testDepart() {
		String result = FileController.depart(DEPART_INSERT);
		assertThat(result, is(EXPECTED));
	}


}
